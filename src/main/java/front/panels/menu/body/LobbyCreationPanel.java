package front.panels.menu.body;

import javafx.scene.layout.GridPane;

public class LobbyCreationPanel extends GridPane {
    private final MainMenuFrame frame;

    public LobbyCreationPanel(MainMenuFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        //TODO: Lobby name, lobby privacy (private/public), player list, observer list, button to add AI
    }
}
