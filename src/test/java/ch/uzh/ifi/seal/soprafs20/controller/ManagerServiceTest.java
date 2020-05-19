package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.repository.ManagerRepository;
import ch.uzh.ifi.seal.soprafs20.service.ManagerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.web.server.ResponseStatusException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@ExtendWith(MockitoExtension.class)
class ManagerserviceTest {

   @Mock
   ManagerRepository managerRepository;

   @InjectMocks
   ManagerService managerService;

    @BeforeEach
    private void setup() {/*
        ManagerRepository managerRepository;
        Manager manager1 = new Manager();
        managerRepository.saveAndFlush(manager1);*/
    }

    /**
     * checks if all participants are returned
     */
    @Test
    public void getManagersTest(){
        Manager testmanager = new Manager();
        testmanager.setUsername("mauro");
        List<Manager> expectedList = new ArrayList<>();
        expectedList.add(testmanager);
        when(managerRepository.findAll()).thenReturn(expectedList);
        List<Manager> returnedList = managerService.getManagers();

        assertEquals(expectedList,returnedList);
    }
    @Test
    public void getManagersByIDPositiveTest(){
        Manager testmanager1 = mock(Manager.class);
        testmanager1.setUsername("mauro");
        Manager testmanager2 = mock(Manager.class);
        testmanager2.setUsername("fabio");


        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);
        managerList.add(testmanager2);

        when(managerService.getManagers()).thenReturn(managerList);
      when(testmanager1.getManagerID()).thenReturn((long) 1);
     when(testmanager2.getManagerID()).thenReturn((long) 2);

        Manager returnedManager = managerService.getManagerById((long) 2);
        assertEquals(testmanager2,returnedManager);
    }

@Test
    public void getManagersByIDNegativeTest(){
        Manager testmanager1 = mock(Manager.class);
        testmanager1.setUsername("mauro");
        Manager testmanager2 = mock(Manager.class);
        testmanager2.setUsername("fabio");


        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);
        managerList.add(testmanager2);

        when(managerService.getManagers()).thenReturn(managerList);
        when(testmanager1.getManagerID()).thenReturn((long) 1);
        when(testmanager2.getManagerID()).thenReturn((long) 2);
        assertThrows(ResponseStatusException.class,()-> managerService.getManagerById((long) 8),"it should return not found message when no manager with this id exists");
    }
    @Test
    public void getManagersByUsernamePositiveTest(){
        Manager testmanager1 = mock(Manager.class);
        testmanager1.setUsername("mauro");
        Manager testmanager2 = new Manager();
        testmanager2.setUsername("fabio");


        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);
        managerList.add(testmanager2);

        when(managerService.getManagers()).thenReturn(managerList);


        Manager returnedManager = managerService.getManagerByUsername("fabio");
        assertEquals(testmanager2,returnedManager);
    }

    @Test
    public void getManagersByUsernameNegativeTest(){
        Manager testmanager1 = mock(Manager.class);
        testmanager1.setUsername("mauro");
        Manager testmanager2 = mock(Manager.class);
        testmanager2.setUsername("fabio");


        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);
        managerList.add(testmanager2);

        when(managerService.getManagers()).thenReturn(managerList);
        assertThrows(ResponseStatusException.class,()-> managerService.getManagerByUsername("Tony"),"it should return not found message when no manager with this username exists");
    }
    @Test
    public void createManagerTestNegative(){
        Manager testmanager1 =new Manager();
        testmanager1.setUsername("mauro");
        Manager testmanager2 = new Manager();
        testmanager2.setUsername("mauro");


        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);

        assertThrows(ResponseStatusException.class,()-> managerService.createManager(testmanager2),"it should return conflict message when no manager with this username exists");
    }
    @Test
    public void createManagerTestPositive(){
        Manager testmanager1 =new Manager();
        testmanager1.setUsername("mauro");
        Manager testmanager2 = new Manager();
        testmanager2.setUsername("fabio");
        Manager testmanager3 = new Manager();
        testmanager2.setUsername("Tony");


        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);
        managerList.add(testmanager2);

        when(managerService.getManagers()).thenReturn(managerList);
