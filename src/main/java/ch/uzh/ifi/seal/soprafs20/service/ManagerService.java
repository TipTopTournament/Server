package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.repository.ManagerRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(ManagerService.class);

    private ManagerRepository managerRepository;

    @Autowired
    public ManagerService(@Qualifier("managerRepository") ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<Manager> getManagers() {
        return this.managerRepository.findAll();
    }

    public ManagerGetDTO getManagerById(Long id) {
        for (Manager manager : getManagers()) {
            if (manager.getManagerID().equals(id)) {
                return DTOMapper.INSTANCE.convertEntityToManagerGetDTO(manager);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No manager found with this Id");
    }

    public Manager createManager(Manager newManager) {
        newManager.setToken(UUID.randomUUID().toString());

        if (!CheckIfUsernameIsTaken(newManager)) {
            newManager = managerRepository.save(newManager);
            managerRepository.flush();

            log.debug("Created Information for Manager: {}", newManager);
            return newManager;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
    }

    private boolean CheckIfUsernameIsTaken(Manager managerToTest) {
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
}
