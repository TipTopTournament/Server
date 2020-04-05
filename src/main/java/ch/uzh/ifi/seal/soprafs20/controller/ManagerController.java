package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Manager;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ManagerPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.ManagerService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/managers/{managerId}")
    @ResponseStatus(HttpStatus.OK)
    @Query
    public ManagerGetDTO getManager(@PathVariable("managerId") long id) {
        return managerService.getManagerById(id);
    }

    @PostMapping("/managers")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createUser(@RequestBody ManagerPostDTO managerPostDTO) {

        // convert API user to internal representation
        Manager managerInput = DTOMapper.INSTANCE.convertManagerPostDTOtoEntity(managerPostDTO);

        // create manager
        Manager createdManager = managerService.createManager(managerInput);
    }
}
