import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class AIPlayerTest {

    Game game;

    public void setUp() throws Exception {

        //Setting up game to with AI players. All methods require the aiPlayer to be in a game context

        game = new Game();
        LinkedList<String> aiPlayers = new LinkedList<String>();
        aiPlayers.add("AI1[B0T]");
        aiPlayers.add("AI2[B0T]");
        game.makePlayers(aiPlayers);
    }

    @Test
    public void aiDraftPhase() {
        try{setUp();} catch (Exception e){}
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
    }

    @Test
    public void findAttackStarter() {
    }

    @Test
    public void findAttackDefender() {
    }

    @Test
    public void wantToDiceFight() {
    }

    @Test
    public void chooseNumDice() {
    }

    @Test
    public void wantToFortify() {
    }

    @Test
    public void findFortifyGiver() {
    }

    @Test
    public void findFortifyReceiver() {
    }
}