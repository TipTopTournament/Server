package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

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
    private boolean isLoggedin;

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
    public boolean isLoggedin() {
        return isLoggedin;
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
    public void setLoggedin(boolean loggedin) {
        isLoggedin = loggedin;
    }
}
