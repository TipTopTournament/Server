package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.TournamentGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.TournamentPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
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

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
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
}
