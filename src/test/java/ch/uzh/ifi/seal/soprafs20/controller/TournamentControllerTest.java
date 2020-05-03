package ch.uzh.ifi.seal.soprafs20.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.seal.soprafs20.constant.GameState;
import ch.uzh.ifi.seal.soprafs20.constant.TournamentState;
import ch.uzh.ifi.seal.soprafs20.entity.Bracket;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Leaderboard;
import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.rest.dto.TournamentPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.ManagerService;
import ch.uzh.ifi.seal.soprafs20.service.ParticipantService;
import ch.uzh.ifi.seal.soprafs20.service.TournamentService;


@WebMvcTest(TournamentController.class)
@Configuration
@ComponentScan("ch.uzh.ifi.seal.soprafs20.service")
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentRepository managerRepository;
    private GameRepository gameRepository;
    private BracketRepository bracketRepository;
    private LeaderboardRepository leaderBoardRepository;
    
    private ParticipantService participantService;
    private ManagerService managerService;
    private TournamentService tournamentService = new TournamentService(tournamentRepository, gameRepository, bracketRepository, leaderBoardRepository);
    
    private Participant testParticipant1;
    private Participant testParticipant2;
    private Participant testParticipant3;
    
    private Tournament testTournament1;
    private Tournament testTournament2;
    
    private Manager testManager1;
    private Manager testManager2;
    
    private Bracket testBracket1;
    private Bracket testBracket2;
    
    private Game testGame1;
    private Game testGame2;
    
    private Leaderboard testLeaderboard1;
    
    @BeforeEach
    private void setup() {
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
    
    /**
     * checks if all tournaments are returned -positive
     */
    @Test
    public void getAllTournamentsPositive() throws Exception{

        List<Tournament> dummyList2 = new ArrayList<>();
        dummyList2.add(testTournament1);
        dummyList2.add(testTournament2);
        
        List<Participant> dummyList1 = new ArrayList<>();
        dummyList1.add(testParticipant1);
        dummyList1.add(testParticipant2);
        dummyList1.add(testParticipant3);

        given(tournamentService.getAllTournaments()).willReturn(dummyList2);

        // mock the request
        MockHttpServletRequestBuilder getAllRequest = get("/tournaments")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getAllRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tournamentName", is(testTournament1.getTournamentName())))
                .andExpect(jsonPath("$[0].winner", is(testTournament1.getWinner())))
                .andExpect(jsonPath("$[0].location", is(testTournament1.getLocation())))
                .andExpect(jsonPath("$[0].tournamentState", is(testTournament1.getTournamentState())))
                .andExpect(jsonPath("$[0].starTime", is(testTournament1.getStartTime())))
                .andExpect(jsonPath("$[0].gameDuration", is(testTournament1.getGameDuration())))
                .andExpect(jsonPath("$[0].breakDuration", is(testTournament1.getBreakDuration())))
                .andExpect(jsonPath("$[0].tournamentCode", is(testTournament1.getTournamentCode())))
                .andExpect(jsonPath("$[0].amountOfPlayers", is(testTournament1.getAmountOfPlayers())))
                .andExpect(jsonPath("$[0].numberTables", is(testTournament1.getNumberTables())))
                .andExpect(jsonPath("$[0].informationBox", is(testTournament1.getInformationBox())))
                .andExpect(jsonPath("$[0].leaderboard", is(testTournament1.getLeaderboard())))
                .andExpect(jsonPath("$[0].bracket", is(testTournament1.getBracket())))
                .andExpect(jsonPath("$[0].activePlayers", is(testTournament1.getActivePlayers())))
		        .andExpect(jsonPath("$[1].tournamentName", is(testTournament2.getTournamentName())))
		        .andExpect(jsonPath("$[1].winner", is(testTournament2.getWinner())))
		        .andExpect(jsonPath("$[1].location", is(testTournament2.getLocation())))
		        .andExpect(jsonPath("$[1].tournamentState", is(testTournament2.getTournamentState())))
		        .andExpect(jsonPath("$[1].starTime", is(testTournament2.getStartTime())))
		        .andExpect(jsonPath("$[1].gameDuration", is(testTournament2.getGameDuration())))
		        .andExpect(jsonPath("$[1].breakDuration", is(testTournament2.getBreakDuration())))
		        .andExpect(jsonPath("$[1].tournamentCode", is(testTournament2.getTournamentCode())))
		        .andExpect(jsonPath("$[1].amountOfPlayers", is(testTournament2.getAmountOfPlayers())))
		        .andExpect(jsonPath("$[1].numberTables", is(testTournament2.getNumberTables())))
		        .andExpect(jsonPath("$[1].informationBox", is(testTournament2.getInformationBox())))
		        .andExpect(jsonPath("$[1].leaderboard", is(testTournament2.getLeaderboard())))
		        .andExpect(jsonPath("$[1].bracket", is(testTournament2.getBracket())))
		        .andExpect(jsonPath("$[1].activePlayers", is(testTournament2.getActivePlayers())));
    }
    
    /**
     * Check if the post request returns the correct status, negative
     */
    @Test
    public void createTournamentNegative() throws Exception{
    	
        TournamentPostDTO tournamentPostDTO = new TournamentPostDTO();
        tournamentPostDTO.setAmountOfPlayers(24);
        tournamentPostDTO.setBreakDuration(10);
        tournamentPostDTO.setGameDuration(15);
        tournamentPostDTO.setInformationBox("INFO1");
        tournamentPostDTO.setLocation("TESTLOCATION1");
        tournamentPostDTO.setNumberTables(4);
        tournamentPostDTO.setStartTime("12:00");
        tournamentPostDTO.setTournamentName("NAME1");

        MockHttpServletRequestBuilder postRequest = post("/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tournamentPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isUnauthorized());
    }
    
    /**
     * Check if the post request returns the correct status, positive
     */
    @Test
    public void createTournamentPositive() throws Exception{
    	
        TournamentPostDTO tournamentPostDTO = new TournamentPostDTO();
        tournamentPostDTO.setAmountOfPlayers(4);
        tournamentPostDTO.setBreakDuration(10);
        tournamentPostDTO.setGameDuration(15);
        tournamentPostDTO.setInformationBox("INFO1");
        tournamentPostDTO.setLocation("TESTLOCATION1");
        tournamentPostDTO.setNumberTables(4);
        tournamentPostDTO.setStartTime("12:00");
        tournamentPostDTO.setTournamentName("NAME1");

        MockHttpServletRequestBuilder postRequest = post("/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tournamentPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isCreated());
    }
    
    /**
     * Checks if the get request using the tournamentCode works -positive
     */
    @Test
    public void getTournamentByTournamentCodePositive() throws Exception{

        given(tournamentService.checkIfTournamentCodeExists(Mockito.any())).willReturn(true);
        given(tournamentService.getTournamentByTournamentCode(Mockito.any())).willReturn(testTournament1);

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/tournaments/TEST1")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isOk())
		        .andExpect(jsonPath("$[0].tournamentName", is(testTournament1.getTournamentName())))
		        .andExpect(jsonPath("$[0].winner", is(testTournament1.getWinner())))
		        .andExpect(jsonPath("$[0].location", is(testTournament1.getLocation())))
		        .andExpect(jsonPath("$[0].starTime", is(testTournament1.getStartTime())))
		        .andExpect(jsonPath("$[0].gameDuration", is(testTournament1.getGameDuration())))
		        .andExpect(jsonPath("$[0].breakDuration", is(testTournament1.getBreakDuration())))
		        .andExpect(jsonPath("$[0].tournamentCode", is(testTournament1.getTournamentCode())))
		        .andExpect(jsonPath("$[0].amountOfPlayers", is(testTournament1.getAmountOfPlayers())))
		        .andExpect(jsonPath("$[0].numberTables", is(testTournament1.getNumberTables())))
		        .andExpect(jsonPath("$[0].informationBox", is(testTournament1.getInformationBox())))
		        .andExpect(jsonPath("$[0].activePlayers", is(testTournament1.getActivePlayers())));
    }
    
    /**
     * Checks if the get request using the tournamentCode works -negative
     */
    @Test
    public void getTournamentByTournamentCodeNegative() throws Exception{

        given(tournamentService.checkIfTournamentCodeExists(Mockito.any())).willReturn(false);
        given(tournamentService.getTournamentByTournamentCode(Mockito.any())).willReturn(testTournament1);

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/tournaments/TEST1")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }
    
    /**
     * Checks if the get request using the tournamentCode works -positive
     */
    @Test
    public void getBracketByTournamentCodePositive() throws Exception{
    	
    	ArrayList<Game> dummyList3 = new ArrayList<>();
    	
    	dummyList3.add(testGame1);
    	dummyList3.add(testGame2);
    	
        given(tournamentService.checkIfTournamentCodeExists(Mockito.any())).willReturn(true);
        given(tournamentService.getBracketByTournamentCode(Mockito.any())).willReturn(dummyList3);

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/tournaments/TEST1/bracket")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isOk())
		        .andExpect(jsonPath("$[0].startTime", is(testGame1.getStartTime())))
		        .andExpect(jsonPath("$[0].gameState", is(testGame1.getGameState())))
		        .andExpect(jsonPath("$[0].score1", is(testGame1.getScore1())))
		        .andExpect(jsonPath("$[0].score2", is(testGame1.getScore2())))
		        .andExpect(jsonPath("$[0].participant1", is(testGame1.getParticipant1())))
		        .andExpect(jsonPath("$[0].participant2", is(testGame1.getParticipant2())))
		        .andExpect(jsonPath("$[0].tournamentCode", is(testGame1.getTournamentCode())))
		        .andExpect(jsonPath("$[1].startTime", is(testGame2.getStartTime())))
		        .andExpect(jsonPath("$[1].gameState", is(testGame2.getGameState())))
		        .andExpect(jsonPath("$[1].score1", is(testGame2.getScore1())))
		        .andExpect(jsonPath("$[1].score2", is(testGame2.getScore2())))
		        .andExpect(jsonPath("$[1].participant1", is(testGame2.getParticipant1())))
		        .andExpect(jsonPath("$[1].participant2", is(testGame2.getParticipant2())))
		        .andExpect(jsonPath("$[1].tournamentCode", is(testGame2.getTournamentCode())));	       
    }
    
    /**
     * Checks if the get request using the tournamentCode works -positive
     */
    @Test
    public void getBracketByTournamentCodeNegative() throws Exception{
    	
    	ArrayList<Game> dummyList3 = new ArrayList<>();
    	
    	dummyList3.add(testGame1);
    	dummyList3.add(testGame2);
    	
        given(tournamentService.checkIfTournamentCodeExists(Mockito.any())).willReturn(false);
        given(tournamentService.getBracketByTournamentCode(Mockito.any())).willReturn(dummyList3);

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/tournaments/TEST1/bracket")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isNotFound());       
    }
    
    
    
    
    
    
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }

}


