package front.utils.handlers;

import front.panels.game.MainGameFrame;
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
        initGameScene();
    }

    public static void initMenuScene() {
        BorderPane pane = FrameHandler.getMainMenuFrame();

        scenes.put("menu", new Scene(pane, WIDTH, HEIGHT));
    }

    public static void initRegisterScene() {
        BorderPane pane = FrameHandler.getMainRegisterFrame();

        scenes.put("register", new Scene(pane, WIDTH, HEIGHT));
    }

    public static void initLoginScene() {
        BorderPane pane = FrameHandler.getMainLoginFrame();

        scenes.put("login", new Scene(pane, WIDTH, HEIGHT));
    }

    public static void initGameScene() {
        BorderPane pane = FrameHandler.getMainGameFrame();


        scenes.put("game", new Scene(pane, MainGameFrame.WIDTH, MainGameFrame.HEIGHT));
    }

    public static Scene getScene(String name) {
        return scenes.get(name);
    }

    public static void changeScene(String name) {
        if(name.equals("game")) {
            stage.setMinHeight(MainGameFrame.HEIGHT);
            stage.setMaxHeight(MainGameFrame.HEIGHT);
            stage.setMinWidth(MainGameFrame.WIDTH + 40);
            stage.setMaxWidth(MainGameFrame.WIDTH + 40);
        }
        else {
            stage.setMinHeight(HEIGHT);
            stage.setMaxHeight(HEIGHT);
            stage.setMinWidth(WIDTH + 40);
            stage.setMaxWidth(WIDTH + 40);
        }
        stage.setScene(getScene(name));
    }

    public static Stage getStage() {
        return stage;
    }
}
