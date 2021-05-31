package front.panels.game.board_elements;

import back.service.game.GameManager;
import front.entities.User;
import front.utils.handlers.FrameHandler;
import front.utils.handlers.GameHandler;
import front.utils.websocket.Message;
import front.utils.websocket.WSClient;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.*;

public class Board {
    public static final Long WIDTH = 1280L;
    public static final Long HEIGHT = 820L;
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

    private PredictionPiece show1, show2, show3, show4;

    public Board() {
        node = new Group();
        generateBackground();
        generateCenter();
        generatePositions();
        generateTriangles();
        addPieces(STARTING_BOARD);
        generateClickable();
    }

    public void setBoard(String board) {
        removePieces();
        addPieces(board);
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
        pieces.forEach(p -> node.getChildren().add(p.getDrawable()));
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
    }

    private void generateClickable() {
        for(Piece piece : pieces) {
            if(piece instanceof PredictionPiece) {
                continue;
            }
            boolean ok = true;
            for(Piece compared : pieces) {
                if(!compared.equals(piece) && (compared.getElevation() > piece.getElevation() && compared.getPosition().equals(piece.getPosition()))) {
                    ok = false;
                    break;
                }
            }
            if(ok) {
                piece.getDrawable().setOnMouseClicked(e -> showOptions(piece));
            }
        }
    }

    public void drawBoard(GridPane gridPane) {
        GridPane.setHalignment(node, HPos.CENTER);
        GridPane.setValignment(node, VPos.CENTER);
        gridPane.add(node, 0, 0);
    }

    private void removePredictions() {
        if(show1 != null) {
            node.getChildren().remove(show1.getDrawable());
        }
        if(show2 != null) {
            node.getChildren().remove(show2.getDrawable());
        }
        if(show3 != null) {
            node.getChildren().remove(show3.getDrawable());
        }
        if(show4 != null) {
            node.getChildren().remove(show4.getDrawable());
        }
    }

