package back.websocket;

import front.utils.websocket.StompSessionHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;

public class Client2
{
    private static String URL = "ws://localhost:8081/ws";
    public static void main(String[] args)
    {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new StompSessionHandler(2L);
        stompClient.connect(URL, sessionHandler);

        new Scanner(System.in).nextLine();
    }
}
