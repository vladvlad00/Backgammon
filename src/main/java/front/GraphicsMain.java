package front;

import front.entities.LobbyUser;
import front.entities.User;
import front.entities.UserRole;
import front.utils.Cookies;
import front.utils.handlers.*;
import front.utils.websocket.Message;
import front.utils.websocket.WSClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void stop() {
        if(FrameHandler.getMainGameFrame().getLobby() != null) {
            for(LobbyUser lobbyUser : FrameHandler.getMainGameFrame().getLobby().getUsers()) {
                if(lobbyUser.getUsername().equals(User.getInstance().getUsername())) {
                    if(FrameHandler.getMainGameFrame().getLobby().getRoleOfUser(User.getInstance().getUsername()).equals(UserRole.SPECTATOR) || FrameHandler.getMainGameFrame().getLobby().getRoleOfUser(User.getInstance().getUsername()).equals(UserRole.HOST_SPECTATOR)) {
                        Map<String, String> options = new HashMap<>();
                        options.put("count", "-1");
                        WSClient.getInstance().sendMessage(new Message("spectator", options));
                        FrameHandler.getMainMenuFrame().goToMenu();
                        NetworkManager.leaveRoom();
                        SceneHandler.changeScene("menu");
                    }
                    else {
                        Map<String, String> options = new HashMap<>();
                        String color;
                        if(GameHandler.getBlackUser().getUsername().equals(User.getInstance().getUsername())) {
                            color = "black";
                        }
                        else {
                            color = "white";
                        }
                        options.put("color", color);
                        options.put("user", User.getInstance().getUsername());
                        WSClient.getInstance().sendMessage(new Message("disconnect", options));
                    }
                }
            }
        }
        else if(User.getInstance().getInRoom() != null && User.getInstance().getInRoom()) {
            NetworkManager.leaveRoom();
        }
    }
}