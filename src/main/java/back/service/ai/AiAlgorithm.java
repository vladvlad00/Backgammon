package back.service.ai;

import back.service.game.Board;
import back.service.game.PlayerColor;
import back.websocket.Message;

public interface AiAlgorithm
{
    public Message getMove(Board board, PlayerColor color);
}
