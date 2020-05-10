package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
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

    @InjectMocks
    private Statistics testStatistics3;

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
        testParticipant1.setParticipantID(69L);
        testParticipant1.setToken("000z0z00-yy1y-2222-x3x3-w444w4444w44");

        testParticipant2.setVorname("Stefano");
        testParticipant2.setNachname("Anzolut");
        testParticipant2.setPassword("banana");
        testParticipant2.setLicenseNumber("123456");
        testParticipant2.setParticipantID(1L);
        testParticipant2.setUserStatus(UserStatus.OFFLINE);
        testParticipant2.setToken("111a1a11-bb2b-3333-c4c4-d555d5555d55");

        testParticipant3.setVorname("Tony");
        testParticipant3.setNachname("Ly");
        testParticipant3.setPassword("apple");
        testParticipant3.setLicenseNumber("654321");
        testParticipant3.setParticipantID(2L);
        testParticipant3.setToken("222b2b22-cc3c-4444-d5d5-e666e6666e66");
        testParticipant3.setStatistics(testStatistics3);
        testStatistics3.setParticipant(testParticipant3);
        testStatistics3.setLosses(69);
        testStatistics3.setWins(420);
        testStatistics3.setPointsConceded(108);
        testStatistics3.setPointsScored(1260);
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

