package back.controller;

import back.service.ai.RandomAiAlgorithm;
import back.service.game.Board;
import back.service.game.PlayerColor;
import back.websocket.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController
{
    @GetMapping("/{difficulty}")
    ResponseEntity<Message> getMove(@PathVariable String difficulty, @RequestBody Map<String, String> requestBody)
    {
        String boardRepresentation = requestBody.get("board");
        String color = requestBody.get("color");

        if (boardRepresentation == null || color == null)
            return ResponseEntity.badRequest().build();

        PlayerColor playerColor;
        if (color.equals("white"))
            playerColor = PlayerColor.WHITE;
        else if (color.equals("black"))
            playerColor = PlayerColor.BLACK;
        else
            return ResponseEntity.badRequest().build();

        Board board = new Board(boardRepresentation);
        if (difficulty.equals("easy"))
            return ResponseEntity.ok(new RandomAiAlgorithm().getMove(board, playerColor));
        return ResponseEntity.badRequest().build();
    }
}
