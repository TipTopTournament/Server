package ch.uzh.ifi.seal.soprafs20.comparators;

import ch.uzh.ifi.seal.soprafs20.entity.Game;

import java.util.Comparator;

public class SortGamesByGameId implements Comparator<Game> {
    public int compare(Game game1, Game game2) {
        return (int)game1.getGameId() - (int)game2.getGameId();
    }
}
