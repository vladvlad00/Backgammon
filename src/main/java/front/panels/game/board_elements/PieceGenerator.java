package front.panels.game.board_elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class PieceGenerator {
    public static Circle getPiece(boolean white, boolean up, int elevation, Double x) {
        Circle circle = new Circle();

        circle.setRadius(Board.PIECE_RADIUS);
        circle.setCenterX(x + Board.TRIANGLE_WIDTH / 2f);

        if(up) {
            circle.setCenterY((elevation * 2f + 1f) * Board.PIECE_RADIUS);
        }
        else {
            circle.setCenterY(Board.HEIGHT - (elevation * 2f + 1f) * Board.PIECE_RADIUS);
        }

        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(1);

        if(white) {
            circle.setFill(Color.WHITESMOKE);
            circle.setStroke(Color.BLACK);
        }
        else {
            circle.setFill(Color.BLACK);
            circle.setStroke(Color.WHITESMOKE);
        }

        return circle;
    }

    public static Circle getOptionPiece(boolean oneDice, boolean up, int elevation, Double x) {
        Circle circle = new Circle();

        circle.setRadius(Board.PIECE_RADIUS);
        circle.setCenterX(x + Board.TRIANGLE_WIDTH / 2f);

        if(up) {
            circle.setCenterY((elevation * 2f + 1f) * Board.PIECE_RADIUS);
        }
        else {
            circle.setCenterY(Board.HEIGHT - (elevation * 2f + 1f) * Board.PIECE_RADIUS);
        }

        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(1);

        if(oneDice) {
            circle.setFill(Color.GREENYELLOW);
            circle.setStroke(Color.BLACK);
        }
        else {
            circle.setFill(Color.CYAN);
            circle.setStroke(Color.BLACK);
        }

        return circle;
    }
}
