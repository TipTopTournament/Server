package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Tournament;
import ch.uzh.ifi.seal.soprafs20.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Manager Service
 * This class is the "worker" and responsible for all functionality related to the user of type Manager
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class ManagerService {

    private ManagerRepository managerRepository;

    @Autowired
    public ManagerService(@Qualifier("managerRepository") ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<Manager> getManagers() {
        return this.managerRepository.findAll();
    }

    public Manager getManagerById(Long id) {
        for (Manager manager : getManagers()) {
            if (manager.getManagerID().equals(id)) {
                return manager;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No manager found with this Id");
    }

    public Manager getManagerByUsername(String username) {
        for (Manager manager : getManagers()) {
            if (manager.getUsername().equals(username)) {
                return manager;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No manager found with this username");
    }

    public Manager createManager(Manager newManager) {
        newManager.setToken(UUID.randomUUID().toString());

        if (!checkIfUsernameIsTaken(newManager)) {
            newManager = managerRepository.save(newManager);
            managerRepository.flush();
            return newManager;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
    }

    private boolean checkIfUsernameIsTaken(Manager managerToTest) {
        for (Manager manager : getManagers()) {
            if (manager.getUsername().equals(managerToTest.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfManagerIdExists(Long id) {
        for (Manager manager : getManagers()) {
            if (manager.getManagerID().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    public void updateStatus(Long id, UserStatus status) {
    	if(checkIfManagerIdExists(id)) {
    		Manager manager = getManagerById(id);
    		manager.setUserStatus(status);
    	}
    	
    	else {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No manager found with this Id");
    	}
    }
    
    public boolean checkUsernameAndPassword(String username, String password) {
        for (Manager manager : getManagers()) {
            if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void addTournamentToManager(Tournament tournament, Manager manager) {
        manager.addTournamentToTournamentList(tournament);

        managerRepository.save(manager);
        managerRepository.flush();
    }
}
