import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class AIPlayerTest {

    private Game game;

    public void setUp() {

        //Setting up game to with AI players. All methods require the aiPlayer to be in a game context

        game = new Game();
        LinkedList<String> aiPlayers = new LinkedList<String>();
        aiPlayers.add("AI1[B0T]");
        aiPlayers.add("AI2[B0T]");
        game.makePlayers(aiPlayers);
    }

    @Test
    public void aiDraftPhase() {
        setUp();

        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();

        int initialTotalTroops = 0;
        for (Territory t: ai.getTerritories()) initialTotalTroops+=t.getTroops();

        ai.aiDraftPhase();

        int finalTotalTroops = 0;
        for (Territory t: ai.getTerritories()) finalTotalTroops+=t.getTroops();

        assertEquals(0, ai.getNumTroops());//Ensure no troops left to give

        assertTrue(finalTotalTroops >= initialTotalTroops+3);//Drafting should always distribute at least 3 troops
    }


    @Test
    public void wantToAttack() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        ai.aiDraftPhase();

        assertTrue(ai.wantToAttack());//Will want to attack if has an attack starter with more than 2 troops on it
    }

    @Test
    public void findAttackStarter() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        ai.aiDraftPhase();

        assertTrue(ai.findAttackStarter() != null); //Should be able to find an attack starter initially
    }

    @Test
    public void findAttackDefender() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        ai.aiDraftPhase();

        assertTrue(ai.findAttackDefender(ai.findAttackStarter()) != null);
        //Attack starters should always find a defender
    }

    @Test
    public void wantToDiceFight() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        Territory attacker = ai.getTerritories().get(0);
        attacker.changeTroops(100); //Always want to dice fight with more than 5 troops

        assertTrue(ai.wantToDiceFight(attacker));

    }

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

    @Test
    public void wantToFortify() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();

        //will want to fortify iff there is a territory with less than 3 troops on it
        ai.getTerritories().get(0).changeTroops(-20);
        //While not possible to get negative troops, will be detected as less than 3
        assertTrue(ai.wantToFortify());
    }

    @Test
    public void findFortifyGiver() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        //Will always be able to find one. Behavior in the comments of the method
        assertNotNull(ai.findFortifyGiver());
    }

    @Test
    public void findFortifyReceiver() {
        setUp();
        AIPlayer ai = (AIPlayer) game.getCurrentPlayerObject();
        assertNotNull(ai.findFortifyReceiver(ai.findFortifyGiver()));
        //Should always be able to find receiver if there is a giver
    }
}