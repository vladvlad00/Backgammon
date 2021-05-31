package front.panels.game;

import front.entities.Lobby;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class MainGameFrame extends BorderPane {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;

    private Stage stage;

    private BoardPanel boardPanel;
    private SidePanel sidePanel;
    private TitlePanel titlePanel;
    private Lobby lobby;

    public MainGameFrame(Stage stage) {
        this.stage = stage;
        init();
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
        List<String> names = lobby.getPlayers();
        titlePanel.updateTitle(names.get(0), names.get(1));
        titlePanel.updateSpectators(lobby.getSpectatorNum());
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
}
