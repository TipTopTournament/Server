package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Manager")
public class Manager extends Person{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long managerID;

    @Column(nullable = false)
    private String username;

    public Long getManagerID() {
        return managerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
