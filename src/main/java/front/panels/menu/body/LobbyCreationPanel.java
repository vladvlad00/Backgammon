package front.panels.menu.body;

import front.entities.Lobby;
import javafx.scene.layout.GridPane;

public class LobbyCreationPanel extends GridPane {
    private final MainMenuFrame frame;

    private Lobby lobby;

    public LobbyCreationPanel(MainMenuFrame frame) {
        this.frame = frame;
        init();
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    private void init() {
        //TODO: Lobby name, lobby privacy (private/public), player list, observer list, button to add AI
    }
}
