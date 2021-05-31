package back.service.game;

import java.util.Arrays;

public class Board
{
    private int[] whiteCheckers;
    private int[] blackCheckers;
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
    }

    public GameState makeMove(PlayerColor color, int initialPosition, int die) throws InvalidMoveException
    {
        if (color == PlayerColor.WHITE)
            move(whiteCheckers, blackCheckers, initialPosition, die);
        else
            move(blackCheckers, whiteCheckers, initialPosition, die);

        // temporar
        updateCheckersNum();

        if (whiteCheckersNum == 0)
            return GameState.WHITE_WIN;
        if (blackCheckersNum == 0)
            return GameState.BLACK_WIN;
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
                opponentCheckers[die] = 0;
                opponentCheckers[0]++;
            }
            playerCheckers[targetPosition]++;
        }

        playerCheckers[initialPosition]--;
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

    @Override
    public String toString()
    {
        return Arrays.toString(whiteCheckers) + Arrays.toString(blackCheckers);
    }
}
