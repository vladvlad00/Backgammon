package front.utils.handlers;

import front.panels.game.MainGameFrame;
import front.panels.menu.body.MainMenuFrame;
import front.panels.menu.login.MainLoginFrame;
import front.panels.menu.register.MainRegisterFrame;
import javafx.stage.Stage;

public class FrameHandler {
    private static MainMenuFrame mainMenuFrame;
    private static MainRegisterFrame mainRegisterFrame;
    private static MainLoginFrame mainLoginFrame;
    private static MainGameFrame mainGameFrame;

    public static void init(Stage stage) {
        mainMenuFrame = new MainMenuFrame(stage);
        mainRegisterFrame = new MainRegisterFrame(stage);
        mainLoginFrame = new MainLoginFrame(stage);
        mainGameFrame = new MainGameFrame(stage);
    }

    public static MainMenuFrame getMainMenuFrame() {
        return mainMenuFrame;
    }

    public static MainRegisterFrame getMainRegisterFrame() {
        return mainRegisterFrame;
    }

    public static MainLoginFrame getMainLoginFrame() {
        return mainLoginFrame;
    }

    public static MainGameFrame getMainGameFrame() {
        return mainGameFrame;
    }
}
