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
        Leaderboard leaderboard = tournamentRepository.findByTournamentCode(tournamentCode).getLeaderboard();

        // update the score of the game
        if (game != null && game.getGameState() == GameState.FINISHED) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Game score has already been set and is locked.");
        }
        else if (game != null) {
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
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found.");
        }
        // update the leaderboard if there is a winner e.g. there is no conflict

        updateLeaderboardWithGame(game, leaderboard);
    }

    public void updateBracketAfterUserLeft(Participant participant, Tournament tournament) {

        Bracket bracket = tournament.getBracket();
        Leaderboard leaderboard = tournament.getLeaderboard();

        for (Game game : bracket.getBracketList()) {
            // go through all the games and find the one which the participant is part of and is not finished
            if (game.getParticipant1() != null && game.getParticipant2() != null) {
                if ((game.getParticipant1().getParticipantID().equals(participant.getParticipantID())
                        || game.getParticipant2().getParticipantID().equals(participant.getParticipantID()))
                        && game.getGameState() != GameState.FINISHED) {

                    gameForfait(game, participant, leaderboard);
                }
            }
        }
    }


    //helpers
    public static String generateTournamentCode() {

        String NUMBER = "0123456789";

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {

            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(NUMBER.length());
            char rndChar = NUMBER.charAt(rndCharAt);

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

    public void gameForfait(Game game, Participant leaver, Leaderboard leaderboard) {

        // if it is participant 1
        if (game.getParticipant1().getParticipantID().equals(leaver.getParticipantID())) {
            game.setScore1(0);
            game.setScore2(3);
            game.setGameState(GameState.FINISHED);
        }
        // if it is participant 2
        else if (game.getParticipant2().getParticipantID().equals(leaver.getParticipantID())) {
            game.setScore2(0);
            game.setScore1(3);
            game.setGameState(GameState.FINISHED);
        }

        this.updateLeaderboardWithGame(game, leaderboard);
    }

    public void updateLeaderboardWithGame(Game game, Leaderboard leaderboard) {
        // update the leaderboard if there is a winner e.g. there is no conflict
        if (game.getGameState() == GameState.FINISHED) {

            if (game.getScore1() > game.getScore2()) {
                // Participant 1 wins
                int position = getPositionFromLeaderboard(game.getParticipant1().getParticipantID(), leaderboard);

                updatePlayerStats(game, game.getParticipant1(), game.getParticipant2());

                leaderboard.setWins(updateWins(leaderboard.getWins(), position));
            }
            else {
                // Participant 2 wins
                int position = getPositionFromLeaderboard(game.getParticipant2().getParticipantID(), leaderboard);

                updatePlayerStats(game, game.getParticipant2(), game.getParticipant1());

                leaderboard.setWins(updateWins(leaderboard.getWins(), position));
            }

            leaderboardRepository.save(leaderboard);
            leaderboardRepository.flush();
        }
    }

    private void updatePlayerStats(Game game, Participant winner, Participant loser) {

        // update winner
        Statistics statsWinner = winner.getStatistics();

        statsWinner.setPointsScored(statsWinner.getPointsScored() + game.getScore1());
        statsWinner.setPointsConceded(statsWinner.getPointsConceded() + game.getScore2());
        statsWinner.setWins(statsWinner.getWins() + 1);
        statsWinner.addGameToHistory(game);

        // update loser
        Statistics statsLoser = loser.getStatistics();

        statsLoser.setPointsScored(statsLoser.getPointsScored() + game.getScore2());
        statsLoser.setPointsConceded(statsLoser.getPointsConceded() + game.getScore1());
        statsLoser.setLosses(statsLoser.getLosses() + 1);
        statsLoser.addGameToHistory(game);
    }
}
