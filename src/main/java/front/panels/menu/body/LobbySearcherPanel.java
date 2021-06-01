package front.panels.menu.body;

import front.utils.handlers.BackgammonEvent;
import front.utils.handlers.LobbyHandler;
import front.entities.Lobby;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class LobbySearcherPanel extends GridPane {
    private final MainMenuFrame frame;

    private ListView<LobbyListItemPanel> list;
    private ObservableList<LobbyListItemPanel> items;
    private Button back;

    public LobbySearcherPanel(MainMenuFrame frame) {
        this.frame = frame;
        init();
    }

    public void init() {
        list = new ListView<>();
        items = FXCollections.observableArrayList();
        GridPane.setHalignment(list, HPos.CENTER);

        back = new Button("<-");
        back.setOnAction(e -> frame.goToMenu());
        GridPane.setValignment(back, VPos.CENTER);

        this.add(back, 0, 0);
        this.add(list, 0, 1);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(97);
        RowConstraints backConstraints = new RowConstraints();
        backConstraints.setPercentHeight(3);
        this.getColumnConstraints().addAll(columnConstraints);
        this.getRowConstraints().addAll(backConstraints, rowConstraints);
    }

    public void refresh() {
        items.clear();
        for(Lobby lobby : LobbyHandler.getAll()) {
            if(lobby.getState().equals("not started")) {
                items.add(new LobbyListItemPanel(lobby));
            }
        }
        list.setItems(items);
    }
}

