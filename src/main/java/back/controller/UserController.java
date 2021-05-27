package back.controller;

import back.entity.GameRoom;
import back.entity.User;
import back.repository.GameRoomRepository;
import back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class UserController
{
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRoomRepository gameRoomRepository;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User user)
    {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PutMapping("/user/room")
    public ResponseEntity<String> joinRoom(@RequestBody Map<String, String> roomJson)
    {
        long roomId;
        String username; //momentan va fi in body, dar facem sa fie pus din token in header
        String role;
        try
        {
            username = roomJson.get("username");
            roomId = Long.parseLong(roomJson.get("roomId"));
            role = roomJson.get("role");
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

        if (role == null)
            return ResponseEntity.badRequest().build();

        Optional<GameRoom> gameRoomOpt = gameRoomRepository.findById(roomId);
        if (gameRoomOpt.isEmpty())
            return ResponseEntity.notFound().build();

        GameRoom gameRoom = gameRoomOpt.get();
        User user = userRepository.findByUsername(username);
        GameRoom oldGameRoom = user.getGameRoom();

        if (oldGameRoom != null)
            oldGameRoom.getUsers().remove(user);

        user.setRole(role);
        user.setGameRoom(gameRoom);
        gameRoom.getUsers().add(user);

        gameRoomRepository.save(gameRoom);
        if (oldGameRoom != null)
            gameRoomRepository.save(oldGameRoom);
        userRepository.save(user);

        return ResponseEntity.ok("User joined room as " + role);
    }

    //username nu va mai fi in path, ci va fi intr-un header automat dupa token
    @DeleteMapping("/user/{username}/room")
    public ResponseEntity<String> leaveRoom(@PathVariable String username)
    {
        User user = userRepository.findByUsername(username);
        user.setRole(null);
        if (user.getGameRoom() != null)
        {
            GameRoom gameRoom = user.getGameRoom();
            gameRoom.getUsers().remove(user);
            user.setGameRoom(null);

            gameRoomRepository.save(gameRoom);
            userRepository.save(user);
        }
        return ResponseEntity.noContent().build();
    }
}
