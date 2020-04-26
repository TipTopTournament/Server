package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Manager")
public class Manager extends Person{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long managerID;

    @Column(nullable = false)
    private String username;

    @OneToMany
    private List<Tournament> tournamentList = new ArrayList<>();

    public Long getManagerID() {
        return managerID;
    }
    public List<Tournament> getTournamentList() {
        return tournamentList;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setTournamentList(List<Tournament> tournamentList) {
        this.tournamentList = tournamentList;
    }
    public void addTournamentToTournamentList(Tournament tournament) {
        this.tournamentList.add(tournament);
    }
}
