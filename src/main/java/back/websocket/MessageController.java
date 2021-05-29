package back.websocket;

import back.service.GameManager;
import back.service.InvalidMoveException;
import back.websocket.command.DiceCommand;
import back.websocket.command.ErrorCommand;
import back.websocket.command.MoveCommand;
import back.websocket.command.StartCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MessageController
{
    @Autowired
    private GameManager gameManager;

    @MessageMapping("/game/{id}")
    @SendTo("/game_info/{id}")
    public Message message(Message m, @DestinationVariable Long id)
    {
        switch (m.getCommand())
        {
            case "start":
                return new StartCommand().runCommand();
            case "dice":
                return new DiceCommand().runCommand();
            case "move":
                try
                {
                    return new MoveCommand(m.getOptions(), gameManager.getGame(id)).runCommand();
                }
                catch (InvalidMoveException e)
                {
                    Map<String, String> options = new HashMap<>();
                    options.put("message", "Invalid move command");
                    return new ErrorCommand(options).runCommand();
                }
            default:
                Map<String, String> options = new HashMap<>();
                options.put("message", "Unknown command " + m.getCommand());
                return new ErrorCommand(options).runCommand();
        }
    }
}
