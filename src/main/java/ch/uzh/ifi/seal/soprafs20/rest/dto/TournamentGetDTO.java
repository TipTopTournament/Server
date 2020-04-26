package ch.uzh.ifi.seal.soprafs20.rest.dto;

import java.time.LocalTime;

public class TournamentGetDTO {

    private long tournamentId;
    private String tournamentName;
    private String tournamentCode;
    private float breakDuration;
    private float gameDuration;
    private LocalTime startTime;
    private int numberTables;
    private int amountOfPlayers;

    public long getTournamentId() {
        return tournamentId;
    }
    public String getTournamentName() {
        return tournamentName;
    }
    public String getTournamentCode() {
        return tournamentCode;
    }
    public float getBreakDuration() {
        return breakDuration;
    }
    public float getGameDuration() {
        return gameDuration;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public int getNumberTables() {
        return numberTables;
    }
    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }
    public void setTournamentCode(String tournamentCode) {
        this.tournamentCode = tournamentCode;
    }
    public void setBreakDuration(float breakDuration) {
        this.breakDuration = breakDuration;
    }
    public void setGameDuration(float gameDuration) {
        this.gameDuration = gameDuration;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public void setNumberTables(int numberTables) {
        this.numberTables = numberTables;
    }
    public void setAmountOfPlayers(int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
    }
}
