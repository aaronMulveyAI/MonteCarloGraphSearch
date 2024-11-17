package org.example.Games.BoardShapes;

public class ShapeSquare implements iBoardShape{

    private int size;
    private int MAX_size;

    public ShapeSquare(int size) {
        this.size = size;
    }

    @Override
    public int[][] createBoard() {
        int[][] newBoard = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                newBoard[i][j] = 0;
            }
        }
        return newBoard;
    }

    @Override
    public String getGUI(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int cell : row) {
                sb.append(cell == 0 ? "-" : (cell == 1 ? "X" : "O"));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getSizeMax() {
        return 0;
    }
}
