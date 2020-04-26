package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

public class ManagerGetDTO {

    private Long managerID;
    private String username;

    public Long getManagerID() {
        return managerID;
    }

    public void setManagerID(Long id) {
        this.managerID = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }
    
}
