# Tile Game

**A playable version with a GUI is available here: https://doctorphd.itch.io/tile-game**

Tile Game is an abstract strategy game I invented in 2011. It was originally meant to be a deeper, more interesting version of Tic-Tac-Toe, but evolved into an interesting strategy game in its own right. A standard game takes about 1 minute to play.

This repository is a reference implementation made in Java, including an (unoptimized) alpha-beta search algorithm.

_It's meant to be a "pen-and-paper" game, but it plays a little better on a whiteboard with different coloured markers._

# Rules

* The standard game is played on a 3x3 grid. Players start with a pool of 10 points each.
* Players take turns claiming tiles. To claim a tile, subtract a number of points from your pool, and place this number onto an open space on the grid.
* If you place a number adjacent to your opponent's tile, and your tile's value is strictly larger than their's, their tile is flipped.
* When a tile is flipped, its value decreases by 1 and ownership is transferred.
* If a tile's value decreases to 0, it becomes locked and can no longer be flipped.
* When all cells have been claimed, or when both players are unable to play, the game ends.
* The winner is the player who owns the most tiles.

### Notes
* You must draw at least 1 point and no more than your current pool.
* If your pool is empty on your turn, your turn is skipped.

# Points of Interest

Board sizes are notated as [ROW]x[COL]x[START POOL]. For example, the standard board is 3x3x10. The standard board is interesting and deep enough for the most part, and has interesting properties while remaining small enough to analyze, though I personally think the game is more interesting on a 5x5x26 board.

* For a 3x3xP board, if 2 <= P < 10, player 2 has the advantage; if P > 10, player 1 has the advantage; otherwise, the game ends in a draw.
* For odd boards, there seems to be a pivot point Q where 2 <= pool < Q results in a player 2 advantage; pool > Q results in a player 1 advantage, and pool = Q results in a draw. It's not clear if this is a general pattern or not, but my guess is that Q = (num P1 turns) x 2
* For even boards, player 2 usually has the advantage.

* The 3x3x10 board has an upper bound of 122,638 x 9! / 8 = 5,562,859,680 possible states.
* The 5x5x26 board has an upper bound of 526,787,770,292,436 x 25! / 8 = 1,021,389,469,157,996,212,330,519,487,152,128,000,000 = 1.02x10^39 possible states.
* The 2x2x4 board has an upper bound of 153 states using the same analysis (the true number of states is a little closer  to 147, less some specific symmetries)
* In a 2x2, Player 2 always has a winning strategy if P > 1.
 

#### Perfect Play

Below is an animation showing perfect play for a 3x3x10 board:

![optimal_play](https://github.com/ItsTristan/TileGame/assets/10429871/2b068679-8365-4276-8ece-129b0c1c5405)


#### Counting function

Below is the python code I used to count the number of ways to draw points from the player pools:

```py3
cache = {}

def count(max_turns, p1_pool, p2_pool):
    if max_turns == 0:
        return 1
    if p1_pool == p2_pool == 0:
        return 1

    key = (max_turns, p1_pool, p2_pool)
    if key in cache:
        return cache[key]

    counter = 0
    for p1 in range(1, p1_pool + 1):
        if p2_pool > 0:
            counter += count(max_turns - 1, p2_pool, p1_pool - p1)
        else:
            counter += count(max_turns - 1, p1_pool - p1, 0)
    
    cache[key] = counter
    return counter
```