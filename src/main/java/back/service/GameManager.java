package back.service;

import back.service.Game;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameManager
{
    private Map<Long, Game> games;

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
