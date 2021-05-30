package front;

import front.entities.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyHandler {
    private static List<Lobby> lobbies;

    private LobbyHandler() {}

    public static void init(List<Lobby> lobbiesArg) {
        lobbies = lobbiesArg;
        lobbies.forEach(Lobby::separateUsers);
    }

    public static Lobby getById(Long id) {
        for(Lobby lobby : lobbies) {
            if(lobby.getId().equals(id)) {
                return lobby;
            }
        }
        return null;
    }
}
