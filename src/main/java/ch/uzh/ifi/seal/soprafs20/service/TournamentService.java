package ch.uzh.ifi.seal.soprafs20.service;

import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import ch.uzh.ifi.seal.soprafs20.constant.GameState;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.transaction.Transactional;

@Service
@Transactional
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;
    private final BracketRepository bracketRepository;
    private final LeaderboardRepository leaderboardRepository;

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
        tournament.setBracket(createBracket(tournament.getAmountOfPlayers(),
                                            tournament.getTournamentCode(),
                                            tournament.getNumberTables(),
                                            tournament.getStartTime(),
                                            tournament.getBreakDuration(),
                                            tournament.getGameDuration()));

        // Leaderboard is generated
        tournament.setLeaderboard(createLeaderboard(tournament.getAmountOfPlayers()));

        // Tournament is saved
        tournamentRepository.save(tournament);
        tournamentRepository.flush();
        return tournament;
    }

    public Bracket createBracket(int numberOfPlayers, String tournamentCode, int tables, String startTime, float breatTime, float gameTime) {
        List<Game> newGames = new ArrayList<>();
        // create new Bracket
        Bracket bracket = new Bracket();

        // Insert All games
        for (int i = 1; i <= numberOfPlayers - 1; i++) {
            Game newGame = new Game();
            newGame.setTournamentCode(tournamentCode);

            newGames.add(newGame);

            gameRepository.save(newGame);
            gameRepository.flush();
        }

        calculateTimes(newGames, startTime, breatTime, gameTime, tables);

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
        for (int i = 0; i < numberOfPlayers; i++) {
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
                    updateBracket(tournamentRepository.findByTournamentCode(tournamentCode));
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

    public void updateGameAsManager(String tournamentCode, long gameId, int score1, int score2) {

        Game game = gameRepository.findByGameId(gameId);
        Leaderboard leaderboard = tournamentRepository.findByTournamentCode(tournamentCode).getLeaderboard();

        game.setScore1(score1);
        game.setScore2(score2);
        game.setGameState(GameState.FINISHED);

        gameRepository.save(game);
        gameRepository.flush();

        updateBracket(tournamentRepository.findByTournamentCode(tournamentCode));
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
        updateBracket(tournament);
    }

    public void updateBracket(Tournament tournament) {
        List<Game> gameList = tournament.getBracket().getBracketList();
        // jaaaa ich weiss isch hardcoded, aber bis mer en besseri lösig findet
        switch (gameList.size()) {

            case 1:
                if (gameList.get(0).getGameState() == GameState.FINISHED && tournament.getWinner() == null) {
                    tournament.setWinner(calculateWinner(gameList.get(0)));
                }
                break;
            case 3:
                if (gameList.get(0).getGameState() == GameState.FINISHED && gameList.get(2).getParticipant1() == null) {
                    gameList.get(2).setParticipant1(calculateWinner(gameList.get(0)));
                }
                if (gameList.get(1).getGameState() == GameState.FINISHED && gameList.get(2).getParticipant2() == null) {
                    gameList.get(2).setParticipant2(calculateWinner(gameList.get(1)));
                }
                if (gameList.get(2).getGameState() == GameState.FINISHED && tournament.getWinner() == null) {
                    tournament.setWinner(calculateWinner(gameList.get(2)));
                }
                break;
            case 7:
                if (gameList.get(0).getGameState() == GameState.FINISHED && gameList.get(4).getParticipant1() == null) {
                    gameList.get(4).setParticipant1(calculateWinner(gameList.get(0)));
                }
                if (gameList.get(1).getGameState() == GameState.FINISHED && gameList.get(4).getParticipant2() == null) {
                    gameList.get(4).setParticipant2(calculateWinner(gameList.get(1)));
                }
                if (gameList.get(2).getGameState() == GameState.FINISHED && gameList.get(5).getParticipant1() == null) {
                    gameList.get(5).setParticipant1(calculateWinner(gameList.get(2)));
                }
                if (gameList.get(3).getGameState() == GameState.FINISHED && gameList.get(5).getParticipant2() == null) {
                    gameList.get(5).setParticipant2(calculateWinner(gameList.get(3)));
                }
                if (gameList.get(4).getGameState() == GameState.FINISHED && gameList.get(6).getParticipant1() == null) {
                    gameList.get(6).setParticipant1(calculateWinner(gameList.get(4)));
                }
                if (gameList.get(5).getGameState() == GameState.FINISHED && gameList.get(6).getParticipant2() == null) {
                    gameList.get(6).setParticipant2(calculateWinner(gameList.get(5)));
                }
                if (gameList.get(5).getGameState() == GameState.FINISHED && tournament.getWinner() == null) {
                    gameList.get(6).setParticipant2(calculateWinner(gameList.get(5)));
                }
                if (gameList.get(6).getGameState() == GameState.FINISHED && tournament.getWinner() == null) {
                    tournament.setWinner(calculateWinner(gameList.get(6)));
                }
                break;
            case 15:
                // game 0
                if (gameList.get(0).getGameState() == GameState.FINISHED && gameList.get(8).getParticipant1() == null) {
                    gameList.get(8).setParticipant1(calculateWinner(gameList.get(0)));
                }
                // game 1
                if (gameList.get(1).getGameState() == GameState.FINISHED && gameList.get(8).getParticipant2() == null) {
                    gameList.get(8).setParticipant2(calculateWinner(gameList.get(1)));
                }
                // game 2
                if (gameList.get(2).getGameState() == GameState.FINISHED && gameList.get(9).getParticipant1() == null) {
                    gameList.get(9).setParticipant1(calculateWinner(gameList.get(2)));
                }
                // game 3
                if (gameList.get(3).getGameState() == GameState.FINISHED && gameList.get(9).getParticipant2() == null) {
                    gameList.get(9).setParticipant2(calculateWinner(gameList.get(3)));
                }
                // game 4
                if (gameList.get(4).getGameState() == GameState.FINISHED && gameList.get(10).getParticipant1() == null) {
                    gameList.get(10).setParticipant1(calculateWinner(gameList.get(4)));
                }
                // game 5
                if (gameList.get(5).getGameState() == GameState.FINISHED && gameList.get(10).getParticipant2() == null) {
                    gameList.get(10).setParticipant2(calculateWinner(gameList.get(5)));
                }
                // game 6
                if (gameList.get(6).getGameState() == GameState.FINISHED && gameList.get(11).getParticipant1() == null) {
                    gameList.get(11).setParticipant1(calculateWinner(gameList.get(6)));
                }
                // game 7
                if (gameList.get(7).getGameState() == GameState.FINISHED && gameList.get(11).getParticipant2() == null) {
                    gameList.get(11).setParticipant2(calculateWinner(gameList.get(7)));
                }
                // game 8
                if (gameList.get(8).getGameState() == GameState.FINISHED && gameList.get(12).getParticipant1() == null) {
                    gameList.get(12).setParticipant1(calculateWinner(gameList.get(8)));
                }
                // game 9
                if (gameList.get(9).getGameState() == GameState.FINISHED && gameList.get(12).getParticipant2() == null) {
                    gameList.get(12).setParticipant2(calculateWinner(gameList.get(9)));
                }
                // game 10
                if (gameList.get(10).getGameState() == GameState.FINISHED && gameList.get(13).getParticipant1() == null) {
                    gameList.get(13).setParticipant1(calculateWinner(gameList.get(10)));
                }
                // game 11
                if (gameList.get(11).getGameState() == GameState.FINISHED && gameList.get(13).getParticipant2() == null) {
                    gameList.get(13).setParticipant2(calculateWinner(gameList.get(11)));
                }
                // game 12
                if (gameList.get(12).getGameState() == GameState.FINISHED && gameList.get(14).getParticipant1() == null) {
                    gameList.get(14).setParticipant1(calculateWinner(gameList.get(12)));
                }
                // game 13
                if (gameList.get(13).getGameState() == GameState.FINISHED && gameList.get(14).getParticipant2() == null) {
                    gameList.get(14).setParticipant2(calculateWinner(gameList.get(13)));
                }
                // game 14 // final
                if (gameList.get(14).getGameState() == GameState.FINISHED && tournament.getWinner() == null) {
                    tournament.setWinner(calculateWinner(gameList.get(14)));
                }
                break;
            default:
        }
    }


    //helpers
    public static Participant calculateWinner(Game game) {
        if (game.getScore1() > game.getScore2()) {
            return game.getParticipant1();
        }
        else {
            return game.getParticipant2();
        }
    }

    public static String generateTournamentCode() {

        String number = "0123456789";

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {

            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(number.length());
            char rndChar = number.charAt(rndCharAt);

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

    private void calculateTimes(List<Game> gameList, String startTime, float breakTime, float gameTime, int tables) {
        LocalTime start = LocalTime.parse(startTime);

        switch (gameList.size()){

            case 1:
                gameList.get(0).setStartTime(startTime);
                break;
            case 3:
                // round1
                start = calculateRounds(gameList.subList(0, 2), start, (long) breakTime + (long) gameTime, tables);
                // round 2
                gameList.get(2).setStartTime(start.toString());
                break;
            case 7:
                // round1
                start = calculateRounds(gameList.subList(0, 4), start, (long) breakTime + (long) gameTime, tables);

                // round 2
                start = calculateRounds(gameList.subList(4, 6), start, (long) breakTime + (long) gameTime, tables);

                //round 3
                gameList.get(6).setStartTime(start.toString());
                break;
            case 15:
                // round1
                start = calculateRounds(gameList.subList(0, 8), start, (long) breakTime + (long) gameTime, tables);

                // round 2
                start = calculateRounds(gameList.subList(8, 12), start, (long) breakTime + (long) gameTime, tables);

                // round 3
                start = calculateRounds(gameList.subList(12, 14), start, (long) breakTime + (long) gameTime, tables);

                // round 4
                gameList.get(14).setStartTime(start.toString());
                break;
        }
    }

    private LocalTime calculateRounds(List<Game> gameList, LocalTime startTime, long addedTime, int tables) {
        if (tables >= gameList.size()) {
            for(Game game : gameList) {
                game.setStartTime(startTime.toString());
            }
        }
        else {
            int counter = 0;
            for (Game game : gameList) {
                game.setStartTime(startTime.toString());
                counter++;
                if (counter == tables) {
                    counter = 0;
                    startTime = startTime.plusMinutes(addedTime);
                }
            }
        }
        if (tables >= gameList.size()) {
            return startTime.plusMinutes(addedTime);
        }
        else if (gameList.size() % tables == 0) {
            return startTime;
        }
        else {
            return startTime.plusMinutes(addedTime);
        }
    }
}
