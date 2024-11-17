package org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.Utils.Optimization.AlphaBeta;

import java.util.*;

public class GraphNode extends AbstractNode {
    public List<GraphAction> parents, children;

    double winner = -1;
    public GraphNode(AbstractBoard state) {
        super(state);
        this.ALPHA_BETA = (state.getPlayer(0) == 1) ?  AlphaBeta.ALPHA : AlphaBeta.BETA;
        this.parents = new LinkedList<>();
        this.children = new LinkedList<>();
    }


    public void addChild(GraphNode child, int[] action){
        GraphAction out_edge = new GraphAction(this, child, action);
        this.children.add(out_edge);
        child.parents.add(out_edge);
    }

    public double simulate(){
        if(this.isTerminal()){
            if(winner == -1){
                winner = state.simulateRandomPlayOut();
                return winner;
            }else {
                return winner;
            }
        }else {
            return state.simulateRandomPlayOut();
        }
    }
    public boolean isRoot(){return this.parents.isEmpty();}
    public boolean isLeaf(){return children.isEmpty();}


}