//    /**
//     * Creates a user which already has a license number in the database
//     *
//     */
//
//    @Test
//    public void createParticipantSuccessWithLicenseNumber() {
//
//        // create dummy participant which will be returned
//        Participant dummyParticipant = new Participant();
//        dummyParticipant.setVorname("Tony");
//        dummyParticipant.setNachname("Ly");
//        dummyParticipant.setLicenseNumber("654321");
//
//        // setup the mocks, testParticipant3 is in the database but has not been registered yet, and therefore doesn't have a password
//        Mockito.when(participantRepository.findByLicenseNumber(Mockito.any())).thenReturn(dummyParticipant);
//
//        // create the participant object
//        Participant createdParticipant = participantService.createParticipant(testParticipant3);
//
//        // assert that data is correct
//        assertEquals(testParticipant3.getVorname(), createdParticipant.getVorname());
//        assertEquals(testParticipant3.getNachname(), createdParticipant.getNachname());
//        assertEquals(testParticipant3.getPassword(), createdParticipant.getPassword());
//
//        // token and licenseNumber must be set and have the correct length
//        assertNotNull(createdParticipant.getLicenseNumber());
//        assertEquals(6, createdParticipant.getLicenseNumber().length());
//        assertNotNull(createdParticipant.getToken());
//
//        // check that stats have been created and the correct values are set
//        assertNotNull(createdParticipant.getStatistics());
//        assertEquals(0, createdParticipant.getStatistics().getWins());
//        assertEquals(0, createdParticipant.getStatistics().getLosses());
//        assertEquals(0, createdParticipant.getStatistics().getPointsScored());
//        assertEquals(0, createdParticipant.getStatistics().getPointsConceded());
//        assertEquals(0, createdParticipant.getStatistics().getHistory().size());
//    }

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

        // setup the mocks, testParticipant3 is in the database and has already been registered
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

    /**
     * Check if the method getParticipantByID returns an exception, given a not assigned ParticipantId
     */
    @Test
    public void participantByIdNegative() throws ResponseStatusException{

        // setup the mocks, there does not exist such an Id
        Mockito.when(participantRepository.findByParticipantID(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> { participantService.getParticipantById(testParticipant1.getParticipantID()); });
    }

    /**
     * Check if the method getParticipantByID returns a participant, given an known ParticipantId
     */
    @Test
    public void participantByIdPositive(){

        // setup the mocks, testParticipant2 is in the database and has already been registered thus has an Id
        Mockito.when(participantRepository.findByParticipantID(Mockito.any())).thenReturn(testParticipant2);

        Participant searchedParticipantById = participantService.getParticipantById(testParticipant2.getParticipantID());

        // assert that data is correct
        assertEquals(testParticipant2.getVorname(), searchedParticipantById.getVorname());
        assertEquals(testParticipant2.getNachname(), searchedParticipantById.getNachname());
        assertEquals(testParticipant2.getPassword(), searchedParticipantById.getPassword());
    }
    /**
     * Check if the method getParticipantByLicense returns a participant, given an known ParticipantId
     */
    @Test
    public void participantByLicenseNumber(){

        // setup the mocks, testParticipant2 is in the database and has already been registered thus has an LicenseNumber
        Mockito.when(participantRepository.findByLicenseNumber(Mockito.any())).thenReturn(testParticipant2);

        Participant searchedParticipantById = participantService.getParticipantByLicenseNumber(testParticipant2.getLicenseNumber());

        // assert that data is correct
        assertEquals(testParticipant2.getVorname(), searchedParticipantById.getVorname());
        assertEquals(testParticipant2.getNachname(), searchedParticipantById.getNachname());
        assertEquals(testParticipant2.getLicenseNumber(), searchedParticipantById.getLicenseNumber());
    }

    /**
     * Check if the method getStatsByParticipantID returns an exception, given a not assigned ParticipantId
     */
    @Test
    public void participantStatsByIdNegative() throws ResponseStatusException{

        // setup the mocks, there does not exist such an Id
        Mockito.when(participantRepository.findByParticipantID(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> { participantService.getStatsByParticipantID(testParticipant1.getParticipantID()); });
    }

    /**
     * Check if the method getStatsByParticipantID returns statistics, given an known ParticipantId
     */
    @Test
    public void participantStatsByIdPositive(){

        // setup the mocks, testParticipant2 is in the database and has already been registered thus has an Id
        Mockito.when(participantRepository.findByParticipantID(Mockito.any())).thenReturn(testParticipant3);

        Statistics statsById = participantService.getStatsByParticipantID(testParticipant3.getParticipantID());

        // assert that data is correct
        assertEquals(testParticipant3.getStatistics().getParticipant().getVorname(), statsById.getParticipant().getVorname());
        assertEquals(testParticipant3.getStatistics().getParticipant().getNachname(), statsById.getParticipant().getNachname());
        assertEquals(testParticipant3.getStatistics().getWins(), statsById.getWins());
        assertEquals(testParticipant3.getStatistics().getLosses(), statsById.getLosses());
        assertEquals(testParticipant3.getStatistics().getPointsConceded(), statsById.getPointsConceded());
        assertEquals(testParticipant3.getStatistics().getPointsScored(), statsById.getPointsScored());
    }

    /**
     * Check if the method which checks participantId and token combinations, positive
     */
    @Test
    public void participantCheckPositive() {
        List<Participant> dummyList = new ArrayList<>();

        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        // setup the mock
        Mockito.when(participantService.getParticipants()).thenReturn(dummyList);

        assertTrue(participantService.checkIfParticipantIdAndToken(testParticipant2.getParticipantID(), testParticipant2.getToken()));
    }

    /**
     * Check if the method which checks participantId and token combinations, negative
     */
    @Test
    public void participantCheckNegative() {
        List<Participant> dummyList = new ArrayList<>();

        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        // setup the mock
        Mockito.when(participantService.getParticipants()).thenReturn(dummyList);

        assertFalse(participantService.checkIfParticipantIdAndToken(testParticipant1.getParticipantID(), testParticipant1.getToken()));
    }

    /**
     * Update UserStatus, -positive
     */
    @Test
    public void updateUserStatusSuccess() {
        //New Status to be set
        UserStatus dummyStatus = UserStatus.ONLINE;

        List<Participant> dummyList = new ArrayList<>();
        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        // setup the mock
        Mockito.when(participantService.getParticipants()).thenReturn(dummyList);
        Mockito.when(participantRepository.findByParticipantID(Mockito.any())).thenReturn(testParticipant2);

        participantService.updateStatus(testParticipant2.getParticipantID(), dummyStatus, testParticipant2.getToken());

        //assert that status updates
        assertEquals(UserStatus.ONLINE, testParticipant2.getUserStatus());


    }

    /**
     * Update UserStatus, -negative throws ResponseException Not Found Since Id given does not exist
     */
    @Test
    public void updateUserStatusFailureNoSuchIdExists() throws ResponseStatusException{
        //New Status to be set
        UserStatus dummyStatus = UserStatus.ONLINE;

        List<Participant> dummyList = new ArrayList<>();
        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        // setup the mock
        Mockito.when(participantService.getParticipants()).thenReturn(dummyList);

        assertThrows(ResponseStatusException.class, () -> {
            participantService.updateStatus(testParticipant1.getParticipantID(), dummyStatus, testParticipant1.getToken());
        });


    }

    /**
     * Update UserStatus, -negative throws ResponseException Unauthorized Since Id and Token do not match
     */
    @Test
    public void updateUserStatusFailureIdAndToken() throws ResponseStatusException{
        //New Status to be set
        UserStatus dummyStatus = UserStatus.ONLINE;

        List<Participant> dummyList = new ArrayList<>();
        dummyList.add(testParticipant2);
        dummyList.add(testParticipant3);

        // setup the mock
        Mockito.when(participantService.getParticipants()).thenReturn(dummyList);

        assertThrows(ResponseStatusException.class, () -> {
            participantService.updateStatus(testParticipant2.getParticipantID(), dummyStatus, testParticipant3.getToken());
        });
    }







}
