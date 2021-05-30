package front.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import front.entities.Lobby;
import front.entities.User;
import front.entities.UserRole;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Lob;
import java.util.List;

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
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(User.getInstance().getToken());
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/room", HttpMethod.GET, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            List<Lobby> body = objectMapper.readValue(response.getBody(), new TypeReference<List<Lobby>>(){});
            return body;
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String joinThroughID(Long roomID, UserRole role) {
        //TODO: join room
        return null;
    }

    public static Lobby createRoom(String roomName) {
        //TODO: create room
        return null;
    }
}
