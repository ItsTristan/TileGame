package players;

import tilegame.TileGame;

public interface Heuristic {
    /**
     * Estimate predicts how valuable the state is when
     * a search has reached its depth limit.
     * @param startBoard the board that we started from
     * @param endBoard the final board that we're evaluating
     * @return the approximate value of the board.
     */
    float estimate(TileGame startBoard, TileGame endBoard);

    /**
     * Calculates the true value of a board state
     * @param endBoard the final board that we're evaluating
     * @return the true value of the board.
     */
    float calculate(TileGame endBoard);
}
