package org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm.TreeNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm.MonteCarloTreeSearch;
import org.example.Player.Agents.Utils.Optimization.AlphaBeta;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Player.Agents.Utils.Tranpositions.TableEntry;
import org.example.Player.Agents.Utils.Tranpositions.TranspositionTable;

import java.util.List;

public class UCTX_SLOW extends MonteCarloTreeSearch {

    protected TranspositionTable TRANSPOSITIONS_TABLE;
    public UCTX_SLOW(iSearchStopCondition STOP_CONDITION) {
        super("UCTX_SLOW", STOP_CONDITION, null);
        TRANSPOSITIONS_TABLE = null;
    }

    @Override
    public int[] search(AbstractBoard state) {
        if(TRANSPOSITIONS_TABLE == null) TRANSPOSITIONS_TABLE = new TranspositionTable(new GraphNode(state));

        TreeNode root = new TreeNode(state, null);
        TableEntry rootEntry = TRANSPOSITIONS_TABLE.getEntry(root);

        if (rootEntry == null){
            TRANSPOSITIONS_TABLE.put(root);
        }

        STOP_CONDITION.initialize();
        while (!STOP_CONDITION.isSearchDone()){
            TreeNode currentNode = (TreeNode) this.treeTraversal(root);
            double winner = this.playOut(currentNode);
            this.backPropagation(currentNode, winner);
            STOP_CONDITION.update();
        }
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
    public int[] finalPolicy(AbstractNode currentNode) {
        AbstractNode bestChild = ((TreeNode)currentNode).children.get(0);

        for (AbstractNode child : ((TreeNode)currentNode).children) {
            TableEntry childTransposed = TRANSPOSITIONS_TABLE.getEntry(child);
            double Qsa = childTransposed.Rsa / childTransposed.Nsa;

            TableEntry bestTransposed = TRANSPOSITIONS_TABLE.getEntry(bestChild);
            double bestQsa = bestTransposed.Rsa / bestTransposed.Nsa;

            if(bestQsa < Qsa){bestChild = child;}
        }

        return ((TreeNode) bestChild).action;
    }

    @Override
    public double policy(AbstractNode currentAction){

        int Nsa = currentAction.Ns;
        int Ns = ((TreeNode)currentAction).parents.get(0).Ns;

        double[] ALPHAS = calculateAlpha(currentAction);
        double[] DERIVATIVES = calculateRefined(currentAction);

       // double Q_gsa = (ALPHAS[0] + DERIVATIVES[0]) / (ALPHAS[1] + DERIVATIVES[1]);
        double Q_gsa = (DERIVATIVES[0]) / (DERIVATIVES[1]);

        //Q_gsa = realRefinedValueOfTheAction(currentAction, Q_gsa);

        Q_gsa = (ALPHAS[0] + DERIVATIVES[0]) / (ALPHAS[1] + DERIVATIVES[1]);


        if(Math.abs(Q_gsa - currentAction.Qs) > 0.3){
            calculateRefined(currentAction);
        }

        double c = 0.4;
        return Q_gsa + c * Math.sqrt(Math.log(Ns) / Nsa);
    }


    private double[] realAlphaOfTheAction(AbstractNode currentAction, double Rsa, double Nsa){
        List<AbstractNode> nondikNode = ((TreeNode) currentAction).children;
        AlphaBeta nondik;
        if(nondikNode.isEmpty()){
            int playerID = currentAction.state.getPlayer(1);
            if (playerID == 1){
                nondik = AlphaBeta.ALPHA;
            }else {
                nondik = AlphaBeta.BETA;
            }
        }else{
            nondik = nondikNode.get(0).ALPHA_BETA;
        }

        AlphaBeta norakoak = currentAction.ALPHA_BETA;
        double[] result = new double[2];
        if(nondik.equals(norakoak)){
            result[0] = Rsa;
            result[1] = Nsa;
            return result;
        }else {
            result[0] = Nsa - Rsa;
            result[1] = Nsa;
            return result;
        }
    }

    private double[] calculateRefined(AbstractNode nodeToExplore){
        double refinedRsa = 0;
        double refinedNsa = 0;
        List<TableEntry> Gsa = TRANSPOSITIONS_TABLE.getGsa(nodeToExplore);

        for (TableEntry Qgsa : Gsa){
            refinedRsa += Qgsa.Rsa;
            refinedNsa += Qgsa.Nsa;
        }

        return realAlphaOfTheAction(nodeToExplore, refinedRsa, refinedNsa);
    }

    private double[] calculateAlpha(AbstractNode currentAction){

        double childsNsa = 0;
        double childsRsa = 0;

        for (AbstractNode child : ((TreeNode)currentAction).children){
            childsRsa += child.Rs;
            childsNsa += child.Ns;
        }

        double RsAlpha = currentAction.Rs - realAlphaOfTheAction(currentAction, childsRsa, childsNsa)[0];
        double NsAlpha = currentAction.Ns - realAlphaOfTheAction(currentAction, childsRsa, childsNsa)[1];

        return new double[]{RsAlpha, NsAlpha};
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
