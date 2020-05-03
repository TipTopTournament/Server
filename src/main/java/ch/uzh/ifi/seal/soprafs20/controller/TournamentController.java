package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserState;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ManagerService;
import ch.uzh.ifi.seal.soprafs20.service.ParticipantService;
import ch.uzh.ifi.seal.soprafs20.service.TournamentService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TournamentController {

    private final TournamentService tournamentService;
    private final ParticipantService participantService;
    private final ManagerService managerService;
    private static final String ERROR_MSG_NOT_EXISTS = "No tournament with such a code exists";
    private static final String ERROR_MSG_MANAGER = "Manager isn't valid.";

    public TournamentController(TournamentService tournamentService, ParticipantService participantService, ManagerService managerService) {
        this.tournamentService = tournamentService;
        this.participantService = participantService;
        this.managerService = managerService;
    }

    @GetMapping("/tournaments")
    @Query
    public List<TournamentGetDTO> getAllTournaments() {
        List<TournamentGetDTO> allTournaments = new ArrayList<>();

        for (Tournament tournament : tournamentService.getAllTournaments()) {
            allTournaments.add(DTOMapper.INSTANCE.convertEntityToTournamentGetDTO(tournament));
        }
        return allTournaments;
    }

    @PostMapping("/tournaments")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createTournament(@RequestBody TournamentPostDTO tournamentPostDTO){

        // check if manager is valid
        Manager manager = managerService.getManagerById(tournamentPostDTO.getManagerId());

        // convert API user to internal representation
        Tournament tournamentInput = DTOMapper.INSTANCE.convertTournamentPostDTOtoEntity(tournamentPostDTO);

        if (tournamentInput.getAmountOfPlayers() > 16) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Too many Participants!");
        }
        // create tournament
        Tournament createdTournament = tournamentService.createTournament(tournamentInput);

        managerService.addTournamentToManager(createdTournament, manager);

        return createdTournament.getTournamentCode();
    }

    @GetMapping("/tournaments/{tournamentCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TournamentGetDTO getTournamentGetDTO(@PathVariable("tournamentCode")  String tournamentCode) {

        // Check if tournament exists
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MSG_NOT_EXISTS);
        }

        return DTOMapper.INSTANCE.convertEntityToTournamentGetDTO(tournamentService.getTournamentByTournamentCode(tournamentCode));
    }

    @GetMapping("/tournaments/{tournamentCode}/bracket")
    @Query
    public List<GameGetDTO> getAllGamesOfTournament(@PathVariable("tournamentCode")  String tournamentCode) {

        List<GameGetDTO> allGamesGetDTO = new ArrayList<>();
        // Check if tournament code exists
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MSG_NOT_EXISTS);
        }
        // return all the games in the bracket
        List<Game> allGames =  tournamentService.getBracketByTournamentCode(tournamentCode);

        for (Game game : allGames) {
            allGamesGetDTO.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }

        return allGamesGetDTO;
    }

    @GetMapping("/tournaments/{tournamentCode}/leaderboard")
    @Query
    public List<LeaderboardGetDTO> getLeaderBoardOfTournament(@PathVariable("tournamentCode")  String tournamentCode) {

        List<LeaderboardGetDTO> leaderboardGetDTOList = new ArrayList<>();

        // Check if tournament code exists
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MSG_NOT_EXISTS);
        }

        Tournament tournament = tournamentService.getTournamentByTournamentCode(tournamentCode);

        for (int i = 0 ; i < tournament.getLeaderboard().getLeaderboardList().size(); i++) {
            LeaderboardGetDTO leaderboardGetDTO = new LeaderboardGetDTO();
            leaderboardGetDTO.setParticipantGetDTO(DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(tournament.getLeaderboard().getLeaderboardList().get(i)));
            leaderboardGetDTO.setWins(tournament.getLeaderboard().getWins().get(i));
            leaderboardGetDTOList.add(leaderboardGetDTO);
        }
        return leaderboardGetDTOList;
    }

    @PutMapping("/tournaments/{tournamentCode}/bracket/{gameId}")
    @ResponseBody
    public void updateGameScore(@PathVariable("tournamentCode") String tournamentCode,
                                @PathVariable("gameId") long gameId,
                                @RequestBody GamePutDTO gamePutDTO) {
        // if tournament does not exist, error
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MSG_NOT_EXISTS);
        }

        tournamentService.updateGameWithScore(tournamentCode, gameId, gamePutDTO.getScore1(), gamePutDTO.getScore2());
    }
    
    @PutMapping("/tournaments/{tournamentCode}/bracket/{gameId}/{managerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void managerUpdateScore(@PathVariable("tournamentCode") String tournamentCode,
    							   @PathVariable("gameId") long gameId,
    							   @PathVariable("managerId") long managerId,
    							   @RequestBody GamePutDTO gamePutDTO){
    	//Check if the tournament exists. If not, 
    	 if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MSG_NOT_EXISTS);
         }

        //Check if the manager exists
        if (!managerService.checkIfManagerIdExists(managerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MSG_MANAGER);
        }
    	 
    	 tournamentService.updateGameAsManager(tournamentCode,gameId,gamePutDTO.getScore1(),gamePutDTO.getScore2());
    	
    } 

    @PutMapping("/tournaments/{tournamentCode}/{participantId}")
    @ResponseBody
    public void userJoinsTournament(@PathVariable("tournamentCode") String tournamentCode,
                                    @PathVariable("participantId") long participantId) {

        // if tournament does not exist, error
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MSG_NOT_EXISTS);
        }

        // if user id is not valid, e.g. user does not exist
        if (!participantService.checkIfParticipantIdExists(participantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such an ID exists.");
        }

        Participant participant = participantService.getParticipantById(participantId);

        // at the start of the tournament the status is NOT READY as a default.
        participant.setUserState(UserState.NOTREADY);

        Tournament tournament = tournamentService.getTournamentByTournamentCode(tournamentCode);

        tournamentService.updateBracketWithNewParticipant(participant, tournament);
    }

    @PutMapping("/tournaments/{tournamentCode}/{participantId}/leave")
    @ResponseBody
    public void userLeavesTournament(@PathVariable("tournamentCode") String tournamentCode,
                                     @PathVariable("participantId") long participantId) {

        // if tournament does not exist, error
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MSG_NOT_EXISTS);
        }

        // if user id is not valid, e.g. user does not exist
        if (!participantService.checkIfParticipantIdExists(participantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such an ID exists.");
        }

        Participant participant = participantService.getParticipantById(participantId);

        Tournament tournament = tournamentService.getTournamentByTournamentCode(tournamentCode);

        // set User state to left
        participant.setUserState(UserState.LEFT);

        // update Bracket
        tournamentService.updateBracketAfterUserLeft(participant, tournament);

    }
}
