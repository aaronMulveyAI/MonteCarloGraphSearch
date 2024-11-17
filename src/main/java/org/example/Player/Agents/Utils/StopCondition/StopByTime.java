package org.example.Player.Agents.Utils.StopCondition;

public class StopByTime implements iSearchStopCondition{

    private final long MAX_TIME;
    private long startTime;
    private int simulations;
    private final boolean PRINT;
    public StopByTime(long maxTime, boolean print) {
        this.MAX_TIME = maxTime;
        this.PRINT = print;
    }
    @Override
    public void initialize() {
        this.startTime = System.currentTimeMillis();
        this.simulations = 0;
    }
    @Override
    public boolean isSearchDone() {
        long time = System.currentTimeMillis();
        long done = time  - startTime;
        return done >= MAX_TIME;
    }

    @Override
    public void update() {
        this.simulations ++;
    }

    public String getResults(String name){
        long endTime = System.currentTimeMillis();
        if(PRINT) System.out.println(name + " -> Simulations: " + simulations + " Time: " + (endTime - startTime) + "ms");
        String result = name + " -> Simulations: " + simulations + " Time: " + (endTime - startTime) + "ms";
        this.simulations = 0;
        return result;
    }
}
