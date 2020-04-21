package ch.uzh.ifi.seal.soprafs20.service;

import java.util.Random;
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

    public Participant getParticipantByLicenseNumber(String licensenumber) {
        return participantRepository.findByLicenseNumber(licensenumber);
    }

    public Participant createParticipant(Participant newParticipant) {

        if (newParticipant.getLicenseNumber() == null) {
            newParticipant.setToken(UUID.randomUUID().toString());
            newParticipant.setLicenseNumber(createNewLicenseNumber());

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
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No participant found with this Id");
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
        Random r = new Random();

        while (exists) {
            newLicensenumber = Integer.toString(r.nextInt((999999-100000)+1) + 100000);

            exists = participantRepository.findByLicenseNumber(newLicensenumber) != null;
        }

        return newLicensenumber;
    }
}
