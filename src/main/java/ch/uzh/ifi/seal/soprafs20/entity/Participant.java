package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.UserState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Participant")
public class Participant extends Person{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long participantID;

    @Column
    private String licenseNumber;

    @Column
    private String code;

    @Column
    private UserState userState;


    // getters
    public Long getParticipantID() {
        return participantID;
    }
    public String getLicenseNumber() {
        return licenseNumber;
    }
    public String getCode() {
        return code;
    }
    public UserState getUserState() {
        return userState;
    }

    //setters
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}
