package tilegame;

public class TileGame {

    /**
     * This class is an implementation of a TileGame board,
     * including helper functions for setting/getting properties
     * of the board.
     */

    public final int rows;
    public final int cols;
    public int turn;
    private final int[][] data;
    private final int[] pools;

    public static final int PLAYER1 = 0;
    public static final int PLAYER2 = 1;
    public static final int NO_PLAYER = -1;

    /*
    Used internally to duplicate the board
     */
    private TileGame(TileGame source) {
        this.rows = source.rows;
        this.cols = source.cols;

        this.pools = new int[2];
        this.pools[0] = source.pools[0];
        this.pools[1] = source.pools[1];

        this.turn = source.turn;

        this.data = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.data[r][c] = source.data[r][c];
            }
        }
    }

    /**
     * Creates a standard 3x3x10 board.
     */
    public TileGame() {
        this(3, 3, 10);
    }

    /**
     * Creates a Tile Game board.
     * @param rows number of rows to use
     * @param cols number of columns to use
     * @param start_pool how much pool each player starts with
     */
    public TileGame(int rows, int cols, int start_pool) {
        this.rows = rows;
        this.cols = cols;

        pools = new int[2];
        pools[0] = start_pool;
        pools[1] = start_pool;

        data = new int[rows][cols];
    }

    /**
     * Gets the raw value stored at the given tile.
     * This value is:
     *     > 0 if player 1 owns it
     *     < 0 if player 2 owns it
     *     = 0 if unclaimed.
     * The raw value is one more than the true value.
     * For example, if player 2 owns the tile, and the 
     * tile's value is 3, then the raw value is -4.
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @return the raw value that represents this tile
     */
    public int getRaw(int row, int col) {
        return data[row][col];
    }

    /**
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @return the value of the given tile.
     * If the tile is unclaimed, returns 0.
     * If the tile is locked, returns 0.
     */
    public int getTileValue(int row, int col) {
        int value = data[row][col];
        if (value > 1) {
            return value - 1;
        } else if (value < -1) {
            return -1 - value;
        } else {
            return 0;
        }
    }

    /**
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @return the owner of the given tile (tilegame.TileGame.PLAYER1 or tilegame.TileGame.PLAYER2).
     * If the tile is unclaimed, returns tilegame.TileGame.NO_PLAYER
     */
    public int getTileOwner(int row, int col) {
        int value = data[row][col];
        if (value > 0) {
            return PLAYER1;
        } else if (value < 0) {
            return PLAYER2;
        } else {
            return NO_PLAYER;
        }
    }

    /**
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @return true if the tile can be claimed, false otherwise
     */
    public boolean isTileOpen(int row, int col) {
        return data[row][col] == 0;
    }

    /**
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @return true if the cell has been claimed, false otherwise
     */
    public boolean isTileClaimed(int row, int col) {
        return data[row][col] != 0;
    }

    /**
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @return true if the cell has been flipped to a 0 value, false otherwise
     */
    public boolean isTileLocked(int row, int col) {
        return data[row][col] == 1 || data[row][col] == -1;
    }

    /**
     * @return the player pool for the current player.
     */
    public int getPlayerPool() {
        return pools[turn];
    }

    /**
     * @param player the player ID (either tilegame.TileGame.PLAYER1 or tilegame.TileGame.PLAYER2)
     * @return the player pool for the given player
     */
    public int getPlayerPool(int player) {
        return pools[player];
    }

    /**
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @return true if the tile is inside of the board, false otherwise
     */
    public boolean tileExists(int row, int col) {
        return 0 <= row && row < rows && 0 <= col && col < cols;
    }

    /**
     * Gets the current player turn
     * @return either tilegame.TileGame.PLAYER1 or tilegame.TileGame.PLAYER2.
     * This function will never return tilegame.TileGame.NO_PLAYER
     */
    public int currentTurn() {
        return turn;
    }

    /**
     * Gets the next turn, given the current turn.
     * This does not alter the internal state.
     * @return either tilegame.TileGame.PLAYER1 or tilegame.TileGame.PLAYER2.
     * This function will never return tilegame.TileGame.NO_PLAYER
     */
    public int nextTurn() {
        return nextTurn(turn);
    }

    /**
     * Gets the next turn, given the player who just played
     *
     * @param prev_turn either tilegame.TileGame.PLAYER1 or tilegame.TileGame.PLAYER2.
     *                  Do not use tilegame.TileGame.NO_PLAYER.
     * @return either tilegame.TileGame.PLAYER1 or tilegame.TileGame.PLAYER2.
     * This function will never return tilegame.TileGame.NO_PLAYER
     */
    public int nextTurn(int prev_turn) {
        if (pools[1-prev_turn] > 0) {
            return 1-prev_turn;
        } else {
            return prev_turn;
        }
    }

    /**
     * Determines if the game is "finished". A game is
     * finished if neither player can make a play.
     * @return true if neither play can play, or false otherwise.
     */
    public boolean isGameFinished() {
        if (pools[0] == 0 && pools[1] == 0) {
            return true;
        } else {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (data[r][c] == 0) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * Determines if the game has a winner, even if the game
     * isn't finished yet.
     * If a player cannot play, this is true if the other player
     * is already winning, since any more plays at this point will
     * only increase the game.
     * If the game is finished, this is always true.
     * @return true if the game has a winner, or false otherwise.
     */
    public boolean isGameForfeitable() {
        if (pools[0] == 0 && pools[1] == 0) {
            return true;
        } else {
            int score = 0;
            int open = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (data[r][c] > 0) {
                        score++;
                    } else if (data[r][c] < 0) {
                        score--;
                    } else {
                        open++;
                    }
                }
            }
            if (pools[0] > 0 && pools[1] == 0 && score > 0) {
                return true;
            } else if (pools[1] > 0 && pools[0] == 0 && score < 0) {
                return true;
            } else {
                return open == 0;
            }
        }
    }


    /**
     * Counts the number of tiles that are unclaimed.
     * This value is not cached.
     * @return the number of open cells, 0 <= result <= rows * cols
     */
    public int countOpenTiles() {
        int result = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (data[r][c] == 0) {
                    result++;
                }
            }
        }

        return result;
    }


    /**
     * Counts the number of tiles that have been claimed.
     * This value is not cached.
     * @return the number of closed cells, 0 <= result <= rows * cols
     */
    public int countClaimedTiles() {
        int result = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (data[r][c] != 0) {
                    result++;
                }
            }
        }

        return result;
    }


    /**
     * Counts the current score of the game.
     * The score is determined by the difference between the number
     * of tiles players.Player 1 owns and the number of tiles players.Player 2 owns.
     * @return > 0 if PLAYER1 is in the lead,
     *         < 0 if PLAYER2 is in the lead,
     *         = 0 if tied.
     */
    public int getScore() {
        int result = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (data[r][c] > 0) {
                    result++;
                } else if (data[r][c] < 0) {
                    result--;
                }
            }
        }
        return result;
    }


    /**
     * Counts the current score of the game.
     * The score is determined by the difference between the number
     * of tiles Player 1 owns and the number of tiles Player 2 owns.
     * This score is normalized for the given player.
     * That is, the result will be positive if the given player is
     * in the lead, and negative if the given player is losing.
     * @param player the player whose score we're evaluating
     * @return > 0 if player is in the lead,
     *         < 0 if player is in the lead,
     *         = 0 if tied.
     */
    public int getScore(int player) {
        int score = getScore();
        if (player == PLAYER1) {
            return score;
        } else if (player == PLAYER2) {
            return -score;
        } else {
            return 0;
        }
    }


    /**
     * Determines the player that is currently winning, assuming
     * the game ends at the current state.
     * This value is not cached.
     * @return tilegame.TileGame.PLAYER1, tilegame.TileGame.PLAYER2, or
     * in the case of a tie, tilegame.TileGame.NO_PLAYER.
     */
    public int getWinner() {
        int score = getScore();
        if (score > 0) {
            return PLAYER1;
        } else if (score < 0) {
            return PLAYER2;
        } else {
            return NO_PLAYER;
        }
    }

    /**
     * Determines if a play is legal. A play is legal if the tile exists,
     * is unclaimed, and the value being played is between 1 and the player's
     * current pool value.
     * The player is assumed to be the current player.
     * @param row 0-indexed row
     * @param col 0-indexed col
     * @param value pool value to consume
     * @return true if the play is valid, or false otherwise
     */
    public boolean isPlayValid(int row, int col, int value) {
        return tileExists(row, col) && data[row][col] == 0 && value > 0 && pools[turn] >= value;
    }

    /**
     * Determines if a play is legal. A play is legal if the tile exists,
     * is unclaimed, and the value being played is between 1 and the player's
     * current pool value.
     * @param row 0-indexed row
     * @param col 0-indexed col
     * @param player the player that will be playing
     * @param value pool value to consume
     * @return true if the play is valid, or false otherwise
     */
    public boolean isPlayValid(int row, int col, int player, int value) {
        return tileExists(row, col) && data[row][col] == 0 && value > 0 && pools[player] >= value;
    }

    /**
     * @return a duplicate of the current board
     */
    public TileGame copy() {
        return new TileGame(this);
    }


    /**
     * Returns a clone that has the play applied.
     * This does not alter the original board.
     * The player is assumed to be the current player.
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @param value the value to play
     * @return a duplicate of the board with the given play applied.
     * Returns null if the play was invalid
     */
    public TileGame copyAndPlay(int row, int col, int value) {
        return copyAndPlay(row, col, turn, value);
    }


    /**
     * Returns a clone that has the play applied.
     * This does not alter the original board.
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @param player the player that will make this play
     * @param value the value to play
     * @return a duplicate of the board with the given play applied.
     * Returns null if the play was invalid
     */
    public TileGame copyAndPlay(int row, int col, int player, int value) {
        TileGame result = copy();
        if (result.play(row, col, player, value)) {
            return result;
        } else {
            return null;
        }
    }


    /**
     * Applies a given play to the board.
     * players.Player is assumed to be the current player.
     * This alters the state of the board, including
     * performing flips, consuming the player's pool
     * and changing the current turn
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @param value the value to play
     * @return true if the play was valid, or false otherwise
     */
    public boolean play(int row, int col, int value) {
        return play(row, col, turn, value);
    }

    /**
     * Applies a given play to the board.
     * This alters the state of the board.
     * This alters the state of the board, including
     * performing flips, consuming the player's pool
     * and changing the current turn
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @param player the player that will make this play.
     * @param value the value to play
     * @return true if the play was valid, or false otherwise
     */
    public boolean play(int row, int col, int player, int value) {
        if (isPlayValid(row, col, player, value)) {

            setTile(row, col, player, value);
            flipTile(row - 1, col, player, value);
            flipTile(row + 1, col, player, value);
            flipTile(row, col - 1, player, value);
            flipTile(row, col + 1, player, value);

            pools[player] -= value;

            turn = nextTurn();

            return true;

        } else {
            return false;
        }
    }

    /**
     * Sets the given tile. This will correctly convert the
     * value to its raw value.
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @param player the player that is claiming the tile.
     *               If set to NO_PLAYER, the tile will be unclaimed.
     * @param value the value to set the tile to.
     *              If player is NO_PLAYER, this value is ignored.
     */
    public void setTile(int row, int col, int player, int value) {
        if (player == PLAYER1) {
            data[row][col] = value + 1;
        } else if (player == PLAYER2) {
            data[row][col] = -1-value;
        } else {
            data[row][col] = 0;
        }
    }

    /**
     * Sets the pool for the given player
     * @param player either tilegame.TileGame.PLAYER1 or tilegame.TileGame.PLAYER2
     * @param new_value the value to change it to
     */
    public void setPool(int player, int new_value) {
        pools[player] = new_value;
    }

    /**
     * Determines if a tile can be flipped.
     * A tile can be flipped if it exists, is claimed,
     * and is not locked
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @return true if the tile can be flipped, or false otherwise
     */
    public boolean isTileFlippable(int row, int col) {
        return tileExists(row, col) && (data[row][col] < -1 || data[row][col] > 1);
    }

    /**
     * Determines if a tile can be flipped.
     * A tile can be flipped if it exists,
     * is claimed by the other player, and is not locked
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     * @param player the player who is performing the flip
     * @return true if the tile can be flipped, or false otherwise
     */
    public boolean isTileFlippable(int row, int col, int player) {
        if (player == PLAYER1) {
            return tileExists(row, col) && (data[row][col] < -1);
        } else if(player == PLAYER2) {
            return tileExists(row, col) && (data[row][col] > 1);
        } else {
            return false;
        }
    }

    /**
     * Flips a tile. If the tile is not flippable, this does nothing.
     * @param row the 0-indexed row
     * @param col the 0-indexeed column
     * @param player the player who is performing the flip
     * @param value the value of the flip
     */
    private void flipTile(int row, int col, int player, int value) {
        if (isTileFlippable(row, col, player) && getTileValue(row, col) < value) {
            flip(row, col);
        }
    }

    /**
     * Flips the raw data value for the given cell.
     * Assumes that the tile is claimed.
     * @param row the 0-indexed row
     * @param col the 0-indexed column
     */
    private void flip(int row, int col) {
        data[row][col] = data[row][col] > 0 ? 1-data[row][col] : -1-data[row][col];
    }

    
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        for (int r = 0; r < rows; r++) {
            result.append("    ");
            for (int c = 0; c < cols; c++) {
                int owner = getTileOwner(r, c);
                int value = getTileValue(r, c);

                // players.Player 1-owned tile
                if (owner == PLAYER1) {
                    if (value > 0) {
                        result.append(String.format("[%d] ", value));
                    } else {
                        result.append("[X] ");
                    }

                    // players.Player 2-owned tile
                } else if (owner == PLAYER2) {
                    if (value > 0) {
                        result.append(String.format("<%d> ", value));
                    } else {
                        result.append("<X> ");
                    }
                    // Unclaimed tile
                } else {
                    result.append(" -  ");
                }
            }
            result.append('\n');
        }

        // Summary Stats
        int score = getScore();
        if (score > 0) {
            result.append(String.format("+%d  ", score));
        } else if (score < 0) {
            result.append(String.format("%d  ", score));
        } else {
            result.append(" =  ");
        }

        // Pools
        result.append(String.format("P1: [%d] / P2: <%d>    ", getPlayerPool(TileGame.PLAYER1), getPlayerPool(TileGame.PLAYER2)));

        return result.toString();
    }

    public static int otherPlayer(int player) {
        return player == NO_PLAYER ? 0 : 1-player;
    }

}
