package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

public class ManagerGetDTO {

    private Long managerID;
    private String vorname;
    private String nachname;

    public Long getManagerID() {
        return managerID;
    }

    public void setManagerID(Long id) {
        this.managerID = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String name) {
        this.vorname = name;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String username) {
        this.nachname = username;
    }
}
