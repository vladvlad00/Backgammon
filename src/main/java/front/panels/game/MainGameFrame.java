package front.panels.game;

import front.entities.Lobby;
import front.utils.handlers.BackgammonEvent;
import front.utils.handlers.FrameHandler;
import front.utils.handlers.GameHandler;
import front.utils.handlers.PopUpHandler;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.security.core.parameters.P;

import java.util.List;

public class MainGameFrame extends BorderPane {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 1000;

    private Stage stage;

    private BoardPanel boardPanel;
    private SidePanel sidePanel;
    private TitlePanel titlePanel;
    private Lobby lobby;

    public MainGameFrame(Stage stage) {
        this.stage = stage;
        init();
        this.addEventHandler(BackgammonEvent.MAKE_MOVE, e -> {
            String gameState = e.getOptions().get("state");
            if(gameState.equals("BLACK_WIN")) {
                PopUpHandler.createSomeoneWon("Black", GameHandler.getBlackUser().getUsername());
            }
            else if(gameState.equals("WHITE_WIN")) {
                PopUpHandler.createSomeoneWon("White", GameHandler.getWhiteUser().getUsername());
            }
            else {
                boardPanel.getBoard().setBoard(e.getOptions().get("board"));
            }
        });
        this.addEventHandler(BackgammonEvent.NEXT_TURN, e -> {
            nextTurn();
        });
        this.addEventHandler(BackgammonEvent.DISCONNECT, e -> {
            if(e.getOptions().get("color").equals("white")) {
                PopUpHandler.createSomeoneWon("Black", GameHandler.getBlackUser().getUsername(), e.getOptions().get("user"));
            }
            else {
                PopUpHandler.createSomeoneWon("White", GameHandler.getWhiteUser().getUsername(), e.getOptions().get("user"));
            }
        });
    }

    private void nextTurn() {
        GameHandler.nextTurn();
        FrameHandler.getMainGameFrame().getSidePanel().setPlayerTurn();
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
        List<String> names = lobby.getPlayers();
        titlePanel.updateTitle(names.get(0), names.get(1));
        titlePanel.initSpectators(lobby.getSpectatorNum());
        sidePanel.setPlayerTurn();
        boardPanel.getBoard().init();
    }

    public Lobby getLobby() {
        return lobby;
    }

    private void init() {
        boardPanel = new BoardPanel(this);
        this.setCenter(boardPanel);
        sidePanel = new SidePanel(this);
        this.setRight(sidePanel);
        titlePanel = new TitlePanel(this);
        this.setTop(titlePanel);
    }

    public SidePanel getSidePanel() {
        return sidePanel;
    }

    public TitlePanel getTitlePanel() {
        return titlePanel;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
