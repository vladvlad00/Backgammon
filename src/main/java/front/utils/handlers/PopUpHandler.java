package front.utils.handlers;

import front.entities.Lobby;
import front.entities.LobbyUser;
import front.entities.UserRole;
import front.panels.menu.body.MainMenuFrame;
import front.utils.websocket.WSClient;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.ExecutionException;

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
                Lobby response = NetworkManager.joinThroughID(lobby.getId(), UserRole.HOST);
                if(response == null) {
                    throw new NullPointerException();
                }
                else {
                    try {
                        WSClient.getInstance().connect(lobby.getId());
                    } catch (ExecutionException | InterruptedException executionException) {
                        executionException.printStackTrace();
                    }
                    FrameHandler.getMainMenuFrame().goToCreate(response);
                    dialog.close();
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
            }

        });
        dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(textField);
        dialogVbox.getChildren().add(button);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public static void createAIDiffPopUp(Long lobbyID) {
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
        dialog.setTitle("Choose AI difficulty");

        Label label = new Label("Choose AI difficulty");
        label.setPadding(new Insets(10, 10, -10, 10));

        Button easy = new Button("Easy");
        easy.setOnAction(e -> {
            LobbyUser user = NetworkManager.addAI(lobbyID, UserRole.AI_EASY);
            if(user == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Could not add AI");
                a.setTitle("Error");
                a.show();
            }
            FrameHandler.getMainMenuFrame().refreshLobby(lobbyID, true);
            dialog.close();
        });

        easy.setPrefWidth(100);

        Button medium = new Button("Medium");
        medium.setOnAction(e -> {
            LobbyUser user = NetworkManager.addAI(lobbyID, UserRole.AI_MEDIUM);
            if(user == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Could not add AI");
                a.setTitle("Error");
                a.show();
            }
            FrameHandler.getMainMenuFrame().refreshLobby(lobbyID, true);
            dialog.close();
        });
        medium.setPrefWidth(100);

        Button hard = new Button("Hard");
        hard.setOnAction(e -> {
            LobbyUser user = NetworkManager.addAI(lobbyID, UserRole.AI_HARD);
            if(user == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Could not add AI");
                a.setTitle("Error");
                a.show();
            }
            FrameHandler.getMainMenuFrame().refreshLobby(lobbyID, true);
            dialog.close();
        });
        hard.setPrefWidth(100);

        dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(easy);
        dialogVbox.getChildren().add(medium);
        dialogVbox.getChildren().add(hard);
        Scene dialogScene = new Scene(dialogVbox, 200, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public static void createCantMove() {
        Stage mainStage = SceneHandler.getStage();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialog.setMinHeight(200);
        dialog.setMaxHeight(200);
        dialog.setMinWidth(200);
        dialog.setMaxWidth(200);
        dialog.setWidth(200);
        dialog.setHeight(200);
        dialog.setTitle(":(");

        Label label = new Label("No moves available");
        label.setPadding(new Insets(10, 10, 10, 10));

        Button ok = new Button("Ok");
        ok.setOnAction(e -> {
            FrameHandler.getMainGameFrame().getBoardPanel().getBoard().nextTurn();
            dialog.close();
        });
        ok.setPrefWidth(100);

        dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(ok);
        Scene dialogScene = new Scene(dialogVbox, 200, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public static void createSomeoneWon(String won, String username) {
        Stage mainStage = SceneHandler.getStage();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialog.setMinHeight(300);
        dialog.setMaxHeight(300);
        dialog.setMinWidth(300);
        dialog.setMaxWidth(300);
        dialog.setWidth(300);
        dialog.setHeight(300);
        dialog.setTitle("Winner");

        Label label = new Label(won + "(" + username + ") won!\nCongratulations!");
        label.setPadding(new Insets(10, 10, 10, 10));

        Button ok = new Button("Back to menu");
        ok.setOnAction(e -> {
            NetworkManager.leaveRoom();
            SceneHandler.changeScene("menu");
            dialog.close();
        });
        ok.setPrefWidth(200);

        Button exit = new Button("Exit game");
        exit.setOnAction(e -> {
            Platform.exit();
            dialog.close();
        });
        exit.setPrefWidth(150);

        dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(ok);
        dialogVbox.getChildren().add(exit);
        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public static void createSomeoneWon(String won, String username, String left) {
        Stage mainStage = SceneHandler.getStage();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialog.setMinHeight(300);
        dialog.setMaxHeight(300);
        dialog.setMinWidth(300);
        dialog.setMaxWidth(300);
        dialog.setWidth(300);
        dialog.setHeight(300);
        dialog.setTitle("Winner");

        Label label = new Label(won + "(" + username + ") won, \n" + left + " left the game.\nCongratulations!");
        label.setPadding(new Insets(10, 10, 10, 10));

        Button ok = new Button("Back to menu");
        ok.setOnAction(e -> {
            NetworkManager.leaveRoom();
            SceneHandler.changeScene("menu");
            dialog.close();
        });
        ok.setPrefWidth(200);

        Button exit = new Button("Exit game");
        exit.setOnAction(e -> {
            Platform.exit();
            dialog.close();
        });
        exit.setPrefWidth(150);

        dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(ok);
        dialogVbox.getChildren().add(exit);
        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
