package ch.uzh.ifi.seal.soprafs20.rest.dto;

import java.time.LocalTime;

public class TournamentPostDTO {

    private String tournamentName;
    private float breakDuration;
    private float gameDuration;
    private LocalTime startTime;
    private int numberTables;
    private int amountOfPlayers;
    private long managerId;

    public float getBreakDuration() {
        return breakDuration;
    }
    public String getTournamentName() {
        return tournamentName;
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
    public long getManagerId() {
        return managerId;
    }

    public void setBreakDuration(float breakDuration) {
        this.breakDuration = breakDuration;
    }
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
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
    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }
}
