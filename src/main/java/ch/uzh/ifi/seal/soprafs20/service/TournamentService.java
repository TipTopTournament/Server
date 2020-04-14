package ch.uzh.ifi.seal.soprafs20.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs20.entity.Bracket;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.repository.BracketRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.ParticipantRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@Transactional
public class TournamentService {

    private final Logger log = LoggerFactory.getLogger(TournamentService.class);

    private TournamentRepository tournamentRepository;
    private GameRepository gameRepository;
    private BracketRepository bracketRepository;

    @Autowired
    public TournamentService(@Qualifier("tournamentRepository")TournamentRepository tournamentRepository,
                             @Qualifier("gameRepository") GameRepository gameRepository,
                             @Qualifier("bracketRepository") BracketRepository bracketRepository) {
        this.tournamentRepository = tournamentRepository;
        this.gameRepository = gameRepository;
        this.bracketRepository = bracketRepository;
    }


    // create methods
    public Tournament createTournament(Tournament tournament){

        // 8-Character Tournament Code is generated
        tournament.setTournamentCode(generateTournamentCode());

        // Bracket is generated
        tournament.setBracket(createBracket(tournament.getAmountOfPlayers(), tournament.getTournamentCode()));

        // Leaderboard is generated


        // Tournament is saved
        tournamentRepository.save(tournament);
        tournamentRepository.flush();

        log.debug("Tournament created");
        return tournament;
    }

    public Bracket createBracket(int numberOfPlayers, String tournamentCode) {
        List<Game> newGames = new ArrayList<>();
        // create new Bracket
        Bracket bracket = new Bracket();

        // Insert All games
        for (int i = 1; i <= numberOfPlayers -1; i++) {
            Game newGame = new Game();
            newGame.setTournamentCode(tournamentCode);
            newGames.add(newGame);
            gameRepository.save(newGame);
            gameRepository.flush();
        }

        // add the games to the bracket
        bracket.setBracketList(newGames);

        // save the bracket
        bracketRepository.save(bracket);
        bracketRepository.flush();

        return bracket;
    }

    // get methods
    public Tournament getTournamentByTournamentCode(String tournamentCode) {
        return tournamentRepository.findByTournamentCode(tournamentCode);
    }

    public List<Game> getBracketByTournamentCode(String tournamentCode) {
        Tournament tournament = tournamentRepository.findByTournamentCode(tournamentCode);

        return tournament.getBracket().getBracketList();
    }

    public List<Tournament> getAllTournaments() {
        return this.tournamentRepository.findAll();
    }

    // check methods
    public boolean checkIfTournamentCodeExists(String tournamentCode) {
        Tournament newTournament = tournamentRepository.findByTournamentCode(tournamentCode);

        return newTournament != null;
    }

    // update methods
    public void updateBracketWithNewParticipant(Participant participant, Tournament tournament) {
        tournament.activePlayers.add(participant);

        // get the right bracket
        Bracket bracket = tournament.getBracket();

        // update games in bracket
        for (Game game : bracket.getBracketList()) {
            if (game.getParticipant1() == participant || game.getParticipant2() == participant) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Participant is already in the tournament");
            }
            else if (game.getParticipant1() == null) {
                game.setParticipant1(participant);
                gameRepository.save(game);
                gameRepository.flush();
                break;
            }
            else if (game.getParticipant2() == null) {
                game.setParticipant2(participant);
                gameRepository.save(game);
                gameRepository.flush();
                break;
            }
        }
        bracketRepository.save(bracket);
        bracketRepository.flush();
    }

    //helpers
    public static String generateTournamentCode() {

        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";

        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {

            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);
        }
        return sb.toString();

    }




}
