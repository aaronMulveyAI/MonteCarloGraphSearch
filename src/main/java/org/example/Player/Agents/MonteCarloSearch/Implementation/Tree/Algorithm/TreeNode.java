package org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.Utils.Optimization.AlphaBeta;

import java.util.LinkedList;
import java.util.List;

public class TreeNode extends AbstractNode {

    public long hash;
    public int[] action;
    public final List<AbstractNode> parents;
    public final List<AbstractNode> children;
    public TreeNode(AbstractBoard state, int[] action) {
        super(state);
        this.action = action;
        final int PLAYER = action == null ? state.getPlayer(0) : state.getPlayer(-1);
        this.ALPHA_BETA = (PLAYER == 1) ?  AlphaBeta.ALPHA : AlphaBeta.BETA;
        this.parents = new LinkedList<>();
        this.children = new LinkedList<>();
        this.hash = 0;
    }

    public void addChild(AbstractNode node){
        this.children.add(node);
        ((TreeNode)node).parents.add(this);
    }
    public boolean isRoot(){return this.parents.isEmpty();}
    public boolean isLeaf(){return children.isEmpty();}
}
