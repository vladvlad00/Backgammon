package front.panels.menu.register;

import front.utils.handlers.SceneHandler;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

public class RegisterResponsePanel extends GridPane {
    private static final Long ONE_SECOND = 1_000_000L;

    private MainRegisterFrame frame;

    private Label message;

    public RegisterResponsePanel(MainRegisterFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        message = new Label("");
        message.setStyle("-fx-font: 32 arial;");

        GridPane.setHalignment(message, HPos.CENTER);

        this.add(message, 0, 0);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100);
        this.getColumnConstraints().addAll(columnConstraints);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(50);
        this.getRowConstraints().addAll(rowConstraints, rowConstraints);
    }

    public void createPositive(String message, Long duration) {
        this.message.setStyle("-fx-font: 32 arial; -fx-text-fill: green;");
        this.message.setText(message);
        FadeTransition fader = new FadeTransition(Duration.seconds(duration), this.message);
        fader.setFromValue(1);
        fader.setToValue(0);
        fader.setOnFinished((e) -> {
            frame.getRegisterBodyPanel().refresh.func();
            SceneHandler.changeScene("menu");
        });
        SequentialTransition seq = new SequentialTransition(this.message, fader);
        seq.play();
    }

    public void createNegative(String message, Long duration) {
        this.message.setStyle("-fx-font: 32 arial; -fx-text-fill: red;");
        this.message.setText(message);
        FadeTransition fader = new FadeTransition(Duration.seconds(duration), this.message);
        fader.setFromValue(1);
        fader.setToValue(0);
        SequentialTransition seq = new SequentialTransition(this.message, fader);
        seq.play();
    }
}