    private void showOptions(Piece piece) {
        removePredictions();
        if(User.getInstance().getInRoom() && User.getInstance().getUsername().equals(GameHandler.getCurrentUser().getUsername())) {
            if(piece.getWhite().equals(GameHandler.getCurrentUser().getWhite())) {
                Integer d1 = FrameHandler.getMainGameFrame().getSidePanel().getFirstDice(); //Move using first dice
                if(d1 == null) {
                    d1 = -1;
                }
                Integer d2 = FrameHandler.getMainGameFrame().getSidePanel().getSecondDice(); //Move using second dice
                if(d2 == null) {
                    d2 = -1;
                }
                boolean dblCondition = d1.equals(d2) && !d1.equals(-1);
                int maxCount = FrameHandler.getMainGameFrame().getSidePanel().getDoubleCount();
                Integer sum = (d1 == -1 ? 0 : d1) + (d2 == -1 ? 0 : d2); //Move using both dice
                Integer dbl = d1 * 4; //Move using 4 dice, if you double
                if(dblCondition) {
                    d2 = d1 * 2; //Move using 2 dice, if you double
                    sum = d1 * 3; //Move using 3 dice, if you double
                    if(maxCount < 4) {
                        dbl = -1;
                    }
                    if(maxCount < 3) {
                        sum = -1;
                    }
                    if(maxCount < 2) {
                        d2 = -1;
                    }
                    if(maxCount < 1) {
                        d1 = -1;
                    }
                }

                int crtPos = piece.getPosition();
                int p1;
                int p2;
                int p3;
                int p4;
                if(piece.getWhite()) {
                    if(d1 < 0) {
                        p1 = -100;
                    }
                    else {
                        p1 = normalToWhite.get(crtPos) - d1;
                        if (p1 > 0 && p1 < 25) {
                            p1 = whiteToNormal.get(p1);
                        } else {
                            p1 = -2;
                        }
                    }
                    if(d2 < 0) {
                        p2 = -100;
                    }
                    else {
                        p2 = normalToWhite.get(crtPos) - d2;
                        if (p2 > 0 && p2 < 25) {
                            p2 = whiteToNormal.get(p2);
                        } else {
                            p2 = -2;
                        }
                    }
                    if(sum < 0) {
                        p3 = -100;
                    }
                    else {
                        p3 = normalToWhite.get(crtPos) - sum;
                        if (p3 > 0 && p3 < 25) {
                            p3 = whiteToNormal.get(p3);
                        } else {
                            p3 = -2;
                        }
                    }
                    if(dbl < 0) {
                        p4 = -100;
                    }
                    else {
                        p4 = normalToWhite.get(crtPos) - dbl;
                        if (p4 > 0 && p4 < 25) {
                            p4 = whiteToNormal.get(p4);
                        } else {
                            p4 = -2;
                        }
                    }
                }
                else {
                    if(d1 < 0) {
                        p1 = -100;
                    }
                    else {
                        p1 = normalToBlack.get(crtPos) - d1;
                        if (p1 > 0 && p1 < 25) {
                            p1 = blackToNormal.get(p1);
                        } else {
                            p1 = -2;
                        }
                    }
                    if(d2 < 0) {
                        p2 = -100;
                    }
                    else {
                        p2 = normalToBlack.get(crtPos) - d2;
                        if (p2 > 0 && p2 < 25) {
                            p2 = blackToNormal.get(p2);
                        } else {
                            p2 = -2;
                        }
                    }
                    if(sum < 0) {
                        p3 = -100;
                    }
                    else {
                        p3 = normalToBlack.get(crtPos) - sum;
                        if (p3 > 0 && p3 < 25) {
                            p3 = blackToNormal.get(p3);
                        } else {
                            p3 = -2;
                        }
                    }
                    if(dbl < 0) {
                        p4 = -100;
                    }
                    else {
                        p4 = normalToBlack.get(crtPos) - dbl;
                        if (p4 > 0 && p4 < 25) {
                            p4 = blackToNormal.get(p4);
                        } else {
                            p4 = -2;
                        }
                    }
                }
                boolean ok;
                int largerOk = 2;
                int elevation;
                Piece capturablePiece;
                if(p1 > -1 && p1 < 24) {
                    ok = true;
                    elevation = 0;
                    capturablePiece = null;
                    for (Piece verify : pieces) {
                        if (verify.getPosition().equals(p1)) {
                            if(!verify.getWhite().equals(piece.getWhite())) {
                                int enemyElevation = verify.getElevation();
                                for(Piece getElev : pieces) {
                                    if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > enemyElevation) {
                                        enemyElevation = getElev.getElevation();
                                    }
                                }
                                if(enemyElevation < 1) {
                                    capturablePiece = verify;
                                }
                                else {
                                    ok = false;
                                    --largerOk;
                                }
                            }
                            else {
                                elevation = verify.getElevation();
                                for(Piece getElev : pieces) {
                                    if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > elevation) {
                                        elevation = getElev.getElevation();
                                    }
                                }
                                ++elevation;
                            }
                            break;
                        }
                    }
                    if(ok) {
                        show1 = new PredictionPiece(PieceGenerator.getOptionPiece(true, p1 <= 11, elevation, positions.get(p1)), p1, elevation, false, d1, -1);
                        Piece finalCapturablePiece = capturablePiece;
                        if(dblCondition) {
                            show1.getDrawable().setOnMouseClicked(e -> {
                                if(makeMove(piece, 1)) {
                                    capturePiece(finalCapturablePiece);
                                }
                            }); //User double, and he moved 3
                        }
                        else {
                            Integer finalD1 = d1;
                            show1.getDrawable().setOnMouseClicked(e -> {
                                if(makeMove(piece, finalD1, -1)) {
                                    capturePiece(finalCapturablePiece);
                                }
                            });
                        }
                        node.getChildren().add(show1.getDrawable());
                    }
                }
                if(p2 > -1 && p2 < 24 && (!dblCondition || largerOk == 2)) {
                    capturablePiece = null;
                    ok = true;
                    elevation = 0;
                    for (Piece verify : pieces) {
                        if (verify.getPosition().equals(p2)) {
                            if(!verify.getWhite().equals(piece.getWhite())) {
                                int enemyElevation = verify.getElevation();
                                for(Piece getElev : pieces) {
                                    if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > enemyElevation) {
                                        enemyElevation = getElev.getElevation();
                                    }
                                }
                                if(enemyElevation < 1) {
                                    capturablePiece = verify;
                                }
                                else {
                                    ok = false;
                                    --largerOk;
                                }
                            }
                            else {
                                elevation = verify.getElevation();
                                for(Piece getElev : pieces) {
                                    if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > elevation) {
                                        elevation = getElev.getElevation();
                                    }
                                }
                                ++elevation;
                            }
                            break;
                        }
                    }
                    if(ok) {
                        show2 = new PredictionPiece(PieceGenerator.getOptionPiece(true, p2 <= 11, elevation, positions.get(p2)), p2, elevation, false, -1, d2);
                        Piece finalCapturablePiece = capturablePiece;
                        if(dblCondition) {
                            show2.getDrawable().setOnMouseClicked(e -> {
                                if(makeMove(piece, 2)) {
                                    capturePiece(finalCapturablePiece);
                                }
                            }); //User double, and he moved 3
                        }
                        else {
                            Integer finalD2 = d2;
                            show2.getDrawable().setOnMouseClicked(e -> {
                                if(makeMove(piece, -1, finalD2)) {
                                    capturePiece(finalCapturablePiece);
                                }
                            });
                        }
                        node.getChildren().add(show2.getDrawable());
                    }
                }
                if(largerOk > 0 && (!dblCondition || largerOk == 2)) {
                    if(p3 > -1 && p3 < 24) {
                        capturablePiece = null;
                        ok = true;
                        elevation = 0;
                        for (Piece verify : pieces) {
                            if (verify.getPosition().equals(p3)) {
                                if(!verify.getWhite().equals(piece.getWhite())) {
                                    int enemyElevation = verify.getElevation();
                                    for(Piece getElev : pieces) {
                                        if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > enemyElevation) {
                                            enemyElevation = getElev.getElevation();
                                        }
                                    }
                                    if(enemyElevation < 1) {
                                        capturablePiece = verify;
                                    }
                                    else {
                                        ok = false;
                                        --largerOk;
                                    }
                                }
                                else {
                                    elevation = verify.getElevation();
                                    for(Piece getElev : pieces) {
                                        if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > elevation) {
                                            elevation = getElev.getElevation();
                                        }
                                    }
                                    ++elevation;
                                }
                                break;
                            }
                        }
                        if(ok) {
                            show3 = new PredictionPiece(PieceGenerator.getOptionPiece(false, p3 <= 11, elevation, positions.get(p3)), p3, elevation, false, d1, d2);
                            Piece finalCapturablePiece = capturablePiece;
                            if(dblCondition) {
                                show3.getDrawable().setOnMouseClicked(e -> {
                                    if(makeMove(piece, 3)) {
                                        capturePiece(finalCapturablePiece);
                                    }
                                });
                            }
                            else {
                                Integer finalD1 = d1;
                                Integer finalD2 = d2;
                                show3.getDrawable().setOnMouseClicked(e -> {
                                    if(makeMove(piece, finalD1, finalD2)) {
                                        capturePiece(finalCapturablePiece);
                                    }
                                });
                            }
                            node.getChildren().add(show3.getDrawable());
                        }
                    }
                }
                if(largerOk == 2 && dblCondition) {
                    if(p4 > -1 && p4 < 24) {
                        capturablePiece = null;
                        ok = true;
                        elevation = 0;
                        for (Piece verify : pieces) {
                            if (verify.getPosition().equals(p4)) {
                                if(!verify.getWhite().equals(piece.getWhite())) {
                                    int enemyElevation = verify.getElevation();
                                    for(Piece getElev : pieces) {
                                        if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > enemyElevation) {
                                            enemyElevation = getElev.getElevation();
                                        }
                                    }
                                    if(enemyElevation < 1) {
                                        capturablePiece = verify;
                                    }
                                    else {
                                        ok = false;
                                        --largerOk;
                                    }
                                }
                                else {
                                    elevation = verify.getElevation();
                                    for(Piece getElev : pieces) {
                                        if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > elevation) {
                                            elevation = getElev.getElevation();
                                        }
                                    }
                                    ++elevation;
                                }
                                break;
                            }
                        }
                        if(ok) {
                            show4 = new PredictionPiece(PieceGenerator.getOptionPiece(false, p4 <= 11, elevation, positions.get(p4)), p4, elevation, false, d1, d1);
                            Piece finalCapturablePiece1 = capturablePiece;
                            show4.getDrawable().setOnMouseClicked(e -> {
                                if(makeMove(piece, 4)) {
                                    capturePiece(finalCapturablePiece1);
                                }
                            });
                            node.getChildren().add(show4.getDrawable());
                        }
                    }
                }
            }
        }
    }

    private void removePieces() {
        for(Piece piece : pieces) {
            node.getChildren().remove(piece.getDrawable());
        }
    }

    private boolean makeMove(Piece pos, int d1, int d2) {
        removePredictions();
        int move = 0;
        if(d1 != -1) {
            if(!FrameHandler.getMainGameFrame().getSidePanel().getFirstDiceAvailable()) {
                return false;
            }
            FrameHandler.getMainGameFrame().getSidePanel().disableFirstDice();
            move += d1;
        }
        if(d2 != -1) {
            if(!FrameHandler.getMainGameFrame().getSidePanel().getSecondDiceAvailable()) {
                return false;
            }
            FrameHandler.getMainGameFrame().getSidePanel().disableSecondDice();
            move += d2;
        }
        sendMove(pos.getPosition(), move);
        return true;
    }

    private boolean makeMove(Piece pos, int count) {
        removePredictions();
        if(FrameHandler.getMainGameFrame().getSidePanel().getDoubleCount() < count) {
            return false;
        }
        FrameHandler.getMainGameFrame().getSidePanel().decreaseDoubleCount(count);

        int move = FrameHandler.getMainGameFrame().getSidePanel().getFirstDice() * count;
        sendMove(pos.getPosition(), move);
        return true;
    }

    private void sendMove(int pos, int count) {
        Map<String, String> options = new HashMap<>();
        if(GameHandler.getCurrentUser().getWhite()) {
            options.put("color", "white");
            options.put("initialPosition", String.valueOf(normalToWhite.get(pos)));
        }
        else {
            options.put("color", "black");
            options.put("initialPosition", String.valueOf(normalToBlack.get(pos)));
        }
        options.put("die", String.valueOf(count));
        WSClient.getInstance().sendMessage(new Message("move", options));
    }

    private void capturePiece(Piece piece) {
        //Capture piece
    }
}
