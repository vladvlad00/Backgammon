package front.utils.handlers;

import front.entities.LobbyUser;

public class GameHandler {
    private static LobbyUser currentUser;
    private static LobbyUser whiteUser;
    private static LobbyUser blackUser;

    public static void init(LobbyUser u1, LobbyUser u2) {
        u2.setWhite(true);
        whiteUser = u2;
        u1.setWhite(false);
        blackUser = u1;
        currentUser = whiteUser;
    }

    public static void nextTurn() {
        if(currentUser.equals(whiteUser)) {
            currentUser = blackUser;
        }
        else {
            currentUser = whiteUser;
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
}
