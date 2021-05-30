package front;

import front.LobbyHandler;
import front.SceneHandler;
import front.entities.Lobby;
import front.entities.UserRole;
import front.panels.menu.body.MainMenuFrame;
import front.utils.NetworkManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.ParseException;

public class PopUpHandler {

    private PopUpHandler() {}

    public static void createNewLobbyPopUp(MainMenuFrame frame) {
        Stage mainStage = SceneHandler.getStage();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialog.setMinHeight(200);
        dialog.setMaxHeight(200);
        dialog.setMinWidth(300);
        dialog.setMaxWidth(300);
        dialog.setWidth(300);
        dialog.setHeight(200);
        dialog.setTitle("Create a new lobby");

        Label label = new Label("Lobby name");
        label.setPadding(new Insets(10, 10, -20, 10));
        TextField textField = new TextField();
        textField.setPromptText("Lobby name");

        Button button = new Button("Create lobby");
        button.setOnAction(e -> {
            Lobby lobby = NetworkManager.createRoom(textField.getText());
            if(lobby == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Could not create lobby");
                a.setTitle("Error");
                a.show();
            }
            else {
                Lobby response = NetworkManager.joinThroughID(lobby.getId(), lobby.getPlayerNum() < 2 ? UserRole.PLAYER : UserRole.SPECTATOR);
                if(response == null) {
                    throw new NullPointerException();
                }
                else {
                    if (true) {
                        FrameHandler.getMainMenuFrame().goToCreate(response);
                        dialog.close();
                    }
                    else {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Could not join lobby");
                        a.setTitle("Error");
                        a.show();
                    }
                }
            }
        });

        dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(textField);
        dialogVbox.getChildren().add(button);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public static void createJoinLobbyPopUp(MainMenuFrame frame) {
        Stage mainStage = SceneHandler.getStage();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialog.setMinHeight(200);
        dialog.setMaxHeight(200);
        dialog.setMinWidth(300);
        dialog.setMaxWidth(300);
        dialog.setWidth(300);
        dialog.setHeight(200);
        dialog.setTitle("Join a lobby");

        Label label = new Label("Lobby id");
        label.setPadding(new Insets(10, 10, -20, 10));
        TextField textField = new TextField();
        textField.setPromptText("Lobby id");
        Button button = new Button("Join lobby");
        button.setOnAction(e -> {
            Long id;
            try {
                id = Long.parseLong(textField.getText());
                String response = LobbyHandler.joinThroughID(id);
                if(response.equals("succ")) {
                    dialog.close();
                }
                else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Could not join lobby");
                    a.setTitle("Error");
                    a.show();
                }
            }
            catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Please input a valid ID");
                a.setTitle("Error");
                a.show();
                return;
            }

        });
        dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(textField);
        dialogVbox.getChildren().add(button);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
