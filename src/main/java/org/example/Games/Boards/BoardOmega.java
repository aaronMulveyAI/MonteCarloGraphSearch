package org.example.Games.Boards;

import org.example.Games.BoardShapes.ShapeHexagon;
import org.example.Utils.RandomGenerator;

import java.util.*;

public class BoardOmega extends AbstractBoard {
    public BoardOmega(int size) {
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
        int MAX_moves = this.SHAPE.getSizeMax();
        int MIN_moves = this.MOVES.size();
        return (MAX_moves - MIN_moves) < 4;
    }
    @Override
    public int getPlayer(int n) {
        return ((this.MOVES.size() + n) % 4) / 2 + 1;
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
        BoardOmega clone = new BoardOmega(this.SHAPE.getSize());
        clone.BOARD = newBoard;
        clone.MOVES = newMoves;
        return clone;
    }

    public static void main(String[] args) {
        AbstractBoard a = new BoardOmega(5);
        double P1_WINS = 0;
        int SIMULATIONS = 100000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < SIMULATIONS; i++) {
            P1_WINS += (a.simulateRandomPlayOut() == 1) ? 1 : 0;
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(P1_WINS / SIMULATIONS);
    }
}
