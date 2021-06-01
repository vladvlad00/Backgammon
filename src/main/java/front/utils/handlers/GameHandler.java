package front.utils.handlers;

import front.entities.LobbyUser;
import front.entities.UserRole;
import front.utils.websocket.Message;
import front.utils.websocket.WSClient;

import java.util.Random;

public class GameHandler {
    private static LobbyUser currentUser;
    private static LobbyUser whiteUser;
    private static LobbyUser blackUser;
    private static boolean rolledDice;
    public static boolean isHost = false;

    public static void init(LobbyUser u1, LobbyUser u2, String starter) {
        if(starter.equals(u1.getUsername())) {
            u1.setWhite(true);
            whiteUser = u1;
            u2.setWhite(false);
            blackUser = u2;
        }
        else {
            u2.setWhite(true);
            whiteUser = u2;
            u1.setWhite(false);
            blackUser = u1;
        }

        currentUser = whiteUser;
        if (isHost)
        {
            checkAiTurn(currentUser);
        }
    }

    public static void nextTurn() {
        if(currentUser.equals(whiteUser)) {
            currentUser = blackUser;
        }
        else {
            currentUser = whiteUser;
        }
        FrameHandler.getMainGameFrame().getSidePanel().updateDice();
        if (isHost)
        {
            checkAiTurn(currentUser);
        }
    }

    public static LobbyUser getCurrentUser() {
        return currentUser;
    }

    public static LobbyUser getWhiteUser() {
        return whiteUser;
    }

    public static LobbyUser getBlackUser() {
        return blackUser;
    }

    public static boolean isRolledDice()
    {
        return rolledDice;
    }

    public static void setRolledDice(boolean rolledDice)
    {
        GameHandler.rolledDice = rolledDice;
    }

    private static void checkAiTurn(LobbyUser user)
    {
        if (!user.getRole().toString().startsWith("AI"))
            return;
        WSClient.getInstance().sendMessage(new Message("dice", null));
    }
}
