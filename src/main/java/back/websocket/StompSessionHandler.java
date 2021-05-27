package back.websocket;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class StompSessionHandler extends StompSessionHandlerAdapter
{
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders)
    {
        System.out.println("New session " + session.getSessionId());
        session.subscribe("/game/vlad", this);
        System.out.println("Subscribed to /game/vlad");
        session.send("/app/hello", getSampleMessage());
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
        System.out.println("Received " + message.getContent());
    }

    private Message getSampleMessage()
    {
        return new Message("Vlad a trimis asta");
    }
}
