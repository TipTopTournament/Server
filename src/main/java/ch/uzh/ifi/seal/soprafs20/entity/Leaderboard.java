package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Leaderboard {

    @Id
    @GeneratedValue
    public long leaderboardID;

    @OneToMany(targetEntity = Participant.class)
    private List<Participant> leaderboardList = new ArrayList<>();

    public List<Participant> getLeaderboardList() {
        return leaderboardList;
    }

    public void setLeaderboardList(List<Participant> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }
}
