package org.example.Lab;

import org.example.Games.Boards.AbstractBoard;
import org.example.Games.Boards.BoardOmega;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD1;

import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD2;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.UCT0;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.UCT1;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.UCT2;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.UCT3;
import org.example.Player.Agents.Utils.StopCondition.StopBySimulations;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Player.Players.AbstractPlayer;
import org.example.Player.Players.PlayerMCS;

import static org.example.Lab.Tournament.one_vs_one;

public class E1_Sim_Diff {

    public static void main(String[] args) {

    }

    public static void all_vs_all(AbstractPlayer[] players, AbstractBoard board, int simulations){

        for (int i = 0; i < players.length; i++) {
            for (int j = i; j < players.length; j++) {
                AbstractPlayer p1 = players[i];
                AbstractPlayer p2 = players[j];
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
        String results = "";

        int counter = 0;

        for (int n = 1; n <= n_games; n++) {

            double winners = one_vs_one(P1, P2, board.clone());
            counter++;

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

            double P1_mean = P1_results / counter;
            double P2_mean = P2_results / counter;
            double ties = 1.0 - (P1_mean + P2_mean);

            results = String.format("| %-8.5f | %-8.5f | %-8.5f | -> %d", P1_mean, P2_mean, ties, counter);
            System.out.print("\r" + results);


            winners = one_vs_one(P2, P1, board.clone());
            counter++;

            switch ((int) winners){
                case 1 ->{
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

        System.out.println("\n" + bar);
    }

}
