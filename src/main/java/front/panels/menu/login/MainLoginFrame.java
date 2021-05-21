package front.panels.menu.login;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainLoginFrame extends BorderPane {
    private Stage stage;

    public MainLoginFrame(Stage stage) {
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
