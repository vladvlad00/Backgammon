package front.panels.menu.body.creation;

import front.SceneHandler;
import front.entities.Lobby;
import front.entities.LobbyUser;
import front.entities.UserRole;
import front.panels.menu.body.MainMenuFrame;
import front.utils.NetworkManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import static front.panels.menu.login.MainLoginFrame.HEIGHT;
import static front.panels.menu.login.MainLoginFrame.WIDTH;

public class LobbyCreationPanel extends GridPane {
    private final MainMenuFrame frame;
    private Lobby lobby;

    private Label lobbyName;
    //Switch intre private / public
    private ObservableList<PlayersListItem> playersListItems;
    private ObservableList<SpectatorListItem> spectatorListItems;
    private ListView<PlayersListItem> players;
    private ListView<SpectatorListItem> spectators;
    private Button back;
    private Button start;
    private Button delete;

    public LobbyCreationPanel(MainMenuFrame frame) {
        this.frame = frame;
        //init();
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void init() {
        this.getChildren().remove(back);
        this.getChildren().remove(lobbyName);
        this.getChildren().remove(players);
        this.getChildren().remove(spectators);
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();

        refresh();
        back = new Button("<-");
        back.setOnAction(e -> {
            NetworkManager.leaveRoom();
            frame.goToMenu();
        });
        GridPane.setValignment(back, VPos.CENTER);

        start = new Button("Start");
        start.setOnAction(e -> SceneHandler.changeScene("game"));
        start.setPrefSize(0.25 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(start, HPos.LEFT);

        delete = new Button("Delete");
        delete.setOnAction(e -> {
            //TODO: DELETE ROOM FOR ALL
        });
        delete.setPrefSize(0.25 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(delete, HPos.RIGHT);

        this.add(back, 0, 0);
        this.add(lobbyName, 0, 1, 2, 1);
        this.add(players, 0, 2);
        this.add(spectators, 1, 2);
        this.add(start, 0, 3);
        this.add(delete, 1, 3);

        ColumnConstraints half = new ColumnConstraints();
        half.setPercentWidth(50);
        RowConstraints backC = new RowConstraints();
        backC.setPercentHeight(5);
        RowConstraints small = new RowConstraints();
        small.setPercentHeight(10);
        RowConstraints big = new RowConstraints();
        big.setPercentHeight(75);
        RowConstraints buttons = new RowConstraints();
        buttons.setPercentHeight(10);

        this.getColumnConstraints().addAll(half, half);
        this.getRowConstraints().addAll(backC, small, big, buttons);
    }

    public void refresh() {
        playersListItems = FXCollections.observableArrayList();
        spectatorListItems = FXCollections.observableArrayList();

        for(LobbyUser lobbyUser : lobby.getUsers()) {
            if(lobbyUser.getRole().equals(UserRole.HOST_SPECTATOR) || lobbyUser.getRole().equals(UserRole.SPECTATOR)) {
                spectatorListItems.add(new SpectatorListItem(lobby, lobbyUser));
            }
            else if(lobbyUser.getRole().equals(UserRole.AI_EASY) || lobbyUser.getRole().equals(UserRole.AI_MEDIUM) || lobbyUser.getRole().equals(UserRole.AI_HARD) || lobbyUser.getRole().equals(UserRole.PLAYER) || lobbyUser.getRole().equals(UserRole.HOST)) {
                playersListItems.add(new PlayersListItem(lobby, lobbyUser));
            }
        }

        spectatorListItems.add(new SpectatorListItem(lobby, null));

        for(int i = 0; i < 2 - lobby.getPlayerNum(); ++i) {
            playersListItems.add(new PlayersListItem(lobby, null));
        }

        players = new ListView<>();
        players.setItems(playersListItems);
        spectators = new ListView<>();
        spectators.setItems(spectatorListItems);

        lobbyName = new Label(lobby.getName());
        GridPane.setHalignment(lobbyName, HPos.CENTER);
        lobbyName.setStyle("-fx-font: 28 arial;");
    }
}
