package front.panels.menu.body;

import front.SceneHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import static front.panels.menu.body.MainMenuFrame.HEIGHT;
import static front.panels.menu.body.MainMenuFrame.WIDTH;

public class LoginRegisterPanel extends GridPane {
    private final MainMenuFrame frame;

    private Button login;
    private Button register;

    public LoginRegisterPanel(MainMenuFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        login = new Button("Log in");
        register = new Button("Register");

        login.setOnAction((e) -> {
            SceneHandler.changeScene("login");
        });
        register.setOnAction((e) -> {
            SceneHandler.changeScene("register");
        });

        login.setPrefSize(0.4 * WIDTH, 0.05 * HEIGHT);
        login.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(login, HPos.LEFT);
        register.setPrefSize(0.4 * WIDTH, 0.05 * HEIGHT);
        register.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(register, HPos.RIGHT);

        this.add(login, 0, 0);
        this.add(register, 2, 0);

        ColumnConstraints column = new ColumnConstraints();
        ColumnConstraints filler = new ColumnConstraints();
        column.setPercentWidth(40);
        filler.setPercentWidth(20);
        this.getColumnConstraints().addAll(column, filler, column);
    }
}