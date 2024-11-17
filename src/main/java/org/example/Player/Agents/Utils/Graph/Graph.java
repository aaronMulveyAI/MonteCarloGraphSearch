package org.example.Player.Agents.Utils.Graph;

import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphNode;

public class Graph {
    protected GraphEntry[] TABLE;
    private static final int SIZE = 1 << 24;

    public Graph() {this.TABLE = new GraphEntry[SIZE];}
    private int getIndex(long hashValue) {
        return Math.abs((int)(hashValue % SIZE));
    }
    public void put(long hashValue, GraphNode node){
        final int HASH_INDEX = this.getIndex(hashValue);
        if(TABLE[HASH_INDEX] == null){
            TABLE[HASH_INDEX] = new GraphEntry(hashValue, node);
        }else {
            GraphEntry nextEntry = TABLE[HASH_INDEX].getEntry(hashValue);
            if (nextEntry == null){
                TABLE[HASH_INDEX].addNext(hashValue, node);
            }
        }
    }

    public GraphEntry get(long hashValue){
        final int HASH_INDEX = this.getIndex(hashValue);
        GraphEntry entry = TABLE[HASH_INDEX];
        return (entry != null) ? entry.getEntry(hashValue) : null;
    }

    public void clear(){
        for (int i = 0; i < SIZE; i++) {
            TABLE[i] = null;
        }
    }
}
