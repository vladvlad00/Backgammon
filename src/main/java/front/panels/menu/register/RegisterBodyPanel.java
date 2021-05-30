package front.panels.menu.register;

import front.SceneHandler;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static front.panels.menu.register.MainRegisterFrame.HEIGHT;
import static front.panels.menu.register.MainRegisterFrame.WIDTH;

public class RegisterBodyPanel extends GridPane {
    private final MainRegisterFrame frame;

    private Label username;
    private TextField usernameField;
    private Label password;
    private PasswordField passwordField;
    private Label confirmPassword;
    private PasswordField confirmPasswordField;
    private Button registerButton;

    public VoidOperator refresh;

    public RegisterBodyPanel(MainRegisterFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        username = new Label("Username");
        username.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(username, HPos.CENTER);
        usernameField = new TextField();
        usernameField.setStyle("-fx-font: 20 arial;");
        usernameField.setPromptText("Username");
        usernameField.setPrefSize(0.6 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(usernameField, HPos.CENTER);

        password = new Label("Password");
        password.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(password, HPos.CENTER);
        passwordField = new PasswordField();
        passwordField.setStyle("-fx-font: 20 arial;");
        passwordField.setPromptText("Password");
        passwordField.setPrefSize(0.6 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(passwordField, HPos.CENTER);

        confirmPassword = new Label("Confirm password");
        confirmPassword.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(confirmPassword, HPos.CENTER);
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setStyle("-fx-font: 20 arial;");
        confirmPasswordField.setPromptText("Confirm password");
        confirmPasswordField.setPrefSize(0.6 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(confirmPasswordField, HPos.CENTER);

        registerButton = new Button("Register");
        registerButton.setStyle("-fx-font: 20 arial;");
        registerButton.setOnAction((e) -> {
            if(!passwordField.getText().equals(confirmPasswordField.getText())) {
                frame.getRegisterResponsePanel().createNegative("Passwords do not match", 2L);
                return;
            }
            if(!validatePassword(passwordField.getText())) {
                frame.getRegisterResponsePanel().createNegative("Password is invalid", 2L);
                return;
            }
            User user = User.getInstance();
            user.setUsername(usernameField.getText());
            user.setPassword(passwordField.getText());
            String response = NetworkManager.register(user);
            if(response.equals("ok")) {
                frame.getRegisterResponsePanel().createPositive("Register successful", 1L);
            }
            else {
                frame.getRegisterResponsePanel().createNegative("Register failed", 2L);
            }
        });
        registerButton.setPrefSize(0.25 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(registerButton, HPos.CENTER);

        this.add(username, 1, 1);
        this.add(usernameField, 2, 1);
        this.add(password, 1, 3);
        this.add(passwordField, 2, 3);
        this.add(confirmPassword, 1, 5);
        this.add(confirmPasswordField, 2, 5);
        this.add(registerButton, 1, 7, 2, 1);

        RowConstraints fill = new RowConstraints();
        fill.setPercentHeight(15);
        RowConstraints normal = new RowConstraints();
        normal.setPercentHeight(10);

        ColumnConstraints fillC = new ColumnConstraints();
        fillC.setPercentWidth(20);
        ColumnConstraints normalC = new ColumnConstraints();
        normalC.setPercentWidth(30);

        this.getRowConstraints().addAll(fill, normal, normal, normal, normal, normal, normal, normal, fill);
        this.getColumnConstraints().addAll(fillC, normalC, normalC, fillC);

        refresh = () -> {
            usernameField.setText("");
            usernameField.setPromptText("Username");

            passwordField.setText("");
            passwordField.setPromptText("Password");

            confirmPasswordField.setText("");
            confirmPasswordField.setPromptText("Confirm password");
        };
    }

    private Boolean validatePassword(String pass) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,20}$");
        Matcher matcher = pattern.matcher(pass);

        return matcher.matches();
    }
}
