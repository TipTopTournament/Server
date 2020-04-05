package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("managerRepository")
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findByName(String name);
    Manager findByUsername(String username);
}
