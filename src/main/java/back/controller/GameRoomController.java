package back.controller;

import back.entity.GameRoom;
import back.entity.User;
import back.repository.GameRoomRepository;
import back.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class GameRoomController
{
    @Autowired
    private GameRoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    private ResponseEntity<GameRoom> createRoom(@RequestBody GameRoom room)
    {
        if (room.getId() != null && roomRepository.findById(room.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        room.setState("not started");

        var createdRoom = roomRepository.save(room);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/room/{id}")
                .buildAndExpand(createdRoom.getId()).toUri();
        return ResponseEntity.created(uri).body(createdRoom);
    }

    @GetMapping
    private ResponseEntity<Iterable<GameRoom>> listAllRooms()
    {
        var allRooms = roomRepository.findAll();
        for (var room : allRooms)
        {
            for (var user : room.getUsers())
            {
                user.setPassword(null);
            }
        }
        return ResponseEntity.ok(allRooms);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GameRoom> listRoomById(@PathVariable Long id)
    {
        var roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty())
            return ResponseEntity.notFound().build();

        var room = roomOpt.get();

        for (var user : room.getUsers())
        {
            user.setPassword(null);
        }

        return ResponseEntity.ok(room);
    }

    @PutMapping("/{id}/start")
    private ResponseEntity<GameRoom> startGame(@PathVariable Long id)
    {
        var roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty())
            return ResponseEntity.notFound().build();

        var room = roomOpt.get();

        room.setState("running");

        var updatedRoom = roomRepository.save(room);

        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<GameRoom> deleteRoom(@PathVariable Long id)
    {
        var roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty())
            return ResponseEntity.notFound().build();

        var room = roomOpt.get();

        for (var user : room.getUsers())
        {
            user.setGameRoom(null);
            userRepository.save(user);
        }

        roomRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ai/{difficulty}")
    private ResponseEntity<User> addAi(@PathVariable Long id, @PathVariable String difficulty)
    {
        Optional<GameRoom> gameRoomOpt = roomRepository.findById(id);
        if (gameRoomOpt.isEmpty())
            return ResponseEntity.notFound().build();

        GameRoom gameRoom = gameRoomOpt.get();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(
                "https://randomuser.me/api/?inc=name&nat=us",String.class);
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            var nameNode = jsonNode.get("results").get(0).get("name");
            String username = nameNode.get("first").asText() + nameNode.get("last").asText();
            User user = new User();
            user.setUsername(username);

            user.setGameRoom(gameRoom);
            user.setRole("AI_" + difficulty.toUpperCase());
            user.setPassword("\uD83D\uDC41");
            gameRoom.getUsers().add(user);

            userRepository.save(user);
            roomRepository.save(gameRoom);
            return ResponseEntity.ok(user);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/ai/{username}")
    private ResponseEntity<User> deleteAi(@PathVariable Long id, @PathVariable String username)
    {
        Optional<GameRoom> gameRoomOpt = roomRepository.findById(id);
        Optional<User> userOpt = userRepository.findById(username);

        if (gameRoomOpt.isEmpty() || userOpt.isEmpty())
            return ResponseEntity.notFound().build();

        GameRoom gameRoom = gameRoomOpt.get();
        User user = userOpt.get();

        gameRoom.getUsers().remove(user);
        user.setGameRoom(null);
        roomRepository.save(gameRoom);
        userRepository.deleteById(username);

        return ResponseEntity.noContent().build();
    }
}
