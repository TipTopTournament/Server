package ch.uzh.ifi.seal.soprafs20.entity;

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
    public void setParticipantID(Long participantID) {
        this.participantID = participantID;
    }
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
