package players;

import tilegame.TileGame;

import java.util.Iterator;


public class MoveList implements Iterable<Move> {
    private final TileGame game;
    private final int player;
    public MoveList(TileGame game, int player) {
        this.game = game;
        this.player = player;
    }

    public MoveList(TileGame game) {
        this.game = game;
        this.player = game.turn;
    }

    @Override
    public Iterator<Move> iterator() {
        return new MoveIterator(game, player);
    }
}


class MoveIterator implements Iterator<Move> {
    private final TileGame game;
    private final int player;

    private int row;
    private int col;
    private int value;

    public MoveIterator(TileGame game, int player) {
        this.game = game;
        this.player = player;
        row = 0;
        col = -1;
        value = 1;
        fetchNext();
    }

    private void fetchNext() {
        do {
            if (++col >= game.cols) {
                col = 0;
                if (++row >= game.rows) {
                    row = 0;
                    if (++value > game.getPlayerPool(player)) {
                        return;
                    }
                }
            }
        } while (game.isTileClaimed(row, col));
    }

    @Override
    public boolean hasNext() {
        return game.isPlayValid(row, col, player, value);
    }

    @Override
    public Move next() {
        Move result = new Move(row, col, value);
        fetchNext();
        return result;
    }
}