when(managerRepository.save(testmanager3)).thenReturn(testmanager3);
        Manager createdManager = managerService.createManager(testmanager3);
        assertEquals(testmanager3,createdManager);
    }
    @Test
    public void checkIfManagerIdExistsTestPositive(){
        Manager testmanager1 =mock(Manager.class);

        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);


        when(managerService.getManagers()).thenReturn(managerList);

        assertTrue(managerService.checkIfManagerIdExists((long) 0));
    }

    @Test
    public void checkIfManagerIdExistsTestNegative(){
        Manager testmanager1 = mock(Manager.class);
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);


        when(managerService.getManagers()).thenReturn(managerList);

        assertFalse(managerService.checkIfManagerIdExists((long) 3));
    }

    @Test
    public void updateManagerStatusSuccessTest(){
        Manager testmanager1 =spy(new Manager());
        testmanager1.setUserStatus(UserStatus.OFFLINE);
        testmanager1.setToken("1");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(testmanager1.getManagerID()).thenReturn((long) 1);
        when(managerService.getManagers()).thenReturn(managerList);

        UserStatus newStatus = UserStatus.ONLINE;

        managerService.updateStatus((long) 1,newStatus,"1");

        assertEquals(newStatus,testmanager1.getUserStatus());


    }

    @Test
    public void updateManagerStatusNegativeIdTokenNotMatchingTest(){
        Manager testmanager1 =spy(new Manager());
        testmanager1.setUserStatus(UserStatus.OFFLINE);
        testmanager1.setToken("1");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(testmanager1.getManagerID()).thenReturn((long) 1);
        when(managerService.getManagers()).thenReturn(managerList);

        UserStatus newStatus = UserStatus.ONLINE;


        assertThrows(ResponseStatusException.class,()->managerService.updateStatus((long) 1,newStatus,"2"),"it should return id and token not match message ");


    }
    @Test
    public void updateManagerStatusNegativeIdTnotFoundTest(){
        Manager testmanager1 =spy(new Manager());
        testmanager1.setUserStatus(UserStatus.OFFLINE);
        testmanager1.setToken("1");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(testmanager1.getManagerID()).thenReturn((long) 1);
        when(managerService.getManagers()).thenReturn(managerList);

        UserStatus newStatus = UserStatus.ONLINE;


        assertThrows(ResponseStatusException.class,()->managerService.updateStatus((long) 3,newStatus,"1"),"it should return id and token not match message ");


    }
    @Test
    public void checkUsernameAndPasswordPositiveTest(){
        Manager testmanager1 =spy(new Manager());
    testmanager1.setUsername("mauro");
    testmanager1.setPassword("hirt");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);
        Boolean returnvalue = managerService.checkUsernameAndPassword("mauro","hirt");

        assertTrue(returnvalue);

    }
    @Test
    public void checkUsernameAndPasswordNegativeTest(){
        Manager testmanager1 =spy(new Manager());
        testmanager1.setUsername("mauro");
        testmanager1.setPassword("hirt");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);
        Boolean returnvalue = managerService.checkUsernameAndPassword("mauro","girt");

        assertFalse(returnvalue);

    }
    @Test
    public void addTournamentToListTest(){
        Manager testmanager1 =spy(new Manager());
        Tournament testtournament = mock(Tournament.class);

        managerService.addTournamentToManager(testtournament,testmanager1);
        assertNotNull(testmanager1.getTournamentList());
    }

    @Test
    public void checkIfTournamentIsInListTestPositive(){
        Manager testmanager1 =spy(new Manager());
        Tournament testtournament = spy(new Tournament());
        testtournament.setTournamentCode("12");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);
        when(testmanager1.getManagerID()).thenReturn((long) 1);

        managerService.addTournamentToManager(testtournament,testmanager1);
        assertTrue(managerService.checkIfTournamentCodeIsInList((long) 1,"12"));
    }
    @Test
    public void checkIfTournamentIsInListTestNegative(){
        Manager testmanager1 =spy(new Manager());
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);
        when(testmanager1.getManagerID()).thenReturn((long) 1);

        assertFalse(managerService.checkIfTournamentCodeIsInList((long) 1,"12"));
    }
}