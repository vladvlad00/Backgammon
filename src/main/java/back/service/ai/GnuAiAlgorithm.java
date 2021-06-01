package back.service.ai;

import back.service.game.Board;
import back.service.game.PlayerColor;
import back.websocket.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GnuAiAlgorithm implements AiAlgorithm
{
    @Override
    public List<Message> getMove(Board board, PlayerColor color, int die1, int die2) throws IOException
    {
        var moveString = GnuBgManager.getInstance().getMoveString(board, color, die1, die2);
        int[] positions = Arrays.stream(moveString.substring(1, moveString.length() - 1).split(", "))
                .mapToInt(Integer::parseInt).toArray();
        List<Message> movesMade = new ArrayList<>();
        for (int i=0;i<positions.length;i+=2)
        {
            if (positions[i] == 25)
                movesMade.add(Message.getMoveMessage(color, 0, positions[i]-positions[i+1]));
            else
                movesMade.add(Message.getMoveMessage(color, positions[i], positions[i]-positions[i+1]));
        }
        return movesMade;
    }
}
