package ch.uzh.ifi.seal.soprafs20.rest.dto;

import java.time.LocalTime;

public class TournamentPostDTO {

    private float breakDuration;
    private float gameDuration;
    private LocalTime startTime;
    private int numberTables;
    private int amountOfPlayers;

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
