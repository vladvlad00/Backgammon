package front.panels.menu.body.creation;

import front.utils.handlers.FrameHandler;
import front.utils.handlers.PopUpHandler;
import front.entities.Lobby;
import front.entities.LobbyUser;
import front.entities.User;
import front.entities.UserRole;
import front.utils.handlers.NetworkManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class PlayersListItem extends GridPane {
    private Label userName;
    private Button join;
    private Button addAI;
    private Button removeAI;
    private Lobby lobby;
    private LobbyUser lobbyUser;

    public PlayersListItem(Lobby lobby, LobbyUser lobbyUser) {
        this.lobby = lobby;
        this.lobbyUser = lobbyUser;
        init();
    }

    private void init() {
        //this.setPrefWidth(MainMenuFrame.WIDTH / 2f);
        UserRole userRole = lobby.getRoleOfUser(User.getInstance().getUsername());
        switch (userRole) {
            case HOST_SPECTATOR:
                if(lobbyUser == null) {
                    addAI = new Button("Add AI");
                    addAI.setOnAction(e -> PopUpHandler.createAIDiffPopUp(lobby.getId()));
                    addAI.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(addAI, HPos.CENTER);

                    this.add(addAI, 1, 0);
                }
                else if(lobbyUser.getRole().equals(UserRole.AI_EASY) || lobbyUser.getRole().equals(UserRole.AI_MEDIUM) || lobbyUser.getRole().equals(UserRole.AI_HARD)) {
                    removeAI = new Button("Remove AI");
                    removeAI.setOnAction(e -> {
                        NetworkManager.removeAI(lobby.getId(), lobbyUser.getUsername());
                        FrameHandler.getMainMenuFrame().refreshLobby(lobby.getId(), true);
                    });
                    removeAI.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(removeAI, HPos.CENTER);

                    this.add(removeAI, 1, 0, 2, 1);
                }
            case SPECTATOR:
                if(lobbyUser == null) {
                    join = new Button("Join");
                    join.setOnAction(e -> {
                        var lobby2 = NetworkManager.joinThroughID(lobby.getId(), userRole.equals(UserRole.HOST_SPECTATOR) ? UserRole.HOST : UserRole.PLAYER);
                        FrameHandler.getMainMenuFrame().refreshLobby(lobby.getId(), true);
                    });
                    join.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(join, HPos.CENTER);

                    this.add(join, 2, 0);
                }
            case HOST:
                if(!userRole.equals(UserRole.HOST_SPECTATOR) && !userRole.equals(UserRole.SPECTATOR) && lobbyUser == null) {
                    addAI = new Button("Add AI");
                    addAI.setOnAction(e -> PopUpHandler.createAIDiffPopUp(lobby.getId()));
                    addAI.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(addAI, HPos.CENTER);

                    this.add(addAI, 1, 0);
                }
                if(!userRole.equals(UserRole.HOST_SPECTATOR) && !userRole.equals(UserRole.SPECTATOR) && lobbyUser != null && (lobbyUser.getRole().equals(UserRole.AI_EASY) || lobbyUser.getRole().equals(UserRole.AI_MEDIUM) || lobbyUser.getRole().equals(UserRole.AI_HARD))) {
                    removeAI = new Button("Remove AI");
                    removeAI.setOnAction(e -> {
                        NetworkManager.removeAI(lobby.getId(), lobbyUser.getUsername());
                        FrameHandler.getMainMenuFrame().refreshLobby(lobby.getId(), true);
                    });
                    removeAI.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(removeAI, HPos.CENTER);

                    this.add(removeAI, 1, 0, 2, 1);
                }
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

    public LobbyUser getLobbyUser() {
        return lobbyUser;
    }
}
