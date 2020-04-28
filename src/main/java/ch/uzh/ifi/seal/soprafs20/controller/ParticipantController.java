package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ParticipantService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.seal.soprafs20.constant.UserState;

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
        Participant participant = participantService.getParticipantById(id);
    	return DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(participant);
    }

    @PostMapping("/participants")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ParticipantGetDTO createParticipant(@RequestBody ParticipantPostDTO participantPostDTO) {
        // convert API user to internal representation
        Participant participantInput = DTOMapper.INSTANCE.convertParticipantPostDTOtoEntity(participantPostDTO);
        // create participant
        Participant createdParticipant = participantService.createParticipant(participantInput);
        return DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(createdParticipant);
    }

    @PutMapping("/participants/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ParticipantPutDTO loginParticipant(@RequestBody ParticipantPostDTO participantPostDTO) {
        Participant participant = DTOMapper.INSTANCE.convertParticipantPostDTOtoEntity(participantPostDTO);
        if (participantService.checkLicenseNumberAndPassword(participant.getLicenseNumber(), participant.getPassword())) {
            return DTOMapper.INSTANCE.convertEntityToParticipantPutDTO(participantService.getParticipantByLicenseNumber(participant.getLicenseNumber()));
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password were incorrect");
        }
    }
    
    @PutMapping("/participants/{participantId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateParticipantState(@RequestBody ParticipantPutDTO participantPutDTO,@PathVariable("participantId") long id) {
    	//Used to get the state
    	Participant participantState = DTOMapper.INSTANCE.convertParticipantPutDTOToEntity(participantPutDTO);
    	UserState state = participantState.getUserState();
    	//Update the Participant State
    	participantService.updateState(id,state);
    }

    @GetMapping("/participants/{participantId}/statistics")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatisticsGetDTO getStatisticFromParticipant(@PathVariable("participantId") long id) {
        StatisticsGetDTO stats = DTOMapper.INSTANCE.convertEntityToStatisticsGetDTO(participantService.getStatsByParticipantID(id));
        List<GameGetDTO> listGames = new ArrayList<>();
        for (Game game : participantService.getStatsByParticipantID(id).getHistory()) {
            GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
            listGames.add(gameGetDTO);
        }
        stats.setHistory(listGames);
        return stats;
    }
}
