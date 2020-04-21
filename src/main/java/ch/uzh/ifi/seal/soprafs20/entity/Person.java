package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

@MappedSuperclass
public abstract class Person {

    @Column
    private String vorname;

    @Column
    private String nachname;

    @Column
    private String password;

    @Column
    private String token;

    @Column
    private UserStatus userstatus;

    //getters
    public String getPassword() {
        return password;
    }
    public String getToken() {
        return token;
    }
    public UserStatus getUserStatus() {
        return userstatus;
    }
    public String getVorname() {
        return vorname;
    }
    public String getNachname() {
        return nachname;
    }

    //setters

    public void setPassword(String password) {
        this.password = password;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setUserStatus(UserStatus userstatus) {
        this.userstatus = userstatus;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
}
