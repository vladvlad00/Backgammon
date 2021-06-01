package front.utils.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import front.entities.Lobby;
import front.entities.LobbyUser;
import front.entities.User;
import front.entities.UserRole;
import front.utils.websocket.Message;
import front.utils.websocket.WSClient;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkManager {
    private static final String URL = "http://localhost:8081";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static String login(User user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> request = new HttpEntity<>(user, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(URL + "/login", request, String.class);
            if (response.getStatusCode().is2xxSuccessful())
                return "ok " + response.getBody();
            return "err";
        }
        catch(Exception e) {
            return "err" + e.getMessage();
        }
    }

    public static String register(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL + "/register", request, String.class);
        if (response.getStatusCode().is2xxSuccessful())
            return "ok";
        return "err";
    }

    public static List<Lobby> getAllLobbies() {
        HttpHeaders headers = getHeaders();
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/room", HttpMethod.GET, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful())
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Lobby>>(){});
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Lobby getLobby(Long id)
    {
        HttpHeaders headers = getHeaders();
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/room/" + id, HttpMethod.GET, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful())
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            Lobby lobby = objectMapper.readValue(response.getBody(), new TypeReference<Lobby>(){});
            lobby.separateUsers();
            return lobby;
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Lobby joinThroughID(Long roomId, UserRole role) {
        HttpHeaders headers = getHeaders();
        Map<String, String> requestJson = new HashMap<>();
        requestJson.put("roomId", roomId.toString());
        requestJson.put("role", role.name());
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestJson, headers);
        ResponseEntity<Lobby> response = restTemplate.exchange(URL + "/user/room", HttpMethod.PUT, request, Lobby.class);
        if (!response.getStatusCode().is2xxSuccessful())
            return null;
        Lobby lobby = response.getBody();
        lobby.separateUsers();
        return lobby;
    }

    public static Lobby createRoom(String roomName) {
        HttpHeaders headers = getHeaders();
        Map<String, String> requestJson = new HashMap<>();
        requestJson.put("name", roomName);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestJson, headers);
        ResponseEntity<Lobby> response = restTemplate.postForEntity(URL + "/room", request, Lobby.class);
        if (!response.getStatusCode().is2xxSuccessful())
            return null;
        return response.getBody();
    }

    public static void leaveRoom() {
        User.getInstance().setInRoom(false);
        HttpHeaders headers = getHeaders();
        HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(URL + "/user/room", HttpMethod.DELETE, entity, String.class);
        WSClient.getInstance().disconnect();
    }

    public static LobbyUser addAI(Long id, UserRole dif) {
        HttpHeaders headers = getHeaders();
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<LobbyUser> response = restTemplate.exchange(URL + "/room/" + id + "/ai/" + dif.toString().split("_")[1].toLowerCase(), HttpMethod.POST, entity, LobbyUser.class);
        if (response.getStatusCode().is2xxSuccessful())
            return response.getBody();
        return null;
    }

    public static void removeAI(Long id, String name) {
        HttpHeaders headers = getHeaders();
        HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(URL + "/room/" + id + "/ai/" + name, HttpMethod.DELETE, entity, LobbyUser.class);
    }

    public static void deleteRoom(Long id) {
        User.getInstance().setInRoom(false);
        HttpHeaders headers = getHeaders();
        HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(URL + "/room/" + id, HttpMethod.DELETE, entity, String.class);
    }

    public static List<Message> getAiMoves(String board, String color, int die1, int die2, String difficulty)
    {
        HttpHeaders headers = getHeaders();
        Map<String, String> requestJson = new HashMap<>();
        requestJson.put("board", board);
        requestJson.put("color", color);
        requestJson.put("die1", String.valueOf(die1));
        requestJson.put("die2", String.valueOf(die2));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL + "/ai/" + difficulty, request, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Message>>(){});
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static HttpHeaders getHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(User.getInstance().getToken());
        return headers;
    }
}
