package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.rest.dto.TournamentPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.TournamentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.BDDMockito.given;

@WebMvcTest(TournamentController.class)
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    private Tournament testTournament;

    @BeforeEach
    private void setup() {
        MockitoAnnotations.initMocks(this);

        testTournament = new Tournament();
        testTournament.setAmountOfPlayers(16);
        testTournament.setBreakDuration(10);
        testTournament.setGameDuration(30);
        testTournament.setInformationBox("The tournament costs 30.-");
        testTournament.setNumberTables(8);
        testTournament.setStartTime("08:00");
        testTournament.setTournamentName("Zürich Open");

    }


    @Test
    public void testCreateTournament() throws Exception {
        TournamentPostDTO tournamentPostDTO = new TournamentPostDTO();
        tournamentPostDTO.setAmountOfPlayers(16);
        tournamentPostDTO.setBreakDuration(10);
        tournamentPostDTO.setGameDuration(30);
        tournamentPostDTO.setInformationBox("The tournament costs 30.-");
        tournamentPostDTO.setNumberTables(8);
        tournamentPostDTO.setStartTime("08:00");
        tournamentPostDTO.setManagerId(420);
        tournamentPostDTO.setTournamentName("Zürich Open");

        given(tournamentService.createTournament(Mockito.any())).willReturn(testTournament);

        MockHttpServletRequestBuilder postRequest = post("/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tournamentPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isCreated());
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
