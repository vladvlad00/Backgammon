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
    private Long spectatorNum;
    private Label capturedPieces;

    public TitlePanel(MainGameFrame frame) {
        this.frame = frame;
        init();
        this.addEventHandler(BackgammonEvent.UPDATE_SPECTATORS, e -> {
            Long change = Long.parseLong(e.getOptions().get("change"));
            updateSpectators(change);
        });
    }

    private void init() {
        title = new Label("ceva ceva");
        GridPane.setHalignment(title, HPos.CENTER);
        title.setStyle("-fx-font: 32 arial;");
        spectators = new Label("ochi");
        GridPane.setHalignment(spectators, HPos.RIGHT);
        spectators.setStyle("-fx-font: 18 arial;");
        capturedPieces = new Label("Captured pieces:\n 0 x ⬤\n 0 x ⚪");
        capturedPieces.setStyle("-fx-font: 18 arial;");
        GridPane.setHalignment(capturedPieces, HPos.LEFT);

        this.add(spectators, 0, 0);
        this.add(title, 1, 0);
        this.add(capturedPieces, 2, 0);

        ColumnConstraints small = new ColumnConstraints();
        ColumnConstraints big = new ColumnConstraints();
        small.setPercentWidth(10);
        big.setPercentWidth(70);

        this.getColumnConstraints().addAll(small, big, small, small);
    }

    public void updateTitle(String p1, String p2) {
        title.setText(p1 + " vs " + p2 + "\nGame ID:" + frame.getLobby().getId());
    }

    public void initSpectators() {
        spectatorNum = frame.getLobby().getSpectatorNum();
    }

    public void updateSpectators(Long count) {
        spectatorNum += count;
        spectators.setText("\uD83D\uDC41 " + spectatorNum);
    }

    public void updateCapturedPieces(Integer white, Integer black) {
        capturedPieces.setText("Captured pieces:\n " + black + " x ⬤\n " + white + " x ⚪");
    }
}
