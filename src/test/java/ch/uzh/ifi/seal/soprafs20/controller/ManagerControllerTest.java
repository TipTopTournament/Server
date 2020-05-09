package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantPutDTO;
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
    public void getParticipantById() throws Exception{

        given(managerService.getManagerById(Mockito.any())).willReturn(testManager1);

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/managers/1")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.managerID", is(testManager1.getManagerID())))
                .andExpect(jsonPath("$.nachname", is(testManager1.getUsername())));
    }

    /**
     * Checks if the correct error is returned if the participant is not found -negative
     */
    @Test
    public void getParticipantByIdFailure() throws Exception {

        given(managerService.getManagerById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No Manager found with this Id"));

        // mock the request
        MockHttpServletRequestBuilder getRequest = get("/participants/1")
                .contentType(MediaType.APPLICATION_JSON);

        // do the request
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    /**
     * Check if the post request returns the correct status, positive
     */

}
