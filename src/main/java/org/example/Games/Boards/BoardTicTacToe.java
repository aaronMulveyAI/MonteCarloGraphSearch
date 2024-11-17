package org.example.Games.Boards;

import org.example.Games.BoardShapes.ShapeSquare;
import org.example.Utils.RandomGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BoardTicTacToe extends AbstractBoard{


    public BoardTicTacToe(){
        super(new ShapeSquare(3));
    }


    @Override
    public double getScore(int playerID) {
        int diagonal1 = 0;
        int diagonal2 = 0;

        for (int i = 0; i < BOARD.length; i++) {
            int row = 0;
            int col = 0;
            for (int j = 0; j < BOARD.length; j++) {
                if(this.BOARD[i][j] == playerID) row++;
                if(this.BOARD[j][i] == playerID) col++;
            }
            // CHEQUEAR FILAS
            if(row == BOARD.length || col == BOARD.length) return playerID;

            if(BOARD[i][i] == playerID) diagonal1++;
            if(BOARD[i][BOARD.length - 1 - i] == playerID) diagonal2++;
            // CHEQUEAR DIAGONALES
            if (diagonal1 == BOARD.length || diagonal2 == BOARD.length) return playerID;
        }

        return 0;
    }

    public LinkedList<int[]> getAllMoves(){
        LinkedList<int[]> allMoves = new LinkedList<>();
        for (int i = 0; i < BOARD.length; i++) {
            for (int j = 0; j < BOARD[0].length; j++) {
                if(BOARD[i][j] == 0){ allMoves.add(new int[]{i, j});}
            }
        }
        Collections.shuffle(allMoves, RandomGenerator.RANDOM_GENERATOR);
        return allMoves;
    }

    @Override
    public boolean isGameOver() {
        if(this.getAllMoves().isEmpty()) return true;
        if(this.getScore(1) != 0) return true;
        return this.getScore(2) != 0;
    }
    @Override
    public int getPlayer(int n) {
        return (this.MOVES.size() - n) % 2 == 0 ? 1 : 2;
    }

    public int getPiece(){
        return this.MOVES.size() % 2 == 0 ? 1 : 2;
    }

    @Override
    public AbstractBoard clone() {
        int[][] newBoard = this.SHAPE.createBoard();
        for (int i = 0; i < BOARD.length; i++) {
            System.arraycopy(this.BOARD[i], 0, newBoard[i], 0, BOARD[0].length);
        }
        LinkedList<int[]> newMoves = new LinkedList<>(this.MOVES);
        BoardTicTacToe clone = new BoardTicTacToe();
        clone.BOARD = newBoard;
        clone.MOVES = newMoves;
        return clone;
    }
}
