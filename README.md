# Tile Game
Tile Game is a pen-and-paper game I invented in 2011. It was created to be a deeper, more interesting version Tic-Tac-Toe, but evolved into an interesting strategy game in its own right. A standard game takes about 1 minute to play.

This repository is a reference implementation made in Java, including an (unoptimized) alpha-beta search algorithm.

A playable version with a GUI is available here: https://doctorphd.itch.io/tile-game

Board sizes are notated as [ROW]x[COL]x[START POOL]. For example, the standard board is 3x3x10. THe standard board is interesting and deep enough for the most part, although I personally think the game is most interesting on a 5x5x26 board.

# Rules
* The standard game is played on a 3x3 grid. Players start with a pool of 10 points each.
* Players take turns putting numbers into the grid. On your turn, you may play a number between 1 and your current pool (inclusive), and subtract that amount from your pool.
* When you place a number on the grid, you claim the tile.
* If you place a number horizontally or vertically adjacent to your opponent's tile, and your tile's number is strictly larger than your opponent's tile's value, their tile is flipped.
* When a tile is flipped, ownership swaps, and its value decreases by 1.
* If a tile's value decreases to 0, it becomes locked, and can no longer be flipped.
* If your pool is empty on your turn, your turn is skipped.
* When all cells have been claimed, or when both players are unable to play, the game ends.
* The winner is the player who owns the most tiles.

# Points of Interest

* The 3x3x10 board has an upper board of 122,638 x 9! / 8 = 5,562,859,680 possible states.
* The 3x3 board is [strongly solved](https://en.wikipedia.org/wiki/Solved_game).
* For a 3x3xP board, if 2 <= P < 10, player 2 has the advantage; if P > 10, player 1 has the advantage; otherwise, the game ends in a draw.

* The 5x5x26 board has an upper bound of 526,787,770,292,436 x 25! / 8 = 1,021,389,469,157,996,212,330,519,487,152,128,000,000 = 1.02x10^39 possible states.
* The 2x2x4 board has an upper bound of 153 states using the same analysis (the true number of states is 147)
* There seems to be a pivot point Q where 2 <= pool < Q results in a player 2 advantage; pool > Q results in a player 1 advantage, and pool = Q results in a draw. It's not clear if this is a general pattern or not.
* In a 2x2, Player 2 always has a winning strategy:
    - If Player 1 plays a 1, play a 2 in the opposite corner.
    - If Player 1 plays a value in [2, P-1], play a 1 adjacent to it.
    - If Player 1 plays P, play 1 in every cell.
