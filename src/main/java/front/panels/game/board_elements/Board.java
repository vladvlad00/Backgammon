package front.panels.game.board_elements;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final Long WIDTH = 1280L;
    public static final Long HEIGHT = 720L;
    public static final Long CENTER_WIDTH = 40L;
    public static final Long BORDER_SIZE = 10L;
    public static final Long TRIANGLES_ROW = 12L;
    public static final Long TRIANGLE_SPACING = 2L;
    public static final Long TRIANGLE_WIDTH = (WIDTH - CENTER_WIDTH - (TRIANGLES_ROW + 2L) * TRIANGLE_SPACING) / TRIANGLES_ROW;
    public static final Long TRIANGLE_HEIGHT = HEIGHT / 3L;
    public static final Long PIECE_RADIUS = TRIANGLE_WIDTH / 2L;

    private Group node;
    private Rectangle background;
    private Rectangle center;
    private List<Polygon> triangles;
    private List<Double> positions;
    private List<Circle> pieces;

    public Board() {
        node = new Group();
        generateBackground();
        generateCenter();
        generatePositions();
        generateTriangles();
        generatePieces();
    }

    private void generateBackground() {
        background = new Rectangle();
        background.setFill(Color.PERU);
        background.setStroke(Color.BLACK);
        background.setStrokeType(StrokeType.OUTSIDE);
        background.setStrokeWidth(BORDER_SIZE);
        background.setX(0);
        background.setY(0);
        background.setWidth(WIDTH);
        background.setHeight(HEIGHT);

        node.getChildren().add(background);
    }

    private void generateCenter() {
        center = new Rectangle();
        center.setFill(Color.SADDLEBROWN);
        center.setX(WIDTH / 2f - CENTER_WIDTH / 2f);
        center.setY(0);
        center.setWidth(CENTER_WIDTH);
        center.setHeight(HEIGHT);

        node.getChildren().add(center);
    }

    private void generatePositions() {
        positions = new ArrayList<>();
        double x = TRIANGLE_SPACING;

        for(int i = 0; i < 6; ++i) {
            positions.add(x);
            x += (TRIANGLE_WIDTH + TRIANGLE_SPACING);
        }

        x += (TRIANGLE_SPACING + CENTER_WIDTH);

        for(int i = 0; i < 6; ++i) {
            positions.add(x);
            x += (TRIANGLE_WIDTH + TRIANGLE_SPACING);
        }

        x -= (TRIANGLE_SPACING + TRIANGLE_WIDTH);

        for(int i = 0; i < 6; ++i) {
            positions.add(x);
            x -= (TRIANGLE_WIDTH + TRIANGLE_SPACING);
        }

        x -= (TRIANGLE_SPACING + CENTER_WIDTH);

        for(int i = 0; i < 6; ++i) {
            positions.add(x);
            x -= (TRIANGLE_WIDTH + TRIANGLE_SPACING);
        }
    }

    private void generateTriangles() {
        triangles = new ArrayList<>();
        boolean white = false;

        for(int i = 0; i < 12; ++i) {
            triangles.add(TriangleGenerator.getTriangle(white, true, positions.get(i)));
            white = !white;
        }

        for(int i = 0; i < 12; ++i) {
            triangles.add(TriangleGenerator.getTriangle(white, false, positions.get(i + 12)));
            white = !white;
        }

        triangles.forEach(t -> node.getChildren().add(t));
    }

    private void generatePieces() {
        //
    }

    public void drawBoard(GridPane gridPane) {
        GridPane.setHalignment(node, HPos.CENTER);
        GridPane.setValignment(node, VPos.CENTER);
        gridPane.add(node, 0, 0);
    }
}
