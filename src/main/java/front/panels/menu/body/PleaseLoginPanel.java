package front.panels.menu.body;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class PleaseLoginPanel extends GridPane {
    private Label label;
    private MainMenuFrame frame;

    public PleaseLoginPanel(MainMenuFrame frame) {
        this.frame = frame;
        this.init();
    }

    private void init() {
        label = new Label("Please log in first");
        label.setStyle("-fx-font: 32 arial;");
        GridPane.setHalignment(label, HPos.CENTER);

        this.add(label, 1, 1);

        ColumnConstraints fillColumns = new ColumnConstraints();
        fillColumns.setPercentWidth(25);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        this.getColumnConstraints().addAll(fillColumns, col, fillColumns);
        RowConstraints fillRows = new RowConstraints();
        fillRows.setPercentHeight(25);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(50);
        this.getRowConstraints().addAll(fillRows, row, fillRows);
    }
}
