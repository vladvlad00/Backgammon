package front.panels.menu.body;

import front.utils.Cookies;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainMenuFrame extends BorderPane {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 900;
    private Stage stage;

    private AccountPanel accountPanel;
    private LoginRegisterPanel loginRegisterPanel;
    private MenuBodyPanel menuBodyPanel;
    private LobbyCreationPanel lobbyCreationPanel;
    private LobbySearcherPanel lobbySearcherPanel;

    public MainMenuFrame(Stage stage) {
        this.stage = stage;
        init();
    }

    private void init() {
        if(!Cookies.getValue("token").isEmpty() && false) {
            //TODO: check if token isn't expired (replace false)
        }
        else {
            loginRegisterPanel = new LoginRegisterPanel(this);
            this.setTop(loginRegisterPanel);
        }
        menuBodyPanel = new MenuBodyPanel(this);
        this.setCenter(menuBodyPanel);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
