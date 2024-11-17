package org.example.Player.Agents.Utils.StopCondition;

public interface iSearchStopCondition {
    void initialize();
    boolean isSearchDone();
    default void update(){}
    String getResults(String name);
}
