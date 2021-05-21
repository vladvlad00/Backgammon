package front.panels.menu.login;

import front.panels.menu.GoBackPanel;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainLoginFrame extends BorderPane {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;

    private Stage stage;

    private LoginBodyPanel loginBodyPanel;
    private LoginResponsePanel loginResponsePanel;
    private GoBackPanel goBackPanel;

    public MainLoginFrame(Stage stage) {
        this.stage = stage;
        init();
    }

    private void init() {
        loginBodyPanel = new LoginBodyPanel(this);
        this.setCenter(loginBodyPanel);
        loginResponsePanel = new LoginResponsePanel(this);
        this.setBottom(loginResponsePanel);
        goBackPanel = new GoBackPanel(this, "menu");
        this.setTop(goBackPanel);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
