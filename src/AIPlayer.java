import java.util.LinkedList;
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

    /**
     * Determines if the current AI player wants to fortify
     *
     * @return Boolean of if the AI player wants to fortify or not
     */
    public boolean wantToFortify(){
        //BEHAVIOR
        //Only want to fortify iff the player has a territory that has 1 or 2 troops
        for (Territory t : super.getTerritories()){
            if(t.getTroops() < 3) return true;
        }
        return false;
    }

    /**
     * Returns the fortify troop giver for the current AI Players fortify stage
     * Will be a territory surrounded by friendly territories if possible,
     * otherwise the territory with the most troops on it
     *
     * @return The selected fortify giver territory
     */
    public Territory findFortifyGiver(){
        Territory current = null;
        int currentHighestTroops = 0;

        //First priority, territories surrounded by only friendlies
        for (Territory t : super.getTerritories()){
            if (t.surroundedByFriendlies() && t.getTroops()>1) return t;

            if (currentHighestTroops < t.getTroops()){
                current = t;
                currentHighestTroops = t.getTroops();
            }
        }
        //Here when there are no territories surrounded by friendlies
        //Now just choose whoever has the most troops.
        return current;

    }

    /**
     * Selects the receiver for the fortify, given the giver territory
     * Reciever is whoever has the most attackable neighbours. This is the most vulnerable to attack
     * AI uses the fortify step defensively
     *
     * @param giver The fortify giver territory
     * @return The fortify receiver territory
     */
    public Territory findFortifyReceiver(Territory giver){
        LinkedList<Territory> fortifiables = super.getFortifiableTerritories(giver);
        int maxAttackableNeighbours = 0;
        Territory current = null;

        for (Territory t : fortifiables){
            System.out.println(t.getInfoString());
            if (maxAttackableNeighbours < t.getAttackableNeighbours().size()){
                current = t;
                maxAttackableNeighbours = t.getAttackableNeighbours().size();
            }
        }
        return current;
    }

}
