package back.websocket.command;

import back.service.game.Game;
import back.service.game.GameState;
import back.service.game.InvalidMoveException;
import back.service.game.PlayerColor;
import back.websocket.Message;

import java.util.Arrays;
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
        int[] dice;
        try
        {
            initialPosition = Integer.parseInt(options.get("initialPosition"));
            dice = Arrays.stream(options.get("die").split(",")).mapToInt(Integer::parseInt).toArray();
        }
        catch (Exception e)
        {
            throw new InvalidMoveException();
        }

        GameState state = GameState.NOT_FINISHED;

        if (dice.length == 2)
        {
            try
            {
                for (int die : dice)
                {
                    state = game.makeMove(playerColor, initialPosition, die);
                    if (initialPosition == 0)
                        initialPosition = 25-die;
                    else
                        initialPosition -= die;
                    if (initialPosition < 0)
                        throw new InvalidMoveException();
                    if (state == GameState.BLACK_WIN || state == GameState.WHITE_WIN)
                        break;
                }
            }
            catch (InvalidMoveException e)
            {
                int aux = dice[0];
                dice[0] = dice[1];
                dice[1] = aux;
                for (int die : dice)
                {
                    state = game.makeMove(playerColor, initialPosition, die);
                    if (initialPosition == 0)
                        initialPosition = 25-die;
                    else
                        initialPosition -= die;
                    if (initialPosition < 0)
                        throw new InvalidMoveException();
                    if (state == GameState.BLACK_WIN || state == GameState.WHITE_WIN)
                        break;
                }
            }
        }
        else
        {
            for (int die : dice)
            {
                state = game.makeMove(playerColor, initialPosition, die);
                if (initialPosition == 0)
                    initialPosition = 25-die;
                else
                    initialPosition -= die;
                if (initialPosition < 0)
                    throw new InvalidMoveException();
                if (state == GameState.BLACK_WIN || state == GameState.WHITE_WIN)
                    break;
            }
        }

        Map<String, String> responseOptions = new HashMap<>();
        responseOptions.put("board", game.getBoard().toString());
        responseOptions.put("state", state.toString());

        return new Message("move", responseOptions);
    }
}
