package ch.uzh.ifi.seal.soprafs20.service;

import java.util.Random;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.repository.ParticipantRepository;
import ch.uzh.ifi.seal.soprafs20.repository.StatisticsRepository;
import ch.uzh.ifi.seal.soprafs20.constant.UserState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * Participant Service
 * This class is the "worker" and responsible for all functionality related to the user of type Participant
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final StatisticsRepository statisticsRepository;
    private final String errorMsgNotFound = "No participant found with this Id";
    Random r = new Random();

    @Autowired
    public ParticipantService(@Qualifier("participantRepository")ParticipantRepository participantRepository,
                              @Qualifier("statisticsRepository")StatisticsRepository statisticsRepository) {
        this.participantRepository = participantRepository;
        this.statisticsRepository = statisticsRepository;
    }

    public List<Participant> getParticipants() {
        return this.participantRepository.findAll();
    }

    public Participant getParticipantById(Long id) {
        if (participantRepository.findByParticipantID(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMsgNotFound);
        }
        else {
            return participantRepository.findByParticipantID(id);
        }
    }

    public Participant getParticipantByLicenseNumber(String licensenumber) {
        return participantRepository.findByLicenseNumber(licensenumber);
    }

    public Statistics getStatsByParticipantID(Long id) {
        if (participantRepository.findByParticipantID(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMsgNotFound);
        }
        else {
            return participantRepository.findByParticipantID(id).getStatistics();
        }
    }

    public Participant createParticipant(Participant newParticipant) {

        if (newParticipant.getLicenseNumber() == null) {

            newParticipant.setToken(UUID.randomUUID().toString());
            newParticipant.setLicenseNumber(createNewLicenseNumber());
            newParticipant.setStatistics(createEmptyStats());

            participantRepository.save(newParticipant);
            participantRepository.flush();

            return newParticipant;
        }
        else if (participantRepository.findByLicenseNumber(newParticipant.getLicenseNumber()) != null
            && participantRepository.findByLicenseNumber(newParticipant.getLicenseNumber()).getPassword() == null) {

            Participant oldOParticipant = participantRepository.findByLicenseNumber(newParticipant.getLicenseNumber());

            // set new values
            oldOParticipant.setToken(UUID.randomUUID().toString());
            oldOParticipant.setPassword(newParticipant.getPassword());
            oldOParticipant.setStatistics(createEmptyStats());
            
            participantRepository.save(oldOParticipant);
            participantRepository.flush();

            return oldOParticipant;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Participant already exists or license number doesn't exist");
        }
    }
    
    public void updateState(Long id, UserState state) {
    	if(checkIfParticipantIdExists(id)) {
    		Participant participant = getParticipantById(id);
    		participant.setUserState(state);
    	}
    	else {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMsgNotFound);
    	}
    }

    public boolean checkIfParticipantIdExists(Long id) {
        for (Participant participant : getParticipants()) {
            if (participant.getParticipantID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkLicenseNumberAndPassword(String licenseNumber, String password) {
        for (Participant participant : getParticipants()) {
            if (participant.getLicenseNumber().equals(licenseNumber) && participant.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private String createNewLicenseNumber() {
        boolean exists = true;
        String newLicensenumber = "";

        while (exists) {
            newLicensenumber = Integer.toString(r.nextInt((999999-100000)+1) + 100000);

            exists = participantRepository.findByLicenseNumber(newLicensenumber) != null;
        }

        return newLicensenumber;
    }

    private Statistics createEmptyStats() {
        Statistics stats = new Statistics();

        stats.setWins(0);
        stats.setLosses(0);
        stats.setPointsScored(0);
        stats.setPointsConceded(0);

        statisticsRepository.save(stats);
        statisticsRepository.flush();

        return stats;
    }
}
