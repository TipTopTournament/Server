package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.Participant;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ParticipantPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


/**
 * Manager Controller
 * This class is responsible for handling all REST request that are related to the manager.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }
    // der einfachheit halber
    @GetMapping("/managers")
    @ResponseStatus(HttpStatus.OK)
    @Query
    public List<ManagerGetDTO> getAllManagers() {
        List<ManagerGetDTO> allManagers = new ArrayList<>();

        for (Manager manager : managerService.getManagers()) {
            allManagers.add(DTOMapper.INSTANCE.convertEntityToManagerGetDTO(manager));
        }
        return allManagers;
    }

    @GetMapping("/managers/{managerId}")
    @ResponseStatus(HttpStatus.OK)
    @Query
    public ManagerGetDTO getManager(@PathVariable("managerId") long id) {
        Manager manager = managerService.getManagerById(id);
    	return DTOMapper.INSTANCE.convertEntityToManagerGetDTO(manager);
    }


    @PostMapping("/managers")
    @ResponseStatus(HttpStatus.CREATED)
    public void createManager(@RequestBody ManagerPostDTO managerPostDTO) {

        // convert API user to internal representation
        Manager managerInput = DTOMapper.INSTANCE.convertManagerPostDTOtoEntity(managerPostDTO);

        // create manager
        Manager createdManager = managerService.createManager(managerInput);
    }

    @PutMapping("/managers/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ManagerPutDTO loginManager(@RequestBody ManagerPostDTO managerPostDTO) {
        Manager manager = DTOMapper.INSTANCE.convertManagerPostDTOtoEntity(managerPostDTO);

        if (managerService.checkUsernameAndPassword(manager.getUsername(), manager.getPassword())) {
            return DTOMapper.INSTANCE.convertEntityToManagerPutDTO(managerService.getManagerByUsername(manager.getUsername()));
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password were incorrect");
        }
    }
    
    @PutMapping("/managers/{managerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateManagerStatus(@RequestBody ManagerPutDTO managerPutDTO,@PathVariable("managerId") long id) {
    	
    	//Used to get the status
    	Manager managerStatus = DTOMapper.INSTANCE.convertManagerPutDTOToEntity(managerPutDTO);
    	UserStatus status = managerStatus.getUserStatus();
    	
    	//Update the Managers Status
    	managerService.updateStatus(id,status);
    }
}
