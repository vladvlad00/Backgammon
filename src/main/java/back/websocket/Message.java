package back.websocket;

import back.service.game.PlayerColor;

import java.util.HashMap;
import java.util.Map;

public class Message
{
    private String command;
    private Map<String, String> options;

    public Message()
    {
    }

    public Message(String command, Map<String, String> options)
    {
        this.command = command;
        this.options = options;
    }

    public static Message getMoveMessage(PlayerColor color, int initialPosition, int die)
    {
        Map<String, String> moveOptions = new HashMap<>();
        moveOptions.put("color", color.toString());
        moveOptions.put("initialPosition", String.valueOf(initialPosition));
        moveOptions.put("die", String.valueOf(die));
        return new Message("move", moveOptions);
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public Map<String, String> getOptions()
    {
        return options;
    }

    public void setOptions(Map<String, String> options)
    {
        this.options = options;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "command='" + command + '\'' +
                ", options=" + options +
                '}';
    }
}
