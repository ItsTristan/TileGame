package players;

import tilegame.TileGame;

public class ABSearchPlayer extends Player implements Heuristic {

    Heuristic heuristic = this;
    int maxDepth = 3;

    /**
     * gamma is used to control how much the bot values future
     * states.
     *  A value gamma = 1.0 treats end states at full value,
     *  A value 0.0 < gamma < 1.0 weakens valuation of deep searches
     *  A value gamma > 1.0 strengths valuation of deep searches
     */
    float gamma = 1.0f;

    public ABSearchPlayer(int player) {
        super(String.format("Computer Player %d", player+1), player);
    }

    public ABSearchPlayer(int player, int maxDepth) {
        this(player);
        this.maxDepth = maxDepth;
    }

    public ABSearchPlayer(int player, Heuristic heuristic) {
        this(player);
        this.heuristic = heuristic;
    }

    public ABSearchPlayer(int player, int maxDepth, Heuristic heuristic) {
        this(player);
        this.maxDepth = maxDepth;
        this.heuristic = heuristic;
    }

    @Override
    public Move getNextMove(TileGame game) {
        // Compute the maximum meaningful play
        int p1Max = game.getPlayerPool(TileGame.PLAYER2) + 1;
        int p2Max = game.getPlayerPool(TileGame.PLAYER1) + 1;
        for (int r = 0; r < game.rows; r++) {
            for (int c = 0; c < game.cols; c++ ) {
                int owner = game.getTileOwner(r,c);
                int flipCost = game.getTileValue(r,c) + 1;
                if (owner == TileGame.PLAYER1 && flipCost > p2Max) {
                    p2Max = flipCost;
                } else if (owner == TileGame.PLAYER2 && flipCost > p1Max) {
                    p1Max = flipCost;
                }
            }
        }

        int poolLimit = player == TileGame.PLAYER1 ? p1Max : p2Max;

        int moveCount = 0;
        for (Move move : getMoveList(game))
            moveCount++;

        System.out.printf("Evaluating %d moves...\n", moveCount);
        int remaining = moveCount;

        float bestWeight = Float.NEGATIVE_INFINITY;
        Move bestMove = null;

        for (Move move : getMoveList(game)) {
            if (move.cost > poolLimit) {
                System.out.printf("\nSkipping %d moves.\n", remaining);
                break;
            }

            float num = (float) (remaining*remaining);
            float approx = 100.0f - 100.0f * num / (moveCount*moveCount);
            System.out.printf("\r%d / %d moves remaining (approx %.1f%%)...       ", remaining--, moveCount, approx);

            float weight = evaluateMove(game, move, bestWeight);

            if (weight > bestWeight) {
                bestWeight = weight;
                bestMove = move;

            }
        }
        System.out.println();
        return bestMove;
    }

    public float evaluateMove(TileGame game, Move move, float bestWeight) {
        if (player == TileGame.PLAYER1) {
            return alphaBeta(game, move.copyAndApply(game), maxDepth-1, bestWeight, Float.POSITIVE_INFINITY, 1.0f);
        } else {
            return -alphaBeta(game, move.copyAndApply(game), maxDepth-1, Float.NEGATIVE_INFINITY, -bestWeight, 1.0f);
        }
    }

    public float alphaBeta(TileGame startBoard, TileGame state, int depth, float alpha, float beta, float lambda) {
        if (state.isGameForfeitable()) {
            return heuristic.calculate(state);
        } else if (depth <= 0) {
            return heuristic.estimate(startBoard, state);
        }

        lambda *= this.gamma;

        // Compute the maximum meaningful play
        int p1Max = 100;// state.getPlayerPool(TileGame.PLAYER2) + 1;
        int p2Max = 100;//state.getPlayerPool(TileGame.PLAYER1) + 1;
        for (int r = 0; r < state.rows; r++) {
            for (int c = 0; c < state.cols; c++ ) {
                int owner = state.getTileOwner(r,c);
                int flipCost = state.getTileValue(r,c) + 1;
                if (owner == TileGame.PLAYER1 && flipCost > p2Max) {
                    p2Max = flipCost;
                } else if (owner == TileGame.PLAYER2 && flipCost > p1Max) {
                    p1Max = flipCost;
                }
            }
        }

        if (state.currentTurn() == TileGame.PLAYER1) {
            float value = Float.NEGATIVE_INFINITY;

            for (Move move : getMoveList(state)) {
                // Skip moves that aren't meaningful
                if (move.cost > p1Max)
                    break;

                value = Float.max(value, alphaBeta(startBoard, move.copyAndApply(state), depth - 1, alpha, beta, lambda));
                if (value > beta)
                    break;
                alpha = Float.max(alpha, value);
            }
            return lambda * value;

        } else {
            float value = Float.POSITIVE_INFINITY;

            for (Move move : getMoveList(state)) {
                // Skip moves that are equally effective
                if (move.cost > p2Max)
                    break;

                value = Float.min(value, alphaBeta(startBoard, move.copyAndApply(state), depth - 1, alpha, beta, lambda));
                if (value < alpha)
                    break;
                beta = Float.max(beta, value);
            }
            return lambda * value;
        }


    }

    // Heuristic function to use
    @Override
    public float estimate(TileGame startBoard, TileGame game) {
        int p1Pool = game.getPlayerPool(TileGame.PLAYER1);
        int p2Pool = game.getPlayerPool(TileGame.PLAYER2);

        int open = game.countOpenTiles();

        int p1Turns = open / 2 + (game.turn == TileGame.PLAYER1 ? open % 2 : 0);
        int p2Turns = open / 2 + (game.turn == TileGame.PLAYER2 ? open % 2 : 0);

        int p1Bonus = Integer.min(p1Pool, p1Turns);
        int p2Bonus = Integer.min(p2Pool, p2Turns);

        return (game.getScore() - startBoard.getScore()) + (p1Bonus - p2Bonus) * ((float)((open - maxDepth) / maxDepth));
    }

    @Override
    public float calculate(TileGame endBoard) {
        return endBoard.getScore() * 10000.0f;
    }
}
