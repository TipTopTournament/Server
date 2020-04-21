package ch.uzh.ifi.seal.soprafs20.entity;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.*;
import javax.servlet.http.Part;
import java.util.List;

@Entity(name = "Statistics")
public class Statistics {

    @Id
    @GeneratedValue
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

    @OneToMany
    private List<Game> history;


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
