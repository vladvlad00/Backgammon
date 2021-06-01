package front.utils.handlers;

import front.entities.LobbyUser;

public class GameHandler {
    private static LobbyUser currentUser;
    private static LobbyUser whiteUser;
    private static LobbyUser blackUser;
    private static boolean rolledDice;

    public static void init(LobbyUser u1, LobbyUser u2) {
        rolledDice = false;
        u1.setWhite(true);
        whiteUser = u1;
        u2.setWhite(false);
        blackUser = u2;
        currentUser = whiteUser;
    }

    public static void nextTurn() {
        if(currentUser.equals(whiteUser)) {
            currentUser = blackUser;
        }
        else {
            currentUser = whiteUser;
        }
        rolledDice = false;
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

    public static boolean isRolledDice() {
        return rolledDice;
    }

    public static void setRolledDice(boolean rolledDice) {
        GameHandler.rolledDice = rolledDice;
    }
}
