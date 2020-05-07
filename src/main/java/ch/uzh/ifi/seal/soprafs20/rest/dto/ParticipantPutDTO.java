package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

public class ParticipantPutDTO{

    private String token;
    private Long participantID;
    private UserStatus userStatus;


    public UserStatus getUserStatus() {
        return userStatus;
    }
    public String getToken() {
        return token;
    }
    public Long getParticipantID() {
        return participantID;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void setParticipantID(long participantID) {
        this.participantID = participantID;
    }
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}

