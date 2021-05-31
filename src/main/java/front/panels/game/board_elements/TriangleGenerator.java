package front.panels.game.board_elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class TriangleGenerator {
    public static Polygon getTriangle(boolean white, boolean up, Double startX) {
        Polygon triangle = new Polygon();

        if(up) {
            triangle.getPoints().addAll(startX, 0D,
                    startX + Board.TRIANGLE_WIDTH, 0D,
                    startX + Board.TRIANGLE_WIDTH / 2, Double.valueOf(Board.TRIANGLE_HEIGHT));
        }
        else {
            triangle.getPoints().addAll(startX, Double.valueOf(Board.HEIGHT),
                    startX + Board.TRIANGLE_WIDTH, Double.valueOf(Board.HEIGHT),
                    startX + Board.TRIANGLE_WIDTH / 2, Double.valueOf(Board.HEIGHT) - Double.valueOf(Board.TRIANGLE_HEIGHT));
        }

        //triangle.setStrokeWidth(1);

        if(white) {
            triangle.setFill(Color.WHITESMOKE);
            //triangle.setStroke(Color.BLACK);
        }
        else {
            triangle.setFill(Color.BLACK);
            //triangle.setStroke(Color.WHITESMOKE);
        }

        return triangle;
    }
}
