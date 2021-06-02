package back.service.ai;

import back.service.game.Board;
import back.service.game.GameState;
import back.service.game.InvalidMoveException;
import back.service.game.PlayerColor;
import back.websocket.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpectiminimaxAiAlgorithm implements AiAlgorithm
{
    private static final int MAX_DEPTH = 3;
    private List<Board.Move> movesToMake;
    private static int nrCalls = 0;

    enum NodeType
    {
        MIN,
        MAX,
        CHANCE
    }

    @Override
    public List<Message> getMove(Board board, PlayerColor color, int die1, int die2) throws CloneNotSupportedException
    {
        expectiMiniMax(NodeType.MAX, board, color, die1, die2, 0);
        System.out.println(nrCalls + " AI calls");
        List<Message> messages = new ArrayList<>();
        for (var move : movesToMake)
            messages.add(Message.getMoveMessage(color, move.initialPosition, move.die));
        return messages;
    }

    float expectiMiniMax(NodeType type, Board board, PlayerColor color, int die1, int die2, int depth) throws CloneNotSupportedException
    {
        nrCalls++;
        if (depth == MAX_DEPTH || board.getWinner() != null)
            return eval(board, color);
        if (type == NodeType.MIN)
        {
            float val = Float.POSITIVE_INFINITY;
            Set<Board> possibleMoves = generateBoards(board, color, die1, die2);
            for (var nextBoard : possibleMoves)
            {
                float moveVal = expectiMiniMax(NodeType.CHANCE, nextBoard,
                        color == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE,
                        0, 0, depth+1);
                val = Math.min(val, moveVal);
            }
            return val;
        }
        else if (type == NodeType.MAX)
        {
            float val = Float.NEGATIVE_INFINITY;
            Set<Board> possibleMoves = generateBoards(board, color, die1, die2);
            for (var nextBoard : possibleMoves)
            {
                float moveVal = expectiMiniMax(NodeType.CHANCE, nextBoard,
                        color == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE,
                        0, 0, depth+1);
                if (moveVal > val)
                {
                    val = moveVal;
                    if (depth == 0)
                        movesToMake = nextBoard.getMoveHistory();
                }
            }
            return val;
        }
        else
        {
            float val = 0;
            for (int i=1;i<=6;i++)
                for (int j=i;j<=6;j++)
                {
                    float moveVal = expectiMiniMax(depth % 4 == 1 ? NodeType.MIN : NodeType.MAX,
                            board, color, i, j, depth+1);
                    val += getDiceChance(i, j) * moveVal;
                }
            return val;
        }
    }

    Set<Board> generateBoards(Board board, PlayerColor color, int die1, int die2) throws CloneNotSupportedException
    {
        int[] dice;
        boolean[] used;
        if (die1 == die2)
        {
            dice = new int[]{die1, die1, die1, die1};
            used = new boolean[4];
        }
        else
        {
            dice = new int[]{die1, die2};
            used = new boolean[2];
        }

        return getBoards(board, color, dice, used);
    }

    Set<Board> getBoards(Board board, PlayerColor color, int[] dice, boolean[] used) throws CloneNotSupportedException
    {
        boolean done = true;
        for (var i : used)
            if (!i)
            {
                done = false;
                break;
            }

        Set<Board> possibleBoards = new HashSet<>();
        if (done || board.getWinner() != null)
        {
            possibleBoards.add(new Board(board));
            return possibleBoards;
        }

        List<Board.Move> possibleMoves = board.getMoves(color, dice, used);
        for (var move : possibleMoves)
        {
            try
            {
                var state = board.makeMove(color, move.initialPosition, dice[move.die]);
                used[move.die] = true;
                possibleBoards.addAll(getBoards(board, color, dice, used));
                used[move.die] = false;
                board.undoLastMove(color, state == GameState.CAPTURED);
            } catch (InvalidMoveException ignored)
            {

            }
        }

        if (possibleBoards.isEmpty())
            possibleBoards.add(new Board(board));

        return possibleBoards;
    }

    float eval(Board board, PlayerColor color)
    {
        PlayerColor winner = board.getWinner();
        if (winner != null)
        {
            if (winner == color)
                return Float.POSITIVE_INFINITY;
            return Float.NEGATIVE_INFINITY;
        }

        int[] playerCheckers = color == PlayerColor.WHITE ? board.getWhiteCheckers() : board.getBlackCheckers();
        int[] opponentCheckers = color == PlayerColor.BLACK ? board.getWhiteCheckers() : board.getBlackCheckers();

        float score = 0;
        int playerCheckersOut = 15;
        int opponentCheckersOut = 15;
        for (int i=0;i<25;i++)
        {
            float coef;
            if (i == 0)
                coef = -1;
            else if (i <= 6)
                coef = 1;
            else if (i <= 12)
                coef = 0.5f;
            else if (i <= 18)
                coef = 0;
            else
                coef = -0.5f;
            score += coef*(playerCheckers[i]-opponentCheckers[i]);
            playerCheckersOut -= playerCheckers[i];
            opponentCheckersOut -= opponentCheckers[i];
        }
        score += 2 * (playerCheckersOut - opponentCheckersOut);
        score += exposedPenalty(playerCheckers, opponentCheckers) - exposedPenalty(opponentCheckers, playerCheckers);
        return score;
    }

    float exposedPenalty(int[] playerCheckers, int[] opponentCheckers)
    {
        float penalty = 0;

        for (int i=1;i<=6;i++)
            for (int j=i;j<=6;j++)
            {
                float chance = getDiceChance(i, j);
                for (int pos=1;pos<=24;pos++)
                    if (playerCheckers[pos] == 1 && canReach(opponentCheckers, 25-pos, i, j))
                        penalty += positionExposedPenalty(pos) * 2 * chance;

            }
        return -penalty;
    }

    float positionExposedPenalty(int pos)
    {
        return (7 - (float)pos / 4);
    }

    boolean canReach(int[] checkers, int target, int die1, int die2)
    {
        if (checkers[0] > 0)
            return target == die1 || target == die2 || target == die1 + die2;
        return  (target - die1 > 0 && checkers[target-die1] > 0 ||
            target - die2 > 0 && checkers[target-die2] > 0 ||
            target - die1 - die2 > 0 && checkers[target-die1-die2] > 0);
    }

    float getDiceChance(int die1, int die2)
    {
        if (die1 == die2)
            return 1.0f / 36;
        return 1.0f / 18;
    }
}
