package ch.uzh.ifi.seal.soprafs20.service;
/*
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.seal.soprafs20.constant.GameState;
import ch.uzh.ifi.seal.soprafs20.constant.TournamentState;
import ch.uzh.ifi.seal.soprafs20.entity.Bracket;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Leaderboard;
import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.repository.BracketRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LeaderboardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TournamentRepository;

class TournamentServiceTest {

	@Mock
	private TournamentRepository tournamentRepository;
	
	@Mock
    private GameRepository gameRepository;
    
	@Mock
	private BracketRepository bracketRepository;
    
	@Mock
	private LeaderboardRepository leaderboardRepository;

    @InjectMocks
    private ParticipantService participantService;
    private TournamentService tournamentService;
    
    private Participant testParticipant1;
    private Participant testParticipant2;
    private Participant testParticipant3;
    
    private Tournament testTournament1;
    private Tournament testTournament2;
       
    private Bracket testBracket1;
    private Bracket testBracket2;
    
    private Game testGame1;
    private Game testGame2;
    
    private Leaderboard testLeaderboard1;
    
    @BeforeEach
    public void setup() {
    	 MockitoAnnotations.initMocks(this);

         // given
         testParticipant1 = new Participant();
         testParticipant2 = new Participant();
         testParticipant3 = new Participant();
         
         List<Participant> dummyList1 = new ArrayList<>();

         testParticipant1.setVorname("Fabio");
         testParticipant1.setNachname("Sisi");
         testParticipant1.setPassword("ferrari");
         testParticipant1.setLicenseNumber("112233");

         testParticipant2.setVorname("Stefano");
         testParticipant2.setNachname("Anzolut");
         testParticipant2.setPassword("banana");
         testParticipant2.setLicenseNumber("123456");

         testParticipant3.setVorname("Tony");
         testParticipant3.setNachname("Ly");
         testParticipant3.setPassword("apple");
         testParticipant3.setLicenseNumber("654321");
         
         testTournament1 = new Tournament();
         testTournament2 = new Tournament();
         
         testBracket1 = new Bracket();
         testBracket2 = new Bracket();
         
         testLeaderboard1 = new Leaderboard();
         
         List<Tournament> dummyList2 = new ArrayList<>();
         
         testTournament1.setAmountOfPlayers(4);
         testTournament1.setBracket(testBracket1);
         testTournament1.setActivePlayers(dummyList1);
         testTournament1.setBreakDuration(10);
         testTournament1.setGameDuration(15);
         testTournament1.setTournamentCode("TEST1");
         testTournament1.setInformationBox("INFO1");
         testTournament1.setLeaderboard(testLeaderboard1);
         testTournament1.setLocation("TESTLOCATION1");
         testTournament1.setNumberTables(4);
         testTournament1.setStartTime("12:00");
         testTournament1.setTournamentName("NAME1");
         testTournament1.setTournamentState(TournamentState.READY);
         testTournament1.setWinner(testParticipant1);
         
         testTournament1.setAmountOfPlayers(8);
         testTournament1.setBracket(testBracket2);
         testTournament1.setActivePlayers(dummyList1);
         testTournament1.setBreakDuration(5);
         testTournament1.setGameDuration(10);
         testTournament1.setTournamentCode("TEST2");
         testTournament1.setInformationBox("INFO2");
         testTournament1.setLeaderboard(testLeaderboard1);
         testTournament1.setLocation("TESTLOCATION2");
         testTournament1.setNumberTables(4);
         testTournament1.setStartTime("22:00");
         testTournament1.setTournamentName("NAME2");
         testTournament1.setTournamentState(TournamentState.DONE);
         testTournament1.setWinner(testParticipant2);
         
         List<Game> dummyList3 = new ArrayList<>();
         
         testGame1 = new Game();
         testGame2 = new Game();
         
         testGame1.setStartTime("10:00");
         testGame1.setGameState(GameState.FINISHED);
         testGame1.setScore1(3);
         testGame1.setScore2(0);
         testGame1.setParticipant1(testParticipant1);
         testGame1.setParticipant2(testParticipant2);
         testGame1.setTournamentCode("TEST1");
         
         testGame2.setStartTime("14:00");
         testGame2.setGameState(GameState.READY);
         testGame2.setScore1(2);
         testGame2.setScore2(3);
         testGame2.setParticipant1(testParticipant3);
         testGame2.setParticipant2(testParticipant2);
         testGame2.setTournamentCode("TEST1");
         
         dummyList3.add(testGame1);
         dummyList3.add(testGame2);
         
         dummyList2.add(testTournament1);
         dummyList2.add(testTournament2);
         
         dummyList1.add(testParticipant1);
         dummyList1.add(testParticipant2);
         dummyList1.add(testParticipant3);
    }
    
    @Test
    public void updateGameWithScoreTest() {
    	
    	Mockito.when(gameRepository.findByGameId(Mockito.any())).thenReturn(testGame1);
    	Mockito.when(tournamentRepository.findByTournamentCode(Mockito.any())).thenReturn(testTournament1);
    	
    	testGame1.setGameState(GameState.FIRSTENTRY);
    	testGame1.setScore1(3);
    	testGame1.setScore1(0);
    	
    	Exception exception = assertThrows(ResponseStatusException.class, () -> {
            tournamentService.updateGameWithScore("TEST1", 1, 2, 3);
        });
    	
    }
}*/
