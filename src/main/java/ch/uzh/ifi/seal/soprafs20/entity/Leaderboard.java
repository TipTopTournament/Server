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

    @Column
    private String wins;

    public List<Participant> getLeaderboardList() {
        return leaderboardList;
    }
    public List<Integer> getWins() {
        List<Integer> result = new ArrayList<>();

        for(String x : wins.split(",")) {
            result.add(Integer.parseInt(x));
        }

        return result;
    }

    public void addParticipant(Participant participant) {
        leaderboardList.add(participant);
    }
    public void setWins(List<Integer> vals) {
        String string = "";
        for (int x : vals) {
            string = string.concat(Integer.toString(x) + ",");
        }
        this.wins = string.substring(0, string.length() - 1);
    }
}
