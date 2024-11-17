package org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm.MonteCarloTreeSearch;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;

public class UCT0 extends MonteCarloTreeSearch {
    public UCT0(iSearchStopCondition STOP_CONDITION) {
        super("UCT0", STOP_CONDITION, null);
    }
    @Override
    public int[] search(AbstractBoard state) {
        return super.search(state);
    }
}
