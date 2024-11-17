package org.example.Player.Players;

import org.example.Games.Game;
import org.example.Utils.RandomGenerator;

import java.util.LinkedList;

public class PlayerRandom extends AbstractPlayer {
    public PlayerRandom() {
        super("Random");
    }

    @Override
    public int[] makeMove(Game game) {
        LinkedList<int[]> emptyCells = game.BOARD.getAllMoves();
        return emptyCells.get(RandomGenerator.getRandomInt(emptyCells.size()));
    }

    @Override
    public void reset() {

    }
}
