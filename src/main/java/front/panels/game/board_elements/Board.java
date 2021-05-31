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
import java.util.Arrays;
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
    public static final Long PIECE_RADIUS = TRIANGLE_WIDTH / 3L;
    private static final String STARTING_BOARD = "[0, 0, 0, 0, 0, 0, 5, 0, 3, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2][0, 0, 0, 0, 0, 0, 5, 0, 3, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2]";
    private static final List<Integer> whiteToNormal = new ArrayList<>(Arrays.asList(
            24, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
    ));
    private static final List<Integer> blackToNormal = new ArrayList<>(Arrays.asList(
            24, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12
    ));
    private static final List<Integer> normalToWhite = new ArrayList<>(Arrays.asList(
            13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0
    ));
    private static final List<Integer> normalToBlack = new ArrayList<>(Arrays.asList(
            12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 0
    ));

    private Group node;
    private Rectangle background;
    private Rectangle center;
    private List<Polygon> triangles;
    private List<Double> positions;
    private List<Piece> pieces;
    private List<Integer> elevation;
    private List<Integer> whites;
    private List<Integer> blacks;
    private Integer whitesOutside;
    private Integer blackOutside;

    public Board() {
        node = new Group();
        generateBackground();
        generateCenter();
        generatePositions();
        generateTriangles();
        addPieces(STARTING_BOARD);
        generateClickable();
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

    public void addPieces(String convertMe) {
        pieces = new ArrayList<>();
        String[] parts = convertMe.split("]\\[");
        whites = convertWtoN(convertString(parts[0]));
        blacks = convertBtoN(convertString(parts[1]));
        generatePieces(whites, true);
        generatePieces(blacks, false);
    }

    private List<Integer> convertWtoN(List<Integer> items) {
        List<Integer> newItems = new ArrayList<>();
        for(int i = 0; i < 25; ++i) {
            newItems.add(items.get(normalToWhite.get(i)));
        }
        return newItems;
    }

    private List<Integer> convertBtoN(List<Integer> items) {
        List<Integer> newItems = new ArrayList<>();
        for(int i = 0; i < 25; ++i) {
            newItems.add(items.get(normalToBlack.get(i)));
        }
        return newItems;
    }

    public List<Integer> convertString(String string) {
        List<Integer> items = new ArrayList<>();
        String[] elements = string.replace("[", "").replace("]", "").split(", ");
        for(String element : elements) {
            items.add(Integer.parseInt(element));
        }
        return items;
    }

    private void generatePieces(List<Integer> items, boolean white) {
        pieces = new ArrayList<>();
        for(int i = 0; i < 12; ++i) {
            for(int j = 0; j < items.get(i); ++j) {
                pieces.add(new Piece(PieceGenerator.getPiece(white, true, j, positions.get(i)), i, j, white));
            }
        }

        for(int i = 0; i < 12; ++i) {
            for(int j = 0; j < items.get(i + 12); ++j) {
                pieces.add(new Piece(PieceGenerator.getPiece(white, false, j, positions.get(i + 12)), i + 12, j, white));
            }
        }

        pieces.forEach(p -> node.getChildren().add(p.getDrawable()));
    }

    private void generateClickable() {
        for(Piece piece : pieces) {
            boolean ok = true;
            for(Piece compared : pieces) {
                if(!compared.equals(piece) && compared.getElevation() > piece.getElevation()) {
                    ok = false;
                    break;
                }
            }
            if(ok) {
                piece.getDrawable().setOnMouseClicked(e -> {

                });
            }
        }
    }

    public void drawBoard(GridPane gridPane) {
        GridPane.setHalignment(node, HPos.CENTER);
        GridPane.setValignment(node, VPos.CENTER);
        gridPane.add(node, 0, 0);
    }
}
