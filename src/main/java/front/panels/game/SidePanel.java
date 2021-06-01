package front.panels.game;

import front.entities.User;
import front.entities.UserRole;
import front.panels.game.board_elements.Board;
import front.panels.game.board_elements.Dice;
import front.utils.handlers.BackgammonEvent;
import front.utils.handlers.FrameHandler;
import front.utils.handlers.GameHandler;
import front.utils.handlers.NetworkManager;
import front.utils.websocket.Message;
import front.utils.websocket.WSClient;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class SidePanel extends GridPane {
    private MainGameFrame frame;

    private Label turn;
    private Button rollDice;
    private Button leaveGame;
    private Dice firstDice;
    private Dice secondDice;
    private Integer doubleCount;

    public SidePanel(MainGameFrame frame) {
        this.frame = frame;
        init();
        this.addEventHandler(BackgammonEvent.ROLL_DICE, e -> {
            getNewDice(Integer.parseInt(e.getOptions().get("die1")), Integer.parseInt(e.getOptions().get("die2")));
            String currentUserRole = GameHandler.getCurrentUser().getRole().toString();
            if (GameHandler.isHost && currentUserRole.startsWith("AI_"))
            {
                String color = GameHandler.getCurrentUser().getWhite() ? "white" : "black";
                int die1 = firstDice.getNumber();
                int die2 = secondDice.getNumber();
                String board = frame.getBoardPanel().getBoard().getBoardString();
                String difficulty = currentUserRole.split("_")[1].toLowerCase(Locale.ROOT);

                var moves = NetworkManager.getAiMoves(board, color, die1, die2, difficulty);
                Thread thread = new Thread(() -> {
                    try
                    {
                        for (var move : moves)
                        {
                            WSClient.getInstance().sendMessage(move);
                                Thread.sleep(1000);
                        }
                    }
                    catch (InterruptedException interruptedException)
                    {
                        interruptedException.printStackTrace();
                    }
                    WSClient.getInstance().sendMessage(new Message("turn", null));
                });
                thread.start();
            }
        });
    }

    private void init() {
        firstDice = new Dice(1);
        firstDice.setSize(100, 100);
        firstDice.setPosition(0, 0);

        secondDice = new Dice(1);
        secondDice.setSize(100, 100);
        secondDice.setPosition(0, 0);

        rollDice = new Button("Roll dice");
        rollDice.setStyle("-fx-font: 20 arial;");
        rollDice.setOnAction(e -> {
            if(User.getInstance().getUsername().equals(GameHandler.getCurrentUser().getUsername()) && !GameHandler.isRolledDice()) {
                rollDiceAction();
            }
        });
        GridPane.setHalignment(rollDice, HPos.CENTER);

        getNewDice(1, 1);

        leaveGame = new Button("Leave");
        leaveGame.setStyle("-fx-font: 20 arial;");
        leaveGame.setOnAction(e -> {
            if(frame.getLobby().getRoleOfUser(User.getInstance().getUsername()).equals(UserRole.SPECTATOR) || frame.getLobby().getRoleOfUser(User.getInstance().getUsername()).equals(UserRole.HOST_SPECTATOR)) {
                Map<String, String> options = new HashMap<>();
                options.put("count", "-1");
                WSClient.getInstance().sendMessage(new Message("spectators", options));
            }
            else {
                Map<String, String> options = new HashMap<>();
                String color;
                if(GameHandler.getBlackUser().getUsername().equals(User.getInstance().getUsername())) {
                    color = "black";
                }
                else {
                    color = "white";
                }
                options.put("color", color);
                options.put("user", User.getInstance().getUsername());
                WSClient.getInstance().sendMessage(new Message("disconnect", options));
            }
        });
        GridPane.setHalignment(leaveGame, HPos.CENTER);

        turn = new Label();
        turn.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(turn, HPos.CENTER);

        this.add(turn, 0, 0, 2, 1);
        this.add(rollDice, 0, 1, 2, 1);
        this.add(leaveGame, 0, 3, 2, 1);

        RowConstraints small = new RowConstraints();
        small.setPercentHeight(10);
        RowConstraints big = new RowConstraints();
        big.setPercentHeight(70);

        ColumnConstraints eq = new ColumnConstraints();
        eq.setPercentWidth(50);

        this.getColumnConstraints().addAll(eq, eq);
        this.getRowConstraints().addAll(small, small, big, small);
    }

    public void setPlayerTurn() {
        turn.setText(GameHandler.getCurrentUser().getUsername() + "'s turn");
    }

    public void rollDiceAction() {
//        int fst = firstDice.randomDice();
//        firstDice.removeFromChildren(this);
//        firstDice.addToChildren(this, 0);
//        int scd = secondDice.randomDice();
//        secondDice.removeFromChildren(this);
//        secondDice.addToChildren(this, 1);
        WSClient.getInstance().sendMessage(new Message("dice", null));
    }

    public void getNewDice(int d1, int d2) {
        rollDice.setDisable(true);
        GameHandler.setRolledDice(true);
        firstDice.setDice(d1);
        firstDice.removeFromChildren(this);
        firstDice.addToChildren(this, 0);
        firstDice.setAvailable(true);
        secondDice.setDice(d2);
        secondDice.removeFromChildren(this);
        secondDice.addToChildren(this, 1);
        secondDice.setAvailable(true);
        if(d1 == d2) {
            doubleCount = 4;
        }
        else {
            doubleCount = 0;
        }
        if(GameHandler.getCurrentUser() != null && GameHandler.getCurrentUser().getUsername().equals(User.getInstance().getUsername())) {
            FrameHandler.getMainGameFrame().getBoardPanel().getBoard().generateHouses();
            FrameHandler.getMainGameFrame().getBoardPanel().getBoard().handleOutside();
//            FrameHandler.getMainGameFrame().getBoardPanel().getBoard().canMove();
        }
    }

    public Integer getFirstDice() {
        if(firstDice.isAvailable()) {
            return firstDice.getNumber();
        }
        else {
            return null;
        }
    }
    public Integer getSecondDice() {
        if(secondDice.isAvailable()) {
            return secondDice.getNumber();
        }
        else {
            return null;
        }
    }

    public void disableFirstDice() {
        firstDice.setAvailable(false);
    }

    public void disableSecondDice() {
        secondDice.setAvailable(false);
    }

    public void decreaseDoubleCount(int count) {
        if(count > 4) {
            System.exit(0);
        }
        else {
            if(doubleCount < count) {
                System.err.println("User hacked the game");
            }
            doubleCount -= count;
        }
    }

    public Integer getDoubleCount() {
        return doubleCount;
    }

    public Boolean getFirstDiceAvailable() {
        return firstDice.isAvailable();
    }

    public Boolean getSecondDiceAvailable() {
        return secondDice.isAvailable();
    }

    public void updateDice() {
        GameHandler.setRolledDice(false);
        if(!frame.getLobby().getRoleOfUser(User.getInstance().getUsername()).equals(UserRole.SPECTATOR) || frame.getLobby().getRoleOfUser(User.getInstance().getUsername()).equals(UserRole.HOST_SPECTATOR)) {
            if(User.getInstance().getUsername().equals(GameHandler.getCurrentUser().getUsername())) {
                rollDice.setDisable(false);
            }
        }
    }
}
