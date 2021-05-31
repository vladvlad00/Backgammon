package front.panels.game;

import front.panels.game.board_elements.Board;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BoardPanel extends GridPane {
    private MainGameFrame frame;

    private Board board;

    public BoardPanel(MainGameFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        board = new Board();

        board.drawBoard(this);

        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        rowConstraints.setPercentHeight(100);
        columnConstraints.setPercentWidth(100);

        this.getColumnConstraints().addAll(columnConstraints);
        this.getRowConstraints().addAll(rowConstraints);
    }
}
