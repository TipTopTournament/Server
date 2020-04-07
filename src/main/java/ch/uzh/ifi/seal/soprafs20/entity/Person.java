package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

@MappedSuperclass
public abstract class Person {

    @Column(nullable = false)
    private String username;

    @Column
    private String password;

    @Column
    private String token;

    @Column
    private String name;

    @Column
    private UserStatus userstatus;

    //getters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getToken() {
        return token;
    }
    public String getName() {
        return name;
    }
    public UserStatus getUserStatus() {
        return userstatus;
    }

    //setters

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUserStatus(UserStatus userstatus) {
        this.userstatus = userstatus;
    }
}
