package tennis;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class DormaKaba
{
    static TennisScoreBoard scoreBoard;
    static TennisPlayer player1;
    static TennisPlayer player2;

    public static void main( String[] args )
    {
        player1 = new TennisPlayer();
        player2 = new TennisPlayer();
        StartScoreBoard();
    }

    private static void StartScoreBoard() {
        SwingUtilities.invokeLater(() -> {
            scoreBoard = new TennisScoreBoard( player1, player2 );
        });
    }
}
