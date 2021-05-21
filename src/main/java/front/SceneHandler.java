package front;

import front.panels.menu.body.MainMenuFrame;
import front.panels.menu.login.MainLoginFrame;
import front.panels.menu.register.MainRegisterFrame;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneHandler {
    private static Map<String, Scene> scenes;
    private static Stage stage;

    private SceneHandler() {}

    public static void init(Stage stageArg) {
        stage = stageArg;

        scenes = new HashMap<>();

        initMenuScene();
        initLoginScene();
        initRegisterScene();
    }

    public static void initMenuScene() {
        BorderPane pane = new MainMenuFrame(stage);
        stage.setMinHeight(940);
        stage.setMinWidth(640);

        scenes.put("menu", new Scene(pane, 900, 500));
    }

    public static void initRegisterScene() {
        BorderPane pane = new MainRegisterFrame(stage);
        stage.setMinHeight(940);
        stage.setMinWidth(640);

        scenes.put("register", new Scene(pane, 900, 500));
    }

    public static void initLoginScene() {
        BorderPane pane = new MainLoginFrame(stage);
        stage.setMinHeight(940);
        stage.setMinWidth(640);

        scenes.put("login", new Scene(pane, 900, 500));
    }

    public static Scene getScene(String name) {
        return scenes.get(name);
    }
}
