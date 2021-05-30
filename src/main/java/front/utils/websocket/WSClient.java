package front.utils.websocket;

import back.websocket.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

public class WSClient
{
    private static String URL = "ws://localhost:8081/ws";
    private static WSClient instance;

    private WebSocketClient client;
    private WebSocketStompClient stompClient;
    private StompSessionHandler sessionHandler;
    private StompSession session;

    private WSClient()
    {
        client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    public void connect(Long roomId) throws ExecutionException, InterruptedException
    {
        sessionHandler = new StompSessionHandler(roomId);
        session = stompClient.connect(URL, sessionHandler).get();
    }

    public void disconnect()
    {
        session.disconnect();
        session = null;
        sessionHandler = null;
    }

    public void sendMessage(Message message)
    {
        session.send(sessionHandler.getSendUrl(), message);
    }

    public static WSClient getInstance()
    {
        if (instance == null)
            instance = new WSClient();
        return instance;
    }
}
