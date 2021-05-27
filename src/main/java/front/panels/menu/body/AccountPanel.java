package front.panels.menu.body;

import front.SceneHandler;
import front.entities.User;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import static front.panels.menu.body.MainMenuFrame.HEIGHT;
import static front.panels.menu.body.MainMenuFrame.WIDTH;

public class AccountPanel extends GridPane {
    private final MainMenuFrame frame;

    private Label name;
    private Button logout;

    public AccountPanel(MainMenuFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        name = new Label("");
        logout = new Button("Log out");


        logout.setOnAction((e) -> {
            User.logout();
            frame.changeLogin();
        });

        name.setPrefSize(0.4 * WIDTH, 0.05 * HEIGHT);
        name.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(name, HPos.LEFT);

        logout.setPrefSize(0.4 * WIDTH, 0.05 * HEIGHT);
        logout.setStyle("-fx-font: 20 arial;");
        GridPane.setHalignment(logout, HPos.RIGHT);

        this.add(name, 0, 0);
        this.add(logout, 2, 0);

        ColumnConstraints column = new ColumnConstraints();
        ColumnConstraints filler = new ColumnConstraints();
        column.setPercentWidth(40);
        filler.setPercentWidth(20);
        this.getColumnConstraints().addAll(column, filler, column);
    }

    public void refresh() {
        name.setText(User.getName());
    }
}
