package org.example.Games.BoardShapes;


public class ShapeHexagon implements iBoardShape{
    private final int size;
    private int MAX_size;

    public ShapeHexagon(int size) {
        this.size = size;
    }

    @Override
    public int[][] createBoard() {
        int n = this.size;
        this.MAX_size = 0;
        int matrixSize = (n * 2 - 1);
        int[][] board = new int[matrixSize][matrixSize];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                int bound =  (i + j);
                int maxBound = (matrixSize + n - 1);
                int minBound = (matrixSize - n);
                if (bound < minBound) { // Out of min hexagonal bounds
                    board[i][j] = NULL;

                } else if (bound >= maxBound) { // Out of max hexagonal bounds
                    board[i][j] = NULL;

                } else {
                    MAX_size ++;
                    board[i][j] = EMPTY; // Belongs to the hexagonal Grid

                }
            }
        }
        return board;
    }

    @Override
    public String getGUI(int[][] board) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (int[] cell : board) {
            sb.append("\t".repeat(board.length));
            int counter = 0;
            for (int i : cell) {
                if (i == -1) counter++;
            }
            String border;
            if (index < board.length / 2) {
                border = "/";
            } else if (index > board.length / 2) {
                border = "\\";
            } else {
                border = "|";
            }
            sb.append(" ".repeat(counter)).append(border);

            for (int i : cell) {
                if (i != -1) {
                    String value = i == 0 ? "·" : i == 1 ? "X" : "O";
                    sb.append(value).append(" ");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            if (index < board.length / 2) {
                border = "\\";
            } else if (index > board.length / 2) {
                border = "/";
            } else {
                border = "|";
            }
            sb.append(border);

            sb.append("\n");
            index++;
        }
        return sb.toString();
    }
    @Override
    public int getSize() {
        return this.size;
    }
    @Override
    public int getSizeMax() {
        return this.MAX_size;
    }
}
