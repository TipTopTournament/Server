package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.GameState;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerState;
import ch.uzh.ifi.seal.soprafs20.constant.TournamentState;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOMapperTest {


    private Participant testParticipant1;
    private Participant testParticipant2;

    @BeforeEach
    public void setup() {

    // given
    testParticipant1 = new Participant();
    testParticipant2 = new Participant();

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
    }
    /**
     * Manager DTO's
     */
    @Test
    public void testPostDTOManager() {
        // create UserPostDTO
        ManagerPostDTO managerPostDTO = new ManagerPostDTO();
        managerPostDTO.setVorname("Peter");
        managerPostDTO.setNachname("Müller");
        managerPostDTO.setUsername("manager1");
        managerPostDTO.setPassword("eifach");

        // MAP -> Create manager
        Manager manager = DTOMapper.INSTANCE.convertManagerPostDTOtoEntity(managerPostDTO);

        // check content
        assertEquals(manager.getVorname(), managerPostDTO.getVorname());
        assertEquals(manager.getNachname(), managerPostDTO.getNachname());
        assertEquals(manager.getUsername(), managerPostDTO.getUsername());
        assertEquals(manager.getPassword(), managerPostDTO.getPassword());
    }

    @Test
    public void testPutDTOManager() {
        // create UserPutDTO
        ManagerPutDTO managerPutDTO = new ManagerPutDTO();
        managerPutDTO.setToken("000z0z00-yy1y-2222-x3x3-w444w4444w44");
        managerPutDTO.setUserStatus(UserStatus.ONLINE);
//        managerPutDTO.setManagerID(1L);

        // MAP -> Update manager
        Manager manager = DTOMapper.INSTANCE.convertManagerPutDTOToEntity(managerPutDTO);

        // check content
        assertEquals(manager.getToken(), managerPutDTO.getToken());
        assertEquals(manager.getManagerID(), managerPutDTO.getManagerID());
        assertEquals(manager.getUserStatus(), managerPutDTO.getUserStatus());
    }

    @Test
    public void testGetDTOManager() {
        // create Manager
        Manager manager = new Manager();
        manager.setUserStatus(UserStatus.ONLINE);
        manager.setUsername("manager1");

        // MAP -> Create managerGetDTO
        ManagerGetDTO managerGetDTO = DTOMapper.INSTANCE.convertEntityToManagerGetDTO(manager);

        // check content
        assertEquals(managerGetDTO.getUsername(), manager.getUsername());
        assertEquals(managerGetDTO.getUserStatus(), manager.getUserStatus());
    }

    /**
     * Participant DTO's
     */
    @Test
    public void testPostDTOParticipant() {
        // create ParticipantPostDTO
        ParticipantPostDTO participantPostDTO = new ParticipantPostDTO();
        participantPostDTO.setVorname("Peter");
        participantPostDTO.setNachname("Müller");
        participantPostDTO.setLicenseNumber("123456");
        participantPostDTO.setPassword("eifach");

        // MAP -> Create participant
        Participant participant = DTOMapper.INSTANCE.convertParticipantPostDTOtoEntity(participantPostDTO);

        // check content
        assertEquals(participant.getVorname(), participantPostDTO.getVorname());
        assertEquals(participant.getNachname(), participantPostDTO.getNachname());
        assertEquals(participant.getLicenseNumber(), participantPostDTO.getLicenseNumber());
        assertEquals(participant.getPassword(), participantPostDTO.getPassword());
    }

    @Test
    public void testPutDTOParticipant() {
        // create ParticipantPutDTO
        ParticipantPutDTO participantPutDTO = new ParticipantPutDTO();
        participantPutDTO.setToken("000z0z00-yy1y-2222-x3x3-w444w4444w44");
        participantPutDTO.setUserStatus(UserStatus.ONLINE);
        participantPutDTO.setParticipantID(1L);

        // MAP -> Update participant
        Participant participant = DTOMapper.INSTANCE.convertParticipantPutDTOToEntity(participantPutDTO);

        // check content
        assertEquals(participant.getToken(), participantPutDTO.getToken());
        assertEquals(participant.getParticipantID(), participantPutDTO.getParticipantID());
        assertEquals(participant.getUserStatus(), participantPutDTO.getUserStatus());
    }

    @Test
    public void testGetDTOParticipant() {
        // create Participant
        Participant participant = new Participant();
        participant.setUserStatus(UserStatus.ONLINE);
        participant.setVorname("Peter");
        participant.setNachname("Müller");
        participant.setLicenseNumber("123456");
        participant.setCode("12345678");
        participant.setParticipantID(1L);

        // MAP -> Create participantGetDTO
        ParticipantGetDTO participantGetDTO = DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(participant);

        // check content
        assertEquals(participantGetDTO.getUserStatus(), participant.getUserStatus());
        assertEquals(participantGetDTO.getVorname(), participant.getVorname());
        assertEquals(participantGetDTO.getNachname(), participant.getNachname());
        assertEquals(participantGetDTO.getLicenseNumber(), participant.getLicenseNumber());
        assertEquals(participantGetDTO.getCode(), participant.getCode());
        assertEquals(participantGetDTO.getParticipantID(), participant.getParticipantID());
    }

    /**
     * Tournament DTO's
     */
    @Test
    public void testPostDTOTournament() {
        // create TournamentPostDTO
        TournamentPostDTO tournamentPostDTO = new TournamentPostDTO();
        tournamentPostDTO.setTournamentName("Zürich Open");
        tournamentPostDTO.setTournamentState(TournamentState.ACTIVE);
        tournamentPostDTO.setBreakDuration(10);
        tournamentPostDTO.setGameDuration(20);
        tournamentPostDTO.setStartTime("10:00");
        tournamentPostDTO.setNumberTables(2);
        tournamentPostDTO.setAmountOfPlayers(4);
        tournamentPostDTO.setInformationBox("hoi das ist die info");
        tournamentPostDTO.setLocation("Binzmühlestrasse 14, 8050 Zürich, Switzerland");

        // MAP -> Create tournament
        Tournament tournament = DTOMapper.INSTANCE.convertTournamentPostDTOtoEntity(tournamentPostDTO);

        // check content
        assertEquals(tournament.getTournamentName(), tournamentPostDTO.getTournamentName());
        assertEquals(tournament.getTournamentState(), tournamentPostDTO.getTournamentState());
        assertEquals(tournament.getBreakDuration(), tournamentPostDTO.getBreakDuration());
        assertEquals(tournament.getGameDuration(), tournamentPostDTO.getGameDuration());
        assertEquals(tournament.getStartTime(), tournamentPostDTO.getStartTime());
        assertEquals(tournament.getNumberTables(), tournamentPostDTO.getNumberTables());
        assertEquals(tournament.getAmountOfPlayers(), tournamentPostDTO.getAmountOfPlayers());
        assertEquals(tournament.getInformationBox(), tournamentPostDTO.getInformationBox());
        assertEquals(tournament.getLocation(), tournamentPostDTO.getLocation());
    }

    @Test
    public void testGetDTOTournament() {
        // create Tournament
        Tournament tournament = new Tournament();
        tournament.setTournamentName("Zürich Open");
        tournament.setTournamentState(TournamentState.ACTIVE);
        tournament.setTournamentCode("12345678");
        tournament.setBreakDuration(10);
        tournament.setGameDuration(20);
        tournament.setStartTime("10:00");
        tournament.setNumberTables(2);
        tournament.setAmountOfPlayers(4);
        tournament.setInformationBox("hoi das ist die info");
        tournament.setLocation("Binzmühlestrasse 14, 8050 Zürich, Switzerland");
        tournament.setWinner(testParticipant2);


        // MAP -> tournamentGetDTO
        TournamentGetDTO tournamentGetDTO = DTOMapper.INSTANCE.convertEntityToTournamentGetDTO(tournament);

        // check content
        assertEquals(tournamentGetDTO.getTournamentName(), tournament.getTournamentName());
        assertEquals(tournamentGetDTO.getTournamentState(), tournament.getTournamentState());
        assertEquals(tournamentGetDTO.getTournamentCode(), tournament.getTournamentCode());
        assertEquals(tournamentGetDTO.getBreakDuration(), tournament.getBreakDuration());
        assertEquals(tournamentGetDTO.getGameDuration(), tournament.getGameDuration());
        assertEquals(tournamentGetDTO.getStartTime(), tournament.getStartTime());
        assertEquals(tournamentGetDTO.getNumberTables(), tournament.getNumberTables());
        assertEquals(tournamentGetDTO.getAmountOfPlayers(), tournament.getAmountOfPlayers());
        assertEquals(tournamentGetDTO.getInformationBox(), tournament.getInformationBox());
        assertEquals(tournamentGetDTO.getLocation(), tournament.getLocation());
        assertEquals(tournamentGetDTO.getWinner().getParticipantID(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(tournament.getWinner()).getParticipantID());
        assertEquals(tournamentGetDTO.getWinner().getVorname(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(tournament.getWinner()).getVorname());
        assertEquals(tournamentGetDTO.getWinner().getNachname(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(tournament.getWinner()).getNachname());
        assertEquals(tournamentGetDTO.getWinner().getLicenseNumber(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(tournament.getWinner()).getLicenseNumber());
        assertEquals(tournamentGetDTO.getWinner().getCode(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(tournament.getWinner()).getCode());
        assertEquals(tournamentGetDTO.getWinner().getUserStatus(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(tournament.getWinner()).getUserStatus());

    }

    /**
     * Game DTO's
     */

    @Test
    public void testGetDTOGame() {
        // create Game
        Game game = new Game();
        game.setGameId(1L);
        game.setGameState(GameState.FINISHED);
        game.setStartTime("13:00");
        game.setScore1(1);
        game.setScore2(3);
        game.setParticipant1(testParticipant1);
        game.setParticipant2(testParticipant2);
        game.setParticipant1Reported(true);
        game.setParticipant2Reported(true);
        game.setTournamentCode("12345678");

        // MAP -> gameGetDTO
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

        // check content
        assertEquals(gameGetDTO.getGameId(), game.getGameId());
        assertEquals(gameGetDTO.getGameState(), game.getGameState());
        assertEquals(gameGetDTO.getStartTime(), game.getStartTime());
        assertEquals(gameGetDTO.getScore1(), game.getScore1());
        assertEquals(gameGetDTO.getScore2(), game.getScore2());
        assertEquals(gameGetDTO.isParticipant1Reported(), game.isParticipant1Reported());
        assertEquals(gameGetDTO.isParticipant2Reported(), game.isParticipant2Reported());
        assertEquals(gameGetDTO.getTournamentCode(), game.getTournamentCode());

        assertEquals(gameGetDTO.getParticipant1().getVorname(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant1()).getVorname());
        assertEquals(gameGetDTO.getParticipant1().getNachname(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant1()).getNachname());
        assertEquals(gameGetDTO.getParticipant1().getParticipantID(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant1()).getParticipantID());
        assertEquals(gameGetDTO.getParticipant1().getLicenseNumber(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant1()).getLicenseNumber());
        assertEquals(gameGetDTO.getParticipant1().getCode(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant1()).getCode());
        assertEquals(gameGetDTO.getParticipant1().getUserStatus(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant1()).getUserStatus());
        assertEquals(gameGetDTO.getParticipant2().getVorname(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant2()).getVorname());
        assertEquals(gameGetDTO.getParticipant2().getNachname(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant2()).getNachname());
        assertEquals(gameGetDTO.getParticipant2().getParticipantID(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant2()).getParticipantID());
        assertEquals(gameGetDTO.getParticipant2().getLicenseNumber(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant2()).getLicenseNumber());
        assertEquals(gameGetDTO.getParticipant2().getCode(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant2()).getCode());
        assertEquals(gameGetDTO.getParticipant2().getUserStatus(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(game.getParticipant2()).getUserStatus());

    }

    @Test
    public void testPutDTOGame() {
        // create GamePutDTO
        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setScore1(1);
        gamePutDTO.setScore2(3);

        //create Game
        Game game = new Game();
        game.setScore1(gamePutDTO.getScore1());
        game.setScore2(gamePutDTO.getScore2());

        // check content
        assertEquals(game.getScore1(), gamePutDTO.getScore1());
        assertEquals(game.getScore2(), gamePutDTO.getScore2());
    }

    /**
     * Statistics GetDTO
     */

    @Test
    public void testGetDTOStatistics() {
        // create Statistics
        Statistics statistics = new Statistics();
        statistics.setStatisticsID(1L);
        statistics.setWins(1);
        statistics.setLosses(3);
        statistics.setPointsScored(5);
        statistics.setPointsConceded(9);
        statistics.addGameToHistory(new Game());

        // MAP -> statisticsGetDTO
        StatisticsGetDTO statisticsGetDTO = DTOMapper.INSTANCE.convertEntityToStatisticsGetDTO(statistics);

        // check content
        assertEquals(statisticsGetDTO.getStatisticsID(), statistics.getStatisticsID());
        assertEquals(statisticsGetDTO.getWins(), statistics.getWins());
        assertEquals(statisticsGetDTO.getLosses(), statistics.getLosses());
        assertEquals(statisticsGetDTO.getPointsScored(), statistics.getPointsScored());
        assertEquals(statisticsGetDTO.getPointsConceded(), statistics.getPointsConceded());
//        assertEquals(statisticsGetDTO.getHistory(), DTOMapper.INSTANCE.convertEntityToGameGetDTO(statistics.getHistory());

    }

    /**
     * Leaderboard GetDTO
     */

    @Test
    public void testGetDTOLeaderboard() {
        // create Leaderboard
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setParticipant(testParticipant1);
        leaderboard.setWins(1);
        leaderboard.setLosses(3);
        leaderboard.setPointsScored(5);
        leaderboard.setPointsConceded(9);
        leaderboard.setPlayerState(PlayerState.ACTIVE);

        // MAP -> LeaderboardGetDTO
        LeaderboardGetDTO leaderboardGetDTO = DTOMapper.INSTANCE.convertEntityToLeaderboardGetDTO(leaderboard);

        // check content
        assertEquals(leaderboardGetDTO.getWins(), leaderboard.getWins());
        assertEquals(leaderboardGetDTO.getLosses(), leaderboard.getLosses());
        assertEquals(leaderboardGetDTO.getPointsScored(), leaderboard.getPointsScored());
        assertEquals(leaderboardGetDTO.getPointsConceded(), leaderboard.getPointsConceded());
        assertEquals(leaderboardGetDTO.getPlayerState(), leaderboard.getPlayerState());
        assertEquals(leaderboardGetDTO.getParticipant().getParticipantID(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(leaderboard.getParticipant()).getParticipantID());
        assertEquals(leaderboardGetDTO.getParticipant().getNachname(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(leaderboard.getParticipant()).getNachname());
        assertEquals(leaderboardGetDTO.getParticipant().getVorname(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(leaderboard.getParticipant()).getVorname());
        assertEquals(leaderboardGetDTO.getParticipant().getCode(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(leaderboard.getParticipant()).getCode());
        assertEquals(leaderboardGetDTO.getParticipant().getLicenseNumber(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(leaderboard.getParticipant()).getLicenseNumber());
        assertEquals(leaderboardGetDTO.getParticipant().getUserStatus(),
                DTOMapper.INSTANCE.convertEntityToParticipantGetDTO(leaderboard.getParticipant()).getUserStatus());

    }

}
