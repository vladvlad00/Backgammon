package front.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class NetworkManager {
    private static final String URL = "http://localhost:8081";

    public static String login(String username, String password) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //TODO de facut mai elegant aici cu o clasa de user
            HttpEntity<String> request = new HttpEntity<>("{ \"username\": \"" + username +
                    "\", \"password\": \"" + password + "\" }", headers);
            ResponseEntity<String> response = restTemplate.postForEntity(URL + "/login", request, String.class);
            if (response.getStatusCode().is2xxSuccessful())
                return "ok " + response.getBody();
            return "err";
        }
        catch(Exception e) {
            return "err" + e.getMessage();
        }
    }

    public static String register(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //TODO de facut mai elegant aici cu o clasa de user
        HttpEntity<String> request = new HttpEntity<>("{ \"username\": \"" + username +
                "\", \"password\": \"" + password + "\" }", headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL + "/register", request, String.class);
        if (response.getStatusCode().is2xxSuccessful())
            return "ok";
        return "err";
    }
}
