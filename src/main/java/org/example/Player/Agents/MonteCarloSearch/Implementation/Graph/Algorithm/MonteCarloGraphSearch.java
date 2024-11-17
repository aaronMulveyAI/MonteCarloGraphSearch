package org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractMonteCarloSearch;
import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.Utils.Graph.Graph;
import org.example.Player.Agents.Utils.Graph.GraphEntry;
import org.example.Player.Agents.Utils.Hashing.ZobristHashing;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Utils.Logger;

import java.util.HashMap;
import java.util.Stack;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public class MonteCarloGraphSearch extends AbstractMonteCarloSearch {

    protected HashMap<Long, GraphNode> GRAPH;
    public final Stack<GraphAction> PATH;
    public static ZobristHashing ZOBRIST;
    public int DEPTH;

    public MonteCarloGraphSearch(String name, iSearchStopCondition STOP_CONDITION, Logger LOGGER) {
        super(name, STOP_CONDITION, LOGGER);
        this.GRAPH = new HashMap<>();
        this.PATH = new Stack<>();
        ZOBRIST = null;
        DEPTH = 0;
    }


    @Override
    public int[] search(AbstractBoard state) {
        if(ZOBRIST == null) ZOBRIST = new ZobristHashing(new GraphNode(state)); // If I do not have Hash f(x), just create one
        GraphNode root = new GraphNode(state); // Create the first node that will be the root
        long rootHash = ZOBRIST.getHash(root);
        reset();
        root = GRAPH.getOrDefault(rootHash, root);
        GRAPH.putIfAbsent(rootHash, root);

        STOP_CONDITION.initialize(); // Inicialice the stop condition

        long backpropagation = 0;
        while (!STOP_CONDITION.isSearchDone()){ // Check if I should stop the search
            long start = System.currentTimeMillis();
            PATH.clear();
            AbstractNode currentNode = treeTraversal(root); // Traverse the graph starting from root return a promising node
            double winner = playOut(currentNode); // Make a random game simulation and give me the winner
            backPropagation(currentNode, winner); // Back-propagate the result till root is reached
            long end = System.currentTimeMillis();
            backpropagation += end - start;
            STOP_CONDITION.update(); // Update the stopping condition
        }

        times.add(backpropagation);
        //System.out.println(backpropagation);
        //System.out.println(this.NUMBER_TRANSPOSED_NODES/ this.NUMBER_NODES);

        STOP_CONDITION.getResults(NAME);
        return finalPolicy(root);
    }

    @Override
    public AbstractNode treeTraversal(AbstractNode currentNode) {
        DEPTH = 0;
        do{
            if (!currentNode.isFullyExpanded()){
                if(!currentNode.isTerminal()){
                    currentNode = expansion(currentNode);
                    int lastActionIndex = ((GraphNode) currentNode).parents.size() - 1; // I calculate the last action index added
                    GraphAction lastAction = ((GraphNode) currentNode).parents.get(lastActionIndex);
                    PATH.add(lastAction); // Now, I can add the last action to the search path
                }
                break;
            }else {
                DEPTH++;
                currentNode = this.argMax(currentNode);
                if (((GraphNode) currentNode).isLeaf() && !currentNode.isTerminal()) {
                    currentNode = expansion(currentNode);
                    int lastActionIndex = ((GraphNode) currentNode).parents.size() - 1; // I calculate the last action index added
                    GraphAction lastAction = ((GraphNode) currentNode).parents.get(lastActionIndex);
                    PATH.add(lastAction); // Now, I can add the last action to the search path
                    return currentNode;
                }
            }
        } while (!((GraphNode) currentNode).isLeaf());

        return currentNode;
    }

    @Override
    public AbstractNode argMax(AbstractNode currentNode) {
        GraphAction bestAction = ((GraphNode) currentNode).children.get(0); // I know from the traversal that the node it has at least 1 action

        for (GraphAction action : ((GraphNode) currentNode).children){ // Iterate for all the actions
            double action_UCT = policy(action); // Calculate the UCT value of the i-action
            double best_UCT = policy(bestAction); // Calculate UCT of the max-action
            if (best_UCT < action_UCT) bestAction = action; // Update if the action-uct is better than current best-uct
        }
        PATH.add(bestAction); // I add the best action to the path, LIFO structure for the back-propagation
        return bestAction.getChild(); // Return the resulting node of playing the best-action in the previous node
    }

    public double policy(GraphAction currentAction) {
        double Rsa = currentAction.Rsa;
        int Nsa = currentAction.Nsa;
        int Ns = currentAction.getParent().Ns;
        double c = 0.4;
        return Rsa / Nsa + c * sqrt(log(Ns) / Nsa); // The basic UCT formula
    }

    @Override
    public double policy(AbstractNode currentNode) {return 0;}

    @Override
    public int[] finalPolicy(AbstractNode currentNode) {
        if(((GraphNode) currentNode).children.isEmpty()) return ((GraphNode) currentNode).parents.get(0).action;
        GraphAction bestAction = ((GraphNode) currentNode).children.get(0); // I know from the traversal that the node it has at least 1 action

        for (GraphAction action : ((GraphNode) currentNode).children){ // Iterate for all the actions
            double action_UCT = action.Rsa / action.Nsa; // Calculate the UCT value of the i-action
            double best_UCT = bestAction.Rsa / bestAction.Nsa;
            if (best_UCT < action_UCT) bestAction = action; // Update if the action-uct is better than current best-uct
        }
        PATH.add(bestAction); // I add the best action to the path, LIFO structure for the back-propagation
        return bestAction.action; // Return the resulting node of playing the best-action in the previous node
    }

    @Override
    public AbstractNode expansion(AbstractNode parent) {
        AbstractBoard currentBoard = parent.state.clone(); // I get parents board
        int[] randomMove = parent.getRandomMove(); // I get a random move posible in that board
        currentBoard.makeMove(randomMove); // I make the move modifying the board
        GraphNode randomState = new GraphNode(currentBoard); // I create a new State with this modified board
        long stateHash = ZOBRIST.getHash(randomState); // I calculate the hash of that new State
        GraphNode childNodeEntry = GRAPH.get(stateHash); // I check if the state already exist
        GraphNode childNode = childNodeEntry;
        if(childNodeEntry == null){ // If the new State does not exist --> I add the node to the graph
            childNode = new GraphNode(currentBoard); // First, I create the node
            GRAPH.put(stateHash, childNode); // Then, I add the node to the graph
            NUMBER_NODES++;
        }else {
            NUMBER_TRANSPOSED_NODES++;
            NUMBER_NODES++;
        }

        ((GraphNode) parent).addChild(childNode, randomMove); // I add the node to the DAG (It will create an edge)

        return childNode; // I return the child node because I need to continue with the traversal.
    }

    @Override
    public double playOut(AbstractNode leafNode) {
        return leafNode.simulate();
    }

    @Override
    public void backPropagation(AbstractNode leafNode, double winner) {
        GraphNode currentNode = (GraphNode) leafNode;
        while (!PATH.isEmpty()){
            GraphAction lastAction = PATH.pop();
            lastAction.getChild().update(winner);
            lastAction.update(winner);
            currentNode = lastAction.getParent();
        }
        currentNode.update(winner);
    }

    @Override
    public void reset() {
        //this.NUMBER_NODES = 0;
        //this.NUMBER_TRANSPOSED_NODES = 0;
        this.GRAPH = new HashMap<>();
    }
}
