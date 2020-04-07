package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

public class ManagerPutDTO {

    private String token;
    
    private UserStatus userStatus;
    
    public UserStatus getUserStatus() {
    	return userStatus;
    }
    
    public void setUserStatus(UserStatus userStatus) {
    	this.userStatus = userStatus;
    }
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
