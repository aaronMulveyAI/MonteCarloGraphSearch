package org.example.Lab;

import org.example.Games.Boards.AbstractBoard;
import org.example.Games.Boards.BoardComplex;
import org.example.Games.Game;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD3;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.UCT0;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.UCT3;
import org.example.Player.Agents.Utils.StopCondition.StopBySimulations;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Player.Players.AbstractPlayer;
import org.example.Player.Players.PlayerMCS;
import org.example.Player.Players.PlayerRandom;
import org.example.Utils.Logger;

import java.util.LinkedList;
import java.util.List;

import static org.example.Utils.StatisticsUtils.removeOutliersAndCalculateStatistics;

public class E3_Backpropagation {

    static Logger logger = new Logger("/Users/aaronmulvey/Documents/MonteCarloGraphSearch/MonteCarloGraphSearch/src/main/java/org/example/Lab/Logs/E3.log");
    public static void main(String[] args) {
        run(1);
        run(2);
        run(3);

    }


    public static void run(int bot){

        AbstractBoard board = new BoardComplex(5);

        List<double[]> todo = new LinkedList<>();

        for (int i = 23; i <= 30; i++) {

            iSearchStopCondition stop = new StopBySimulations(i * 2000, false);

            AbstractPlayer[] allPlayers = new AbstractPlayer[]{
                    new PlayerRandom(),
                    new PlayerMCS(new UCT0(stop)),
                    new PlayerMCS(new UCT3(stop)),
                    new PlayerMCS(new UCD3(stop, null)),


            };

            AbstractPlayer[] players =  new AbstractPlayer[]{
                    allPlayers[0],
                    allPlayers[bot],
            };
            Game game = new Game(board.clone(), players);
            game.runGame();

            List<Long> times = ((PlayerMCS) players[1]).mcts.times;
            todo.add(removeOutliersAndCalculateStatistics(times));

            System.out.print("\r SIMULATION : " + i);

        }

        String mean = "";
        mean += "c(";
        System.out.print("c(");
        for (double[] data : todo){
            System.out.print(data[0] + ",");
            mean += data[0] + ",";
        }

        mean += ")\n";
        System.out.print(")\n");

        String sd = "";
        sd += "c(";
        System.out.print("c(");
        for (double[] data : todo){
            System.out.print(data[1] + ",");
            sd += data[1] + ",";
        }

        sd += ")\n";

        logger.log(mean);
        //logger.log(sd);
    }
}
