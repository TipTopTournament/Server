package ch.uzh.ifi.seal.soprafs20.comparators;

import ch.uzh.ifi.seal.soprafs20.entity.Leaderboard;

import java.util.Comparator;

public class SortLeaderboardById implements Comparator<Leaderboard> {
    public int compare(Leaderboard leaderboard1, Leaderboard leaderboard2) {
        return leaderboard2.getWins() - leaderboard1.getWins();
    }
}
