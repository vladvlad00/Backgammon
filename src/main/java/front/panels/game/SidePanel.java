package front.panels.game;

import front.panels.game.board_elements.Dice;
import front.utils.handlers.BackgammonEvent;
import front.utils.websocket.Message;
import front.utils.websocket.WSClient;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class SidePanel extends GridPane {
    private MainGameFrame frame;

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
        });
    }

    private void init() {
        firstDice = new Dice(1);
        firstDice.setSize(100, 100);
        firstDice.setPosition(0, 0);

        secondDice = new Dice(1);
        secondDice.setSize(100, 100);
        secondDice.setPosition(0, 0);

        getNewDice(1, 1);

        rollDice = new Button("Roll dice");
        rollDice.setStyle("-fx-font: 20 arial;");
        rollDice.setOnAction(e -> {
            rollDiceAction();
        });
        GridPane.setHalignment(rollDice, HPos.CENTER);

        leaveGame = new Button("Leave");
        leaveGame.setStyle("-fx-font: 20 arial;");
        leaveGame.setOnAction(e -> {
            //leave game
        });
        GridPane.setHalignment(leaveGame, HPos.CENTER);

        this.add(rollDice, 0, 0, 2, 1);
        this.add(leaveGame, 0, 2, 2, 1);

        RowConstraints small = new RowConstraints();
        small.setPercentHeight(10);
        RowConstraints big = new RowConstraints();
        big.setPercentHeight(80);

        ColumnConstraints eq = new ColumnConstraints();
        eq.setPercentWidth(50);

        this.getColumnConstraints().addAll(eq, eq);
        this.getRowConstraints().addAll(small, big, small);
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
}
