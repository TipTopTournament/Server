package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.repository.ParticipantRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.constant.UserState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import javax.servlet.http.Part;
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

    private final Logger log = LoggerFactory.getLogger(ParticipantService.class);

    private ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(@Qualifier("participantRepository")ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> getParticipants() {
        return this.participantRepository.findAll();
    }

    public Participant getParticipantById(Long id) {
        for (Participant participant : getParticipants()) {
            if (participant.getParticipantID().equals(id)) {
                return participant;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No participant found with this Id");
    }

    public Participant getParticipantByUsername(String username) {
        for (Participant participant : getParticipants()) {
            if (participant.getUsername().equals(username)) {
                return participant;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No manager found with this username");
    }

    public Participant createParticipant(Participant newParticipant) {
        newParticipant.setToken(UUID.randomUUID().toString());

        if(!CheckIfUsernameIsTaken(newParticipant)) {
            newParticipant = participantRepository.save(newParticipant);
            participantRepository.flush();

            log.debug("Created Information for Manager: {}", newParticipant);
            return newParticipant;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
    }
    
    public void updateState(Long id, UserState state) {
    	if(checkIfParticipantIdExists(id)) {
    		Participant participant = getParticipantById(id);
    		participant.setUserState(state);
    	}
    	else {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No participant found with this Id");
    	}
    }

    private boolean CheckIfUsernameIsTaken(Participant participantToTest) {
        for (Participant participant : getParticipants()) {
            if (participant.getUsername().equals(participantToTest.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfParticipantIdExists(Long id) {
        for (Participant participant : getParticipants()) {
            if (participant.getParticipantID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        for (Participant participant : getParticipants()) {
            if (participant.getUsername().equals(username) && participant.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
