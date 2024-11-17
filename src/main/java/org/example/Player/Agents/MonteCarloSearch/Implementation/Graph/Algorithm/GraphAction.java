package org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm;

import org.example.Player.Agents.Utils.Optimization.AlphaBeta;
import org.example.Player.Agents.Utils.Optimization.RewardStrategy;

import java.util.Arrays;
import java.util.Objects;

public class GraphAction {
    private final GraphNode PARENT;
    private final GraphNode CHILD;
    public int[] action;
    public int Nsa;
    public double Rsa, Qsa;

    public GraphAction(GraphNode parent, GraphNode child, int[] action){
        this.PARENT = parent;
        this.CHILD = child;
        this.action = action;
        this.Qsa = 0;
        this.Nsa = 0;
        this.Rsa = 0;
    }

    public void update(double winner){
        final AlphaBeta ALPHA_BETA = PARENT.ALPHA_BETA;
        double reward = RewardStrategy.getReward(ALPHA_BETA, winner);
        this.Rsa += reward;
        this.Nsa++;
        this.Qsa = Rsa / Nsa;
    }

    public int Ns(){
        return PARENT.Ns;
    }
    public GraphNode getParent() {
        return PARENT;
    }
    public GraphNode getChild() {
        return CHILD;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GraphAction that = (GraphAction) obj;
        return Objects.equals(PARENT, that.PARENT) &&
                Objects.equals(CHILD, that.CHILD) &&
                Arrays.equals(action, that.action);
    }

}
