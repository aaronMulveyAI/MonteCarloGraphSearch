package org.example.Lab;

import org.example.Games.Boards.*;
import org.example.Games.Game;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.*;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.*;
import org.example.Player.Agents.Utils.StopCondition.*;
import org.example.Player.Players.*;
import org.example.Utils.Logger;

public class Tournament {
    static Logger logger = new Logger("/Users/aaronmulvey/Documents/MonteCarloGraphSearch/MonteCarloGraphSearch/src/main/java/org/example/Lab/Logs/All_vs_All.log");
    public static void main(String[] args) {

        int boardSize = 5;
        int simulations = 10000;
        int experimentSize = 50;

        logger.log(" ");
        logger.log(" ");
        logger.log(" ");
        logger.log("Experiment start Omega board size = " + boardSize);
        logger.log("Stop by simulations n = " + simulations);
        logger.log("Experiment size = " + experimentSize);

        AbstractBoard board = new BoardComplex(boardSize);

        iSearchStopCondition stop = new StopBySimulations(simulations, false);

        AbstractPlayer[] Players = new AbstractPlayer[]{
                new PlayerMCS(new UCT0(stop)),
                new PlayerMCS(new UCT1(stop)),
                new PlayerMCS(new UCT2(stop)),
                new PlayerMCS(new UCT3(stop)),
        };

        all_vs_all(Players, board, experimentSize);
    }
    public static void all_vs_all(AbstractPlayer[] players, AbstractBoard board, int simulations){

        for (AbstractPlayer p1 : players) {
            for (AbstractPlayer p2 : players) {
                if(p1.getNAME().equals(p2.getNAME())) continue;
                fairPlay(p1, p2, board, simulations);
            }
        }
    }

    public static void fairPlay(AbstractPlayer P1, AbstractPlayer P2, AbstractBoard board,  int n_games){

        double P1_results = 0;
        double P2_results = 0;

        String bar = "----------------------------------";
        String slider = String.format("|  %-7s |  %-7s |   %-6s |", P1.getNAME(), P2.getNAME(), "Ties");
        System.out.println(bar);
        System.out.printf(slider + "\n");
        logger.log(bar);
        logger.log(slider);
        String results = "";

        for (int n = 1; n <= n_games; n++) {

            double winners = one_vs_one(P1, P2, board.clone());

            switch ((int) winners){
                case 1 ->{
                    P1_results += 1;
                    P2_results += 0;
                }
                case 2 -> {
                    P1_results += 0;
                    P2_results += 1;
                }
            }

            double P1_mean = P1_results / n;
            double P2_mean = P2_results / n;
            double ties = 1.0 - (P1_mean + P2_mean);

            results = String.format("| %-8.5f | %-8.5f | %-8.5f | -> %d", P1_mean, P2_mean, ties, n);
            System.out.print("\r" + results);
        }
        logger.log(results);
        System.out.println("\n" + bar);
        logger.log(bar);
    }



    public static double one_vs_one(AbstractPlayer p1, AbstractPlayer p2, AbstractBoard board){
        AbstractPlayer[] players = new AbstractPlayer[]{p1, p2};
        Game game = new Game(board.clone(), players);
        return game.runGame();
    }
}
