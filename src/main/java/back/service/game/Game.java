package back.service.game;

public class Game
{
    private Board board;

    public Game()
    {
        //this.board = new Board("[0, 5, 2, 1, 2, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0][0, 5, 2, 1, 2, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]");
        this.board = new Board();
    }

    public GameState makeMove(PlayerColor color, int initialPosition, int die) throws InvalidMoveException
    {
        return board.makeMove(color, initialPosition, die);
    }

    public Board getBoard()
    {
        return board;
    }
}
