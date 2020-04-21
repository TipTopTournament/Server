package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserState;

public class ParticipantPutDTO{

    private String token;
    private Long participantID;
    private UserState userState;


    public UserState getUserState() {
        return userState;
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
    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}

