package ch.uzh.ifi.seal.soprafs20.service;

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

// in the manangerservice tests the repository is always mocked and not tested
@ExtendWith(MockitoExtension.class)
class ManagerserviceTest {

   @Mock
   ManagerRepository managerRepository;

   @InjectMocks
   ManagerService managerService;


    /**
     * testing if get managers works when mocking the repository (kind of trivial here)
     */
    @Test
     void getManagersTest(){
        Manager testmanager = new Manager();
        testmanager.setUsername("mauro");
        List<Manager> expectedList = new ArrayList<>();
        expectedList.add(testmanager);
        when(managerRepository.findAll()).thenReturn(expectedList);
        List<Manager> returnedList = managerService.getManagers();

        assertEquals(expectedList,returnedList);
    }
    /**
     * testing if the correct manager gets returned when searching by ID that corresponds to existing manager
     */
    @Test
     void getManagersByIDPositiveTest(){
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
    /**
     * checking if an exception is thrown when searching for and Id that doesnt exist
     */
@Test
     void getManagersByIDNegativeTest(){
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
    /**
     * checking if correct manager is returned when searching for manager by username
     */
    @Test
     void getManagersByUsernamePositiveTest(){
        Manager testmanager1 = new Manager();
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
    /**
     * checking if assertion is thrown when searching for username of non existing manager
     */
    @Test
     void getManagersByUsernameNegativeTest(){
        Manager testmanager1 = new Manager();
        testmanager1.setUsername("mauro");
        Manager testmanager2 = new Manager();
        testmanager2.setUsername("fabio");


        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);
        managerList.add(testmanager2);

        when(managerService.getManagers()).thenReturn(managerList);
        assertThrows(ResponseStatusException.class,()-> managerService.getManagerByUsername("Tony"),"it should return not found message when no manager with this username exists");
    }
    /**
     * testing if assertion is thrown when trying to create a manager with an existing username
     */
    @Test
     void createManagerTestNegative(){
        Manager testmanager1 =new Manager();
        testmanager1.setUsername("mauro");
        Manager testmanager2 = new Manager();
        testmanager2.setUsername("mauro");


        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);

        assertThrows(ResponseStatusException.class,()-> managerService.createManager(testmanager2),"it should return conflict message when no manager with this username exists");
    }
    /**
     * test if manaager can be created when there is no conflict
     */
    @Test
     void createManagerTestPositive(){
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
    /**
     * checking if the helperfunction returns true when mamanger ID exists
     */
    @Test
     void checkIfManagerIdExistsTestPositive(){
        Manager testmanager1 =mock(Manager.class);

        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);


        when(managerService.getManagers()).thenReturn(managerList);

        assertTrue(managerService.checkIfManagerIdExists((long) 0));
    }
    /**
     * testing if helperfunction returns false when managerId does not exist
     */
    @Test
     void checkIfManagerIdExistsTestNegative(){
        Manager testmanager1 = mock(Manager.class);
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);


        when(managerService.getManagers()).thenReturn(managerList);

        assertFalse(managerService.checkIfManagerIdExists((long) 3));
    }
    /**
     *testing if the managerstatus is succesfully upddated when correct input is given
     */
    @Test
     void updateManagerStatusSuccessTest(){
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
    /**
     * testing if assertion is thrown when token and id do not match
     */
    @Test
     void updateManagerStatusNegativeIdTokenNotMatchingTest(){
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
    /**
     * testing if assertion is thrown when invalid Id is given to the function call
     */
    @Test
     void updateManagerStatusNegativeIdTnotFoundTest(){
        Manager testmanager1 =spy(new Manager());
        testmanager1.setUserStatus(UserStatus.OFFLINE);
        testmanager1.setToken("1");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(testmanager1.getManagerID()).thenReturn((long) 1);
        when(managerService.getManagers()).thenReturn(managerList);

        UserStatus newStatus = UserStatus.ONLINE;


        assertThrows(ResponseStatusException.class,()->managerService.updateStatus((long) 3,newStatus,"1"),"it should return id not found message ");


    }
    /**
     * test if function returns true when given a valid password and username
     */
    @Test
     void checkUsernameAndPasswordPositiveTest(){
        Manager testmanager1 =spy(new Manager());
    testmanager1.setUsername("mauro");
    testmanager1.setPassword("hirt");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);
        Boolean returnvalue = managerService.checkUsernameAndPassword("mauro","hirt");

        assertTrue(returnvalue);


    }
    /**
     * test if function returns false when given invalid / nonexistening password username pair
     */
    @Test
     void checkUsernameAndPasswordNegativeTest(){
        Manager testmanager1 =spy(new Manager());
        testmanager1.setUsername("mauro");
        testmanager1.setPassword("hirt");
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);
        Boolean returnvalue = managerService.checkUsernameAndPassword("mauro","girt");

        assertFalse(returnvalue);

    }
    /**
     * check if torunament gets added succesfully to a managers tournament list
     */
    @Test
     void addTournamentToListTest(){
        Manager testmanager1 =spy(new Manager());
        Tournament testtournament = mock(Tournament.class);

        managerService.addTournamentToManager(testtournament,testmanager1);
        assertNotNull(testmanager1.getTournamentList());
    }
    /**
     * check if function returns true when searching for an existing tournament beloning to an existing managers tournamentlist
     */
    @Test
     void checkIfTournamentIsInListTestPositive(){
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
    /**
     *check if function returns false when searching for an nonexisting tournament a managers tournamentlist
     */
    @Test
     void checkIfTournamentIsInListTestNegative(){
        Manager testmanager1 =spy(new Manager());
        List<Manager> managerList = new ArrayList<>();
        managerList.add(testmanager1);

        when(managerService.getManagers()).thenReturn(managerList);
        when(testmanager1.getManagerID()).thenReturn((long) 1);

        assertFalse(managerService.checkIfTournamentCodeIsInList((long) 1,"12"));
    }
}