package front.panels.game;

import front.utils.handlers.BackgammonEvent;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class TitlePanel extends GridPane {
    private MainGameFrame frame;

    private Label title;
    private Label spectators;

    public TitlePanel(MainGameFrame frame) {
        this.frame = frame;
        init();
        this.addEventHandler(BackgammonEvent.UPDATE_SPECTATORS, e -> {
//            Update spectators number;
//            Lobby lobby = e.response(); sau ceva
//            frame.setLobby(newLobby);
        });
    }

    private void init() {
        title = new Label("ceva ceva");
        GridPane.setHalignment(title, HPos.CENTER);
        title.setStyle("-fx-font: 32 arial;");
        spectators = new Label("ochi");
        GridPane.setHalignment(spectators, HPos.RIGHT);
        spectators.setStyle("-fx-font: 18 arial;");

        this.add(spectators, 0, 0);
        this.add(title, 1, 0);

        ColumnConstraints small = new ColumnConstraints();
        ColumnConstraints big = new ColumnConstraints();
        small.setPercentWidth(10);
        big.setPercentWidth(90);

        this.getColumnConstraints().addAll(small, big);
    }

    public void updateTitle(String p1, String p2) {
        title.setText(p1 + " vs " + p2);
    }

    public void updateSpectators(Long spectatorNum) {
        spectators.setText("\uD83D\uDC41 " + spectatorNum);
    }
}
