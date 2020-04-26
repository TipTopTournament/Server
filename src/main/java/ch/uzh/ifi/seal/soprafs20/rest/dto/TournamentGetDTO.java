package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class TournamentGetDTO {

    private long tournamentId;
    private String tournamentName;
    private String tournamentCode;
    private ParticipantGetDTO winner;
    private float breakDuration;
    private float gameDuration;
    private String startTime;
    private int numberTables;
    private int amountOfPlayers;
    private String informationBox;

    public long getTournamentId() {
        return tournamentId;
    }
    public ParticipantGetDTO getWinner() {
        return winner;
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
    public String getStartTime() {
        return startTime;
    }
    public int getNumberTables() {
        return numberTables;
    }
    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }
    public String getInformationBox() {
        return informationBox;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }
    public void setWinner(ParticipantGetDTO winner) {
        this.winner = winner;
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
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setNumberTables(int numberTables) {
        this.numberTables = numberTables;
    }
    public void setAmountOfPlayers(int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
    }
    public void setInformationBox(String informationBox) {
        this.informationBox = informationBox;
    }
}
