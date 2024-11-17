package org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm.TreeNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm.MonteCarloTreeSearch;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Player.Agents.Utils.Tranpositions.TableEntry;
import org.example.Player.Agents.Utils.Tranpositions.TranspositionTable;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public class UCT1 extends MonteCarloTreeSearch {

    protected TranspositionTable TRANSPOSITIONS_TABLE;
    public UCT1(iSearchStopCondition STOP_CONDITION) {
        super("UCT1", STOP_CONDITION, null);
        this.TRANSPOSITIONS_TABLE = null;
    }

    @Override
    public int[] search(AbstractBoard state) {
        reset();
        if(TRANSPOSITIONS_TABLE == null) TRANSPOSITIONS_TABLE = new TranspositionTable(new GraphNode(state));
        return super.search(state);
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

        ((TreeNode)currentNode).addChild(nodeToExpand);
        return nodeToExpand;
    }


    public void backpropagate(AbstractNode node, double winner) {
        node.update(winner);
        TRANSPOSITIONS_TABLE.update(node, winner);
    }
    @Override
    public void reset() {
        this.TRANSPOSITIONS_TABLE = null;
    }
}
