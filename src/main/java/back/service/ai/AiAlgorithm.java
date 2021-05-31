package back.service.ai;

import back.service.game.Board;
import back.service.game.PlayerColor;
import back.websocket.Message;

import java.util.List;

public interface AiAlgorithm
{
    List<Message> getMove(Board board, PlayerColor color, int die1, int die2);
}
