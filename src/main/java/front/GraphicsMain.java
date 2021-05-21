package front;

import javafx.application.Application;
import javafx.stage.Stage;

public class GraphicsMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneHandler.init(stage);

        stage.setTitle("Backgammon");
        stage.setScene(SceneHandler.getScene("menu"));
        stage.show();
    }
}