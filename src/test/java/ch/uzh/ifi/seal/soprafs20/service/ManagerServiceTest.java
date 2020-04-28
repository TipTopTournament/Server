package ch.uzh.ifi.seal.soprafs20.service;

import java.util.List;
import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerService managerService;

    private Manager testManager1;
    private Manager testManager2;
    private Manager testManager3;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testManager1 = new Manager();
        testManager2 = new Manager();
        testManager3 = new Manager();
        List<Manager> dummyList = new ArrayList<>();

        testManager1.setUsername("fabio");
        testManager1.setVorname("Fabio");
        testManager1.setNachname("Sisi");
        testManager1.setPassword("ferrari");

        testManager2.setUsername("stef");
        testManager2.setVorname("Stefano");
        testManager2.setNachname("Anzolut");
        testManager2.setPassword("apple");

        testManager3.setUsername("ty");
        testManager3.setVorname("Tony");
        testManager3.setNachname("Ly");
        testManager3.setPassword("banana");

        dummyList.add(testManager1);
        dummyList.add(testManager2);
        dummyList.add(testManager3);

    }
    /**
     * checks the username/password combination -positive
     */
    @Test
    public void createNewManager() {
        List<Manager> dummyList = new ArrayList<>();
        dummyList.add(testManager1);
        dummyList.add(testManager2);
        dummyList.add(testManager3);

        Mockito.when(managerService.getManagers()).thenReturn(dummyList);
        assertTrue(managerService.checkUsernameAndPassword(testManager1.getUsername(), testManager1.getPassword()));

    }
}
