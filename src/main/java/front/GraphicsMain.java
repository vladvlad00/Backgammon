package front;

import front.entities.User;
import front.utils.Cookies;
import front.utils.handlers.LobbyHandler;
import front.utils.handlers.NetworkManager;
import front.utils.handlers.SceneHandler;
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
        SceneHandler.changeScene("game"); //TODO: CHANGE ME BACK TO MENU :(
        stage.show();
    }

    @Override
    public void stop() {
        if(User.getInstance().getInRoom() != null && User.getInstance().getInRoom()) {
            NetworkManager.leaveRoom();
        }
    }
}