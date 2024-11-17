package org.example.Player.Agents.Utils.Graph;

import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Algorithm.GraphNode;

public class GraphEntry {

    public long hashValue;

    public GraphNode node;
    public GraphEntry next;

    public GraphEntry(long hashValue, GraphNode node){
        this.hashValue = hashValue;
        this.node = node;
    }

    public GraphEntry getEntry(long searchHashValue){
        GraphEntry current = this;
        while (current != null){
            if(current.hashValue == searchHashValue){
                return current;
            }else {
                current = current.next;
            }
        }
        return null;
    }

    public void addNext(long hashValue, GraphNode node) {
        GraphEntry nextEntry = new GraphEntry(hashValue, node);
        GraphEntry current = this;
        while (current.next != null){
            current = current.next;
        }
        current.next = nextEntry;
    }
}
