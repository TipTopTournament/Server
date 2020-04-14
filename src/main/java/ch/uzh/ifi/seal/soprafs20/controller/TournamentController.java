package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.TournamentGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.TournamentPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ParticipantService;
import ch.uzh.ifi.seal.soprafs20.service.TournamentService;
import org.apache.coyote.Response;
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

    public TournamentController(TournamentService tournamentService, ParticipantService participantService) {
        this.tournamentService = tournamentService;
        this.participantService = participantService;
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

        // convert API user to internal representation
        Tournament tournamentInput = DTOMapper.INSTANCE.convertiTournamentPostDTOtoEntity(tournamentPostDTO);

        if (tournamentInput.getAmountOfPlayers() > 16) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Zu viele Teilnehmen!");
        }
        // create participant
        Tournament createdTournament = tournamentService.createTournament(tournamentInput);

        return createdTournament.getTournamentCode();
    }

    @GetMapping("/tournaments/{tournamentCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TournamentGetDTO getTournamentGetDTO(@PathVariable("tournamentCode")  String tournamentCode) {

        // Check if tournament exists
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tournament with such a code exists.");
        }

        return DTOMapper.INSTANCE.convertEntityToTournamentGetDTO(tournamentService.getTournamentByTournamentCode(tournamentCode));
    }

    @GetMapping("/tournaments/{tournamentCode}/bracket")
    @Query
    public List<GameGetDTO> getAllGamesOfTournament(@PathVariable("tournamentCode")  String tournamentCode) {

        List<GameGetDTO> allGamesGetDTO = new ArrayList<>();
        // Check if tournament code exists
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tournament with such a code exists.");
        }
        // return all the games in the bracket
        List<Game> allGames =  tournamentService.getBracketByTournamentCode(tournamentCode);

        for (Game game : allGames) {
            allGamesGetDTO.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }

        return allGamesGetDTO;
    }
    /*
    @GetMapping("/tournaments/{tournamentCode}/leaderboard")
    @Query
    public List<List<Object>> getLeaderBoardOfTournament(@PathVariable("tournamentCode")  String tournamentCode) {

        // Check if tournament code exists
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tournament with such a code exists.");
        }

        Tournament tournament = tournamentService.getTournamentByTournamentCode(tournamentCode);

        return tournament.getLeaderboard().getLeaderboardList();

    }
    */
    @PutMapping("/tournaments/{tournamentCode}/{participantId}")
    @ResponseBody
    public void userJoinsTournament(@PathVariable("tournamentCode") String tournamentCode,
                                    @PathVariable("participantId") long participantId) {

        // if tournament does not exist, error
        if (!tournamentService.checkIfTournamentCodeExists(tournamentCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tournament with such a code exists.");
        }

        // if user id is not valid, e.g. user does not exist
        if (!participantService.checkIfParticipantIdExists(participantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such an ID exists.");
        }

        Participant participant = participantService.getParticipantById(participantId);

        Tournament tournament = tournamentService.getTournamentByTournamentCode(tournamentCode);

        tournamentService.updateBracketWithNewParticipant(participant, tournament);
    }
}
