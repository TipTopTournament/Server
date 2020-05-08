package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "vorname", target = "vorname")
    @Mapping(source = "nachname", target = "nachname")
    @Mapping(source = "password", target = "password")
    Manager convertManagerPostDTOtoEntity(ManagerPostDTO managerPostDTO);

    @Mapping(source = "userStatus", target = "userStatus")
    Manager convertManagerPutDTOToEntity(ManagerPutDTO managerPutDTO);
    
    @Mapping(source = "managerID", target = "managerID")
    @Mapping(source = "username", target = "username")
    ManagerGetDTO convertEntityToManagerGetDTO(Manager manager);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "managerID", target = "managerID")
    ManagerPutDTO convertEntityToManagerPutDTO(Manager manager);
    
    @Mapping(source = "vorname", target = "vorname")
    @Mapping(source = "nachname", target = "nachname")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "licenseNumber", target = "licenseNumber")
    Participant convertParticipantPostDTOtoEntity(ParticipantPostDTO participantPostDTO);
    
    @Mapping(source = "userState", target = "userState")
    Participant convertParticipantPutDTOToEntity(ParticipantPutDTO participantPutDTO);
    
    @Mapping(source = "participantID", target = "participantID")
    @Mapping(source = "licenseNumber", target = "licenseNumber")
    @Mapping(source = "vorname", target = "vorname")
    @Mapping(source = "nachname", target = "nachname")
    @Mapping(source = "userState", target = "userState")
    @Mapping(source = "code", target = "code")
    ParticipantGetDTO convertEntityToParticipantGetDTO(Participant participant);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "participantID", target = "participantID")
    ParticipantPutDTO convertEntityToParticipantPutDTO(Participant participant);

    @Mapping(source = "breakDuration", target = "breakDuration")
    @Mapping(source = "tournamentName", target = "tournamentName")
    @Mapping(source = "gameDuration", target = "gameDuration")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "numberTables", target = "numberTables")
    @Mapping(source = "amountOfPlayers", target = "amountOfPlayers")
    @Mapping(source = "informationBox", target = "informationBox")
    @Mapping(source = "location", target = "location")
    Tournament convertTournamentPostDTOtoEntity(TournamentPostDTO tournamentPostDTO);

    @Mapping(source = "tournamentId", target = "tournamentId")
    @Mapping(source = "winner", target = "winner")
    @Mapping(source = "tournamentName", target = "tournamentName")
    @Mapping(source = "tournamentCode", target = "tournamentCode")
    @Mapping(source = "breakDuration", target = "breakDuration")
    @Mapping(source = "gameDuration", target = "gameDuration")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "numberTables", target = "numberTables")
    @Mapping(source = "amountOfPlayers", target = "amountOfPlayers")
    @Mapping(source = "informationBox", target = "informationBox")
    @Mapping(source = "location", target = "location")
    TournamentGetDTO convertEntityToTournamentGetDTO(Tournament tournament);

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "gameState", target = "gameState")
    @Mapping(source = "score1", target = "score1")
    @Mapping(source = "score2", target = "score2")
    @Mapping(source = "participant1", target = "participant1")
    @Mapping(source = "participant2", target = "participant2")
    @Mapping(source = "tournamentCode", target = "tournamentCode")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "wins", target = "wins")
    @Mapping(source = "losses", target = "losses")
    @Mapping(source = "pointsScored", target = "pointsScored")
    @Mapping(source = "pointsConceded", target = "pointsConceded")
    StatisticsGetDTO convertEntityToStatisticsGetDTO(Statistics statistics);

    @Mapping(source = "wins", target = "wins")
    @Mapping(source = "losses", target = "losses")
    @Mapping(source = "pointsScored", target = "pointsScored")
    @Mapping(source = "pointsConceded", target = "pointsConceded")
    @Mapping(source = "playerState", target = "playerState")
    LeaderboardGetDTO convertEntityToLeaderboardGetDTO(Leaderboard leaderboard);
}
