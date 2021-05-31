package back.service.game;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameManager
{
    //TODO FOLOSESTE ASTEA !!!
    private Map<Long, Game> games = new HashMap<>();

    public void createGame(Long roomId)
    {
        games.put(roomId, new Game());
    }

    public Game getGame(Long roomId)
    {
        return games.get(roomId);
    }

    public void deleteGame(Long roomId)
    {
        games.remove(roomId);
    }
}
