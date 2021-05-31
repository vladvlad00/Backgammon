package back.websocket.command;

import back.service.game.InvalidMoveException;
import back.websocket.Message;

import java.util.Map;

public class DeleteCommand extends Command
{
    public DeleteCommand()
    {
    }

    public DeleteCommand(Map<String, String> options)
    {
        super(options);
    }

    @Override
    public Message runCommand()
    {
        return new Message("delete", null);
    }
}
