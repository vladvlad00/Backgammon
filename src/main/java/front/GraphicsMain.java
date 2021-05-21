package front;

import front.utils.Cookies;
import javafx.application.Application;
import javafx.stage.Stage;

public class GraphicsMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Cookies.init();
        SceneHandler.init(stage);

        stage.setTitle("Backgammon");
        SceneHandler.changeScene("menu");
        stage.show();
    }
}