package front;

import front.utils.Cookies;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GraphicsMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Cookies.init();
        SceneHandler.init(stage);
        LobbyHandler.init(new ArrayList<>());

        stage.setTitle("Backgammon");
        SceneHandler.changeScene("menu");
        stage.show();
    }
}