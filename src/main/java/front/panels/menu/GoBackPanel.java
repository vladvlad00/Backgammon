package front.panels.menu;

import front.SceneHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class GoBackPanel extends GridPane {
    private BorderPane frame;
    private String oldScene;

    private Button back;

    public GoBackPanel(BorderPane frame, String oldScene) {
        this.frame = frame;
        this.oldScene = oldScene;
        init();
    }

    private void init() {
        back = new Button("<-");
        back.setOnAction((e) -> {
            SceneHandler.changeScene(oldScene);
        });

        this.add(back, 0, 0);
    }
}
