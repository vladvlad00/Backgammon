package front.panels.menu.body;

import front.entities.User;
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
        loginRegisterPanel = new LoginRegisterPanel(this);
        accountPanel = new AccountPanel(this);
        if(!Cookies.getValue("token").isEmpty() && false) {
            //TODO: check if token isn't expired (replace false)
        }
        else {
            this.setTop(loginRegisterPanel);
        }
        menuBodyPanel = new MenuBodyPanel(this);
        this.setCenter(menuBodyPanel);
    }

    public void changeLogin() {
        if(User.getToken() == null || User.getToken().isEmpty()) {
            this.setTop(loginRegisterPanel);
        }
        else {
            accountPanel.refresh();
            this.setTop(accountPanel);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
