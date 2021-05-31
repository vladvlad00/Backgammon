package front.utils.websocket;

import front.utils.websocket.Message;
import front.utils.handlers.BackgammonEvent;
import front.utils.handlers.FrameHandler;
import javafx.application.Platform;
import javafx.event.Event;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.HashMap;

public class StompSessionHandler extends StompSessionHandlerAdapter
{
    private Long roomId;
    private String sendUrl;
    private String subscribeUrl;

    public StompSessionHandler(Long roomId)
    {
        this.roomId = roomId;
        this.sendUrl = "/app/game/" + roomId;
        this.subscribeUrl = "/game_info/" + roomId;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders)
    {
        session.subscribe(subscribeUrl, this);
        System.out.println("Subscribed to " + subscribeUrl);
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
        if (message == null)
        {
            // probabil inseamna ca s-a dat disconnect - e bine
            return;
        }
        if (message.getCommand().equals("refresh"))
        {
            Platform.runLater(
                    () -> Event.fireEvent(FrameHandler.getMainMenuFrame().getLobbyCreationPanel(),
                            new BackgammonEvent(BackgammonEvent.REFRESH_LOBBY, null))
            );
        }
        else if (message.getCommand().equals("dice"))
        {
            Platform.runLater(
                    () -> Event.fireEvent(FrameHandler.getMainGameFrame().getSidePanel(),
                            new BackgammonEvent(BackgammonEvent.ROLL_DICE, message.getOptions()))
            );
        }
        else if (message.getCommand().equals("start"))
        {
            Platform.runLater(
                    () -> Event.fireEvent(FrameHandler.getMainMenuFrame().getLobbyCreationPanel(),
                            new BackgammonEvent(BackgammonEvent.START_GAME, null))
            );
        }
        else if (message.getCommand().equals("delete"))
        {
            Platform.runLater(
                    () -> Event.fireEvent(FrameHandler.getMainMenuFrame().getLobbyCreationPanel(),
                            new BackgammonEvent(BackgammonEvent.DELETE_LOBBY, null))
            );
        }
        else if (message.getCommand().equals("move"))
        {
            Platform.runLater(
                    () -> Event.fireEvent(FrameHandler.getMainGameFrame(),
                            new BackgammonEvent(BackgammonEvent.MAKE_MOVE, message.getOptions()))
            );
        }
        System.out.println("Received " + message.toString());
    }

    public Long getRoomId()
    {
        return roomId;
    }

    public String getSendUrl()
    {
        return sendUrl;
    }

    public String getSubscribeUrl()
    {
        return subscribeUrl;
    }
}
