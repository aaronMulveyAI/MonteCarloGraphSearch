package org.example.Player.Agents.Utils.StopCondition;

public class StopBySimulations implements iSearchStopCondition {
    public final long MAX_SIMULATIONS;
    public long simulations;
    public long startTime;
    public final boolean PRINT;
    public StopBySimulations(long maxSimulations, boolean print) {
        this.MAX_SIMULATIONS = maxSimulations;
        simulations = 0;
        this.PRINT = print;
    }
    @Override
    public void initialize() {
        startTime = System.currentTimeMillis();
        simulations = 0;
    }
    @Override
    public boolean isSearchDone() {
        return simulations >= MAX_SIMULATIONS;
    }
    @Override
    public void update() {
        this.simulations ++;
    }

    public String getResults(String name){
        long endTime = System.currentTimeMillis();
        if(PRINT) System.out.println(name + " -> Simulations: " + simulations + " Time: " + (endTime - startTime) + "ms");
        return name + " -> Simulations: " + simulations + " Time: " + (endTime - startTime) + "ms";
    }
}

