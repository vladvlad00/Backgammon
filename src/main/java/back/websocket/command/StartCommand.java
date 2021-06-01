package back.websocket.command;

import back.websocket.Message;

import java.util.Map;

public class StartCommand extends Command
{
    public StartCommand()
    {
    }

    public StartCommand(Map<String, String> options)
    {
        super(options);
    }

    @Override
    public Message runCommand()
    {
        return new Message("start", options);
    }
}
