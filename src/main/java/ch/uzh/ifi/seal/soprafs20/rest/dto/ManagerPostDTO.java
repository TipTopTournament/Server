package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class ManagerPostDTO {

    private String vorname;
    private String nachname;
    private String password;
    private String username;


    public String getNachname() {
        return nachname;
    }
    public String getVorname() {
        return vorname;
    }
    public String getPassword() { return password; }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public void setUsername(String licenseNumber) {
        this.username = licenseNumber;
    }
}
