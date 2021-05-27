package back.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController
{
    @MessageMapping("/hello")
    @SendTo("/game/vlad")
    public Message message(Message m)
    {
        return new Message("A functionat");
    }
}
