package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.UserState;

import javax.persistence.*;

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

    @Column
    private int eloScore;

    @Column
    private int ranking;

    @OneToOne
    private Statistics statistics;

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
    public int getEloScore() {
        return eloScore;
    }
    public int getRanking() {
        return ranking;
    }
    public Statistics getStatistics() {
        return statistics;
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
    public void setParticipantID(Long participantID) {
        this.participantID = participantID;
    }
    public void setEloScore(int eloScore) {
        this.eloScore = eloScore;
    }
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
