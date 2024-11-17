package org.example.Games.BoardShapes;

public interface iBoardShape {

    int NULL = -1;  /* Not available position */
    int EMPTY = 0;  /* Empty position */

    /**
     * @return the representation of the board
     */
    int[][] createBoard();

    /**
     * Used in the toString() od the abstract board
     * @param board the current state of the board
     * @return the String that represents the GUI
     */
    String getGUI(int[][] board);

    /**
     * @return the original size of the create board method
     */
    int getSize();

    /**
     * @return the amount of empty cells in the crateBoard method
     */
    int getSizeMax();
}
