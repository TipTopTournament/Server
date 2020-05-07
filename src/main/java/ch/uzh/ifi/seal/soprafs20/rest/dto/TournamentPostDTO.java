package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class TournamentPostDTO {

    private String tournamentName;
    private float breakDuration;
    private float gameDuration;
    private String startTime;
    private int numberTables;
    private int amountOfPlayers;
    private long managerId;
    private String informationBox;
    private String location;

    public float getBreakDuration() {
        return breakDuration;
    }
    public String getTournamentName() {
        return tournamentName;
    }
    public float getGameDuration() {
        return gameDuration;
    }
    public String getStartTime() {
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
    public String getInformationBox() {
        return informationBox;
    }
    public String getLocation() {
        return location;
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
    public void setStartTime(String startTime) {
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
    public void setInformationBox(String informationBox) {
        this.informationBox = informationBox;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
