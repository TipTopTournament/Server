package ch.uzh.ifi.seal.soprafs20.controller;
/*
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import ch.uzh.ifi.seal.soprafs20.constant.GameState;
import ch.uzh.ifi.seal.soprafs20.constant.TournamentState;
import ch.uzh.ifi.seal.soprafs20.entity.Bracket;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Leaderboard;
import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.repository.ParticipantRepository;
import ch.uzh.ifi.seal.soprafs20.service.ManagerService;
import ch.uzh.ifi.seal.soprafs20.service.ParticipantService;
import ch.uzh.ifi.seal.soprafs20.service.TournamentService;

@WebAppConfiguration
@SpringBootTest
class IntegrationTest {
	
	@Qualifier("participantRepository")
	@Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantService participantService;
    
    @BeforeEach
    private void setup() {
        participantRepository.deleteAll();
    }
    
     @Test
     public void userCreatedSuccess() {
    	 Participant testParticipant = new Participant();
    	 testParticipant.setNachname("Arandjelovic");
    	 testParticipant.setVorname("Vukasin");
    	 testParticipant.setLicenseNumber("111222");
    	 
    	 Participant createdParticipant = participantService.createParticipant(testParticipant);
    	 
    	 assertEquals(testParticipant.getLicenseNumber(),createdParticipant.getLicenseNumber());
    	 assertEquals(testParticipant.getNachname(),createdParticipant.getNachname());
    	 assertEquals(testParticipant.getVorname(),createdParticipant.getVorname());
    	 assertNotNull(testParticipant.getToken());
     }
}
*/