package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.service.ManagerService;
import ch.uzh.ifi.seal.soprafs20.service.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerService managerService;

    private Manager testManager1;
    private Manager testManager2;
    private Manager testManager3;



    @BeforeEach
    private void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testManager1 = new Manager();
        testManager2 = new Manager();
        testManager3 = new Manager();;
        List<Participant> dummyList = new ArrayList<>();

        testManager1.setVorname("Fabio");
        testManager1.setNachname("Sisi");
        testManager1.setPassword("ferrari");
        testManager1.setUsername("Manager1");
        testManager1.setUserStatus(UserStatus.OFFLINE);

         testManager1.setVorname("Mauro");
         testManager1.setNachname("Hirt");
         testManager1.setPassword("benz");
         testManager1.setUsername("Manager2");
        testManager1.setUserStatus(UserStatus.OFFLINE);
        /*
        dummyList.add(testParticipant1);
        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);   */
    }

    /**
     * Check if the post request returns the correct status, positive
     */
    @Test
    public void createManager() throws Exception{

        ManagerPostDTO managerPostDTO = new ManagerPostDTO();
        managerPostDTO.setVorname("Fabio");
        managerPostDTO.setNachname("Sisi");
        managerPostDTO.setUsername("Manager1");


        given(managerService.createManager(Mockito.any())).willReturn(testManager1);

        // mock the request
        MockHttpServletRequestBuilder postRequest = post("/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(managerPostDTO));

        // do the request and evaluate
        mockMvc.perform(postRequest).andExpect(status().isCreated());
    }
    /**
     * Check if the post request returns the correct status, negative
     */
    @Test
    public void createManagerFailure() throws Exception{

        ManagerPostDTO managerPostDTO = new ManagerPostDTO();
        managerPostDTO.setVorname("Fabio");
        managerPostDTO.setNachname("Sisi");
        managerPostDTO.setPassword("ferrari");

        given(managerService.createManager(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        // mock the request
        MockHttpServletRequestBuilder postRequest = post("/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(managerPostDTO));

        // do the request and evaluate
        mockMvc.perform(postRequest).andExpect(status().isConflict());
    }


    // helpers
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
    /**
     * Check if the login works, -positive
     */
    @Test
    public void loginPositive() throws Exception {
        ManagerPostDTO managerPostDTO = new ManagerPostDTO();
        managerPostDTO.setVorname("Fabio");
        managerPostDTO.setNachname("Sisi");
        managerPostDTO.setPassword("ferrari");

        given(managerService.checkUsernameAndPassword(Mockito.any(), Mockito.any())).willReturn(true);

        // mock the request
        MockHttpServletRequestBuilder putRequest = put("/managers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(managerPostDTO));

        // do the request
        mockMvc.perform(putRequest).andExpect(status().isOk());
    }

    /**
     * Check if the login works, -negative
     */
    @Test
    public void loginNegative() throws Exception {
        ManagerPostDTO managerPostDTO = new ManagerPostDTO();
        managerPostDTO.setVorname("Fabio");
        managerPostDTO.setNachname("Sisi");
        managerPostDTO.setPassword("ferrari");

        given(managerService.checkUsernameAndPassword(Mockito.any(), Mockito.any())).willReturn(false);

        // mock the request
        MockHttpServletRequestBuilder putRequest = put("/managers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(managerPostDTO));

        // do the request
        mockMvc.perform(putRequest).andExpect(status().isUnauthorized());
    }
    @Test
    public void getManagerById() throws Exception{

        given(managerService.getManagerById(Mockito.any())).willReturn(testManager1);

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/managers/1")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.managerID", is(testManager1.getManagerID())))
                .andExpect(jsonPath("$.username", is(testManager1.getUsername())));
    }

    /**
     * Checks if the correct error is returned if the participant is not found -negative
     */
    @Test
    public void getManagerByIdFailure() throws Exception {

        given(managerService.getManagerById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No Manager found with this Id"));

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/participants/1")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    /**
     * Check if the post request returns the correct status when updating the manager Status with positive status
     */
    @Test
    public void updateManagerStatusSuccesful() throws Exception {
        ManagerPutDTO managerPutDTO = new ManagerPutDTO();
        managerPutDTO.setToken("111111111444443");
        managerPutDTO.setUserStatus(UserStatus.OFFLINE);
        managerPutDTO.setManagerID((long) 1);
                // mock the request
        MockHttpServletRequestBuilder putRequest = put("/managers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(managerPutDTO));

        // do the request
        mockMvc.perform(putRequest).andExpect(status().isOk());
    }
    /**
     * Check if the post request returns the correct status when updating the manager Status with incorrect token Id combination
     */
    @Test
    public void updateManagerStatusTokenIdNotMatch() throws Exception {
        ManagerPutDTO managerPutDTO = new ManagerPutDTO();
        managerPutDTO.setToken("111111111444443");
        managerPutDTO.setUserStatus(UserStatus.OFFLINE);
        managerPutDTO.setManagerID((long) 1);
        willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Manager Id and token do not match, status update prevented!")).given(managerService).updateStatus(Mockito.any(),Mockito.any(),Mockito.any());
        // mock the request
        MockHttpServletRequestBuilder putRequest = put("/managers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(managerPutDTO));

        // do the request
        mockMvc.perform(putRequest).andExpect(status().isUnauthorized());
    }
    /**
     * Check if the post request returns the correct status when updating the manager Status with incorrect Id
     */
    @Test
    public void updateManagerStatusWrongId() throws Exception {
        ManagerPutDTO managerPutDTO = new ManagerPutDTO();
        managerPutDTO.setToken("111111111444443");
        managerPutDTO.setUserStatus(UserStatus.OFFLINE);
        managerPutDTO.setManagerID((long) 1);
        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"No manager found with this Id")).given(managerService).updateStatus(Mockito.any(),Mockito.any(),Mockito.any());
        // mock the request
        MockHttpServletRequestBuilder putRequest = put("/managers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(managerPutDTO));

        // do the request
        mockMvc.perform(putRequest).andExpect(status().isNotFound());
    }

    /**
     * Check if the get request returns the correct status when succesfully returning list of tournaments
     */

    @Test
    public void getAllParticipants() throws Exception{
Tournament tournament1= new Tournament();
Tournament  tournament2 = new Tournament();
tournament1.setAmountOfPlayers(1);
tournament1.setBreakDuration(10);
tournament1.setInformationBox("this is a test");
tournament1.setTournamentCode("1111233333");
tournament1.setLocation("baden");
tournament1.setTournamentName("testtournament");
tournament1.setAmountOfPlayers(0);
tournament1.setBracket(new Bracket());
tournament1.setLeaderboard(new Leaderboard());
tournament2.setAmountOfPlayers(0);
tournament2.setBreakDuration(10.0F);
tournament2.setInformationBox("this is a test");
tournament2.setTournamentCode("1111233333");
tournament2.setLocation("baden");
tournament2.setTournamentName("testtournament");




        List<Tournament> dummyList = new ArrayList<>();
        dummyList.add(tournament1);
        dummyList.add(tournament2);
        testManager1.setTournamentList(dummyList);


        given(managerService.getManagerById(Mockito.any())).willReturn(testManager1);

            ;

        // mock the request
        MockHttpServletRequestBuilder getAllRequest = get("/managers/1/tournaments")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getAllRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tournamentName", is(tournament1.getTournamentName())))
                .andExpect(jsonPath("$[0].amountOfPlayers", is(tournament1.getAmountOfPlayers())))
                .andExpect(jsonPath("$[0].tournamentCode", is(tournament1.getTournamentCode())))
                .andExpect(jsonPath("$[0].informationBox", is(tournament1.getInformationBox())))
                .andExpect(jsonPath("$[0].location", is(tournament1.getLocation())))
                .andExpect(jsonPath("$[1].tournamentName", is(tournament1.getTournamentName())))
                .andExpect(jsonPath("$[1].amountOfPlayers", is(tournament1.getAmountOfPlayers())))
                .andExpect(jsonPath("$[1].tournamentCode", is(tournament1.getTournamentCode())))
                .andExpect(jsonPath("$[1].informationBox", is(tournament1.getInformationBox())))
                .andExpect(jsonPath("$[1].location", is(tournament1.getLocation())));

    }

    /**
     * Check if the get request returns the correct status when nonexisting manager id is given
     */
    @Test
    public void getAllParticipantsFailure() throws Exception{
        /*TournamentGetDTO tournamentGetDTO= new TournamentGetDTO();
        tournamentGetDTO.setAmountOfPlayers(2);
        tournamentGetDTO.setBreakDuration(10);
        tournamentGetDTO.setGameDuration(10);
        tournamentGetDTO.setInformationBox("test");
        tournamentGetDTO.setLocation("baden");
        tournamentGetDTO.setNumberTables(5);
        tournamentGetDTO.setTournamentCode("23421234");
        tournamentGetDTO.setTournamentId(1);
        tournamentGetDTO.setTournamentName("TESTtournament");
        tournamentGetDTO.setStartTime("08:00");*/

        given(managerService.getManagerById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No Manager found with this Id"));

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/managers/1/tournaments")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }
}


