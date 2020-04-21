package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserState;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;

public class ParticipantGetDTO {

    private Long participantID;
    private String licenseNumber;
    private String vorname;
    private String nachname;
    private UserState userState;

    public Long getParticipantID() {
        return participantID;
    }

    public void setParticipantID(Long id) {
        this.participantID = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVorname() {
        return vorname;
    }
    public String getNachname() {
        return nachname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}
