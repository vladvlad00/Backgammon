package front.panels.game.board_elements;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dice {
    public static final Long ONE_SECOND = 1_000_000_000L;

    private Group group;
    private Rectangle body;
    private List<Circle> dots;
    private int number;
    private boolean available;

    public Dice(int number) {
        group = new Group();

        body = new Rectangle();
        body.setStroke(Color.BLACK);
        body.setStrokeWidth(2);
        body.setFill(Color.WHITESMOKE);

        this.number = number;
        addTheDots();
    }

    public void setPosition(int x, int y) {
        body.setX(x);
        body.setY(y);
    }

    public void setSize(int w, int h) {
        body.setWidth(w);
        body.setHeight(h);
    }

    public int randomDice() {
        Random random = new Random(System.nanoTime());
        number = random.nextInt(6) + 1;
        addTheDots();
        return number;
    }

    public void setDice(int number) {
        this.number = number;
        addTheDots();
    }

    private void addTheDots() {
        group.getChildren().clear();
        group.getChildren().add(body);
        dots = new ArrayList<>();
        if(number == 1) {
            Circle first = new Circle();
            first.setFill(Color.BLACK);
            first.setRadius(body.getWidth() / 10);
            first.setCenterX((body.getX() + body.getWidth()) / 2);
            first.setCenterY((body.getY() + body.getHeight()) / 2);

            dots.add(first);
            group.getChildren().add(first);
        }
        else if(number == 2) {
            Circle first = new Circle();
            first.setFill(Color.BLACK);
            first.setRadius(body.getWidth() / 10);
            first.setCenterX((body.getX() + body.getWidth()) / 4);
            first.setCenterY((body.getY() + body.getHeight()) / 4);

            Circle second = new Circle();
            second.setFill(Color.BLACK);
            second.setRadius(body.getWidth() / 10);
            second.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            second.setCenterY((body.getY() + body.getHeight()) / 4 * 3);

            dots.add(first);
            dots.add(second);
            group.getChildren().addAll(first, second);
        }
        else if(number == 3) {
            Circle first = new Circle();
            first.setFill(Color.BLACK);
            first.setRadius(body.getWidth() / 10);
            first.setCenterX((body.getX() + body.getWidth()) / 4);
            first.setCenterY((body.getY() + body.getHeight()) / 4);

            Circle second = new Circle();
            second.setFill(Color.BLACK);
            second.setRadius(body.getWidth() / 10);
            second.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            second.setCenterY((body.getY() + body.getHeight()) / 4 * 3);

            Circle third = new Circle();
            third.setFill(Color.BLACK);
            third.setRadius(body.getWidth() / 10);
            third.setCenterX((body.getX() + body.getWidth()) / 2);
            third.setCenterY((body.getY() + body.getHeight()) / 2);

            dots.add(first);
            dots.add(second);
            dots.add(third);
            group.getChildren().addAll(first, second, third);
        }
        else if(number == 4) {
            Circle first = new Circle();
            first.setFill(Color.BLACK);
            first.setRadius(body.getWidth() / 10);
            first.setCenterX((body.getX() + body.getWidth()) / 4);
            first.setCenterY((body.getY() + body.getHeight()) / 4);

            Circle second = new Circle();
            second.setFill(Color.BLACK);
            second.setRadius(body.getWidth() / 10);
            second.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            second.setCenterY((body.getY() + body.getHeight()) / 4 * 3);

            Circle third = new Circle();
            third.setFill(Color.BLACK);
            third.setRadius(body.getWidth() / 10);
            third.setCenterX((body.getX() + body.getWidth()) / 4);
            third.setCenterY((body.getY() + body.getHeight()) / 4 * 3);

            Circle fourth = new Circle();
            fourth.setFill(Color.BLACK);
            fourth.setRadius(body.getWidth() / 10);
            fourth.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            fourth.setCenterY((body.getY() + body.getHeight()) / 4);

            dots.add(first);
            dots.add(second);
            dots.add(third);
            dots.add(fourth);
            group.getChildren().addAll(first, second, third, fourth);
        }
        else if(number == 5) {
            Circle first = new Circle();
            first.setFill(Color.BLACK);
            first.setRadius(body.getWidth() / 10);
            first.setCenterX((body.getX() + body.getWidth()) / 4);
            first.setCenterY((body.getY() + body.getHeight()) / 4);

            Circle second = new Circle();
            second.setFill(Color.BLACK);
            second.setRadius(body.getWidth() / 10);
            second.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            second.setCenterY((body.getY() + body.getHeight()) / 4 * 3);

            Circle third = new Circle();
            third.setFill(Color.BLACK);
            third.setRadius(body.getWidth() / 10);
            third.setCenterX((body.getX() + body.getWidth()) / 4);
            third.setCenterY((body.getY() + body.getHeight()) / 4 * 3);

            Circle fourth = new Circle();
            fourth.setFill(Color.BLACK);
            fourth.setRadius(body.getWidth() / 10);
            fourth.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            fourth.setCenterY((body.getY() + body.getHeight()) / 4);

            Circle fifth = new Circle();
            fifth.setFill(Color.BLACK);
            fifth.setRadius(body.getWidth() / 10);
            fifth.setCenterX((body.getX() + body.getWidth()) / 2);
            fifth.setCenterY((body.getY() + body.getHeight()) / 2);

            dots.add(first);
            dots.add(second);
            dots.add(third);
            dots.add(fourth);
            dots.add(fifth);
            group.getChildren().addAll(first, second, third, fourth, fifth);
        }
        else if(number == 6) {
            Circle first = new Circle();
            first.setFill(Color.BLACK);
            first.setRadius(body.getWidth() / 10);
            first.setCenterX((body.getX() + body.getWidth()) / 4);
            first.setCenterY((body.getY() + body.getHeight()) / 4);

            Circle second = new Circle();
            second.setFill(Color.BLACK);
            second.setRadius(body.getWidth() / 10);
            second.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            second.setCenterY((body.getY() + body.getHeight()) / 4 * 3);

            Circle third = new Circle();
            third.setFill(Color.BLACK);
            third.setRadius(body.getWidth() / 10);
            third.setCenterX((body.getX() + body.getWidth()) / 4);
            third.setCenterY((body.getY() + body.getHeight()) / 4 * 3);

            Circle fourth = new Circle();
            fourth.setFill(Color.BLACK);
            fourth.setRadius(body.getWidth() / 10);
            fourth.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            fourth.setCenterY((body.getY() + body.getHeight()) / 4);

            Circle fifth = new Circle();
            fifth.setFill(Color.BLACK);
            fifth.setRadius(body.getWidth() / 10);
            fifth.setCenterX((body.getX() + body.getWidth()) / 4 * 3);
            fifth.setCenterY((body.getY() + body.getHeight()) / 2);

            Circle sixth = new Circle();
            sixth.setFill(Color.BLACK);
            sixth.setRadius(body.getWidth() / 10);
            sixth.setCenterX((body.getX() + body.getWidth()) / 4);
            sixth.setCenterY((body.getY() + body.getHeight()) / 2);

            dots.add(first);
            dots.add(second);
            dots.add(third);
            dots.add(fourth);
            dots.add(fifth);
            dots.add(sixth);
            group.getChildren().addAll(first, second, third, fourth, fifth, sixth);
        }
    }

    public void addToChildren(GridPane gridPane, int col) {
        GridPane.setHalignment(group, HPos.CENTER);
        GridPane.setValignment(group, VPos.CENTER);
        gridPane.add(group, col, 2);
    }

    public void removeFromChildren(GridPane gridPane) {
        gridPane.getChildren().remove(group);
    }

    public int getNumber() {
        return number;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
