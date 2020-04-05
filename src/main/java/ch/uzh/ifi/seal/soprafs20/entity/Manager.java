package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity(name = "Manager")
public class Manager extends Person{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long managerID;

    public Long getManagerID() {
        return managerID;
    }
}
