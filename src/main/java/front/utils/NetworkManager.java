package front.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import front.entities.Lobby;
import front.entities.User;
import front.entities.UserRole;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkManager {
    private static final String URL = "http://localhost:8081";

    public static String login(User user) {
        try {
            RestTemplate restTemplate = new RestTemplate();
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
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL + "/register", request, String.class);
        if (response.getStatusCode().is2xxSuccessful())
            return "ok";
        return "err";
    }

    public static List<Lobby> getAllLobbies() {
        RestTemplate restTemplate = new RestTemplate();
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
        RestTemplate restTemplate = new RestTemplate();
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
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        Map<String, String> requestJson = new HashMap<>();
        requestJson.put("roomId", roomId.toString());
        requestJson.put("role", role.name());
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestJson, headers);
        ResponseEntity<Lobby> response = restTemplate.exchange(URL + "/user/room", HttpMethod.PUT, request, Lobby.class);
        if (!response.getStatusCode().is2xxSuccessful())
            return null;
        return response.getBody();
    }

    public static Lobby createRoom(String roomName) {
        RestTemplate restTemplate = new RestTemplate();
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
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(URL + "/user/room", HttpMethod.DELETE, entity, String.class);
    }

    private static HttpHeaders getHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(User.getInstance().getToken());
        return headers;
    }
}
