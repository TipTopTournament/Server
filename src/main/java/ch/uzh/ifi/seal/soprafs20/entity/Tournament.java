package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.TournamentState;
import org.springframework.data.annotation.Persistent;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;


@Entity(name = "Tournament")
public class Tournament {

    @Id
    @GeneratedValue
    public long tournamentId;

    @Column
    public TournamentState tournamentState;

    @Column
    public LocalTime startTime;

    @Column
    public float gameDuration;

    @Column
    public float breakDuration;

    @Column
    public String tournamentCode;

    @Column
    public int amountOfPlayers;

    @Column
    public int numberTables;

    @OneToOne
    public Leaderboard leaderboard;

    @OneToOne
    public Bracket bracket;

    @OneToMany
    public List<Participant> activePlayers;

    public long getTournamentId() {
        return tournamentId;
    }
    public TournamentState getTournamentState() {
        return tournamentState;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public float getGameDuration() {
        return gameDuration;
    }
    public float getBreakDuration() {
        return breakDuration;
    }
    public String getTournamentCode() {
        return tournamentCode;
    }
    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }
    public int getNumberTables() {
        return numberTables;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public Bracket getBracket() {
        return bracket;
    }
    public List<Participant> getActivePlayers() {
        return activePlayers;
    }

    public void setTournamentState(TournamentState tournamentState) {
        this.tournamentState = tournamentState;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public void setGameDuration(float gameDuration) {
        this.gameDuration = gameDuration;
    }
    public void setBreakDuration(float breakDuration) {
        this.breakDuration = breakDuration;
    }
    public void setTournamentCode(String tournamentCode) {
        this.tournamentCode = tournamentCode;
    }
    public void setAmountOfPlayers(int numberOfPlayers) {
        this.amountOfPlayers = numberOfPlayers;
    }
    public void setNumberTables(int numberTables) {
        this.numberTables = numberTables;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    public void setBracket(Bracket bracket) {
        this.bracket = bracket;
    }
    public void setActivePlayers(List<Participant> activePlayers) {
        this.activePlayers = activePlayers;
    }
}
