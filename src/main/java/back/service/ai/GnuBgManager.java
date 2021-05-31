package back.service.ai;

import back.service.game.Board;
import back.service.game.PlayerColor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GnuBgManager
{
    private static GnuBgManager instance;
    private ProcessBuilder builder;
    private Process process;
    private InputStream out;
    private OutputStream in;

    private GnuBgManager() throws IOException
    {
        builder = new ProcessBuilder("C:\\Program Files (x86)\\gnubg\\gnubg-cli.exe");
        builder.redirectErrorStream(true);
        process = builder.start();
        out = process.getInputStream();
        in = process.getOutputStream();

        byte[] buffer = new byte[4000];
        int nr = out.available();
        if (nr > 0)
        {
            int n = out.read(buffer, 0, Math.min(nr, buffer.length));
            System.out.println(new String(buffer, 0, n));
        }

        in.write(">\n".getBytes(StandardCharsets.UTF_8), 0, ">\n".getBytes(StandardCharsets.UTF_8).length);
        in.flush();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
        }

        nr = out.available();
        if (nr > 0)
        {
            int n = out.read(buffer, 0, Math.min(nr, buffer.length));
            System.out.println(new String(buffer, 0, n));
        }
    }

    public static GnuBgManager getInstance() throws IOException
    {
        if (instance == null)
            instance = new GnuBgManager();
        return instance;
    }

    public String getMoveString(Board board, PlayerColor color, int die1, int die2) throws IOException
    {
        var playerBoard = color == PlayerColor.WHITE
                                ? board.getWhiteCheckers().clone()
                                : board.getBlackCheckers().clone();
        var opponentBoard = color == PlayerColor.BLACK
                ? board.getWhiteCheckers().clone()
                : board.getBlackCheckers().clone();

        rotateLeft(playerBoard);
        rotateLeft(opponentBoard);

        String boardString = Arrays.toString(opponentBoard) + "," + Arrays.toString(playerBoard);
        String diceString = die1 + "," + die2;
        String cmd = "ctx = {'cubeful': 0, 'deterministic': 1,'noise': 0.00001,'plies': 1}\n" +
                "b = (" + boardString + ")\n" +
                "gnubg.findbestmove(b,None,ctx,(" + diceString +"))\n";

        in.write(cmd.getBytes(StandardCharsets.UTF_8), 0, cmd.getBytes(StandardCharsets.UTF_8).length);
        in.flush();

        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException ignored)
        {
        }

        byte[] buffer = new byte[4000];
        int nr = out.available();
        if (nr > 0)
        {
            int n = out.read(buffer, 0, Math.min(nr, buffer.length));
            String s = new String(buffer, 0, n);
            return s.substring(s.indexOf('('), s.indexOf(')')+1);
        }
        return null;
    }

    private void rotateLeft(int[] v)
    {
        int aux = v[0];
        for (int i=0;i<v.length-1;i++)
            v[i] = v[i+1];
        v[v.length-1] = aux;
    }
}
