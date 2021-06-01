package front.panels.menu.body.creation;

import front.entities.User;
import front.utils.handlers.*;
import front.entities.Lobby;
import front.entities.LobbyUser;
import front.entities.UserRole;
import front.panels.menu.body.MainMenuFrame;
import front.utils.websocket.Message;
import front.utils.websocket.WSClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Comparator;
import java.util.List;

import static front.panels.menu.login.MainLoginFrame.HEIGHT;
import static front.panels.menu.login.MainLoginFrame.WIDTH;

public class LobbyCreationPanel extends GridPane {
    private final MainMenuFrame frame;
    private Lobby lobby;

    private Label lobbyName;
    //Switch intre private / public
    private ObservableList<PlayersListItem> playersListItems;
    private ObservableList<SpectatorListItem> spectatorListItems;
    private ObservableList<String> whitePlayerList;
    private ListView<PlayersListItem> players;
    private ListView<SpectatorListItem> spectators;
    private ComboBox<String> whitePlayer;
    private Label whiteDesc;
    private Button back;
    private Button start;
    private Button delete;

    public LobbyCreationPanel(MainMenuFrame frame) {
        this.frame = frame;
        this.addEventHandler(BackgammonEvent.REFRESH_LOBBY, e -> frame.refreshLobby(lobby.getId(), false));
        this.addEventHandler(BackgammonEvent.START_GAME, e -> startGame());
        this.addEventHandler(BackgammonEvent.DELETE_LOBBY, e -> leaveRoom());
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void init() {
        this.getChildren().remove(back);
        this.getChildren().remove(lobbyName);
        this.getChildren().remove(players);
        this.getChildren().remove(spectators);
        this.getChildren().remove(whitePlayer);
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();

        refresh();
        back = new Button("<-");
        back.setOnAction(e -> {
            leaveRoom();
        });
        GridPane.setValignment(back, VPos.CENTER);

        start = new Button("Start");
        start.setOnAction(e -> {
            if(lobby.getPlayerNum() != 2) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("You need two players to start the game!");
                a.setTitle("Error");
                a.show();
            }
            else {
                String username = User.getInstance().getUsername();
                if (lobby.getRoleOfUser(username).equals(UserRole.HOST) || lobby.getRoleOfUser(username).equals(UserRole.HOST_SPECTATOR))
                    GameHandler.isHost = true;
                WSClient.getInstance().sendMessage(new Message("start", null));
                NetworkManager.startLobby(lobby.getId());
            }
        });
        start.setPrefSize(0.25 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(start, HPos.LEFT);
        start.setDisable(true);

        delete = new Button("Delete");
        delete.setOnAction(e -> {
            WSClient.getInstance().sendMessage(new Message("delete", null));
            NetworkManager.deleteRoom(lobby.getId());
        });
        delete.setPrefSize(0.25 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(delete, HPos.RIGHT);
        delete.setDisable(true);

        for(LobbyUser lobbyUser : lobby.getUsers()) {
            if((lobbyUser.getRole().equals(UserRole.HOST) || lobbyUser.getRole().equals(UserRole.HOST_SPECTATOR)) && User.getInstance().getUsername().equals(lobbyUser.getUsername())) {
                start.setDisable(false);
                delete.setDisable(false);
                break;
            }
        }


        whiteDesc = new Label("White player\n(First to move)");
        whiteDesc.setStyle("-fx-font: 16 arial;");
        whiteDesc.setPadding(new Insets(5, 5, 5, 5));
        GridPane.setHalignment(whiteDesc, HPos.RIGHT);
        whitePlayerList = FXCollections.observableArrayList();
        whitePlayerList.addAll("Player 1", "Player 2", "Random");
        whitePlayer = new ComboBox<>(whitePlayerList);
        whitePlayer.setPromptText("Who starts");
        whitePlayer.setStyle("-fx-font: 18 arial;");
        GridPane.setHalignment(whitePlayer, HPos.LEFT);

        this.add(back, 0, 0);
        this.add(lobbyName, 0, 1, 3, 1);
        this.add(players, 0, 2, 2, 1);
        this.add(spectators, 1, 2, 2, 1);
        this.add(start, 0, 3);
        this.add(delete, 1, 3);
        this.add(whiteDesc, 1, 0);
        this.add(whitePlayer, 2, 0);

        ColumnConstraints half = new ColumnConstraints();
        ColumnConstraints littleHalf = new ColumnConstraints();
        ColumnConstraints little = new ColumnConstraints();
        half.setPercentWidth(50);
        littleHalf.setPercentWidth(30);
        little.setPercentWidth(20);
        RowConstraints backC = new RowConstraints();
        backC.setPercentHeight(10);
        RowConstraints small = new RowConstraints();
        small.setPercentHeight(10);
        RowConstraints big = new RowConstraints();
        big.setPercentHeight(70);
        RowConstraints buttons = new RowConstraints();
        buttons.setPercentHeight(10);

        this.getColumnConstraints().addAll(half, little, littleHalf);
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

        playersListItems.sort(Comparator.comparing(p -> p.getLobbyUser().getUsername()));
        spectatorListItems.sort(Comparator.comparing(s -> s.getLobbyUser().getUsername()));

        spectatorListItems.add(new SpectatorListItem(lobby, null));

        for(int i = 0; i < 2 - lobby.getPlayerNum(); ++i) {
            playersListItems.add(new PlayersListItem(lobby, null));
        }

        players = new ListView<>();
        players.setItems(playersListItems);
        spectators = new ListView<>();
        spectators.setItems(spectatorListItems);

        lobbyName = new Label(lobby.getName() + "\n" + "Game ID: " + lobby.getId());
        GridPane.setHalignment(lobbyName, HPos.CENTER);
        lobbyName.setStyle("-fx-font: 28 arial;");
    }

    private void startGame() {
        GameHandler.init(players.getItems().get(0).getLobbyUser(), players.getItems().get(1).getLobbyUser(), whitePlayer.getValue());
        FrameHandler.getMainGameFrame().setLobby(lobby);
        FrameHandler.getMainGameFrame().getSidePanel().updateDice();
        SceneHandler.changeScene("game");
    }

    public void leaveRoom() {
        NetworkManager.leaveRoom();
        frame.goToMenu();
    }
}
