package org.example.Player.Agents.Utils.Optimization;

public final class RewardStrategy {
    private static final double WIN = 1;
    private static final double LOSE = 0;
    private static final double TIE = 0.5;

    public static double getReward(AlphaBeta node, double winner){
        double playerID = node.X;
        if(playerID == winner){
            return WIN;
        } else if (0 == winner) {
            return TIE;
        }else {
            return LOSE;
        }
    }
}