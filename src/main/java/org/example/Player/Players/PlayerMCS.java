package org.example.Player.Players;

import org.example.Games.Game;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractMonteCarloSearch;

public class PlayerMCS extends AbstractPlayer {

    public AbstractMonteCarloSearch mcts;
    public PlayerMCS(AbstractMonteCarloSearch mcts) {
        super(mcts.NAME);
        this.mcts = mcts;
    }

    @Override
    public int[] makeMove(Game game) {
        return mcts.search(game.BOARD);
    }

    @Override
    public void reset() {
        mcts.reset();
    }
}
