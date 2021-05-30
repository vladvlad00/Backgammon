package front.panels.menu.body.creation;

import front.FrameHandler;
import front.entities.Lobby;
import front.entities.LobbyUser;
import front.entities.User;
import front.entities.UserRole;
import front.panels.menu.body.MainMenuFrame;
import front.utils.NetworkManager;
import front.utils.VoidOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Collection;
import java.util.Objects;

public class PlayersListItem extends GridPane {
    private Label userName;
    private Button join;
    private ObservableList<Button> ais;
    private ComboBox<Button> addAI;
    private Lobby lobby;
    private LobbyUser lobbyUser;

    public PlayersListItem(Lobby lobby, LobbyUser lobbyUser) {
        this.lobby = lobby;
        this.lobbyUser = lobbyUser;
        init();
    }

    private void init() {
        initAI();
        UserRole userRole = lobby.getRoleOfUser(User.getInstance().getUsername());
        switch (userRole) {
            case HOST_SPECTATOR:
                if(lobbyUser == null) {
                    addAI.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(addAI, HPos.RIGHT);

                    this.add(addAI, 1, 0);
                }
            case SPECTATOR:
                if(lobbyUser == null) {
                    join = new Button("Join");
                    join.setOnAction(e -> {
                        NetworkManager.leaveRoom();
                        NetworkManager.joinThroughID(lobby.getId(), userRole.equals(UserRole.HOST_SPECTATOR) ? UserRole.HOST : UserRole.PLAYER);
                        FrameHandler.getMainMenuFrame().goToCreate(NetworkManager.getLobby(lobby.getId()));
                    });
                    join.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(join, HPos.RIGHT);

                    this.add(join, 2, 0);
                }
            case HOST:
            case PLAYER:
                if(lobbyUser != null) {
                    userName = new Label(lobbyUser.getUsername());
                    userName.setPadding(new Insets(10 ,10, 10, 10));
                    GridPane.setHalignment(userName, HPos.LEFT);
                }
                else {
                    userName = new Label("Open slot");
                    userName.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(userName, HPos.LEFT);
                }
                this.add(userName, 0, 0);
        }

        ColumnConstraints name = new ColumnConstraints();
        name.setPercentWidth(50);
        ColumnConstraints buttons = new ColumnConstraints();
        buttons.setPercentWidth(25);
        RowConstraints full = new RowConstraints();
        full.setPercentHeight(100);

        this.getColumnConstraints().addAll(name, buttons, buttons);
        this.getRowConstraints().addAll(full);
    }

    private void initAI() {
        ais = FXCollections.observableArrayList();
        addAI = new ComboBox<>();

        Button easy = new Button("Easy");
        easy.setOnAction(e -> {
            //SEND REQUEST TO ADD AI
        });
        Button medium = new Button("Medium");
        medium.setOnAction(e -> {
            //SEND REQUEST TO ADD AI
        });
        Button hard = new Button("Hard");
        hard.setOnAction(e -> {
            //SEND REQUEST TO ADD AI
        });

        ais.addAll(easy, medium, hard);
        addAI.setItems(ais);
        addAI.setPromptText("Add AI");
    }
}
