package front.panels.menu.body;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainMenuFrame extends BorderPane {
    private Stage stage;

    public MainMenuFrame(Stage stage) {
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
