package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class ParticipantPostDTO {

    private String vorname;
    private String nachname;
    private String password;
    private String licenseNumber;

    public String getVorname() {
        return vorname;
    }
    public String getNachname() {
        return nachname;
    }
    public String getPassword() { return password; }
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
