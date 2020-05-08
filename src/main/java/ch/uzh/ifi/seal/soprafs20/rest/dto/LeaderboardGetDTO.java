package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.PlayerState;

public class LeaderboardGetDTO {

    private ParticipantGetDTO participant;
    private int wins;
    private int losses;
    private int pointsScored;
    private int pointsConceded;
    private PlayerState playerState;

    public ParticipantGetDTO getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantGetDTO participant) {
        this.participant = participant;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getPointsScored() {
        return pointsScored;
    }

    public void setPointsScored(int pointsScored) {
        this.pointsScored = pointsScored;
    }

    public int getPointsConceded() {
        return pointsConceded;
    }

    public void setPointsConceded(int pointsConceded) {
        this.pointsConceded = pointsConceded;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }
}
