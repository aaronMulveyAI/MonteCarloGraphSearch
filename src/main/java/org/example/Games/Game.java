package org.example.Games;

import org.example.Games.Boards.AbstractBoard;
import org.example.Games.Boards.BoardComplex;
import org.example.Games.Boards.BoardOmega;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD1;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD2;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Graph.Implementation.UCD3;
import org.example.Player.Agents.MonteCarloSearch.Implementation.Tree.Implementations.*;
import org.example.Player.Agents.Utils.StopCondition.StopBySimulations;
import org.example.Player.Agents.Utils.StopCondition.StopByTime;
import org.example.Player.Players.AbstractPlayer;
import org.example.Player.Agents.Utils.StopCondition.iSearchStopCondition;
import org.example.Player.Players.PlayerMCS;
import org.example.Player.Players.PlayerRandom;


public class Game {
    public AbstractBoard BOARD;
    public AbstractPlayer[] PLAYERS;
    public Game(AbstractBoard board, AbstractPlayer[] players){
        this.PLAYERS = players;
        this.BOARD = board.clone();
    }
    public double runGame(){
        boolean canPlay = true;
        while(canPlay){

            canPlay = this.runOneTurn();
        }
        PLAYERS[0].reset();
        PLAYERS[1].reset();
        return this.BOARD.getWinner();
    }
    public boolean runOneTurn(){
        if(BOARD.isGameOver()){
            return false;
        }else {
            int playerIndex = this.BOARD.getPlayer(0) - 1;
            AbstractPlayer currentPlayer = PLAYERS[playerIndex];
            int[] playerAction = currentPlayer.makeMove(this);
            this.BOARD.makeMove(playerAction);
            return true;
        }
    }

    public static void main(String[] args) {

        AbstractBoard board = new BoardComplex(5);
        //AbstractBoard board = new BoardOmega(5);
        //AbstractBoard board = new BoardTicTacToe();
        iSearchStopCondition stop = new StopBySimulations(10000, true);

        AbstractPlayer[] allPlayers = new AbstractPlayer[]{
                //new PlayerMCS(new UCT0(stop)),
                new PlayerRandom(),
                //new PlayerMCS(new UCT3(stop)),
                new PlayerMCS(new UCD3(stop, null)),

        };

        AbstractPlayer[] players =  new AbstractPlayer[]{
                allPlayers[0],
                allPlayers[1],
        };


        double P1_WINS = 0;
        int SIMULATIONS = 1;
        long start = System.currentTimeMillis();
        for (int i = 0; i < SIMULATIONS; i++) {
            System.out.println("\n\n\n");
            Game game = new Game(board.clone(), players);
            int winner = (game.runGame() == 1) ? 1 : 0;
            P1_WINS += winner;
            int index = winner == 1 ? 0 : 1;
            //System.out.println(i + " " + players[index].getNAME());
        }

        long end = System.currentTimeMillis();
        System.out.println("\nTime: " + (end - start));
        System.out.println(players[0].getNAME() + " " + P1_WINS / SIMULATIONS);

    }
}
