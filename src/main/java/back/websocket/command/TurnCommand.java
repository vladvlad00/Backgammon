package back.websocket.command;

import back.websocket.Message;

import java.util.Map;

public class TurnCommand extends Command
{
    public TurnCommand()
    {
    }

    public TurnCommand(Map<String, String> options)
    {
        super(options);
    }

    @Override
    public Message runCommand()
    {
        return new Message("turn", null);
    }
}
