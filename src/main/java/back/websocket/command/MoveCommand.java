package back.websocket.command;

import back.service.game.Game;
import back.service.game.GameState;
import back.service.game.InvalidMoveException;
import back.service.game.PlayerColor;
import back.websocket.Message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MoveCommand extends Command
{
    Game game;

    public MoveCommand()
    {
    }

    public MoveCommand(Map<String, String> options)
    {
        super(options);
    }

    public MoveCommand(Map<String, String> options, Game game)
    {
        super(options);
        this.game = game;
    }

    @Override
    public Message runCommand() throws InvalidMoveException
    {
        String color = options.get("color");

        if (color == null)
            throw new InvalidMoveException();

        PlayerColor playerColor;
        if (color.toLowerCase(Locale.ROOT).equals("white"))
            playerColor = PlayerColor.WHITE;
        else if (color.toLowerCase(Locale.ROOT).equals("black"))
            playerColor = PlayerColor.BLACK;
        else
            throw new InvalidMoveException();

        int initialPosition;
        int die;
        try
        {
            initialPosition = Integer.parseInt(options.get("initialPosition"));
            die = Integer.parseInt(options.get("die"));
        }
        catch (Exception e)
        {
            throw new InvalidMoveException();
        }

        GameState state = game.makeMove(playerColor, initialPosition, die);

        Map<String, String> responseOptions = new HashMap<>();
        responseOptions.put("board", game.getBoard().toString());
        responseOptions.put("state", state.toString());

        return new Message("move", responseOptions);
    }
}
