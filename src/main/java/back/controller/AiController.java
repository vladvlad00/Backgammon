package back.controller;

import back.service.ai.ExpectiminimaxAiAlgorithm;
import back.service.ai.GnuAiAlgorithm;
import back.service.ai.RandomAiAlgorithm;
import back.service.game.Board;
import back.service.game.PlayerColor;
import back.websocket.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController
{
    @PostMapping("/{difficulty}")
    ResponseEntity<Iterable<Message>> getMove(@PathVariable String difficulty, @RequestBody Map<String, String> requestBody) throws IOException, CloneNotSupportedException
    {
        String boardRepresentation = requestBody.get("board");
        String color = requestBody.get("color");
        int die1, die2;

        try
        {
            die1 = Integer.parseInt(requestBody.get("die1"));
            die2 = Integer.parseInt(requestBody.get("die2"));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

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
        switch (difficulty)
        {
            case "easy":
                return ResponseEntity.ok(new RandomAiAlgorithm().getMove(board, playerColor, die1, die2));
            case "medium":
                return ResponseEntity.ok(new ExpectiminimaxAiAlgorithm().getMove(board, playerColor, die1, die2));
            case "hard":
                return ResponseEntity.ok(new GnuAiAlgorithm().getMove(board, playerColor, die1, die2));
        }
        return ResponseEntity.badRequest().build();
    }
}
