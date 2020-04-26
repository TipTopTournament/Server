package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.TournamentState;

import javax.persistence.*;
import java.util.List;


@Entity(name = "Tournament")
public class Tournament {

    @Id
    @GeneratedValue
    public long tournamentId;

    @Column
    public String tournamentName; // something like Zürich Open or Champions League 23.4.20 by Tony

    @OneToOne
    public Participant winner;

    @Column String location;

    @Column
    public TournamentState tournamentState;

    @Column
    public String startTime;

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

    @Column
    public String informationBox;

    @OneToOne
    public Leaderboard leaderboard;

    @OneToOne
    public Bracket bracket;

    @OneToMany
    public List<Participant> activePlayers;

    public long getTournamentId() {
        return tournamentId;
    }
    public String getTournamentName() {
        return tournamentName;
    }
    public Participant getWinner() {
        return winner;
    }
    public TournamentState getTournamentState() {
        return tournamentState;
    }
    public String getStartTime() {
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
    public String getLocation() {
        return location;
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
    public String getInformationBox() {
        return informationBox;
    }

    public void setTournamentName(String tournamentName){
        this.tournamentName = tournamentName;
    }
    public void setWinner(Participant winner) {
        this.winner = winner;
    }
    public void setTournamentState(TournamentState tournamentState) {
        this.tournamentState = tournamentState;
    }
    public void setStartTime(String startTime) {
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
    public void setLocation(String location) {
        this.location = location;
    }
    public void setInformationBox(String informationBox) {
        this.informationBox = informationBox;
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
