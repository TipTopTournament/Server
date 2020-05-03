package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.repository.ParticipantRepository;
import ch.uzh.ifi.seal.soprafs20.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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
        testParticipant3.setPassword("apple");
        testParticipant3.setLicenseNumber("654321");
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

    /**
     * Creates a user which already has a license number
     
    @Test
    public void createParticipantSuccessWithLicenseNumber() {
        // create dummy participant which will be returned
        Participant dummyParticipant = new Participant();
        dummyParticipant.setVorname("Stefano");
        dummyParticipant.setNachname("Anzolut");
        dummyParticipant.setLicenseNumber("123456");

        // setup the mocks, testuser2 is in the database but has not been registered yet, and therefore doesn't have a password
        Mockito.when(participantRepository.findByLicenseNumber(Mockito.any())).thenReturn(dummyParticipant);

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
    /**
     * A user with this license number exists but already has a password, therefore, an exception should be thrown
     */
    @Test
    public void createParticipantButPasswordHasBeenSet() throws ResponseStatusException{
        // create dummy participant which will be returned
        Participant dummyParticipant = new Participant();
        dummyParticipant.setVorname("Tony");
        dummyParticipant.setNachname("Ly");
        dummyParticipant.setPassword("apple");
        dummyParticipant.setLicenseNumber("654321");

        // setup the mocks, testuser2 is in the database and has already been registered
        Mockito.when(participantRepository.findByLicenseNumber(Mockito.any())).thenReturn(dummyParticipant);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            Participant createdParticipant = participantService.createParticipant(testParticipant3);
        });

    }

    /**
     * Tests if the list of participants which is returned is correct
     */
    @Test
    public void checkListOfParticipants() {
        List<Participant> dummyList = new ArrayList<>();

        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        // setup the additional mock
        Mockito.when(participantRepository.findAll()).thenReturn(dummyList);

        // test
        List<Participant> list = participantService.getParticipants();

        // assert the correctness of participant2
        assertEquals(testParticipant2.getVorname(), list.get(0).getVorname());
        assertEquals(testParticipant2.getNachname(), list.get(0).getNachname());
        assertEquals(testParticipant2.getLicenseNumber(), list.get(0).getLicenseNumber());


        // assert the correctness of participant3
        assertEquals(testParticipant3.getVorname(), list.get(1).getVorname());
        assertEquals(testParticipant3.getNachname(), list.get(1).getNachname());
        assertEquals(testParticipant3.getLicenseNumber(), list.get(1).getLicenseNumber());

    }

    /**
     * Check if the method which checks license number and password combinations, positive
     */
    @Test
    public void passwordCheckPositive() {
        List<Participant> dummyList = new ArrayList<>();

        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        // setup the mock
        Mockito.when(participantService.getParticipants()).thenReturn(dummyList);

        assertTrue(participantService.checkLicenseNumberAndPassword(testParticipant2.getLicenseNumber(), testParticipant2.getPassword()));
    }

    /**
     * Check if the method which checks license number and password combinations, negative
     */
    @Test
    public void passwordCheckNegative() {
        List<Participant> dummyList = new ArrayList<>();

        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        // setup the mock
        Mockito.when(participantService.getParticipants()).thenReturn(dummyList);

        assertFalse(participantService.checkLicenseNumberAndPassword(testParticipant1.getLicenseNumber(), testParticipant1.getPassword()));
    }
}
