package org.example.Games.Boards;

import org.example.Games.BoardShapes.ShapeHexagon;
import org.example.Utils.RandomGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class BoardComplex extends AbstractBoard {

    public BoardComplex(int size) {
        super(new ShapeHexagon(size));
    }

    public double getScore(int playerID) {
        int matrixSize = BOARD.length; // n
        boolean[][] visited = new boolean[matrixSize][matrixSize];
        double score = 1;

        for (int row = 0; row < matrixSize; row++) { // O(n^2)
            for (int col = 0; col < matrixSize; col++) { // O(n)
                if(BOARD[row][col] == playerID && !visited[row][col]){
                    int cluster = 1;
                    LinkedList<int[]> queue = new LinkedList<>();
                    queue.add(new int[]{row, col});
                    visited[row][col] = true;

                    while (!queue.isEmpty()){ // O(n)
                        int[] currentCell = queue.pollLast();
                        List<int[]> cellNeighbors = new LinkedList<>();

                        for (int Q = -1; Q <= 1; Q++) { // O(1)
                            for (int R = -1; R <= 1; R++) { // O(1)
                                if(Q == R) continue;
                                int newRow = currentCell[0] + Q;
                                int newCol = currentCell[1] + R;
                                if(newCol >= 0 && newRow >= 0 && newCol < matrixSize && newRow < matrixSize){
                                    if(BOARD[newRow][newCol] != -1){
                                        cellNeighbors.add(new int[]{newRow, newCol});
                                    }
                                }
                            }
                        }

                        for (int[] neighbor: cellNeighbors){ // O(1)
                            if(BOARD[neighbor[0]][neighbor[1]] == playerID && !visited[neighbor[0]][neighbor[1]]){
                                visited[neighbor[0]][neighbor[1]] = true;
                                queue.add(neighbor);
                                cluster++;
                            }
                        }
                    }
                    score *= cluster;
                }
            }
        }
        return score;
    }


    @Override
    public boolean isGameOver() {
        int MAX_moves = this.SHAPE.getSizeMax();
        int MIN_moves = this.MOVES.size();
        return (MAX_moves - MIN_moves) < 4;
    }
    @Override
    public int getPlayer(int n) {
        return ((this.MOVES.size() + n) % 4) / 2 + 1;
    }

    public void makeMove(int[] move){
        this.BOARD[move[0]][move[1]] = move[2];
        this.MOVES.add(new int[]{move[0], move[1], move[2]});
    }

    @Override
    public LinkedList<int[]> getAllMoves(){

        LinkedList<int[]> allMoves = new LinkedList<>();

        if(this.MOVES.size() % 2 == 0){
            for (int i = 0; i < BOARD.length; i++) {
                for (int j = 0; j < BOARD[0].length; j++) {
                    if(BOARD[i][j] == 0){
                        allMoves.add(new int[]{i, j, 1});
                        allMoves.add(new int[]{i, j, 2});
                    }
                }
            }
        }else {

            int[] lastMove = this.MOVES.getLast();
            int color = lastMove[2] == 1 ? 2 : 1;
            for (int i = 0; i < BOARD.length; i++) {
                for (int j = 0; j < BOARD[0].length; j++) {
                    if(BOARD[i][j] == 0){
                        allMoves.add(new int[]{i, j, color});
                    }
                }
            }
        }

        Collections.shuffle(allMoves, RandomGenerator.RANDOM_GENERATOR);
        return allMoves;
    }

    @Override
    public int getPiece() {
        return this.MOVES.size() % 2 == 0 ? (byte) 1 : (byte) 2;
    }

    @Override
    public AbstractBoard clone() {
        int[][] newBoard = this.SHAPE.createBoard();
        for (int i = 0; i < BOARD.length; i++) {
            System.arraycopy(this.BOARD[i], 0, newBoard[i], 0, BOARD[0].length);
        }
        LinkedList<int[]> newMoves = new LinkedList<>(this.MOVES);
        BoardComplex clone = new BoardComplex(this.SHAPE.getSize());
        clone.BOARD = newBoard;
        clone.MOVES = newMoves;
        return clone;
    }
}
