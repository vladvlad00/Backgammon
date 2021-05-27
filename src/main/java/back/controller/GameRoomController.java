package back.controller;

import back.entity.GameRoom;
import back.repository.GameRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/room")
public class GameRoomController
{
    @Autowired
    private GameRoomRepository roomRepository;

    @PostMapping
    private ResponseEntity<GameRoom> createRoom(@RequestBody GameRoom room)
    {
        if (room.getId() != null && roomRepository.findById(room.getId()).isPresent())
            return ResponseEntity.badRequest().build();

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

        return ResponseEntity.ok(roomOpt.get());
    }
}
