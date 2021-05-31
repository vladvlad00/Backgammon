package front.utils.handlers;

import front.entities.LobbyUser;

public class GameHandler {
    private static LobbyUser currentUser;
    private static LobbyUser whiteUser;
    private static LobbyUser blackUser;

    public static void init(LobbyUser u1, LobbyUser u2) {
        whiteUser = u1;
        blackUser = u2;
        currentUser = whiteUser;
    }
}
