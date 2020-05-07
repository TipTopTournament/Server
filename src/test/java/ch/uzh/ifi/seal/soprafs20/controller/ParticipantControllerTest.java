package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantPutDTO;
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
import java.util.ArrayList;
import java.util.List;
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


@WebMvcTest(ParticipantController.class)
public class ParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipantService participantService;

    private Participant testParticipant1;
    private Participant testParticipant2;
    private Participant testParticipant3;

    @BeforeEach
    private void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testParticipant1 = new Participant();
        testParticipant2 = new Participant();
        testParticipant3 = new Participant();
        List<Participant> dummyList = new ArrayList<>();

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

        dummyList.add(testParticipant1);
        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);
    }

    /**
     * checks if all participants are returned
     */
    @Test
    public void getAllParticipants() throws Exception{

        List<Participant> dummyList = new ArrayList<>();
        dummyList.add(testParticipant1);
        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        given(participantService.getParticipants()).willReturn(dummyList);

        // mock the request
        MockHttpServletRequestBuilder getAllRequest = get("/participants")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getAllRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].vorname", is(testParticipant1.getVorname())))
                .andExpect(jsonPath("$[0].nachname", is(testParticipant1.getNachname())))
                .andExpect(jsonPath("$[0].licenseNumber", is(testParticipant1.getLicenseNumber())))
                .andExpect(jsonPath("$[1].vorname", is(testParticipant2.getVorname())))
                .andExpect(jsonPath("$[1].nachname", is(testParticipant2.getNachname())))
                .andExpect(jsonPath("$[1].licenseNumber", is(testParticipant2.getLicenseNumber())))
                .andExpect(jsonPath("$[2].vorname", is(testParticipant3.getVorname())))
                .andExpect(jsonPath("$[2].nachname", is(testParticipant3.getNachname())))
                .andExpect(jsonPath("$[2].licenseNumber", is(testParticipant3.getLicenseNumber())));

    }

    /**
     * Checks if the get request using the participantId works -positive
     */
    @Test
    public void getParticipantById() throws Exception{

        given(participantService.getParticipantById(Mockito.any())).willReturn(testParticipant1);

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/participants/1")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.vorname", is(testParticipant1.getVorname())))
                .andExpect(jsonPath("$.nachname", is(testParticipant1.getNachname())))
                .andExpect(jsonPath("$.licenseNumber", is(testParticipant1.getLicenseNumber())));
    }

    /**
     * Checks if the correct error is returned if the participant is not found -negative
     */
    @Test
    public void getParticipantByIdFailure() throws Exception {

        given(participantService.getParticipantById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No participant found with this Id"));

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/participants/1")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    /**
     * Check if the post request returns the correct status, positive
     */
    @Test
    public void createParticipant() throws Exception{

        ParticipantPostDTO participantPostDTO = new ParticipantPostDTO();
        participantPostDTO.setVorname("Fabio");
        participantPostDTO.setNachname("Sisi");
        participantPostDTO.setPassword("ferrari");

        given(participantService.createParticipant(Mockito.any())).willReturn(testParticipant1);

        // mock the request
        MockHttpServletRequestBuilder postRequest = post("/participants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantPostDTO));

        // do the request and evaluate
        mockMvc.perform(postRequest).andExpect(status().isCreated());
    }

    /**
     * Check if the post request returns the correct status, negative
     */
    @Test
    public void createParticipantFailure() throws Exception{

        ParticipantPostDTO participantPostDTO = new ParticipantPostDTO();
        participantPostDTO.setVorname("Fabio");
        participantPostDTO.setNachname("Sisi");
        participantPostDTO.setPassword("ferrari");

        given(participantService.createParticipant(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        // mock the request
        MockHttpServletRequestBuilder postRequest = post("/participants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantPostDTO));

        // do the request and evaluate
        mockMvc.perform(postRequest).andExpect(status().isConflict());
    }

    /**
     * Check if the login works, -positive
     */
    @Test
    public void loginPositive() throws Exception {
        ParticipantPostDTO participantPostDTO = new ParticipantPostDTO();
        participantPostDTO.setVorname("Fabio");
        participantPostDTO.setNachname("Sisi");
        participantPostDTO.setPassword("ferrari");

        given(participantService.checkLicenseNumberAndPassword(Mockito.any(), Mockito.any())).willReturn(true);

        // mock the request
        MockHttpServletRequestBuilder putRequest = put("/participants/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantPostDTO));

        // do the request
        mockMvc.perform(putRequest).andExpect(status().isOk());
    }

    /**
     * Check if the login works, -negative
     */
    @Test
    public void loginNegative() throws Exception {
        ParticipantPostDTO participantPostDTO = new ParticipantPostDTO();
        participantPostDTO.setVorname("Fabio");
        participantPostDTO.setNachname("Sisi");
        participantPostDTO.setPassword("ferrari");

        given(participantService.checkLicenseNumberAndPassword(Mockito.any(), Mockito.any())).willReturn(false);

        // mock the request
        MockHttpServletRequestBuilder putRequest = put("/participants/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantPostDTO));

        // do the request
        mockMvc.perform(putRequest).andExpect(status().isUnauthorized());
    }

    /**
     * test the statistics endpoint, -positive
     */
    @Test
    public void statisticsTest() throws Exception {
        Statistics stats = new Statistics();
        stats.setWins(13);
        stats.setLosses(2);
        stats.setPointsScored(46);
        stats.setPointsConceded(32);

        testParticipant1.setStatistics(stats);

        given(participantService.getStatsByParticipantID(Mockito.any())).willReturn(testParticipant1.getStatistics());

        // mock the request
        MockHttpServletRequestBuilder statsRequest = get("/participants/1/statistics");

        // do the request
        mockMvc.perform(statsRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.wins", is(13)))
                .andExpect(jsonPath("$.losses", is(2)))
                .andExpect(jsonPath("$.pointsScored", is(46)))
                .andExpect(jsonPath("$.pointsConceded", is(32)));
    }

    /**
     * test the statistics endpoint, -negative for Id not found
     */
    @Test
    public void statisticsTestFailure() throws Exception{
        Statistics stats = new Statistics();
        stats.setWins(13);
        stats.setLosses(2);
        stats.setPointsScored(46);
        stats.setPointsConceded(32);

        testParticipant1.setStatistics(stats);
        given(participantService.getStatsByParticipantID(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // mock the request
        MockHttpServletRequestBuilder statsRequest = get("/participants/333/statistics");

        // do the request
        mockMvc.perform(statsRequest).andExpect(status().isNotFound());
    }

    /**
     * Check if the status update works, -positive
     */
    @Test
    public void updateStatusPositive() throws Exception{

        testParticipant1.setToken("111a1a11-bb2b-3333-c4c4-d555d5555d55");
        testParticipant1.setUserStatus(UserStatus.OFFLINE);

        ParticipantPutDTO participantPutDTO = new ParticipantPutDTO();
        participantPutDTO.setToken("111a1a11-bb2b-3333-c4c4-d555d5555d55");
        participantPutDTO.setUserStatus(UserStatus.ONLINE);

        participantService.updateStatus(Mockito.any(), Mockito.any());

        // mock the request
        MockHttpServletRequestBuilder updateRequest = put("/participants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantPutDTO));

        // do the request
        mockMvc.perform(updateRequest).andExpect(status().isOk());

    }

    /**
     * Check if the status update works, -negative no Id found
     */
    @Test
    public void updateStatusNegative() throws Exception{

        testParticipant1.setToken("111a1a11-bb2b-3333-c4c4-d555d5555d55");
        testParticipant1.setUserStatus(UserStatus.OFFLINE);

        ParticipantPutDTO participantPutDTO = new ParticipantPutDTO();
        participantPutDTO.setParticipantID(69);
        participantPutDTO.setToken("111a1a11-bb2b-3333-c4c4-d555d5555d55");
        participantPutDTO.setUserStatus(UserStatus.ONLINE);

        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(participantService).updateStatus(Mockito.any(), Mockito.any());

        // mock the request
        MockHttpServletRequestBuilder updateRequest = put("/participants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantPutDTO));

        // do the request
        mockMvc.perform(updateRequest).andExpect(status().isNotFound());


    }

    /**
     * Check if the status update works, -negative cannot update someone else status
     */
    @Test
    public void updateStatusNotAuthorized() throws Exception{

        testParticipant1.setToken("111a1a11-bb2b-3333-c4c4-d555d5555d55");
        testParticipant1.setUserStatus(UserStatus.OFFLINE);

        ParticipantPutDTO participantPutDTO = new ParticipantPutDTO();
        participantPutDTO.setToken("254h7x88-jk5c-6254-o8o2-y642f7988p06");
        participantPutDTO.setUserStatus(UserStatus.ONLINE);

        willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).given(participantService).updateStatus(Mockito.any(), Mockito.any());
        // mock the request
        MockHttpServletRequestBuilder updateRequest = put("/participants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantPutDTO));

        // do the request
        mockMvc.perform(updateRequest).andExpect(status().isUnauthorized());

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
}
