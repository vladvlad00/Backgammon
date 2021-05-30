package front.panels.menu.body;

import front.entities.Lobby;
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
    private PleaseLoginPanel pleaseLoginPanel;

    public MainMenuFrame(Stage stage) {
        this.stage = stage;
        init();
    }

    private void init() {
        loginRegisterPanel = new LoginRegisterPanel(this);
        accountPanel = new AccountPanel(this);
        menuBodyPanel = new MenuBodyPanel(this);
        lobbyCreationPanel = new LobbyCreationPanel(this);
        lobbySearcherPanel = new LobbySearcherPanel(this);
        pleaseLoginPanel = new PleaseLoginPanel(this);
        if(!Cookies.getValue("token").isEmpty() && false) {
            //TODO: check if token isn't expired (replace false)
            this.setCenter(menuBodyPanel);
        }
        else {
            this.setTop(loginRegisterPanel);
            this.setCenter(pleaseLoginPanel);
        }
    }

    public void changeLogin() {
        if(User.getInstance().getToken() == null || User.getInstance().getToken().isEmpty()) {
            this.setTop(loginRegisterPanel);
            this.setCenter(pleaseLoginPanel);
        }
        else {
            accountPanel.refresh();
            this.setTop(accountPanel);
            this.setCenter(menuBodyPanel);
        }
    }

    public void goToSearch() {
        this.setCenter(lobbySearcherPanel);
    }

    public void goToCreate(Lobby lobby) {
        lobbyCreationPanel.setLobby(lobby);
        this.setCenter(lobbyCreationPanel);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
