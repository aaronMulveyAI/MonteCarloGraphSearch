package org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation;

import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.MonteCarloGraphSearch;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Utils.Logger;

public class UCD1 extends MonteCarloGraphSearch {
    public UCD1(iSearchStopCondition STOP_CONDITION, Logger LOGGER) {
        super("UCD1", STOP_CONDITION, LOGGER);
    }

}
