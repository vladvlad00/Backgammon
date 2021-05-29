package back.websocket.command;

import back.websocket.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DiceCommand extends Command
{
    public DiceCommand()
    {
    }

    public DiceCommand(Map<String, String> options)
    {
        super(options);
    }

    @Override
    public Message runCommand()
    {
        Map<String, String> options = new HashMap<>();
        int nr1 = ThreadLocalRandom.current().nextInt(1, 7);
        int nr2 = ThreadLocalRandom.current().nextInt(1, 7);
        options.put("die1", String.valueOf(nr1));
        options.put("die2", String.valueOf(nr2));
        return new Message("dice", options);
    }
}
