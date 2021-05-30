package front;

import front.entities.Lobby;
import front.entities.UserRole;
import front.utils.NetworkManager;
import javafx.scene.Scene;

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

    public static List<Lobby> getAll() {
        return lobbies;
    }

    public static String joinThroughID(Long id) throws NullPointerException {
        Lobby lobby;
        lobby = NetworkManager.getLobby(id);
        if(lobby == null) {
            throw new NullPointerException();
        }

        Lobby response = NetworkManager.joinThroughID(id, (lobby.getPlayerNum() - lobby.getAINum() == 0 && lobby.getSpectatorNum() == 0) ? (lobby.getAINum() == 2 ? UserRole.HOST_SPECTATOR : UserRole.HOST) : (lobby.getPlayerNum() < 2 ? UserRole.PLAYER : UserRole.SPECTATOR));
        if(response == null) {
            throw new NullPointerException();
        }
        else {
            if (true) {
                FrameHandler.getMainMenuFrame().goToCreate(response);
                return "succ";
            }
            else {
                return "err";
            }
        }
    }
}
