package org.example.Player.Agents.MonteCarloSearch.Algorithm;

import org.example.Games.Boards.AbstractBoard;
import org.example.Player.Agents.Utils.Optimization.AlphaBeta;
import org.example.Player.Agents.Utils.Optimization.RewardStrategy;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractNode {

    public AbstractBoard state;
    public AlphaBeta ALPHA_BETA;
    public LinkedList<int[]> movesLeft;
    public int Ns;
    public double Rs;
    public double Qs;

    public double winner = -1;

    public AbstractNode(AbstractBoard state) {
        this.state = state;
        this.movesLeft = state.getAllMoves();
        this.Ns = 0;
        this.Qs = 0;
        this.Rs = 0;
    }

    public int[] getRandomMove(){
        return movesLeft.pop();
    }

    public void update(double winner){
        this.Rs += RewardStrategy.getReward(ALPHA_BETA, winner);
        this.Ns++;
        this.Qs = Rs / Ns;
    }

    public double simulate(){
        if(this.isTerminal()){
            if(winner == -1){
                winner = state.simulateRandomPlayOut();
                return winner;
            }else {
                return winner;
            }
        }else {
            return state.simulateRandomPlayOut();
        }
    }
    public boolean isTerminal(){
        return this.state.isGameOver();
    }
    public boolean isFullyExpanded(){return this.movesLeft.isEmpty();}
}
