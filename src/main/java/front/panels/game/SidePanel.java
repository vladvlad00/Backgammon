package front.panels.game;

import front.panels.game.board_elements.Dice;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class SidePanel extends GridPane {
    private MainGameFrame frame;

    private Button rollDice;
    private Button leaveGame;
    private Dice firstDice;
    private Dice secondDice;

    public SidePanel(MainGameFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        firstDice = new Dice(1);
        firstDice.setSize(100, 100);
        firstDice.setPosition(0, 0);

        secondDice = new Dice(1);
        secondDice.setSize(100, 100);
        secondDice.setPosition(0, 0);

        rollDiceAction();

        rollDice = new Button("Roll dice");
        rollDice.setStyle("-fx-font: 20 arial;");
        rollDice.setOnAction(e -> {
            rollDiceAction();
        });
        GridPane.setHalignment(rollDice, HPos.CENTER);

        leaveGame = new Button("Leave");
        leaveGame.setStyle("-fx-font: 20 arial;");
        leaveGame.setOnAction(e -> {
            //leave game
        });
        GridPane.setHalignment(leaveGame, HPos.CENTER);

        this.add(rollDice, 0, 0, 2, 1);
        this.add(leaveGame, 0, 2, 2, 1);

        RowConstraints small = new RowConstraints();
        small.setPercentHeight(10);
        RowConstraints big = new RowConstraints();
        big.setPercentHeight(80);

        ColumnConstraints eq = new ColumnConstraints();
        eq.setPercentWidth(50);

        this.getColumnConstraints().addAll(eq, eq);
        this.getRowConstraints().addAll(small, big, small);
    }

    public void rollDiceAction() {
        int fst = firstDice.randomDice();
        firstDice.removeFromChildren(this);
        firstDice.addToChildren(this, 0);
        int scd = secondDice.randomDice();
        secondDice.removeFromChildren(this);
        secondDice.addToChildren(this, 1);
    }
}
