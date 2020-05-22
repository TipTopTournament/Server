package ch.uzh.ifi.seal.soprafs20.integration;

import ch.uzh.ifi.seal.soprafs20.constant.GameState;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerState;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.BracketRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LeaderboardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TournamentRepository;
import ch.uzh.ifi.seal.soprafs20.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TournamentIntegration {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private BracketRepository bracketRepository;

    @Mock
    private LeaderboardRepository leaderboardRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    private Tournament testTournament1;
    private Participant testParticipant1;
    private Participant testParticipant2;
    private Statistics testStats1;
    private Statistics testStats2;
    private Leaderboard leaderboard1;
    private Leaderboard leaderboard2;
    private Leaderboard newleaderboard1;
    private Leaderboard newleaderboard2;
    private List<Leaderboard> wholeLeaderboard;
    private List<Leaderboard> newWholeLeaderboard;

    @InjectMocks
    TournamentService tournamentService;

    @BeforeEach
     void setup() {
        MockitoAnnotations.initMocks(this);

        testTournament1 = new Tournament();
        testParticipant1 = new Participant();
        testParticipant2 = new Participant();
        testStats1 = new Statistics();
        testStats2 = new Statistics();
        leaderboard1 = new Leaderboard();
        leaderboard2 = new Leaderboard();
        newleaderboard1 = new Leaderboard();
        newleaderboard2 = new Leaderboard();
        wholeLeaderboard = new ArrayList<>();
        newWholeLeaderboard = new ArrayList<>();

        testStats1.setStatisticsID(7L);
        testStats1.setWins(2);
        testStats1.setLosses(3);
        testStats1.setPointsScored(14);
        testStats1.setPointsConceded(69);

        testStats2.setStatisticsID(11L);
        testStats2.setWins(2);
        testStats2.setLosses(3);
        testStats2.setPointsScored(14);
        testStats2.setPointsConceded(69);

        testTournament1.setAmountOfPlayers(4);
        testTournament1.setBreakDuration(10);
        testTournament1.setGameDuration(15);
        testTournament1.setTournamentCode("TEST1");
        testTournament1.setInformationBox("INFO1");
        testTournament1.setLocation("TESTLOCATION1");
        testTournament1.setNumberTables(4);
        testTournament1.setStartTime("12:00");
        testTournament1.setTournamentName("NAME1");

        testParticipant1.setParticipantID(1L);
        testParticipant1.setVorname("Fabio");
        testParticipant1.setNachname("Sisi");
        testParticipant1.setPassword("ferrari");
        testParticipant1.setLicenseNumber("112233");
        testParticipant1.setStatistics(testStats1);

        testParticipant2.setParticipantID(2L);
        testParticipant2.setVorname("Stefano");
        testParticipant2.setNachname("Anzolut");
        testParticipant2.setPassword("banana");
        testParticipant2.setLicenseNumber("123456");
        testParticipant2.setStatistics(testStats2);

        leaderboard1.setParticipant(testParticipant1);
        leaderboard1.setWins(0);
        leaderboard1.setLosses(0);
        leaderboard1.setPointsScored(0);
        leaderboard1.setPointsConceded(0);
        leaderboard1.setPlayerState(PlayerState.ACTIVE);
        leaderboard1.setTournamentCode("12345678");

        newleaderboard1.setParticipant(testParticipant1);
        newleaderboard1.setWins(1);
        newleaderboard1.setLosses(0);
        newleaderboard1.setPointsScored(3);
        newleaderboard1.setPointsConceded(1);
        newleaderboard1.setPlayerState(PlayerState.ACTIVE);
        newleaderboard1.setTournamentCode("12345678");

        leaderboard2.setParticipant(testParticipant1);
        leaderboard2.setWins(0);
        leaderboard2.setLosses(0);
        leaderboard2.setPointsScored(0);
        leaderboard2.setPointsConceded(0);
        leaderboard2.setPlayerState(PlayerState.ACTIVE);
        leaderboard2.setTournamentCode("12345678");

        newleaderboard2.setParticipant(testParticipant1);
        newleaderboard2.setWins(0);
        newleaderboard2.setLosses(1);
        newleaderboard2.setPointsScored(1);
        newleaderboard2.setPointsConceded(3);
        newleaderboard2.setPlayerState(PlayerState.ACTIVE);
        newleaderboard2.setTournamentCode("12345678");

        wholeLeaderboard.add(leaderboard1);
        wholeLeaderboard.add(leaderboard2);

        newWholeLeaderboard.add(newleaderboard1);
        newWholeLeaderboard.add(newleaderboard2);
    }

    @Test
     void addGamesToBracket() {
        List<Integer> differentPlayers = new ArrayList<Integer>();
        differentPlayers.add(2);
        differentPlayers.add(4);
        differentPlayers.add(8);
        differentPlayers.add(16);

        String tournamentCode = "12345678";

        // for all amounts of players

        for(int i : differentPlayers) {
            Bracket bracket = tournamentService.createBracket(i, tournamentCode, 8, "10:00", 10, 20);

            assertEquals(i-1, bracket.getBracketList().size());

            for (Game game : bracket.getBracketList()) {
                assertEquals(tournamentCode, game.getTournamentCode());
            }
        }
    }

    @Test
     void checkTimes() {
        String tournamentCode = "12345678";

        Bracket bracket = tournamentService.createBracket(4, tournamentCode, 1, "10:00", 10, 20);

        // Check if times are correct

        assertEquals("10:00", bracket.getBracketList().get(0).getStartTime());
        assertEquals("10:30", bracket.getBracketList().get(1).getStartTime());
        assertEquals("11:00", bracket.getBracketList().get(2).getStartTime());
    }

    @Test
     void addPlayerToLeaderboard() {
        tournamentService.createLeaderboardEntry(testParticipant1, testTournament1);
        tournamentService.createLeaderboardEntry(testParticipant2, testTournament1);

        Mockito.when(leaderboardRepository.findAllByTournamentCode(Mockito.any())).thenReturn(wholeLeaderboard);

        assertEquals(2, tournamentService.getLeaderboardFromTournament(testTournament1.getTournamentCode()).size());

        // check stats for new participants, since they should have 0 wins etc in a new tournament

        for (Leaderboard leaderboard : tournamentService.getLeaderboardFromTournament(testTournament1.getTournamentCode())) {
            assertEquals(0, leaderboard.getWins());
            assertEquals(0, leaderboard.getLosses());
            assertEquals(0, leaderboard.getPointsScored());
            assertEquals(0, leaderboard.getPointsConceded());
            assertEquals("12345678", leaderboard.getTournamentCode());
            assertEquals(PlayerState.ACTIVE, leaderboard.getPlayerState());
            assertNotNull(leaderboard.getParticipant());
            assertNotNull(leaderboard.getLeaderboardId());
        }
    }

    @Test
     void addBracketToTournament() {

        Tournament createdTournament = tournamentService.createTournament(testTournament1);

        assertNotNull(createdTournament.getBracket());
        assertEquals(3, createdTournament.getBracket().getBracketList().size());
    }

    @Test
     void addLeaderBoardToTournament() {

        Tournament createdTournament = tournamentService.createTournament(testTournament1);

        // at this point the leaderboard is empty and should therefore return an empty list
        assertEquals(new ArrayList<>(), tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()));
    }

    @Test
     void playersReportScores() {

        Tournament createdTournament = tournamentService.createTournament(testTournament1);

        Game firstGame = createdTournament.getBracket().getBracketList().get(0);



        // user 1 joins
        tournamentService.updateBracketWithNewParticipant(testParticipant1, createdTournament);
        tournamentService.createLeaderboardEntry(testParticipant1, createdTournament);

        // user 2 joins
        tournamentService.updateBracketWithNewParticipant(testParticipant2, createdTournament);
        tournamentService.createLeaderboardEntry(testParticipant2, createdTournament);

        // check if they are playing against each other
        assertEquals(testParticipant1, firstGame.getParticipant1());
        assertEquals(testParticipant2, firstGame.getParticipant2());

        // player1 reports their scores
        Mockito.when(gameRepository.findByGameId(Mockito.anyLong())).thenReturn(firstGame);
        Mockito.when(tournamentRepository.findByTournamentCode(Mockito.any())).thenReturn(testTournament1);
        tournamentService.updateGameWithScore(createdTournament.getTournamentCode(), firstGame.getGameId(), 3, 1, testParticipant1.getParticipantID());

        // check for the first entry status update
        assertEquals(GameState.FIRSTENTRY, firstGame.getGameState());
        assertTrue(firstGame.isParticipant1Reported());

        // player2 reports their scores
        tournamentService.updateGameWithScore(createdTournament.getTournamentCode(), firstGame.getGameId(), 3, 1, testParticipant2.getParticipantID());

        // check the game status
        assertEquals(GameState.FINISHED, firstGame.getGameState());
        assertEquals(3, firstGame.getScore1());
        assertEquals(1, firstGame.getScore2());
        assertTrue(firstGame.isParticipant2Reported());

        // check the leaderboard
        Mockito.when(leaderboardRepository.findAllByTournamentCode(Mockito.any())).thenReturn(newWholeLeaderboard);
        //wins
        assertEquals(1, tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()).get(0).getWins());
        assertEquals(0, tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()).get(1).getWins());
        //losses
        assertEquals(0, tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()).get(0).getLosses());
        assertEquals(1, tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()).get(1).getLosses());
        //points scored
        assertEquals(3, tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()).get(0).getPointsScored());
        assertEquals(1, tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()).get(1).getPointsScored());
        //points conceded
        assertEquals(1, tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()).get(0).getPointsConceded());
        assertEquals(3, tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()).get(1).getPointsConceded());
    }
}
