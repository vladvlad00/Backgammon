package front.panels.menu.register;

import front.panels.menu.GoBackPanel;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainRegisterFrame extends BorderPane {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;

    private Stage stage;

    private RegisterBodyPanel registerBodyPanel;
    private RegisterResponsePanel registerResponsePanel;
    private GoBackPanel goBackPanel;

    public MainRegisterFrame(Stage stage) {
        this.stage = stage;
        init();
    }

    private void init() {
        registerBodyPanel = new RegisterBodyPanel(this);
        this.setCenter(registerBodyPanel);
        registerResponsePanel = new RegisterResponsePanel(this);
        this.setBottom(registerResponsePanel);
        goBackPanel = new GoBackPanel(this, "menu", registerBodyPanel.refresh);
        this.setTop(goBackPanel);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public RegisterResponsePanel getRegisterResponsePanel() {
        return registerResponsePanel;
    }

    public RegisterBodyPanel getRegisterBodyPanel() {
        return registerBodyPanel;
    }
}