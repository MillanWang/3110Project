import java.util.Random;

/**
 * This class represents an AI player in the game RISK
 */
public class AIPlayer extends Player{

    /**
     * A constructor that sets the player class variables.
     *
     * @param name
     * @ param String  the name of the player
     */
    public AIPlayer(String name) {
        super(name);
    }

    /**
     * Does the draft part of a phase for the current AI player's turn
     *
     * @return String containing the full result of the AIPlayers Draft
     */
    @Override
    public String aiDraftPhase(){
        String str = "";
        Random r = new Random();
        String[] territories = this.getTerritoriesList();
        this.bonusTroops();
        int draftTroops = this.getNumTroops();

        while (draftTroops > 0){
            String randomTerritory, randomTroop;
            randomTerritory = territories[r.nextInt(territories.length)];
            int troopToDraft =  r.nextInt(draftTroops) + 1;
            randomTroop = "" + troopToDraft;
            System.out.println(randomTerritory);
            System.out.println(randomTroop);
            str = str +  this.draftPhase(randomTerritory, randomTroop) + "\n";
            System.out.println(str);
            draftTroops = draftTroops - troopToDraft;
        }

        return str;
    }
}
