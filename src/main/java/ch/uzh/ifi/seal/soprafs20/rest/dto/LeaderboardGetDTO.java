package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class LeaderboardGetDTO {

    ParticipantGetDTO participant;
    int wins;

    public ParticipantGetDTO getParticipant() {
        return participant;
    }

    public int getWins() {
        return wins;
    }

    public void setParticipantGetDTO(ParticipantGetDTO participant) {
        this.participant = participant;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
