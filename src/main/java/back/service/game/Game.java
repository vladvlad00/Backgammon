package back.service.game;

public class Game
{
    private Board board;

    public GameState makeMove(PlayerColor color, int initialPosition, int die) throws InvalidMoveException
    {
        return board.makeMove(color, initialPosition, die);
    }

    public Board getBoard()
    {
        return board;
    }
}
