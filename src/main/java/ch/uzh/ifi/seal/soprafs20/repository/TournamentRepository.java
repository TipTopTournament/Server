package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("tournamentRepository")
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Tournament findByTournamentCode(String tournamentCode);
}
