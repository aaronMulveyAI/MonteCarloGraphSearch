package org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractMonteCarloSearch;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Utils.Logger;

import java.util.Stack;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public class MonteCarloTreeSearch extends AbstractMonteCarloSearch {
    public final Stack<AbstractNode> PATH;
    public MonteCarloTreeSearch(String NAME, iSearchStopCondition STOP_CONDITION, Logger LOGGER) {
        super(NAME, STOP_CONDITION, LOGGER);
        PATH = new Stack<>();
    }

    @Override
    public int[] search(AbstractBoard state) {

        TreeNode root = new TreeNode(state, null);
        STOP_CONDITION.initialize();

        long backpropagation = 0;
        while (!STOP_CONDITION.isSearchDone()){
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
       // System.out.println(backpropagation);
        STOP_CONDITION.getResults(NAME);
        return finalPolicy(root);
    }

    @Override
    public AbstractNode treeTraversal(AbstractNode currentNode) {
        PATH.add(currentNode);
        do{
            if (!currentNode.isFullyExpanded()){
                if(!currentNode.isTerminal()){
                    currentNode = expansion(currentNode);
                    PATH.add(currentNode);
                    return currentNode;
                }
                break;
            }else {
                currentNode = this.argMax(currentNode);
                if (((TreeNode)currentNode).isLeaf() && !currentNode.isTerminal()) {
                    currentNode = expansion(currentNode);
                    PATH.add(currentNode);
                    return currentNode;
                }
            }
        } while (!((TreeNode)currentNode).isLeaf());

        return currentNode;
    }

    @Override
    public AbstractNode argMax(AbstractNode currentNode) {
        AbstractNode bestChild = ((TreeNode)currentNode).children.get(0);

        for (AbstractNode child : ((TreeNode)currentNode).children) {
            double Qsa = policy(child);
            double bestQsa = policy(bestChild);
            if(bestQsa < Qsa){bestChild = child;}
        }
        return bestChild;
    }

    @Override
    public double policy(AbstractNode currentNode) {
        double Qsa = currentNode.Qs;
        int Nsa = currentNode.Ns;
        int Ns = ((TreeNode) currentNode).parents.get(0).Ns;
        double c = 0.4;
        return Qsa + c * sqrt(log(Ns) / Nsa);
    }

    @Override
    public int[] finalPolicy(AbstractNode currentNode) {
        AbstractNode bestChild = ((TreeNode)currentNode).children.get(0);

        for (AbstractNode child : ((TreeNode)currentNode).children) {
            double Qsa = child.Qs;
            double bestQsa = bestChild.Qs;
            if(bestQsa < Qsa){bestChild = child;}
        }

        return ((TreeNode) bestChild).action;
    }
    @Override
    public AbstractNode expansion(AbstractNode parent) {
        int[] randomAction = parent.getRandomMove();
        AbstractBoard state = parent.state.clone();
        state.makeMove(randomAction);
        TreeNode nodeToExpand = new TreeNode(state, randomAction);
        ((TreeNode) parent).addChild(nodeToExpand);
        return nodeToExpand;
    }
    @Override
    public double playOut(AbstractNode leafNode) {
        return leafNode.simulate();
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
        node.update(winner);
    }
    @Override
    public void reset() {}
}
