package players;

import tilegame.TileGame;

import java.util.Scanner;

public class CLIPlayer extends Player {

    Scanner scanner;

    public CLIPlayer(int player) {
        super(player);
        scanner = new Scanner(System.in);
    }

    public CLIPlayer(String name, int player) {
        super(name, player);
        scanner = new Scanner(System.in);
    }

    @Override
    public Move getNextMove(TileGame game) {

        while (true) {
            // Prompt for a move
            int row = getInt(scanner, "Row (1~%d): ", game.rows) - 1;
            int col = getInt(scanner, "Col (1~%d): ", game.cols) - 1;
            int val = getInt(scanner, "Value (1~%d): ", game.getPlayerPool());

            TileGame preview = game.copyAndPlay(row, col, val);
            System.out.println(preview);

            if (preview == null) {
                System.out.println("***** Invalid selection ***** ");
            } else {
                System.out.print("Is this correct? [Y/n]: ");

                String response = scanner.nextLine().toLowerCase();
                if (response.length() == 0 || response.charAt(0) == 'y') {
                    return new Move(row, col, val);
                }
            }

            System.out.println(game);
        }


    }

    private static int getInt(Scanner scanner, String prompt, int max) {
        System.out.printf(prompt, max);

        // Fetch the next integer and discard the newline character
        int result = scanner.nextInt();
        scanner.nextLine();

        // Repeat the fetch on an invalid entry
        while (result <= 0 || result > max) {
            System.out.println("***** Invalid entry! *****");
            System.out.printf(prompt, max);
            result = scanner.nextInt();
            scanner.nextLine();
        }

        return result;
    }

}
