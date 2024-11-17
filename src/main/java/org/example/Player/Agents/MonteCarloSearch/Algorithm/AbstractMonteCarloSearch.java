package org.example.Player.Agents.MonteCarloSearch.Algorithm;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Utils.Logger;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMonteCarloSearch {
    public String NAME;
    public final Logger LOGGER;
    public List<Long> times = new LinkedList<>();
    public double NUMBER_NODES;
    public double NUMBER_TRANSPOSED_NODES;

    protected final iSearchStopCondition STOP_CONDITION;
    public AbstractMonteCarloSearch(String NAME, iSearchStopCondition STOP_CONDITION, Logger LOGGER) {
        this.NAME = NAME;
        this.LOGGER = LOGGER;
        this.STOP_CONDITION = STOP_CONDITION;
        this.NUMBER_TRANSPOSED_NODES = 0;
        this.NUMBER_NODES = 0;
    }

    public void logg(String message){
        if(this.LOGGER != null){
            LOGGER.write(message);
            LOGGER.save();
        }
    }
    public abstract int[] search(AbstractBoard state);
    public abstract AbstractNode treeTraversal(AbstractNode currentNode);
    public abstract AbstractNode argMax(AbstractNode currentNode);
    public abstract double policy(AbstractNode currentNode);
    public abstract int[] finalPolicy(AbstractNode currentNode);
    public abstract AbstractNode expansion(AbstractNode parent);
    public abstract double playOut(AbstractNode leafNode);
    public abstract void backPropagation(AbstractNode leafNode, double winner);
    public abstract void reset();

}
