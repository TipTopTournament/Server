package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.GameState;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;

public class GameGetDTO {

    private long gameId;
    private String startTime;
    private GameState gameState = GameState.NOTREADY;
    private int score1;
    private int score2;
    private ParticipantGetDTO participant1;
    private ParticipantGetDTO participant2;
    private String tournamentCode;


    public long getGameId() {
        return gameId;
    }
    public String getStartTime() {
        return startTime;
    }
    public GameState getGameState() {
        return gameState;
    }
    public int getScore1() {
        return score1;
    }
    public int getScore2() {
        return score2;
    }
    public ParticipantGetDTO getParticipant1() {
        return participant1;
    }
    public ParticipantGetDTO getParticipant2() {
        return participant2;
    }
    public String getTournamentCode() {
        return tournamentCode;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public void setScore1(int score1) {
        this.score1 = score1;
    }
    public void setScore2(int score2) {
        this.score2 = score2;
    }
    public void setParticipant1(Participant participant1) {
        this.participant1 = DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(participant1);
    }
    public void setParticipant2(Participant participant2) {
        this.participant2 = DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(participant2);
    }
    public void setTournamentCode(String tournamentCode) {
        this.tournamentCode = tournamentCode;
    }
}
