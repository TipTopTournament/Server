package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ParticipantService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Participant Controller
 * This class is responsible for handling all REST request that are related to the manager.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    // der einfachheit halber
    @GetMapping("/participants")
    @ResponseStatus(HttpStatus.OK)
    @Query
    public List<ParticipantGetDTO> getAllParticipants() {
        List<ParticipantGetDTO> allParticipants = new ArrayList<>();

        for (Participant participant : participantService.getParticipants()) {
            allParticipants.add(DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(participant));
        }
        return allParticipants;
    }

    @GetMapping("/participants/{participantId}")
    @ResponseStatus(HttpStatus.OK)
    @Query
    public ParticipantGetDTO getParticipant(@PathVariable("participantId") long id) {
        return participantService.getParticipantById(id);
    }

    @PostMapping("/participants")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createParticipant(@RequestBody ParticipantPostDTO participantPostDTO) {

        // convert API user to internal representation
        Participant participantInput = DTOMapper.INSTANCE.convertParticipantPostDTOtoEntity(participantPostDTO);

        // create participant
        Participant createdParticipant = participantService.createParticipant(participantInput);
    }
}
