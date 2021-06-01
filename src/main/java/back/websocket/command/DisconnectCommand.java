package back.websocket.command;

import back.service.game.InvalidMoveException;
import back.websocket.Message;

import java.util.Map;

public class DisconnectCommand extends Command
{
    public DisconnectCommand()
    {
    }

    public DisconnectCommand(Map<String, String> options)
    {
        super(options);
    }

    @Override
    public Message runCommand()
    {
        return new Message("disconnect", options);
    }
}
