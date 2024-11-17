package org.example.Player.Agents.Utils.Tranpositions;

import org.example.Player.Agents.Utils.Optimization.AlphaBeta;
import org.example.Player.Agents.Utils.Optimization.RewardStrategy;

import java.util.HashSet;
import java.util.Set;

public class TableEntry {
    final long HASH_VALUE;
    public AlphaBeta ALPHA_BETA;
    public int Nsa;
    public double Rsa;
    public Set<Long> Ns;
    public Set<Long> Gsa;
    private TableEntry next_due_collision;

    public TableEntry(long hashValue, AlphaBeta alphaBeta){
        this.HASH_VALUE = hashValue;
        this.ALPHA_BETA = alphaBeta;
        this.Nsa = 0;
        this.Rsa = 0;
        this.Ns = new HashSet<>();
        this.Gsa = new HashSet<>();
        this.next_due_collision = null;
    }

    public TableEntry getEntry(long searchHashValue){
        TableEntry current = this;
        while (current != null){
            if(current.HASH_VALUE == searchHashValue){
                return current;
            }else {
                current = current.next_due_collision;
            }
        }
        return null;
    }

    public void addNext(long hashValue, AlphaBeta alphaBeta) {
        TableEntry nextEntry = new TableEntry(hashValue, alphaBeta);
        TableEntry current = this;
        while (current.next_due_collision != null){
            current = current.next_due_collision;
        }
        current.next_due_collision = nextEntry;
    }

    public void update(double winner){
        this.Rsa += RewardStrategy.getReward(this.ALPHA_BETA, winner);
        Nsa++;
    }

    public void addChild(long childHash){
        if(!Gsa.contains(childHash)){
            this.Gsa.add(childHash);
        }
    }

    public void addParent(long parentHash){
        if(!Ns.contains(parentHash)){
            this.Ns.add(parentHash);
        }
    }

}
