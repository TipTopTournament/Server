package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Statistics")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long statisticsID;

    @OneToOne
    private Participant participant;

    @Column
    private int wins;

    @Column
    private int losses;

    @Column
    private int pointsScored;

    @Column
    private int pointsConceded;

    @ManyToMany(targetEntity = Game.class)
    private List<Game> history = new ArrayList<>();


    // getters
    public Long getStatisticsID() {
        return statisticsID;
    }
    public Participant getParticipant() {
        return participant;
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
    public List<Game> getHistory() {
        return history;
    }

    // setters


    public void setStatisticsID(Long statisticsID) {
        this.statisticsID = statisticsID;
    }
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
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
    public void addGameToHistory(Game game) {
        this.history.add(game);
    }
}
