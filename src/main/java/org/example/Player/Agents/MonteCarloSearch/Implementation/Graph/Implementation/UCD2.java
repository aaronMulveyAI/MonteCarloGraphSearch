package org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation;

import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphAction;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.MonteCarloGraphSearch;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Utils.Logger;

public class UCD2 extends MonteCarloGraphSearch {
    public UCD2(iSearchStopCondition STOP_CONDITION, Logger LOGGER) {
        super("UCD2", STOP_CONDITION, LOGGER);
    }

    @Override
    public void backPropagation(AbstractNode leafNode, double winner) {
        GraphNode currentNode = (GraphNode) leafNode;

        while (!PATH.isEmpty()){
            GraphAction lastAction = PATH.pop();
            lastAction.getChild().update(winner);
            for (GraphAction parentAction : currentNode.parents){
                parentAction.update(winner);
            }
            currentNode = lastAction.getParent();
        }
        currentNode.update(winner);
    }
}
