package org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm.TreeNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm.MonteCarloTreeSearch;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Player.Agents.Utils.Tranpositions.Table;
import org.example.Player.Agents.Utils.Tranpositions.TableEntry;
import org.example.Player.Agents.Utils.Tranpositions.TranspositionTable;

import java.util.Stack;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public class UCT3 extends MonteCarloTreeSearch {
    protected TranspositionTable TRANSPOSITIONS_TABLE;
    public static int ROUND;
    public UCT3(iSearchStopCondition STOP_CONDITION) {
        super("UCT3", STOP_CONDITION, null);
        TRANSPOSITIONS_TABLE = null;
        ROUND = 0;
    }

    @Override
    public int[] search(AbstractBoard state) {
        reset();
        if (TRANSPOSITIONS_TABLE == null) TRANSPOSITIONS_TABLE = new TranspositionTable(new GraphNode(state));
        TreeNode root = new TreeNode(state, null);
        TableEntry rootEntry = TRANSPOSITIONS_TABLE.getEntry(root);

        if (rootEntry == null) {
            TRANSPOSITIONS_TABLE.put(root);
        }

        STOP_CONDITION.initialize();
        long backpropagation = 0;
        while (!STOP_CONDITION.isSearchDone()) {
            long start = System.currentTimeMillis();
            PATH.clear();
            TreeNode currentNode = (TreeNode) this.treeTraversal(root);
            double winner = this.playOut(currentNode);
            this.backPropagation(currentNode, winner);
            long end = System.currentTimeMillis();
            backpropagation += end - start;
            STOP_CONDITION.update();
        }

        times.add(backpropagation);
        //System.out.println(backpropagation);
        STOP_CONDITION.getResults(NAME);
        return finalPolicy(root);
    }

    @Override
    public AbstractNode expansion(AbstractNode currentNode) {

        int[] randomAction = currentNode.getRandomMove();
        AbstractBoard state = currentNode.state.clone();
        state.makeMove(randomAction);
        TreeNode nodeToExpand = new TreeNode(state, randomAction);
        TableEntry transposedNode = TRANSPOSITIONS_TABLE.getEntry(nodeToExpand);

        if(transposedNode == null){
            TRANSPOSITIONS_TABLE.put(nodeToExpand);
        }else {
            nodeToExpand.Ns += transposedNode.Nsa;
            nodeToExpand.Rs += transposedNode.Rsa;
            nodeToExpand.Qs = nodeToExpand.Rs / nodeToExpand.Ns;
        }
        TRANSPOSITIONS_TABLE.addChild(currentNode, nodeToExpand);
        ((TreeNode)currentNode).addChild(nodeToExpand);
        return nodeToExpand;
    }

    @Override
    public double policy(AbstractNode currentNode) {
        TableEntry transposedNode = TRANSPOSITIONS_TABLE.get(currentNode);
        int Nsa = transposedNode.Nsa;
        double Rsa = transposedNode.Rsa;
        int Ns = ((TreeNode)currentNode).isRoot() ? 1 : ((TreeNode)currentNode).parents.get(0).Ns;
        double Qsa = Rsa / Nsa;
        double c = 0.4;
        return Qsa + c * sqrt(log(Ns)/ Nsa);
    }


    @Override
    public int[] finalPolicy(AbstractNode currentNode) {
        AbstractNode bestChild = ((TreeNode)currentNode).children.get(0);

        for (AbstractNode child : ((TreeNode)currentNode).children) {
            TableEntry childTransposed = TRANSPOSITIONS_TABLE.getEntry(child);
            TableEntry bestTransposed = TRANSPOSITIONS_TABLE.getEntry(bestChild);
            double Qsa = childTransposed.Rsa / childTransposed.Nsa;
            double bestQsa = bestTransposed.Rsa / bestTransposed.Nsa;
            if(bestQsa < Qsa){bestChild = child;}
        }

        return ((TreeNode) bestChild).action;
    }

    @Override
    public void backPropagation(AbstractNode leafNode, double winner) {
        AbstractNode currentNode = leafNode;
        while (!((TreeNode)currentNode).isRoot()){
            backpropagate(currentNode, winner);
            currentNode = ((TreeNode)currentNode).parents.get(0);
        }
        backpropagate(currentNode, winner);
    }

    public void backpropagate(AbstractNode node, double winner) {

        if(((TreeNode)node).isLeaf()) {
            TRANSPOSITIONS_TABLE.updateAll(node, winner, PATH.size());
        }
        node.update(winner);
    }
    @Override
    public void reset() {
        this.TRANSPOSITIONS_TABLE = null;
    }
}
