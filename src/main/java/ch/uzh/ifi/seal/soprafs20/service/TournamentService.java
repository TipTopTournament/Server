package ch.uzh.ifi.seal.soprafs20.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs20.constant.GameState;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;

@Service
@Transactional
public class TournamentService {

    private final Logger log = LoggerFactory.getLogger(TournamentService.class);

    private TournamentRepository tournamentRepository;
    private GameRepository gameRepository;
    private BracketRepository bracketRepository;
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    public TournamentService(@Qualifier("tournamentRepository")TournamentRepository tournamentRepository,
                             @Qualifier("gameRepository") GameRepository gameRepository,
                             @Qualifier("bracketRepository") BracketRepository bracketRepository,
                             @Qualifier("leaderboardRepository") LeaderboardRepository leaderboardRepository) {
        this.tournamentRepository = tournamentRepository;
        this.gameRepository = gameRepository;
        this.bracketRepository = bracketRepository;
        this.leaderboardRepository = leaderboardRepository;
    }


    // create methods
    public Tournament createTournament(Tournament tournament){

        // 8-Character Tournament Code is generated
        tournament.setTournamentCode(generateTournamentCode());

        // Bracket is generated
        tournament.setBracket(createBracket(tournament.getAmountOfPlayers(), tournament.getTournamentCode()));

        // Leaderboard is generated
        tournament.setLeaderboard(createLeaderboard(tournament.getAmountOfPlayers()));

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

    public Leaderboard createLeaderboard(int numberOfPlayers) {
        Leaderboard leaderboard = new Leaderboard();

        // wins are stored in a string because of jpa being müehsam
        List<Integer> leaderB = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers - 1; i++) {
            leaderB.add(0);
        }

        leaderboard.setWins(leaderB);

        // save the leaderboard
        leaderboardRepository.save(leaderboard);
        leaderboardRepository.flush();

        return leaderboard;
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

    public boolean checkIfParticipantIsInLeaderboard(String licensenumber, List<Participant> liste) {
        for (Participant participant : liste) {
            if (participant.getLicenseNumber().equals(licensenumber)) {
                return true;
            }
        }
        return false;
    }

    // update methods
    public void updateBracketWithNewParticipant(Participant participant, Tournament tournament) {
        if (tournament.getLeaderboard().getLeaderboardList().size() == tournament.getAmountOfPlayers()) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Tournament is already full.");
        }

        // add player to the leaderboard
        // get the right leaderboard
        Leaderboard leaderboard = tournament.getLeaderboard();

        // check if participant is already in there
        if (checkIfParticipantIsInLeaderboard(participant.getLicenseNumber(), leaderboard.getLeaderboardList())) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        leaderboard.addParticipant(participant);
        leaderboardRepository.save(leaderboard);
        leaderboardRepository.flush();

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

    public void updateGameWithScore(String tournamentCode, long gameId, int score1, int score2) {

        Game game = gameRepository.findByGameId(gameId);

        // update the score of the game
        if (game != null) {
            // check if first entry
            if (game.getGameState() == GameState.NOTREADY) {
                game.setScore1(score1);
                game.setScore2(score2);
                game.setGameState(GameState.FIRSTENTRY);
            }

            else if (game.getGameState() == GameState.FIRSTENTRY) {

                // check if the score is different
                if (game.getScore1() != score1 || game.getScore2() != score2) {
                    game.setGameState(GameState.CONFLICT);
                }
                else {
                    game.setGameState(GameState.FINISHED);
                }
            }
            gameRepository.save(game);
            gameRepository.flush();
        }
        // update the leaderboard if there is a winner e.g. there is no conflict
        if (game.getGameState() == GameState.FINISHED) {

            Leaderboard leaderboard = tournamentRepository.findByTournamentCode(tournamentCode).getLeaderboard();

            if (game.getScore1() > game.getScore2()) {
                // Participant 1 wins
                int position = getPositionFromLeaderboard(game.getParticipant1().getParticipantID(), leaderboard);

                leaderboard.setWins(updateWins(leaderboard.getWins(), position));
            }
            else {
                // Participant 2 wins
                int position = getPositionFromLeaderboard(game.getParticipant2().getParticipantID(), leaderboard);

                leaderboard.setWins(updateWins(leaderboard.getWins(), position));
            }

            leaderboardRepository.save(leaderboard);
            leaderboardRepository.flush();
        }
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

    public static int getPositionFromLeaderboard(long participantId, Leaderboard leaderboard) {
        for (int i = 0; i < leaderboard.getLeaderboardList().size() - 1; i++) {
            if (leaderboard.getLeaderboardList().get(i).getParticipantID() == participantId) {
                return i;
            }
        }
        return 0;
    }

    public static List<Integer> updateWins(List<Integer> oldList, int position) {
        oldList.set(position, oldList.get(position) + 1);
        return oldList;
    }
}
