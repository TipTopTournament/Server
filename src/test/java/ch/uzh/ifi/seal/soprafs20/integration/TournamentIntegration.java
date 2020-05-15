package ch.uzh.ifi.seal.soprafs20.integration;

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
    private Leaderboard leaderboard1;
    private Leaderboard leaderboard2;
    private List<Leaderboard> wholeLeaderboard;

    @InjectMocks
    TournamentService tournamentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testTournament1 = new Tournament();
        testParticipant1 = new Participant();
        testParticipant2 = new Participant();
        leaderboard1 = new Leaderboard();
        leaderboard2 = new Leaderboard();
        wholeLeaderboard = new ArrayList<>();

        testTournament1.setAmountOfPlayers(4);
        testTournament1.setBreakDuration(10);
        testTournament1.setGameDuration(15);
        testTournament1.setTournamentCode("TEST1");
        testTournament1.setInformationBox("INFO1");
        testTournament1.setLocation("TESTLOCATION1");
        testTournament1.setNumberTables(4);
        testTournament1.setStartTime("12:00");
        testTournament1.setTournamentName("NAME1");

        testParticipant1.setVorname("Fabio");
        testParticipant1.setNachname("Sisi");
        testParticipant1.setPassword("ferrari");
        testParticipant1.setLicenseNumber("112233");

        testParticipant2.setVorname("Stefano");
        testParticipant2.setNachname("Anzolut");
        testParticipant2.setPassword("banana");
        testParticipant2.setLicenseNumber("123456");

        leaderboard1.setParticipant(testParticipant1);
        leaderboard1.setWins(0);
        leaderboard1.setLosses(0);
        leaderboard1.setPointsScored(0);
        leaderboard1.setPointsConceded(0);
        leaderboard1.setPlayerState(PlayerState.ACTIVE);
        leaderboard1.setTournamentCode("12345678");

        leaderboard2.setParticipant(testParticipant1);
        leaderboard2.setWins(0);
        leaderboard2.setLosses(0);
        leaderboard2.setPointsScored(0);
        leaderboard2.setPointsConceded(0);
        leaderboard2.setPlayerState(PlayerState.ACTIVE);
        leaderboard2.setTournamentCode("12345678");

        wholeLeaderboard.add(leaderboard1);
        wholeLeaderboard.add(leaderboard2);
    }

    @Test
    public void addGamesToBracket() {
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
    public void checkTimes() {
        String tournamentCode = "12345678";

        Bracket bracket = tournamentService.createBracket(4, tournamentCode, 1, "10:00", 10, 20);

        // Check if times are correct

        assertEquals("10:00", bracket.getBracketList().get(0).getStartTime());
        assertEquals("10:30", bracket.getBracketList().get(1).getStartTime());
        assertEquals("11:00", bracket.getBracketList().get(2).getStartTime());
    }

    @Test
    public void addPlayerToLeaderboard() {
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
    public void addBracketToTournament() {

        Tournament createdTournament = tournamentService.createTournament(testTournament1);

        assertNotNull(createdTournament.getBracket());
        assertEquals(3, createdTournament.getBracket().getBracketList().size());
    }

    @Test
    public void addLeaderBoardToTournament() {

        Tournament createdTournament = tournamentService.createTournament(testTournament1);

        // at this point the leaderboard is empty and should therefore return an empty list
        assertEquals(new ArrayList<>(), tournamentService.getLeaderboardFromTournament(createdTournament.getTournamentCode()));
    }
}
