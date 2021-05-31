package front.panels.menu.body;

import front.utils.handlers.SceneHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import static front.panels.menu.body.MainMenuFrame.HEIGHT;
import static front.panels.menu.body.MainMenuFrame.WIDTH;

public class LoginRegisterPanel extends GridPane {
    private final MainMenuFrame frame;

    private Button login;
    private Button register;
    private Separator separator;

    public LoginRegisterPanel(MainMenuFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        login = new Button("Log in");
        register = new Button("Register");
        separator = new Separator(Orientation.HORIZONTAL);

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
        separator.setPrefHeight(10);
        separator.setMaxWidth(Double.MAX_VALUE);

        this.add(login, 0, 0);
        this.add(register, 2, 0);
        this.add(separator, 0, 1, 3, 1);

        ColumnConstraints column = new ColumnConstraints();
        ColumnConstraints filler = new ColumnConstraints();
        column.setPercentWidth(40);
        filler.setPercentWidth(20);
        this.getColumnConstraints().addAll(column, filler, column);
    }
}