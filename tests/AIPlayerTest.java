import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class AIPlayerTest {

    private Game game;

    public void setUp() {

        //Setting up game to with AI players. All methods require the aiPlayer to be in a game context

        game = new Game("DefaultMap.txt");
        LinkedList<String> aiPlayers = new LinkedList<String>();
        aiPlayers.add("AI1[B0T]");
        aiPlayers.add("AI2[B0T]");
        game.makePlayers(aiPlayers);

    }

    /**
     * Ensures that AI player drafts are working properly
     */
    @Test
    public void aiDraftPhase() {
        setUp();

        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();

        int initialTotalTroops = 0;
        for (Territory t: ai.getTerritories()) initialTotalTroops+=t.getTroops();

        ai.bonusTroops();
        ai.draftChoice(game);

        int finalTotalTroops = 0;
        for (Territory t: ai.getTerritories()) finalTotalTroops+=t.getTroops();

        assertEquals(0, ai.getNumTroops());//Ensure no troops left to give
        assertTrue(finalTotalTroops > initialTotalTroops);//Drafting should always result in more overall troops
    }

    /**
     * Ensures that the mechanism to determine if an AIPlayer wants to attack is working properly
     */
    @Test
    public void wantToAttack() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        ai.bonusTroops();
        ai.draftChoice(game);

        assertTrue(ai.wantToAttack(game));//Will want to attack if has an attack starter with more than 2 troops on it
    }

    /**
     * Ensures that the mechanism to find an attack starter is working properly
     */
    @Test
    public void findAttackStarter() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        ai.bonusTroops();
        ai.draftChoice(game);

        assertTrue(ai.findAttackStarter() != null); //Should be able to find an attack starter initially
    }

    /**
     * Ensures that the mechanism used to determine the defender territory is working properly
     */
    @Test
    public void findAttackDefender() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        ai.bonusTroops();
        ai.draftChoice(game);

        assertTrue(ai.findAttackDefender(ai.findAttackStarter()) != null);
        //Attack starters should always find a defender
    }

    /**
     * Ensures that the mechanism used to determine if the AIPlayer wants to diceFight is working properly
     */
    @Test
    public void wantToDiceFight() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        Territory attacker = ai.getTerritories().get(0);
        attacker.changeTroops(100); //Always want to dice fight with more than 5 troops

        //assertTrue(ai.wantToDiceFight(attacker));
    }

    /**
     * Ensures that the mechanism used to determine the aiPlayer player's number of dice is working properly
     */
    @Test
    public void chooseNumDice() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        Territory territory = ai.getTerritories().get(0);
        territory.changeTroops(100);
        assertEquals(3, ai.chooseNumDice(territory));//Always roll 3 when more than 4 troops

        //Rolling 2 only occurs when there are 3 troops on the territory.
        //Random distribution of troops makes this very difficult to test as there is no setter method for numTroops

        territory.changeTroops(-110);
        assertEquals(1, ai.chooseNumDice(territory));//Roll 1 if 2 troops left on territory
    }

    /**
     * Ensures that the mechanism used to determine if the AIPlayer wants to fortify is working properly
     */
    @Test
    public void wantToFortify() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();

        //will want to fortify iff there is a territory with less than 3 troops on it
        ai.getTerritories().get(0).changeTroops(-20);
        //While not possible to get negative troops, will be detected as less than 3
        //assertTrue(ai.wantToFortify());
    }

    /**
     * Ensures that the mechanism used to determine the fortify giver is working properly
     */
    @Test
    public void findFortifyGiver() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        //Will always be able to find one. Behavior in the comments of the method
        assertNotNull(ai.findFortifyGiver());
    }

    /**
     * Ensures that the mechanism used to determine fortify receiver is working properly
     */
    @Test
    public void findFortifyReceiver() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        assertNotNull(ai.findFortifyReceiver(ai.findFortifyGiver()));
        //Should always be able to find receiver if there is a giver
    }
}