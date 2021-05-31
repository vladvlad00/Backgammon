package front.panels.menu.body.creation;

import front.utils.handlers.FrameHandler;
import front.entities.Lobby;
import front.entities.LobbyUser;
import front.entities.User;
import front.entities.UserRole;
import front.utils.handlers.NetworkManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SpectatorListItem extends GridPane {
    private Label userName;
    private Button join;
    private Lobby lobby;
    private LobbyUser lobbyUser;

    public SpectatorListItem(Lobby lobby, LobbyUser lobbyUser) {
        this.lobby = lobby;
        this.lobbyUser = lobbyUser;
        init();
    }

    public void init() {
        UserRole userRole = lobby.getRoleOfUser(User.getInstance().getUsername());
        switch (userRole) {
            case HOST:
            case PLAYER:
                if(lobbyUser == null) {
                    join = new Button("Join");
                    join.setOnAction(e -> {
                        var lobby2 = NetworkManager.joinThroughID(lobby.getId(), userRole.equals(UserRole.HOST) ? UserRole.HOST_SPECTATOR : UserRole.SPECTATOR);
                        FrameHandler.getMainMenuFrame().goToCreate(lobby2);
                    });
                    join.setPadding(new Insets(10, 10, 10, 10));
                    GridPane.setHalignment(join, HPos.RIGHT);

                    this.add(join, 2, 0);
                }
            case HOST_SPECTATOR:
            case SPECTATOR:
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
    }
}
