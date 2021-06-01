package front.panels.game.board_elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece {
    private Circle drawable;
    private Integer position;
    private Integer elevation;
    private Boolean white;
    private Boolean winnable;

    public Piece(Circle drawable, Integer position, Integer elevation, Boolean white) {
        this.drawable = drawable;
        this.elevation = elevation;
        this.position = position;
        this.white = white;
        this.winnable = false;
    }

    public Circle getDrawable() {
        return drawable;
    }

    public void setDrawable(Circle drawable) {
        this.drawable = drawable;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public Boolean getWhite() {
        return white;
    }

    public void setWhite(Boolean white) {
        this.white = white;
    }

    public Boolean getWinnable() {
        return winnable;
    }

    public void setWinnable(Boolean winnable) {
        if(winnable) {
            this.drawable.setStroke(Color.YELLOWGREEN);
            this.drawable.setStrokeWidth(5);
        }
        else {
            this.drawable.setStroke(white ? Color.BLACK : Color.WHITESMOKE);
            this.drawable.setStrokeWidth(1);
        }
        this.winnable = winnable;
    }
}
