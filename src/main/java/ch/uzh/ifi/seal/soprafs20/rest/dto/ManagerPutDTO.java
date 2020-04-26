package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

public class ManagerPutDTO {

    private String token;
    private UserStatus userStatus;
    private Long managerID;
    
    public UserStatus getUserStatus() {
    	return userStatus;
    }

    public Long getManagerID() {
        return managerID;
    }

    public void setUserStatus(UserStatus userStatus) {
    	this.userStatus = userStatus;
    }

    public void setManagerID(Long managerID) {
        this.managerID = managerID;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
