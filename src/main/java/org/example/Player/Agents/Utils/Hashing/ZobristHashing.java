package org.example.Player.Agents.Utils.Hashing;

import org.example.Player.Agents.MonteCarloSearch.Algorithm.AbstractNode;
import org.example.Utils.RandomGenerator;


public class ZobristHashing {
    private static long[][][] ZOBRIST_TABLE;
    private final int WIDTH;
    private final int HEIGHT;
    private final int N;

    public ZobristHashing(AbstractNode node) {
        this.WIDTH = node.state.BOARD.length;
        this.HEIGHT = node.state.BOARD.length;
        this.N = 3;
        initializeZobristTable();
    }

    private void initializeZobristTable() {

        ZOBRIST_TABLE = new long[N][WIDTH][HEIGHT];
        for (int pieceType = 0; pieceType < N; pieceType++) {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    ZOBRIST_TABLE[pieceType][x][y] = RandomGenerator.getRandomLong();
                }
            }
        }
    }

    public long getHash(AbstractNode node) {
        long hash = 0L;
        for (byte x = 0; x < WIDTH; x++) {
            for (byte y = 0; y < HEIGHT; y++) {
                int pieceType = node.state.BOARD[x][y];
                if (pieceType != -1) {
                    hash ^= ZOBRIST_TABLE[pieceType][x][y];
                }
            }
        }
        return hash;
    }
}

