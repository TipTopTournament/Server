package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

@Entity
public class Leaderboard {

    @Id
    @GeneratedValue
    public long leaderboardId;

    @OneToMany(targetEntity = Participant.class)
    private List<Participant> leaderboardList = new ArrayList<>();

    public List<Participant> getLeaderboardList() {
        return leaderboardList;
    }

    public void addParticipant(Participant participant) {
        leaderboardList.add(participant);
    }

}
