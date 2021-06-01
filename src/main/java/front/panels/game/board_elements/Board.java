package front.panels.game.board_elements;

import back.service.game.GameManager;
import front.entities.User;
import front.utils.handlers.FrameHandler;
import front.utils.handlers.GameHandler;
import front.utils.handlers.PopUpHandler;
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
    //private static final String STARTING_BOARD = "[0, 0, 0, 0, 0, 0, 5, 0, 3, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2][0, 0, 0, 0, 0, 0, 5, 0, 3, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2]";
    private static final String STARTING_BOARD = "[0, 5, 2, 1, 2, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0][0, 5, 2, 1, 2, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]";
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
    private List<Integer> whites;
    private List<Integer> blacks;
    private Integer whitesOutside;
    private Integer blackOutside;
    private Integer whiteHouse;
    private Integer blackHouse;
    private String boardString;
    private Boolean finished;

    private PredictionPiece show1, show2, show3, show4;

    public Board() {
        boardString = STARTING_BOARD;
        node = new Group();
        generateBackground();
        generateCenter();
        generatePositions();
        generateTriangles();
        addPieces(STARTING_BOARD);
        generateClickable();
    }

    public void init() {
        setBoard(STARTING_BOARD);
        finished = false;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public String getBoardString()
    {
        return boardString;
    }

    public void setBoard(String board) {
        boardString = board;
        removePieces();
        addPieces(board);
        FrameHandler.getMainGameFrame().getTitlePanel().updateCapturedPieces(whitesOutside, blackOutside);
        if(GameHandler.getCurrentUser().getUsername().equals(User.getInstance().getUsername())) {
            generateHouses();
            handleOutside();
            //canMove();
        }
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
        whitesOutside = whites.get(24);
        blackOutside = blacks.get(24);
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
                if(!piece.getWinnable()) {
                    if(piece.getDrawable().getOnMouseClicked() == null) {
                        piece.getDrawable().setOnMouseClicked(e -> {
                            showOptions(piece, false);
                        });
                    }
                }
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

    private void removeOnClick() {
        for(Piece piece : pieces) {
            piece.getDrawable().setOnMouseClicked(null);
        }
    }

    private boolean showOptions(Piece piece, boolean justVerify) {
        if((GameHandler.getCurrentUser().getWhite() && whitesOutside == 0) || (!GameHandler.getCurrentUser().getWhite() && blackOutside == 0)) {
            removePredictions();
        }
        if(User.getInstance().getUsername().equals(GameHandler.getCurrentUser().getUsername())) {
            if(piece.getWhite().equals(GameHandler.getCurrentUser().getWhite())) {
                if(piece.getWhite() && whitesOutside > 0 && piece.getPosition() != -1) {
                    return false;
                }
                if(!piece.getWhite() && blackOutside > 0 && piece.getPosition() != -1) {
                    return false;
                }
                if(!GameHandler.isRolledDice()) {
                    return true;
                }
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
                boolean outside = false;
                int crtPos = piece.getPosition();
                int p1;
                int p2;
                int p3;
                int p4;
                if(piece.getWhite()) {
                    if(crtPos == -1) {
                        crtPos = 11;
                        outside = true;
                    }
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
                    if(crtPos == -1) {
                        crtPos = 12;
                        outside = true;
                    }
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
                if(outside) {
                    if(piece.getWhite()) {
                        if(p1 > 0) {
                            p1 = whiteToNormal.get(normalToWhite.get(p1) + 1);
                            if (p1 < 6 || p1 > 11) {
                                p1 = -1;
                            }
                        }
                        if(p2 > 0) {
                            p2 = whiteToNormal.get(normalToWhite.get(p2) + 1);
                            if (p2 < 6 || p2 > 11) {
                                p2 = -1;
                            }
                        }
                        if(p3 > 0) {
                            p3 = whiteToNormal.get(normalToWhite.get(p3) + 1);
                            if (p3 < 6 || p3 > 11) {
                                p3 = -1;
                            }
                            else if(normalToWhite.get(p3) > 6) {
                                p3 = -1;
                            }
                        }
                        if(p4 > 0) {
                            p4 = whiteToNormal.get(normalToWhite.get(p4) + 1);
                            if (p4 < 6 || p4 > 11) {
                                p4 = -1;
                            }
                            else if(normalToWhite.get(p4) > 6) {
                                p4 = -1;
                            }
                        }
                    }
                    else {
                        if(p1 > 0) {
                            p1 = blackToNormal.get(normalToBlack.get(p1) + 1);
                            if (p1 < 12 || p1 > 17) {
                                p1 = -1;
                            }
                        }
                        if(p2 > 0) {
                            p2 = blackToNormal.get(normalToBlack.get(p2) + 1);
                            if (p2 < 12 || p2 > 17) {
                                p2 = -1;
                            }
                        }
                        if(p3 > 0) {
                            p3 = blackToNormal.get(normalToBlack.get(p3) + 1);
                            if (p3 < 12 || p3 > 17) {
                                p3 = -1;
                            }
                            else if(normalToBlack.get(p3) > 6) {
                                p3 = -1;
                            }
                        }
                        if(p4 > 0) {
                            p4 = blackToNormal.get(normalToBlack.get(p4) + 1);
                            if (p4 < 12 || p4 > 17) {
                                p4 = -1;
                            }
                            else if(normalToBlack.get(p4) > 6) {
                                p4 = -1;
                            }
                        }
                    }
                }
                boolean ok;
                int largerOk = 2;
                boolean canMoveSomething = false;
                int elevation;
                if(p1 > -1 && p1 < 24) {
                    ok = true;
                    elevation = 0;
                    for (Piece verify : pieces) {
                        if (verify.getPosition().equals(p1)) {
                            if(!verify.getWhite().equals(piece.getWhite())) {
                                int enemyElevation = verify.getElevation();
                                for(Piece getElev : pieces) {
                                    if(getElev.getPosition().equals(verify.getPosition()) && getElev.getElevation() > enemyElevation) {
                                        enemyElevation = getElev.getElevation();
                                    }
                                }
                                if(enemyElevation > 0) {
                                    ok = false;
                                    --largerOk;
                                }
                                else {
                                    elevation = 1;
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
                        canMoveSomething = true;
                        if (!justVerify) {
                            show1 = new PredictionPiece(PieceGenerator.getOptionPiece(false, p1 <= 11, elevation, positions.get(p1)), p1, elevation, false, d1, -1);
                            if (dblCondition) {
                                show1.getDrawable().setOnMouseClicked(e -> {
                                    makeMove(piece, 1);
                                }); //User double, and he moved 3
                            } else {
                                Integer finalD1 = d1;
                                show1.getDrawable().setOnMouseClicked(e -> {
                                    makeMove(piece, finalD1, -1);
                                });
                            }
                            node.getChildren().add(show1.getDrawable());
                        }
                    }
                }
                if(p2 > -1 && p2 < 24 && (!dblCondition || largerOk == 2)) {
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
                                if(enemyElevation > 0) {
                                    ok = false;
                                    --largerOk;
                                }
                                else {
                                    elevation = 1;
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
                        canMoveSomething = true;
                        if (!justVerify) {
                            show2 = new PredictionPiece(PieceGenerator.getOptionPiece(false, p2 <= 11, elevation, positions.get(p2)), p2, elevation, false, -1, d2);
                            if (dblCondition) {
                                show2.getDrawable().setOnMouseClicked(e -> {
                                    makeMove(piece, 2);
                                }); //User double, and he moved 3
                            } else {
                                Integer finalD2 = d2;
                                show2.getDrawable().setOnMouseClicked(e -> {
                                    makeMove(piece, -1, finalD2);
                                });
                            }
                            node.getChildren().add(show2.getDrawable());
                        }
                    }
                }
                if(largerOk > 0 && (!dblCondition || largerOk == 2)) {
                    if(p3 > -1 && p3 < 24) {
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
                                    if(enemyElevation > 0) {
                                        ok = false;
                                        --largerOk;
                                    }
                                    else {
                                        elevation = 1;
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
                            canMoveSomething = true;
                            if(!justVerify) {
                                show3 = new PredictionPiece(PieceGenerator.getOptionPiece(false, p3 <= 11, elevation, positions.get(p3)), p3, elevation, false, d1, d2);
                                if (dblCondition) {
                                    show3.getDrawable().setOnMouseClicked(e -> {
                                        makeMove(piece, 3);
                                    });
                                } else {
                                    Integer finalD1 = d1;
                                    Integer finalD2 = d2;
                                    show3.getDrawable().setOnMouseClicked(e -> {
                                        makeMove(piece, finalD1, finalD2);
                                    });
                                }
                                node.getChildren().add(show3.getDrawable());
                            }
                        }
                    }
                }
                if(largerOk == 2 && dblCondition) {
                    if(p4 > -1 && p4 < 24) {
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
                                    if(enemyElevation > 0) {
                                        ok = false;
                                        --largerOk;
                                    }
                                    else {
                                        elevation = 1;
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
                            canMoveSomething = true;
                            if (!justVerify) {
                                show4 = new PredictionPiece(PieceGenerator.getOptionPiece(false, p4 <= 11, elevation, positions.get(p4)), p4, elevation, false, d1, d1);
                                show4.getDrawable().setOnMouseClicked(e -> {
                                    makeMove(piece, 4);
                                });
                                node.getChildren().add(show4.getDrawable());
                            }
                        }
                    }
                }
//                if(justVerify) {
                if((dblCondition ? largerOk != 2 : largerOk == 0) && !canMoveSomething) {
                    return false; //can not move
                }
                else {
                    return true; //can still move
                }
//                }
            }
        }
        return false;
    }

    private void removePieces() {
        whites.clear();
        blacks.clear();
        for(Piece piece : pieces) {
            node.getChildren().remove(piece.getDrawable());
        }
    }

    private boolean makeMove(Piece pos, int d1, int d2) {
        removePredictions();
        StringBuilder moveS = new StringBuilder();
        if(d1 != -1) {
            if(!FrameHandler.getMainGameFrame().getSidePanel().getFirstDiceAvailable()) {
                return false;
            }
            FrameHandler.getMainGameFrame().getSidePanel().disableFirstDice();
            moveS.append(d1);
        }
        if(d2 != -1) {
            if(!FrameHandler.getMainGameFrame().getSidePanel().getSecondDiceAvailable()) {
                return false;
            }
            FrameHandler.getMainGameFrame().getSidePanel().disableSecondDice();
            if(d1 != -1) {
                moveS.append(",");
            }
            moveS.append(d2);
        }
        System.out.println(moveS.toString());
        sendMove(pos.getPosition(), moveS.toString());
        if(!FrameHandler.getMainGameFrame().getSidePanel().getSecondDiceAvailable() && !FrameHandler.getMainGameFrame().getSidePanel().getFirstDiceAvailable()) {
            nextTurn();
        }
        else {
//            generateHouses();
//            handleOutside();
            //canMove();
        }
        return true;
    }

    private boolean makeMove(Piece pos, int count) {
        removePredictions();
        if(FrameHandler.getMainGameFrame().getSidePanel().getDoubleCount() < count) {
            return false;
        }
        FrameHandler.getMainGameFrame().getSidePanel().decreaseDoubleCount(count);

        int move = FrameHandler.getMainGameFrame().getSidePanel().getFirstDice();
        StringBuilder moveS = new StringBuilder();
        for(int i = 0; i < count; ++i) {
            moveS.append(move);
            if(i != count - 1) {
                moveS.append(",");
            }
        }
        System.out.println(moveS.toString());
        sendMove(pos.getPosition(), moveS.toString());
        if(FrameHandler.getMainGameFrame().getSidePanel().getDoubleCount() == 0) {
            nextTurn();
        }
        else {
//            generateHouses();
//            handleOutside();
            //canMove();
        }
        return true;
    }

    private void sendMove(int pos, String move) {
        Map<String, String> options = new HashMap<>();
        if(GameHandler.getCurrentUser().getWhite()) {
            options.put("color", "white");
            options.put("initialPosition", String.valueOf(normalToWhite.get(pos == -1 ? 24 : pos)));
        }
        else {
            options.put("color", "black");
            options.put("initialPosition", String.valueOf(normalToBlack.get(pos == -1 ? 24 : pos)));
        }
        options.put("die", move);
        WSClient.getInstance().sendMessage(new Message("move", options));
    }

    public void nextTurn() {
        if(finished) {
            return;
        }
        WSClient.getInstance().sendMessage(new Message("turn", null));
    }

    public void generateHouses() {
        removeOnClick();
        for(Piece piece : pieces) {
            piece.setWinnable(false);
        }
        blackHouse = 0;
        whiteHouse = 0;
        if(GameHandler.getCurrentUser().getWhite() && whitesOutside != 0) {
            return;
        }
        if(!GameHandler.getCurrentUser().getWhite() && blackOutside != 0) {
            return;
        }
        boolean canMove = canMove();
        boolean isWhite = GameHandler.getCurrentUser().getWhite();
        for(Piece piece : pieces) {
            int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
            if (piece.getWhite().equals(isWhite) && (pos > 6 || pos < 1)) {
                if(canMove) {
                    return;
                }
                else {
                    PopUpHandler.createCantMove();
                }
            }
        }
        Integer d1 = FrameHandler.getMainGameFrame().getSidePanel().getFirstDice();
        Integer d2 = FrameHandler.getMainGameFrame().getSidePanel().getSecondDice();
        boolean dbl = d1 != null && (d1.equals(d2));
        Integer d3 = (d1 == null ? null : (d2 == null ? null : d1 + d2));
        Integer d4 = null;
        if(dbl) {
            d4 = d1 * 4;
            if(FrameHandler.getMainGameFrame().getSidePanel().getDoubleCount() < 4) {
                d4 = null;
            }
            d3 = d1 * 3;
            if(FrameHandler.getMainGameFrame().getSidePanel().getDoubleCount() < 3) {
                d3 = null;
            }
            d2 = d1 * 2;
            if(FrameHandler.getMainGameFrame().getSidePanel().getDoubleCount() < 2) {
                d2 = null;
            }
            if(FrameHandler.getMainGameFrame().getSidePanel().getDoubleCount() < 1) {
                d1 = null;
            }
        }

        int elevation;
        Piece p;
        boolean step2 = false;
        int step3 = -1;
        if(d1 != null) {
            elevation = -1;
            p = null;
            for (Piece piece : pieces) {
                int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                if (piece.getWhite() == isWhite && pos > 0 && pos < 7 && piece.getElevation() > elevation && pos == d1) {
                    elevation = piece.getElevation();
                    p = piece;
                }
            }
            if (p != null) {
                if(p.getDrawable().getOnMouseClicked() == null) {
                    if(isWhite) {
                        ++whiteHouse;
                    }
                    else {
                        ++blackHouse;
                    }
                    p.setWinnable(true);
                    Piece finalP = p;
                    Integer finalD1 = d1;
                    p.getDrawable().setOnMouseClicked(e -> {
                        if (dbl) {
                            makeMove(finalP, 1);
                        } else {
                            makeMove(finalP, finalD1, -1);
                        }
                    });
                }
            }
            else if(canMove) {
                for(Piece piece : pieces) {
                    int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                    if(piece.getWhite() == isWhite && pos > d1) {
                        step2 = true;
                    }
                }
            }
            if((!canMove || !step2) && p == null) {
                for (Piece piece : pieces) {
                    int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                    if (piece.getWhite() == isWhite && pos > 0 && pos < 7 && pos < d1 && pos >= step3) {
                        step3 = pos;
                        if(piece.getElevation() > elevation) {
                            elevation = piece.getElevation();
                            p = piece;
                        }
                    }
                }
                if (p != null) {
                    if(p.getDrawable().getOnMouseClicked() == null) {
                        if(isWhite) {
                            ++whiteHouse;
                        }
                        else {
                            ++blackHouse;
                        }
                        p.setWinnable(true);
                        Piece finalP = p;
                        Integer finalD1 = d1;
                        p.getDrawable().setOnMouseClicked(e -> {
                            if (dbl) {
                                makeMove(finalP, 1);
                            } else {
                                makeMove(finalP, finalD1, -1);
                            }
                        });
                    }
                }
            }
            if(step3 == -1 && (!canMove || !step2) && p == null) {
                System.out.println("You shouldn't be here");
            }
        }
        if(d2 != null && d2 < 7 && d2 > 0) {
            elevation = -1;
            p = null;
            for (Piece piece : pieces) {
                int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                if (piece.getWhite() == isWhite && pos > 0 && pos < 7 && piece.getElevation() > elevation && pos == d2) {
                    elevation = piece.getElevation();
                    p = piece;
                }
            }
            if (p != null) {
                if(p.getDrawable().getOnMouseClicked() == null) {
                    if(isWhite) {
                        ++whiteHouse;
                    }
                    else {
                        ++blackHouse;
                    }
                    p.setWinnable(true);
                    Piece finalP = p;
                    Integer finalD2 = d2;
                    p.getDrawable().setOnMouseClicked(e -> {
                        if (dbl) {
                            makeMove(finalP, 2);
                        } else {
                            makeMove(finalP, -1, finalD2);
                        }
                    });
                }
            }
            else if(canMove) {
                for(Piece piece : pieces) {
                    int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                    if(piece.getWhite() == isWhite && pos > d2) {
                        step2 = true;
                    }
                }
            }
            if((!canMove || !step2) && p == null) {
                for (Piece piece : pieces) {
                    int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                    if (piece.getWhite() == isWhite && pos > 0 && pos < 7 && pos < d2 && pos >= step3) {
                        step3 = pos;
                        if(piece.getElevation() > elevation) {
                            elevation = piece.getElevation();
                            p = piece;
                        }
                    }
                }
                if (p != null) {
                    if(p.getDrawable().getOnMouseClicked() == null) {
                        if(isWhite) {
                            ++whiteHouse;
                        }
                        else {
                            ++blackHouse;
                        }
                        p.setWinnable(true);
                        Piece finalP = p;
                        Integer finalD2 = d2;
                        p.getDrawable().setOnMouseClicked(e -> {
                            if (dbl) {
                                makeMove(finalP, 2);
                            } else {
                                makeMove(finalP, -1, finalD2);
                            }
                        });
                    }
                }
            }
            if(step3 == -1 && (!canMove || !step2) && p == null) {
                System.out.println("You shouldn't be here");
            }
        }
        if(d3 != null && d3 < 7 && d3 > 0) {
            elevation = -1;
            p = null;
            for (Piece piece : pieces) {
                int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                if (piece.getWhite() == isWhite && pos > 0 && pos < 7 && piece.getElevation() > elevation && pos == d3) {
                    elevation = piece.getElevation();
                    p = piece;
                }
            }
            if (p != null) {
                if(p.getDrawable().getOnMouseClicked() == null) {
                    if(isWhite) {
                        ++whiteHouse;
                    }
                    else {
                        ++blackHouse;
                    }
                    p.setWinnable(true);
                    Piece finalP = p;
                    Integer finalD1 = d1;
                    Integer finalD2 = d2;
                    p.getDrawable().setOnMouseClicked(e -> {
                        if (dbl) {
                            makeMove(finalP, 3);
                        } else {
                            makeMove(finalP, finalD1, finalD2);
                        }
                    });
                }
            }
            else if(canMove) {
                for(Piece piece : pieces) {
                    int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                    if(piece.getWhite() == isWhite && pos > d3) {
                        step2 = true;
                    }
                }
            }
            if((!canMove || !step2) && p == null) {
                for (Piece piece : pieces) {
                    int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                    if (piece.getWhite() == isWhite && pos > 0 && pos < 7 && pos < d3 && pos >= step3) {
                        step3 = pos;
                        if(piece.getElevation() > elevation) {
                            elevation = piece.getElevation();
                            p = piece;
                        }
                    }
                }
                if (p != null) {
                    if(p.getDrawable().getOnMouseClicked() == null) {
                        if(isWhite) {
                            ++whiteHouse;
                        }
                        else {
                            ++blackHouse;
                        }
                        p.setWinnable(true);
                        Piece finalP = p;
                        Integer finalD1 = d1;
                        Integer finalD2 = d2;
                        p.getDrawable().setOnMouseClicked(e -> {
                            if (dbl) {
                                makeMove(finalP, 3);
                            } else {
                                makeMove(finalP, finalD1, finalD2);
                            }
                        });
                    }
                }
            }
            if(step3 == -1 && (!canMove || !step2) && p == null) {
                System.out.println("You shouldn't be here");
            }
        }
        if(d4 != null && d4 < 7 && d4 > 0) {
            elevation = -1;
            p = null;
            for (Piece piece : pieces) {
                int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                if (piece.getWhite() == isWhite && pos > 0 && pos < 7 && piece.getElevation() > elevation && pos == d4) {
                    elevation = piece.getElevation();
                    p = piece;
                }
            }
            if (p != null) {
                if(p.getDrawable().getOnMouseClicked() == null) {
                    if(isWhite) {
                        ++whiteHouse;
                    }
                    else {
                        ++blackHouse;
                    }
                    p.setWinnable(true);
                    Piece finalP = p;
                    p.getDrawable().setOnMouseClicked(e -> {
                        makeMove(finalP, 4);
                    });
                }
            }
            else if(canMove) {
                for(Piece piece : pieces) {
                    int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                    if(piece.getWhite() == isWhite && pos > d4) {
                        step2 = true;
                    }
                }
            }
            if((!canMove || !step2) && p == null) {
                for (Piece piece : pieces) {
                    int pos = (isWhite ? normalToWhite : normalToBlack).get(piece.getPosition());
                    if (piece.getWhite() == isWhite && pos > 0 && pos < 7 && pos < d4 && pos >= step3) {
                        step3 = pos;
                        if(piece.getElevation() > elevation) {
                            elevation = piece.getElevation();
                            p = piece;
                        }
                    }
                }
                if (p != null) {
                    if(p.getDrawable().getOnMouseClicked() == null) {
                        if(isWhite) {
                            ++whiteHouse;
                        }
                        else {
                            ++blackHouse;
                        }
                        p.setWinnable(true);
                        Piece finalP = p;
                        p.getDrawable().setOnMouseClicked(e -> {
                            makeMove(finalP, 4);
                        });
                    }
                }
            }
            if(step3 == -1 && (!canMove || !step2) && p == null) {
                System.out.println("You shouldn't be here");
            }
        }
        if(!canMove) {
            if(isWhite && whiteHouse < 1) {
                PopUpHandler.createCantMove();
            }
            if(!isWhite && blackHouse < 1) {
                PopUpHandler.createCantMove();
            }
        }
    }

    public void handleOutside() {
        if(GameHandler.getCurrentUser().getWhite() && whitesOutside == 0) {
            return;
        }
        if(!GameHandler.getCurrentUser().getWhite() && blackOutside == 0) {
            return;
        }
        boolean moveable = showOptions(new Piece(null, -1, 0, GameHandler.getCurrentUser().getWhite()), false);
        if(!moveable) {
            PopUpHandler.createCantMove();
        }
    }

    public boolean canMove() {
        if(GameHandler.getCurrentUser().getWhite() && whitesOutside != 0) {
            return true;
        }
        if(!GameHandler.getCurrentUser().getWhite() && blackOutside != 0) {
            return true;
        }
        boolean moveable = false;
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
                moveable = moveable || showOptions(piece, true);
            }
//            moveable = moveable || ((GameHandler.getCurrentUser().getWhite() && whiteHouse > 0) || (!GameHandler.getCurrentUser().getWhite() && blackHouse > 0));
        }
        if(!moveable) {
//            PopUpHandler.createCantMove();
            return false;
        }
        return true;
    }
}
