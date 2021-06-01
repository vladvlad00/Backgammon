package back.websocket;

import back.service.game.GameManager;
import back.service.game.InvalidMoveException;
import back.websocket.command.*;
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
        System.out.println("Received message: " + m);
        switch (m.getCommand())
        {
            case "start":
                gameManager.createGame(id);
                return new StartCommand(m.getOptions()).runCommand();
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
            case "refresh":
                return new RefreshCommand().runCommand();
            case "delete":
                return new DeleteCommand().runCommand();
            case "turn":
                return new TurnCommand().runCommand();
            case "spectator":
                return new SpectatorCommand(m.getOptions()).runCommand();
            case "disconnect":
                return new DisconnectCommand(m.getOptions()).runCommand();
            default:
                Map<String, String> options = new HashMap<>();
                options.put("message", "Unknown command " + m.getCommand());
                return new ErrorCommand(options).runCommand();
        }
    }
}
