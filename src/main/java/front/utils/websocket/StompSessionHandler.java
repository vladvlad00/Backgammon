package front.utils.websocket;

import back.websocket.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.HashMap;

public class StompSessionHandler extends StompSessionHandlerAdapter
{
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders)
    {
        System.out.println("New session " + session.getSessionId());
        session.subscribe("/game_info/2", this);
        System.out.println("Subscribed");
        session.send("/app/game/2", getSampleMessage());
        System.out.println("Message sent");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                byte[] payload, Throwable exception)
    {
        System.out.println("Got exception " + exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers)
    {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload)
    {
        Message message = (Message) payload;
        System.out.println("Received " + message.toString());
    }

    private Message getSampleMessage()
    {
        HashMap<String, String> options = new HashMap<>();
        options.put("test_param1", "aaa");
        options.put("test_param2", "bbb");
        return new Message("test_command", options);
    }
}
