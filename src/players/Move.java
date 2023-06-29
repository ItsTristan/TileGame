package players;

import tilegame.TileGame;

public class Move {
    final int row;
    final int col;
    final int cost;

    public Move(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.cost = value;
    }

    public boolean apply(TileGame game) {
        return game.play(row, col, cost);
    }

    public TileGame copyAndApply(TileGame game) {
        return game.copyAndPlay(row, col, cost);
    }

    @Override
    public String toString() {
        return String.format("[%d,%d,%d]", row, col, cost);
    }
}

class WeightedMove extends Move implements Comparable<WeightedMove> {
    float weight;

    public WeightedMove(Move move, float weight) {
        super(move.row, move.col, move.cost);
        this.weight = weight;
    }

    public WeightedMove(int row, int col, int value, float weight) {
        super(row, col, value);
        this.weight = weight;
    }

    @Override
    public int compareTo(WeightedMove other) {
        return Float.compare(weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("[%d,%d,%d - %.03f]", row, col, cost, weight);
    }
}