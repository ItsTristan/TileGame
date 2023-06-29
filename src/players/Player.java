package players;

import tilegame.TileGame;

public abstract class Player {

    public final String name;
    public final int player;

    public Player(int player) {
        this.name = String.format("Player %d", player+1);
        this.player = player;
    }

    public Player(String name, int player) {
        this.name = name;
        this.player = player;
    }

    public abstract Move getNextMove(TileGame game);

    public MoveList getMoveList(TileGame game) {
        return new MoveList(game, game.turn);
    }
}