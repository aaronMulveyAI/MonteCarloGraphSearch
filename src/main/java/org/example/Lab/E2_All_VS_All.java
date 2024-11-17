package org.example.Lab;

import org.example.Games.Boards.*;

import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD1;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD2;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD3;

import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.*;
import org.example.Player.Agents.Utils.StopCondition.*;
import org.example.Player.Players.*;
import org.example.Utils.Logger;

import static org.example.Lab.Tournament.*;

public class E2_All_VS_All {

    static Logger logger = new Logger("/Users/aaronmulvey/Documents/MonteCarloGraphSearch/MonteCarloGraphSearch/src/main/java/org/example/Lab/Logs/E2_All_VS_All.log");

    public static void main(String[] args) {

        int boardSize = 5;
        int simulations = 500;
        int experimentSize = 50;

        logger.log("----------------------------------------------------------------------------");
        logger.log("----------------------------------------------------------------------------");
        logger.log("----------------------------------------------------------------------------");
        logger.log("----------------------------------------------------------------------------");
        logger.log("----------------------------------------------------------------------------");
        logger.log("----------------------------------------------------------------------------");
        logger.log("----------------------------------------------------------------------------");
        logger.log(" ");
        logger.log(" ");
        logger.log("Experiment start Omega board size = " + boardSize);
        logger.log("Stop by SIMULATIONS n = " + simulations);
        logger.log("Experiment size = " + experimentSize * 2);

        AbstractBoard board = new BoardComplex(boardSize);

        iSearchStopCondition stop = new StopByTime(simulations, false);
        AbstractPlayer[] Players = new AbstractPlayer[]{
                //new PlayerMCS(new UCT0(stop)),
                //new PlayerMCS(new UCT1(stop)),
                new PlayerMCS(new UCT2(stop)),
                new PlayerMCS(new UCT3(stop)),
                //new PlayerMCS(new UCD1(stop, null)),
                //new PlayerMCS(new UCD2(stop, null)),
                new PlayerMCS(new UCD3(stop, null))
        };

        all_vs_all(Players, board, experimentSize);

    }

    public static void all_vs_all(AbstractPlayer[] players, AbstractBoard board, int simulations) {

        for (int i = 0; i < players.length; i++) {
            for (int j = i; j < players.length; j++) {
                AbstractPlayer p1 = players[i];
                AbstractPlayer p2 = players[j];
                if (p1.getNAME().equals(p2.getNAME())) continue;
                fairPlay(p1, p2, board, simulations);
            }
        }
    }


    public static void fairPlay(AbstractPlayer P1, AbstractPlayer P2, AbstractBoard board, int n_games) {

        double P1_results = 0;
        double P2_results = 0;

        String bar = "----------------------------------";
        String slider = String.format("|  %-7s |  %-7s |   %-6s |", P1.getNAME(), P2.getNAME(), "Ties");
        System.out.println(bar);
        System.out.printf(slider + "\n");
        logger.log(bar);
        logger.log(slider);
        String results = "";

        int counter = 0;

        for (int n = 1; n <= n_games; n++) {

            double winners = one_vs_one(P1, P2, board.clone());
            counter++;

            switch ((int) winners) {
                case 1 -> {
                    P1_results += 1;
                    P2_results += 0;
                }
                case 2 -> {
                    P1_results += 0;
                    P2_results += 1;
                }
            }

            double P1_mean = P1_results / counter;
            double P2_mean = P2_results / counter;
            double ties = 1.0 - (P1_mean + P2_mean);

            results = String.format("| %-8.5f | %-8.5f | %-8.5f | -> %d", P1_mean, P2_mean, ties, counter);
            System.out.print("\r" + results);


            winners = one_vs_one(P2, P1, board.clone());
            counter++;

            switch ((int) winners) {
                case 1 -> {
                    P1_results += 0;
                    P2_results += 1;
                }
                case 2 -> {
                    P1_results += 1;
                    P2_results += 0;
                }
            }

            P1_mean = P1_results / counter;
            P2_mean = P2_results / counter;
            ties = 1.0 - (P1_mean + P2_mean);

            results = String.format("| %-8.5f | %-8.5f | %-8.5f | -> %d", P1_mean, P2_mean, ties, counter);
            System.out.print("\r" + results);
        }

        logger.log(results);
        System.out.println("\n" + bar);
        logger.log(bar);
    }
}