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
        int targetPosition;

        if (initialPosition == 0)
            targetPosition = 25-die;
        else
            targetPosition = initialPosition - die;
        int opponentPosition = 25-targetPosition;

        if (opponentCheckers[opponentPosition] > 1)
            throw new InvalidMoveException();
        if (opponentCheckers[opponentPosition] == 1)
        {
            opponentCheckers[die] = 0;
            opponentCheckers[0]++;
        }
        playerCheckers[initialPosition]--;
        if (targetPosition > 0)
            playerCheckers[targetPosition]++;
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
        return "Board{" +
                "whiteCheckers=" + Arrays.toString(whiteCheckers) +
                ", blackCheckers=" + Arrays.toString(blackCheckers) +
                '}';
    }
}
