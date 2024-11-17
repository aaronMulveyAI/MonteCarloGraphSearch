package org.example.Player.Agents.Utils.Tranpositions;

import org.example.Player.Agents.Utils.Optimization.AlphaBeta;

import java.util.Set;

public class Table {
    protected final TableEntry[] TABLE;

    private static final int SIZE = 1 << 24;

    public Table() {
        this.TABLE = new TableEntry[SIZE];
    }

    private int getIndex(long hashValue) {
        return Math.abs((int)(hashValue % SIZE));
    }

    public void put(long hashValue, AlphaBeta alphaBeta){
        final int HASH_INDEX = this.getIndex(hashValue);
        if(TABLE[HASH_INDEX] == null){
            TABLE[HASH_INDEX] = new TableEntry(hashValue, alphaBeta);
        }else {
            TableEntry nextEntry = TABLE[HASH_INDEX].getEntry(hashValue);
            if (nextEntry == null){
                TABLE[HASH_INDEX].addNext(hashValue, alphaBeta);
            }
        }
    }

    public TableEntry getEntry(long hashValue){
        final int HASH_INDEX = this.getIndex(hashValue);
        TableEntry entry = TABLE[HASH_INDEX];
        return (entry != null) ? entry.getEntry(hashValue) : null;
    }

    public void update(long hashValue, double winner){
        TableEntry entry = this.getEntry(hashValue);
        if(entry != null) entry.update(winner);
    }

    public void updateAll(long hashValue, double winner, int depth){
        TableEntry child = this.getEntry(hashValue);
        Set<Long> Ns = child.Ns;
        if(depth == 0 || Ns.isEmpty()){
            this.update(hashValue, winner);
            for (Long parent : Ns){
                this.update(parent, winner);
            }
        }else {
            this.update(hashValue, winner);
            for (Long parent : Ns){
                this.updateAll(parent, winner, depth - 1);
            }
        }


    }
}
