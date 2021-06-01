package back.service.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board
{
    public static class Move
    {
        public Move()
        {
        }

        public Move(int initialPosition, int die)
        {
            this.initialPosition = initialPosition;
            this.die = die;
        }

        @Override
        public int hashCode()
        {
            return initialPosition * 25 + die;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj.getClass() != this.getClass())
                return false;
            Move other = (Move) obj;
            return initialPosition == other.initialPosition && die == other.die;
        }

        public int initialPosition;
        public int die;
    }

    private int[] whiteCheckers;
    private int[] blackCheckers;
    private List<Move> moveHistory = new ArrayList<>();
    int whiteCheckersNum;
    int blackCheckersNum;

    public Board()
    {
        whiteCheckers = new int[25];
        whiteCheckers[6] = 5;
        whiteCheckers[8] = 3;
        whiteCheckers[13] = 5;
        whiteCheckers[24] = 2;
        whiteCheckersNum = 15;
        blackCheckers = new int[25];
        blackCheckers[6] = 5;
        blackCheckers[8] = 3;
        blackCheckers[13] = 5;
        blackCheckers[24] = 2;
        blackCheckersNum = 15;
    }

    public Board(String representation)
    {
        String[] parts = representation.split("]\\[");
        whiteCheckers = Arrays.stream(parts[0].substring(1).split(", "))
                .mapToInt(Integer::parseInt).toArray();
        blackCheckers = Arrays.stream(parts[1].substring(0, parts[1].length() - 1).split(", "))
                .mapToInt(Integer::parseInt).toArray();
        updateCheckersNum();
    }

    public Board(Board other)
    {
        whiteCheckers = Arrays.copyOf(other.whiteCheckers, 25);
        blackCheckers = Arrays.copyOf(other.blackCheckers, 25);
        whiteCheckersNum = other.whiteCheckersNum;
        blackCheckersNum = other.blackCheckersNum;
        moveHistory = new ArrayList<>();
        for (var move : other.moveHistory)
            moveHistory.add(new Move(move.initialPosition, move.die));
    }

    @Override
    public int hashCode()
    {
        return (Arrays.toString(whiteCheckers) + Arrays.toString(blackCheckers)).hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj.getClass() != this.getClass())
            return false;
        Board other = (Board) obj;
        return Arrays.equals(whiteCheckers, other.whiteCheckers) && Arrays.equals(blackCheckers, other.blackCheckers);
    }

    public PlayerColor getWinner()
    {
        if (whiteCheckersNum == 0)
            return PlayerColor.WHITE;
        if (blackCheckersNum == 0)
            return PlayerColor.BLACK;
        return null;
    }

    public List<Move> getMoves(PlayerColor color, int[] dice, boolean[] used)
    {
        List<Move> moves = new ArrayList<>();

        int[] playerCheckers = color == PlayerColor.WHITE ? whiteCheckers : blackCheckers;
        int[] opponentCheckers = color == PlayerColor.BLACK ? whiteCheckers : blackCheckers;

        if (playerCheckers[0] > 0)
        {
            for (int i=0;i<dice.length;i++)
                if (!used[i])
                    tryToAddMove(moves, 0, i, playerCheckers, opponentCheckers, dice);
        }
        else if (canBearoff(color))
        {
            for (int i=0;i<dice.length;i++)
                if (!used[i])
                    tryToBearoff(moves, color, i, dice);
        }
        else
        {
            for (int pos=1;pos<=24;pos++)
            {
                for (int i=0;i<dice.length;i++)
                    if (!used[i] && pos > dice[i])
                        tryToAddMove(moves, pos, i, playerCheckers, opponentCheckers, dice);
            }
        }

        return moves;
    }

    void tryToAddMove(List<Move> moves, int initialPosition, int i, int[] playerCheckers, int[] opponentCheckers, int[] dice)
    {
        try
        {
            Move move = new Move(initialPosition, i);
            if (!moves.contains(move))
            {
                testMove(playerCheckers, opponentCheckers, initialPosition, dice[i]);
                moves.add(move);
            }
        }
        catch (InvalidMoveException ignored)
        {

        }
    }

    void tryToBearoff(List<Move> moves, PlayerColor color, int i, int[] dice)
    {
        try
        {
            int pos = testBearoff(color, dice[i]);
            Move move = new Move(pos, i);
            if (!moves.contains(move))
                moves.add(move);
        }
        catch (InvalidMoveException ignored)
        {

        }
    }

    public GameState makeMove(PlayerColor color, int initialPosition, int die) throws InvalidMoveException
    {
        int prevCaptured, captured;
        if (color == PlayerColor.WHITE)
        {
            prevCaptured = blackCheckers[0];
            move(whiteCheckers, blackCheckers, initialPosition, die);
            captured = blackCheckers[0];
        }
        else
        {
            prevCaptured = whiteCheckers[0];
            move(blackCheckers, whiteCheckers, initialPosition, die);
            captured = blackCheckers[0];
        }

        // temporar
        updateCheckersNum();

        moveHistory.add(new Move(initialPosition, die));

        if (whiteCheckersNum == 0)
            return GameState.WHITE_WIN;
        if (blackCheckersNum == 0)
            return GameState.BLACK_WIN;
        if (prevCaptured != captured)
            return GameState.CAPTURED;
        return GameState.NOT_FINISHED;
    }

    private void move(int[] playerCheckers, int[] opponentCheckers, int initialPosition, int die) throws InvalidMoveException
    {
        if (playerCheckers[initialPosition] <= 0)
            throw new InvalidMoveException();

        int targetPosition;

        if (initialPosition == 0)
            targetPosition = 25-die;
        else
            targetPosition = initialPosition - die;

        if (targetPosition > 0)
        {
            int opponentPosition = 25-targetPosition;
            if (opponentCheckers[opponentPosition] > 1)
                throw new InvalidMoveException();
            if (opponentCheckers[opponentPosition] == 1)
            {
                opponentCheckers[opponentPosition] = 0;
                opponentCheckers[0]++;
            }
            playerCheckers[targetPosition]++;
        }

        playerCheckers[initialPosition]--;
    }

    private void testMove(int[] playerCheckers, int[] opponentCheckers, int initialPosition, int die) throws InvalidMoveException
    {
        if (playerCheckers[initialPosition] <= 0)
            throw new InvalidMoveException();

        int targetPosition;

        if (initialPosition == 0)
            targetPosition = 25-die;
        else
            targetPosition = initialPosition - die;

        if (targetPosition > 0)
        {
            int opponentPosition = 25-targetPosition;
            if (opponentCheckers[opponentPosition] > 1)
                throw new InvalidMoveException();
        }
    }

    public void undoLastMove(PlayerColor color, boolean captured)
    {
        Move move = moveHistory.get(moveHistory.size()-1);
        if (color == PlayerColor.WHITE)
            undo(whiteCheckers, blackCheckers, move.initialPosition, move.die, captured);
        else
            undo(blackCheckers, whiteCheckers, move.initialPosition, move.die, captured);

        updateCheckersNum();

        moveHistory.remove(moveHistory.size()-1);
    }

    private void undo(int[] playerCheckers, int[] opponentCheckers, int initialPosition, int die, boolean captured)
    {
        int targetPosition;

        if (initialPosition == 0)
            targetPosition = 25-die;
        else
            targetPosition = initialPosition - die;

        if (targetPosition > 0)
            playerCheckers[targetPosition]--;
        playerCheckers[initialPosition]++;
        if (captured)
        {
            opponentCheckers[0]--;
            opponentCheckers[25-targetPosition]++;
        }
    }

    private void updateCheckersNum()
    {
        whiteCheckersNum = 0;
        blackCheckersNum = 0;
        for (int i=0;i<25;i++)
        {
            whiteCheckersNum += whiteCheckers[i];
            blackCheckersNum += blackCheckers[i];
        }
    }

    public boolean canBearoff(PlayerColor color)
    {
        var checkers = color == PlayerColor.WHITE ? whiteCheckers : blackCheckers;

        for (int i=7;i<=24;i++)
            if (checkers[i] > 0)
                return false;
        return checkers[0] == 0;
    }

    public int bearoff(PlayerColor color, int die) throws InvalidMoveException
    {
        var checkers = color == PlayerColor.WHITE ? whiteCheckers : blackCheckers;

        if (checkers[die] > 0)
        {
            checkers[die]--;
            return die;
        }

        var opponentCheckers = color == PlayerColor.BLACK ? whiteCheckers : blackCheckers;
        for (int i=die+1;i<=6;i++)
            if (checkers[i] > 0)
            {
                move(checkers, opponentCheckers, i, die);
                return i;
            }

        for (int i=die-1;i>=1;i--)
            if (checkers[i] > 0)
            {
                checkers[i]--;
                return i;
            }
        throw new InvalidMoveException();
    }

    int testBearoff(PlayerColor color, int die) throws InvalidMoveException
    {
        var checkers = color == PlayerColor.WHITE ? whiteCheckers : blackCheckers;

        if (checkers[die] > 0)
            return die;

        for (int i=die+1;i<=6;i++)
            if (checkers[i] > 0)
                return i;

        for (int i=die-1;i>=1;i--)
            if (checkers[i] > 0)
                return i;

        throw new InvalidMoveException();
    }

    public int[] getWhiteCheckers()
    {
        return whiteCheckers;
    }

    public int[] getBlackCheckers()
    {
        return blackCheckers;
    }

    public int getWhiteCheckersNum()
    {
        return whiteCheckersNum;
    }

    public int getBlackCheckersNum()
    {
        return blackCheckersNum;
    }

    public List<Move> getMoveHistory()
    {
        return moveHistory;
    }

    @Override
    public String toString()
    {
        return Arrays.toString(whiteCheckers) + Arrays.toString(blackCheckers);
    }
}
