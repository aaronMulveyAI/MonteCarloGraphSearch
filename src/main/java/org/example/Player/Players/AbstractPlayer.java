package org.example.Player.Players;

import org.example.Games.Game;

public abstract class AbstractPlayer {
    private final String NAME;
    public AbstractPlayer(String name){
        this.NAME = name;
    }
    public abstract int[] makeMove(Game game);
    public abstract void reset();
    public String getNAME() {
        return NAME;
    }
}
