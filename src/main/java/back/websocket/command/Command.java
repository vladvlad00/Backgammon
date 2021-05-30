package back.websocket.command;

import back.service.game.InvalidMoveException;
import back.websocket.Message;

import java.util.Map;

public abstract class Command
{
    protected Map<String, String> options;

    public abstract Message runCommand() throws InvalidMoveException;

    public Command()
    {
    }

    public Command(Map<String, String> options)
    {
        this.options = options;
    }

    public Map<String, String> getOptions()
    {
        return options;
    }

    public void setOptions(Map<String, String> options)
    {
        this.options = options;
    }
}
