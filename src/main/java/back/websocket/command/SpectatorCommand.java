package back.websocket.command;

import back.service.game.InvalidMoveException;
import back.websocket.Message;

import java.util.Map;

public class SpectatorCommand extends Command
{
    public SpectatorCommand()
    {
    }

    public SpectatorCommand(Map<String, String> options)
    {
        super(options);
    }

    @Override
    public Message runCommand()
    {
        return new Message("spectator", options);
    }
}
