package org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation;

import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphAction;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.MonteCarloGraphSearch;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Utils.Logger;

public class UCD3 extends MonteCarloGraphSearch {

    public UCD3(iSearchStopCondition STOP_CONDITION, Logger LOGGER) {
        super("UCD3", STOP_CONDITION, LOGGER);
    }

    @Override
    public void backPropagation(AbstractNode leafNode, double winner) {
        GraphNode currentNode = (GraphNode) leafNode;
        this.updateAll(currentNode, winner, PATH.size());
        PATH.clear();
    }

    public void updateAll(GraphNode leafNode, double winner, int depth){
        if (depth == 0){
            for (GraphAction parentAction : leafNode.parents){
                parentAction.getParent().update(winner);
                parentAction.update(winner);
            }
        }else {
            leafNode.update(winner);
            for (GraphAction parentAction : leafNode.parents){
                parentAction.getParent().update(winner);
                parentAction.update(winner);
                updateAll(parentAction.getParent(), winner, depth - 1);
            }
        }
    }
}
