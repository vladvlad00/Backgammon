package front.panels.menu.body;

import front.SceneHandler;
import front.entities.User;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import static front.panels.menu.body.MainMenuFrame.HEIGHT;
import static front.panels.menu.body.MainMenuFrame.WIDTH;

public class AccountPanel extends GridPane {
    private final MainMenuFrame frame;

    private Label name;
    private Button logout;
    private Separator separator;

    public AccountPanel(MainMenuFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        name = new Label("");
        logout = new Button("Log out");
        separator = new Separator(Orientation.HORIZONTAL);

        logout.setOnAction((e) -> {
            User.getInstance().logout();
            frame.changeLogin();
        });

        name.setPrefSize(0.4 * WIDTH, 0.05 * HEIGHT);
        name.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(name, HPos.LEFT);

        logout.setPrefSize(0.3 * WIDTH, 0.025 * HEIGHT);
        logout.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(logout, HPos.RIGHT);

        separator.setPrefHeight(10);
        separator.setMaxWidth(Double.MAX_VALUE);

        this.add(name, 1, 0);
        this.add(logout, 3, 0);
        this.add(separator, 0, 1, 4, 1);

        ColumnConstraints column = new ColumnConstraints();
        ColumnConstraints filler = new ColumnConstraints();
        column.setPercentWidth(40);
        filler.setPercentWidth(10);
        this.getColumnConstraints().addAll(filler, column, filler, column);
    }

    public void refresh() {
        name.setText(User.getInstance().getUsername());
    }
}
