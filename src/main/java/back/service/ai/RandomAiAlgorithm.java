package back.service.ai;

import back.service.game.Board;
import back.service.game.PlayerColor;
import back.websocket.Message;

public class RandomAiAlgorithm implements AiAlgorithm
{
    @Override
    public Message getMove(Board board, PlayerColor color)
    {
        int[] playerCheckers;
        int[] opponentCheckers;

        if (color == PlayerColor.WHITE)
        {
            playerCheckers = board.getWhiteCheckers();
            opponentCheckers = board.getBlackCheckers();
        }
        else
        {
            playerCheckers = board.getBlackCheckers();
            opponentCheckers = board.getWhiteCheckers();
        }
        return null;
    }
}
