package back.websocket.command;

import back.service.InvalidMoveException;
import back.websocket.Message;

import java.util.Map;

public class RefreshCommand extends Command
{
    public RefreshCommand()
    {
    }

    public RefreshCommand(Map<String, String> options)
    {
        super(options);
    }

    @Override
    public Message runCommand()
    {
        return new Message("refresh", null);
    }
}
