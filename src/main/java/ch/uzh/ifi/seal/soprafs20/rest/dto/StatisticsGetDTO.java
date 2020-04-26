package ch.uzh.ifi.seal.soprafs20.rest.dto;

import java.util.List;

public class StatisticsGetDTO {

    private Long statisticsID;
    private int wins;
    private int losses;
    private int pointsScored;
    private int pointsConceded;
    private List<GameGetDTO> history;

    // getters
    public Long getStatisticsID() {
        return statisticsID;
    }
    public int getWins() {
        return wins;
    }
    public int getLosses() {
        return losses;
    }
    public int getPointsScored() {
        return pointsScored;
    }
    public int getPointsConceded() {
        return pointsConceded;
    }
    public List<GameGetDTO> getHistory() {
        return history;
    }

    // setters
    public void setWins(int wins) {
        this.wins = wins;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }
    public void setPointsScored(int pointsScored) {
        this.pointsScored = pointsScored;
    }
    public void setPointsConceded(int pointsConceded) {
        this.pointsConceded = pointsConceded;
    }
    public void setStatisticsID(Long statisticsID) {
        this.statisticsID = statisticsID;
    }
    public void setHistory(List<GameGetDTO> history) {
        this.history = history;
    }
}
