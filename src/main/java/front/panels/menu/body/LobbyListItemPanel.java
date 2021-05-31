package front.panels.menu.body;

import front.utils.handlers.LobbyHandler;
import front.entities.Lobby;
import front.utils.websocket.WSClient;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.concurrent.ExecutionException;

import static front.panels.menu.login.MainLoginFrame.HEIGHT;
import static front.panels.menu.login.MainLoginFrame.WIDTH;

public class LobbyListItemPanel extends GridPane {
    private Label name;
    private Label players;
    private Label spectators;
    private Button join;

    private Long lobbyID;

    public LobbyListItemPanel(Lobby lobby) {
        lobbyID = lobby.getId();
        init();
        updateName();
        updatePlayers();
        updateSpectators();
    }

    private void init() {
        this.name = new Label("");
        this.players = new Label("");
        this.spectators = new Label("");
        this.join = new Button("Join");

        this.name.setStyle("-fx-font: 18 arial;");
        this.name.setPadding(new Insets(5, 20, 5, 20));
        GridPane.setHalignment(this.name, HPos.LEFT);
        this.players.setStyle("-fx-font: 18 arial;");
        this.players.setPadding(new Insets(5, 5, 5, 20));
        GridPane.setHalignment(this.players, HPos.LEFT);
        this.spectators.setStyle("-fx-font: 18 arial;");
        this.spectators.setPadding(new Insets(5, 20, 5, 5));
        GridPane.setHalignment(this.spectators, HPos.LEFT);
        this.join.setStyle("-fx-font: 20 arial;");
        this.join.setPadding(new Insets(10, 20, 10, 20));
        this.join.setPrefSize(0.15 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(this.join, HPos.RIGHT);

        this.join.setOnAction(e -> {
            String response = LobbyHandler.joinThroughID(lobbyID);
            if(!response.equals("succ")) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Could not join lobby");
                a.setTitle("Error");
                a.show();
            }
        });

        this.add(this.name, 0, 0, 2, 1);
        this.add(this.players, 0, 1, 1, 1);
        this.add(this.spectators, 1, 1, 1, 1);
        this.add(this.join, 2, 0, 1, 2);

        ColumnConstraints fstC = new ColumnConstraints();
        ColumnConstraints scdC = new ColumnConstraints();
        ColumnConstraints trdC = new ColumnConstraints();
        RowConstraints fstR = new RowConstraints();
        RowConstraints scdR = new RowConstraints();

        fstC.setPercentWidth(20);
        scdC.setPercentWidth(20);
        trdC.setPercentWidth(60);

        fstR.setPercentHeight(50);
        scdR.setPercentHeight(50);

        this.getColumnConstraints().addAll(fstC, scdC, trdC);
        this.getRowConstraints().addAll(fstR, scdR);
    }

    public void updateName() {
        this.name.setText(LobbyHandler.getById(lobbyID).getName());
    }

    public void updatePlayers() {
        this.players.setText("\uD83D\uDC64 " + LobbyHandler.getById(lobbyID).getPlayerNum() + " / 2");
    }

    public void updateSpectators() {
        this.spectators.setText("\uD83D\uDC41 " + LobbyHandler.getById(lobbyID).getSpectatorNum());
    }
}
