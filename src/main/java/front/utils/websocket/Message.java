package front.utils.websocket;

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
