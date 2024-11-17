package org.example.Player.Agents.Utils.Tranpositions;

import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Algorithm.TreeNode;
import org.example.Player.Agents.Utils.Hashing.ZobristHashing;
import org.example.Player.Agents.Utils.Optimization.AlphaBeta;

import java.util.LinkedList;
import java.util.List;

public class TranspositionTable {

    private final ZobristHashing ZOBRIST;
    private final Table TABLE;
    public int SIZE;
    public TranspositionTable(AbstractNode state){
        this.ZOBRIST = new ZobristHashing(state);
        this.TABLE = new Table();
        this.SIZE = 0;
    }

    public void put(AbstractNode node){
        SIZE++;
        AlphaBeta nodeAlphaBeta = node.ALPHA_BETA;
        long transposedHashValue = this.ZOBRIST.getHash(node);
        this.TABLE.put(transposedHashValue, nodeAlphaBeta);
    }

    public TableEntry get(AbstractNode node){
        long transposedHashValue;
        if(((TreeNode) node).hash == 0){
            transposedHashValue = this.ZOBRIST.getHash(node);
            ((TreeNode) node).hash = transposedHashValue;
        }else{
            transposedHashValue = ((TreeNode) node).hash;
        }
        return this.TABLE.getEntry(transposedHashValue);
    }
    public TableEntry getEntry(AbstractNode node){
        long transposedHashValue;
        if(((TreeNode) node).hash == 0){
            transposedHashValue = this.ZOBRIST.getHash(node);
            ((TreeNode) node).hash = transposedHashValue;
        }else{
            transposedHashValue = this.ZOBRIST.getHash(node);
           // transposedHashValue = ((TreeNode) node).hash;
        }
        return this.TABLE.getEntry(transposedHashValue);
    }

    public List<TableEntry> getGsa(AbstractNode node){
        long transposedHashValue = this.ZOBRIST.getHash(node);
        TableEntry Qsa = this.TABLE.getEntry(transposedHashValue);
        List<TableEntry> Gsa = new LinkedList<>();

        for(long G_sa_i : Qsa.Gsa){
            Gsa.add(this.TABLE.getEntry(G_sa_i));
        }

        return Gsa;
    }

    public void update(AbstractNode node, double winner){
        long transposedHashValue = this.ZOBRIST.getHash(node);
        this.TABLE.update(transposedHashValue, winner);
    }

    public void updateAll(AbstractNode node, double winner, int depth){
        long transposedHashValue = this.ZOBRIST.getHash(node);
        this.TABLE.updateAll(transposedHashValue, winner, depth);
    }

    public void addChild(AbstractNode parent, AbstractNode child){
        TableEntry parentEntry = this.getEntry(parent);
        TableEntry childEntry = this.getEntry(child);
        long parentHashValue = this.ZOBRIST.getHash(parent);
        long childHashValue = this.ZOBRIST.getHash(child);
        parentEntry.addChild(childHashValue);
        childEntry.addParent(parentHashValue);
    }
}
