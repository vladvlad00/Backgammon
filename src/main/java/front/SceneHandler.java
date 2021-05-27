package front;

import front.panels.menu.body.MainMenuFrame;
import front.panels.menu.login.MainLoginFrame;
import front.panels.menu.register.MainRegisterFrame;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import static front.panels.menu.body.MainMenuFrame.HEIGHT;
import static front.panels.menu.body.MainMenuFrame.WIDTH;

public class SceneHandler {
    private static Map<String, Scene> scenes;
    private static Stage stage;

    private SceneHandler() {}

    public static void init(Stage stageArg) {
        stage = stageArg;

        scenes = new HashMap<>();

        FrameHandler.init(stage);

        initMenuScene();
        initLoginScene();
        initRegisterScene();
    }

    public static void initMenuScene() {
        BorderPane pane = FrameHandler.getMainMenuFrame();
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH + 40);

        scenes.put("menu", new Scene(pane, WIDTH, HEIGHT));
    }

    public static void initRegisterScene() {
        BorderPane pane = FrameHandler.getMainRegisterFrame();
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH + 40);

        scenes.put("register", new Scene(pane, WIDTH, HEIGHT));
    }

    public static void initLoginScene() {
        BorderPane pane = FrameHandler.getMainLoginFrame();
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH + 40);

        scenes.put("login", new Scene(pane, WIDTH, HEIGHT));
    }

    public static Scene getScene(String name) {
        return scenes.get(name);
    }

    public static void changeScene(String name) {
        stage.setScene(getScene(name));
    }
}
