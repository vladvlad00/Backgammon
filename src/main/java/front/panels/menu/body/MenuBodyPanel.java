package front.panels.menu.body;

import front.utils.handlers.LobbyHandler;
import front.utils.handlers.PopUpHandler;
import front.utils.handlers.NetworkManager;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import static front.panels.menu.login.MainLoginFrame.HEIGHT;
import static front.panels.menu.login.MainLoginFrame.WIDTH;

public class MenuBodyPanel extends GridPane {
    private final MainMenuFrame frame;

    private Button create;
    private Button list;
    private Button join;

    public MenuBodyPanel(MainMenuFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        create = new Button("Create new lobby");
        create.setStyle("-fx-font: 20 arial;");
        create.setPrefSize(0.5 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(create, HPos.CENTER);
        create.setOnAction(e -> PopUpHandler.createNewLobbyPopUp(frame));

        list = new Button("Search for a lobby");
        list.setStyle("-fx-font: 20 arial;");
        list.setPrefSize(0.5 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(list, HPos.CENTER);
        list.setOnAction(e -> {
            LobbyHandler.init(NetworkManager.getAllLobbies());
            frame.goToSearch();
        });

        join = new Button("Join a lobby");
        join.setStyle("-fx-font: 20 arial;");
        join.setPrefSize(0.5 * WIDTH, 0.1 * HEIGHT);
        GridPane.setHalignment(join, HPos.CENTER);
        join.setOnAction(e -> PopUpHandler.createJoinLobbyPopUp(frame));

        this.add(create, 1, 1);
        this.add(list, 1, 3);
        this.add(join, 1, 5);

        RowConstraints fill = new RowConstraints();
        fill.setPercentHeight(10);
        RowConstraints normal = new RowConstraints();
        normal.setPercentHeight(20);

        ColumnConstraints fillC = new ColumnConstraints();
        fillC.setPercentWidth(25);
        ColumnConstraints normalC = new ColumnConstraints();
        normalC.setPercentWidth(50);

        this.getRowConstraints().addAll(fill, normal, fill, normal, fill, normal, fill);
        this.getColumnConstraints().addAll(fillC, normalC, fillC);
    }
}