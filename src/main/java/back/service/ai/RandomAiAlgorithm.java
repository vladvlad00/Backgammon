package back.service.ai;

import back.service.game.Board;
import back.service.game.InvalidMoveException;
import back.service.game.PlayerColor;
import back.websocket.Message;

import java.util.*;

public class RandomAiAlgorithm implements AiAlgorithm
{
    @Override
    public List<Message> getMove(Board board, PlayerColor color, int die1, int die2)
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

        int movesRemaining = die1 == die2 ? 4 : 2;
        int movesRemaining1 = movesRemaining / 2;
        int movesRemaining2 = movesRemaining / 2;

        List<Message> movesMade = new ArrayList<>();

        while (playerCheckers[0] > 0 && movesRemaining > 0)
        {
            if (movesRemaining1 > 0)
                try
                {
                    board.makeMove(color, 0, die1);
                    movesMade.add(Message.getMoveMessage(color, 0, die1));
                    movesRemaining1--;
                    movesRemaining--;
                    continue;
                } catch (InvalidMoveException ignored)
                {

                }
            if (movesRemaining2 > 0)
                try
                {
                    board.makeMove(color, 0, die2);
                    movesMade.add(Message.getMoveMessage(color, 0, die2));
                    movesRemaining2--;
                    movesRemaining--;
                    continue;
                } catch (InvalidMoveException ignored)
                {

                }
            break;
        }

        if (playerCheckers[0] > 0)
            return movesMade;

        List<Integer> positions = new ArrayList<>();

        for (int i=1;i<=24;i++)
            positions.add(i);

        while (!board.canBearoff(color))
        {
            Collections.shuffle(positions);
            boolean canMove = false;
            for (int pos : positions)
            {
                if (movesRemaining1 > 0 && pos > die1)
                    try
                    {
                        board.makeMove(color, pos, die1);
                        movesMade.add(Message.getMoveMessage(color, pos, die1));
                        movesRemaining1--;
                        movesRemaining--;
                        canMove = true;
                        continue;
                    } catch (InvalidMoveException ignored)
                    {

                    }
                if (movesRemaining2 > 0 && pos > die2)
                    try
                    {
                        board.makeMove(color, pos, die2);
                        movesMade.add(Message.getMoveMessage(color, pos, die2));
                        movesRemaining2--;
                        movesRemaining--;
                        canMove = true;
                        continue;
                    } catch (InvalidMoveException ignored)
                    {

                    }
            }
            if (!canMove)
                return movesMade;
        }

        while (movesRemaining > 0)
        {
            if (movesRemaining1 > 0)
                try
                {
                    int initialPosition = board.bearoff(color, die1);
                    movesMade.add(Message.getMoveMessage(color, initialPosition, die1));
                    movesRemaining1--;
                    movesRemaining--;
                    continue;
                } catch (InvalidMoveException ignored)
                {

                }
            if (movesRemaining2 > 0)
                try
                {
                    int initialPosition = board.bearoff(color, die2);
                    movesMade.add(Message.getMoveMessage(color, initialPosition, die2));
                    movesRemaining2--;
                    movesRemaining--;
                    continue;
                } catch (InvalidMoveException ignored)
                {

                }
            break;
        }
        return movesMade;
    }
}
