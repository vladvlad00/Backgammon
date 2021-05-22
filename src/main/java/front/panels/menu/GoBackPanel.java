package front.panels.menu;

import front.SceneHandler;
import front.utils.VoidOperator;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class GoBackPanel extends GridPane {
    private BorderPane frame;
    private String oldScene;

    private Button back;
    private VoidOperator op;

    public GoBackPanel(BorderPane frame, String oldScene) {
        this.frame = frame;
        this.oldScene = oldScene;
        init();
    }

    public GoBackPanel(BorderPane frame, String oldScene, VoidOperator op) {
        this.frame = frame;
        this.oldScene = oldScene;
        this.op = op;
        init();
    }

    private void init() {
        back = new Button("<-");
        back.setOnAction((e) -> {
            if(op != null ) {
                op.func();
            }
            SceneHandler.changeScene(oldScene);
        });

        this.add(back, 0, 0);
    }
}
