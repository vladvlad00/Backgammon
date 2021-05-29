package back.websocket.command;

import back.websocket.Message;

import java.util.Map;

public class ErrorCommand extends Command
{
    public ErrorCommand()
    {
    }

    public ErrorCommand(Map<String, String> options)
    {
        super(options);
    }

    @Override
    public Message runCommand()
    {
        return new Message("error", options);
    }
}
