package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.repository.ParticipantRepository;
import ch.uzh.ifi.seal.soprafs20.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
    private ParticipantService participantService;

    private Participant testParticipant1;
    private Participant testParticipant2;
    private Participant testParticipant3;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testParticipant1 = new Participant();
        testParticipant2 = new Participant();
        testParticipant3 = new Participant();

        testParticipant1.setVorname("Fabio");
        testParticipant1.setNachname("Sisi");
        testParticipant1.setPassword("ferrari");

        testParticipant2.setVorname("Stefano");
        testParticipant2.setNachname("Anzolut");
        testParticipant2.setPassword("banana");
        testParticipant2.setLicenseNumber("123456");

        testParticipant3.setVorname("Tony");
        testParticipant3.setNachname("Ly");
    }

    /**
     * Completely new User without a license number should be created with empty stats
     */
    @Test
    public void createParticipantSuccessWithoutLicenseNumber() {
        Participant createdParticipant = participantService.createParticipant(testParticipant1);

        // assert that the data is correct
        assertEquals(testParticipant1.getVorname(), createdParticipant.getVorname());
        assertEquals(testParticipant1.getNachname(), createdParticipant.getNachname());
        assertEquals(testParticipant1.getPassword(), createdParticipant.getPassword());

        // token and licenseNumber must be set and have the correct length
        assertNotNull(createdParticipant.getLicenseNumber());
        assertEquals(6, createdParticipant.getLicenseNumber().length());
        assertNotNull(createdParticipant.getToken());

        // check that stats have been created and the correct values are set
        assertNotNull(createdParticipant.getStatistics());
        assertEquals(0, createdParticipant.getStatistics().getWins());
        assertEquals(0, createdParticipant.getStatistics().getLosses());
        assertEquals(0, createdParticipant.getStatistics().getPointsScored());
        assertEquals(0, createdParticipant.getStatistics().getPointsConceded());
        assertEquals(0, createdParticipant.getStatistics().getHistory().size());
    }
    /* doesn't quite work, I'll fix it --Fabio, 27.04.20
    @Test
    public void createParticipantSuccessWithLicenseNumber() {

        // setup the mocks, testuser2 is in the database but has not been registered yet, and therefore doesn't have a password
        Mockito.when(participantRepository.findByLicenseNumber(Mockito.any())).thenReturn(testParticipant2);
        Mockito.when(participantRepository.findByLicenseNumber(Mockito.any()).getPassword()).thenReturn(null);

        // create the participant object
        Participant createdParticipant = participantService.createParticipant(testParticipant2);

        // assert that data is correct
        assertEquals(testParticipant2.getVorname(), createdParticipant.getVorname());
        assertEquals(testParticipant2.getNachname(), createdParticipant.getNachname());
        assertEquals(testParticipant2.getPassword(), createdParticipant.getPassword());

        // token and licenseNumber must be set and have the correct length
        assertNotNull(createdParticipant.getLicenseNumber());
        assertEquals(6, createdParticipant.getLicenseNumber().length());
        assertNotNull(createdParticipant.getToken());

        // check that stats have been created and the correct values are set
        assertNotNull(createdParticipant.getStatistics());
        assertEquals(0, createdParticipant.getStatistics().getWins());
        assertEquals(0, createdParticipant.getStatistics().getLosses());
        assertEquals(0, createdParticipant.getStatistics().getPointsScored());
        assertEquals(0, createdParticipant.getStatistics().getPointsConceded());
        assertEquals(0, createdParticipant.getStatistics().getHistory().size());
    }


     */

}
