package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bracket {

    @Id
    @GeneratedValue
    public long bracketID;

    @OneToMany(targetEntity = Game.class)
    private List<Game> bracketList = new ArrayList<>();


    public List<Game> getBracketList() {
        return bracketList;
    }
    public void setBracketList(List<Game> bracketList) {
        this.bracketList = bracketList;
    }
}
