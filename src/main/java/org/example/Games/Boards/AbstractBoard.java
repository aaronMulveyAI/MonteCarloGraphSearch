package org.example.Games.Boards;

import org.example.Games.BoardShapes.iBoardShape;

import org.example.Utils.RandomGenerator;

import java.util.*;

public abstract class AbstractBoard {
    public int[][] BOARD;
    public LinkedList<int[]> MOVES;
    public final iBoardShape SHAPE;
    public AbstractBoard(iBoardShape SHAPE) {
        this.SHAPE = SHAPE;
        this.BOARD = SHAPE.createBoard();
        this.MOVES = new LinkedList<>();
    }
    public void makeMove(int[] move){
        this.BOARD[move[0]][move[1]] = this.getPiece();
        this.MOVES.add(new int[]{move[0], move[1]});
    }

    public abstract LinkedList<int[]> getAllMoves();
    public double simulateRandomPlayOut(){
        AbstractBoard clonedBoard = this.clone();
        LinkedList<int[]> allMoves = clonedBoard.getAllMoves();

        while (!clonedBoard.isGameOver()){
            clonedBoard.makeMove(allMoves.pop());
        }

        return clonedBoard.getWinner();
    }
    public int getWinner(){
        double P1_Winner = this.getScore(1);
        double P2_Winner = this.getScore(2);
        if(P1_Winner > P2_Winner) return 1;
        if(P1_Winner < P2_Winner) return 2;
        return 0;
    }

    @Override
    public String toString() {
        String board = SHAPE.getGUI(BOARD);
        String score = "\t\t\tP1: " + getScore(1) +  " || P2: " + getScore(2) + " -> " + this.getWinner() + "\n";
        return board + score;
    }
    public abstract double getScore(int playerID);
    public abstract boolean isGameOver();
    public abstract int getPlayer(int n);
    public abstract int getPiece();
    public abstract AbstractBoard clone();
}
