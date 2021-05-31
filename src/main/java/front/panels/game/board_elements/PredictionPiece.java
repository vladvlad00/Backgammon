package front.panels.game.board_elements;

import javafx.scene.shape.Circle;

public class PredictionPiece extends Piece{
    private Integer dice1;
    private Integer dice2;

    public PredictionPiece(Circle drawable, Integer position, Integer elevation, Boolean white, Integer dice1, Integer dice2) {
        super(drawable, position, elevation, white);
        this.dice1 = dice1;
        this.dice2 = dice2;
    }

    public Integer getDice1() {
        return dice1;
    }

    public void setDice1(Integer dice1) {
        this.dice1 = dice1;
    }

    public Integer getDice2() {
        return dice2;
    }

    public void setDice2(Integer dice2) {
        this.dice2 = dice2;
    }
}
