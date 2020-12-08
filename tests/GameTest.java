import junit.framework.TestCase;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class GameTest extends TestCase {

    private Game game;

    public void setUp() throws Exception {
        game = new Game("DefaultMap.txt");
    }


    @Test
    public void testNextTurn() {
        LinkedList list = new LinkedList<String>();
        list.add("Mo");
        list.add("Nemo");
        game.makePlayers(list);
        String previous = game.getCurrentPlayer();
        game.nextTurn();
        // checking that the current player is not the previous player after calling next turn
        assertNotEquals(previous, game.getCurrentPlayer());
    }

    @Test
    public void testHasWinner() {
        // if there is only one player left i.e the current player
        // that player is the winner
        LinkedList list = new LinkedList<String>();
        list.add("Mo");
        game.makePlayers(list);
        assertEquals(0, game.getPlayersList().size());
        assertEquals("Mo", game.getCurrentPlayer());
        assertTrue(game.hasWinner());
    }


    @Test
    public void testTakeoverTerritory() {

        LinkedList list = new LinkedList<String>();
        list.add("Mo");
        list.add("Nemo");
        game.makePlayers(list);

        Territory attacker = game.getCurrentPlayerObject().getTerritories().get(0);
        Territory defender = game.getPlayersList().peek().getTerritories().get(0);
        game.takeoverTerritory(game.getCurrentPlayerObject(), attacker, defender, 3);
        assertEquals(game.getCurrentPlayer(), defender.getOwner());
        assertEquals(22, game.getCurrentPlayerObject().getTerritoriesList().length);
    }

    @Test
    public void testMakePlayers() {
        LinkedList list = new LinkedList<String>();
        list.add("Mo");
        list.add("Nemo");
        game.makePlayers(list);
        // after make players is called the only left in the list of players is the next player
        assertEquals(1, game.getPlayersList().size());
        // since selection of player is random at first either could be the current player
        assertTrue(game.getCurrentPlayer().equals("Nemo") || game.getCurrentPlayer().equals("Mo"));
    }


    @Test
    public void testSaveAndLoad(){

        LinkedList<String> names = new LinkedList<>();
        names.add("first");
        names.add("second[B0T]");
        names.add("third");
        game.makePlayers(names);

        game.saveGame("testfile.txt");
        Game loadedGame = Game.loadGame("testfile.txt");

        assertTrue(game.equals(loadedGame));
    }
}
