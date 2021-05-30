package front.panels.menu.login;


import front.FrameHandler;
import front.entities.User;
import front.utils.NetworkManager;
import front.utils.VoidOperator;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import static front.panels.menu.login.MainLoginFrame.WIDTH;
import static front.panels.menu.login.MainLoginFrame.HEIGHT;


public class LoginBodyPanel extends GridPane {
    private final MainLoginFrame frame;

    private Label username;
    private TextField usernameField;
    private Label password;
    private PasswordField passwordField;
    private Button loginButton;

    public VoidOperator refresh;

    public LoginBodyPanel(MainLoginFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        username = new Label("Username");
        username.setStyle("-fx-font: 24 arial;");
        GridPane.setHalignment(username, HPos.CENTER);
        usernameField = new TextField();
        usernameField.setStyle("-fx-font: 20 arial;");
        usernameField.setPromptText("Username");
        usernameField.setPrefSize(0.6 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(usernameField, HPos.CENTER);

        password = new Label("Password");
        password.setStyle("-fx-font: 24 arial;");
        GridPane.setHalignment(password, HPos.CENTER);
        passwordField = new PasswordField();
        passwordField.setStyle("-fx-font: 20 arial;");
        passwordField.setPromptText("Password");
        passwordField.setPrefSize(0.6 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(passwordField, HPos.CENTER);

        loginButton = new Button("Log in");
        loginButton.setStyle("-fx-font: 20 arial;");
        loginButton.setOnAction((e) -> {
            User user = User.getInstance();
            user.setUsername(usernameField.getText());
            user.setPassword(passwordField.getText());
            String response = NetworkManager.login(user);
            if(response.startsWith("ok")) {
                frame.getLoginResponsePanel().createPositive("Login successful", 1L);
                user.setToken(response.split(" ")[1]);
                FrameHandler.getMainMenuFrame().changeLogin();
            }
            else {
                frame.getLoginResponsePanel().createNegative("Login failed\n" +
                        (response.contains("403") ? "Account does not exist" : "Exception")
                        , 2L);
            }
        });
        loginButton.setPrefSize(0.25 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(loginButton, HPos.CENTER);

        this.add(username, 1, 1);
        this.add(usernameField, 2, 1);
        this.add(password, 1, 3);
        this.add(passwordField, 2, 3);
        this.add(loginButton, 1, 5, 2, 1);

        RowConstraints fill = new RowConstraints();
        fill.setPercentHeight(20);
        RowConstraints normal = new RowConstraints();
        normal.setPercentHeight(12);

        ColumnConstraints fillC = new ColumnConstraints();
        fillC.setPercentWidth(20);
        ColumnConstraints normalC = new ColumnConstraints();
        normalC.setPercentWidth(30);

        this.getRowConstraints().addAll(fill, normal, normal, normal, normal, normal, fill);
        this.getColumnConstraints().addAll(fillC, normalC, normalC, fillC);

        refresh = () -> {
            usernameField.setText("");
            usernameField.setPromptText("Username");

            passwordField.setText("");
            passwordField.setPromptText("Password");
        };
    }
}
