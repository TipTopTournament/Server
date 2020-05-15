package ch.uzh.ifi.seal.soprafs20.integration;

import ch.uzh.ifi.seal.soprafs20.entity.Bracket;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.repository.BracketRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TournamentIntegration {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private BracketRepository bracketRepository;

    @InjectMocks
    TournamentService tournamentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addGamesToBracket() {
        List<Integer> differentPlayers = new ArrayList<Integer>();
        differentPlayers.add(2);
        differentPlayers.add(4);
        differentPlayers.add(8);
        differentPlayers.add(16);

        String tournamentCode = "12345678";

        // for all amounts of players

        for(int i : differentPlayers) {
            Bracket bracket = tournamentService.createBracket(i, tournamentCode, 8, "10:00", 10, 20);

            assertEquals(i-1, bracket.getBracketList().size());

            for (Game game : bracket.getBracketList()) {
                assertEquals(tournamentCode, game.getTournamentCode());
            }
        }
    }

    @Test
    public void checkTimes() {
        String tournamentCode = "12345678";

        Bracket bracket = tournamentService.createBracket(4, tournamentCode, 1, "10:00", 10, 20);

        // Check if times are correct

        assertEquals("10:00", bracket.getBracketList().get(0).getStartTime());
        assertEquals("10:30", bracket.getBracketList().get(1).getStartTime());
        assertEquals("11:00", bracket.getBracketList().get(2).getStartTime());
    }
    
}
