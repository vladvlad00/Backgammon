package front.panels.menu.register;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainRegisterFrame extends BorderPane {
    private Stage stage;

    public MainRegisterFrame(Stage stage) {
        this.stage = stage;
        init();
    }

    private void init() {
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}