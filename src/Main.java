import players.*;
import tilegame.TileGame;

public class Main {

    /*
     * This class is a sample game controller
     */

    private static final int PLAYER_VS_PLAYER = 0;
    private static final int PLAYER_VS_BOT = 1;
    private static final int BOT_VS_BOT = 2;
    private static final int SOLVER = 3;

    public static void main(String[] args) {

        // Set the game mode here:
        final int mode = PLAYER_VS_PLAYER;


        TileGame game = new TileGame(3, 3, 10);

        Player p1;
        Player p2;

        if (mode == PLAYER_VS_PLAYER) {
            p1 = new CLIPlayer(TileGame.PLAYER1);
            p2 = new CLIPlayer(TileGame.PLAYER2);
        } else if (mode == PLAYER_VS_BOT) {
            p1 = new CLIPlayer(TileGame.PLAYER1);
            p2 = new ABSearchPlayer(TileGame.PLAYER2);
        } else if (mode == BOT_VS_BOT) {
            p1 = new ABSearchPlayer(TileGame.PLAYER1);
            p2 = new ABSearchPlayer(TileGame.PLAYER2);
        } else if (mode == SOLVER) {
            p1 = new ABSearchPlayer(TileGame.PLAYER1, game.rows * game.cols);
            p2 = new ABSearchPlayer(TileGame.PLAYER2, game.rows * game.cols);
        }

        while (!game.isGameForfeitable()) {
            // Get the current player
            Player current_player;
            if (game.turn == TileGame.PLAYER1) {
                current_player = p1;
            } else {
                current_player = p2;
            }

            // Print the current state
            System.out.print(current_player.name);
            System.out.println("'s turn!");
            System.out.println(game);
            System.out.println();

            // Get the next move
            Move move = current_player.getNextMove(game.copy());
            if (!move.apply(game)) {
                System.out.println(" ***** INVALID ENTRY ***** ");
            }

        }

        System.out.println(game);

        // Game over!
        System.out.println();
        System.out.println("=-=-=-=-=-=-=");
        System.out.println("| GAME OVER |");
        System.out.println("=-=-=-=-=-=-=");
        System.out.println();

        // Display the winner
        int winner = game.getWinner();
        if (winner == TileGame.PLAYER1) {
            System.out.printf("%s Wins!%n", p1.name);
        } else if (winner == TileGame.PLAYER2) {
            System.out.printf("%s Wins!%n", p2.name);
        } else if (winner == TileGame.NO_PLAYER) {
            System.out.println("Draw!");
        }
    }


}