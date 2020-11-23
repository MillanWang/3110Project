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

    public boolean wantToAttack(){
        return this.findAttackStarter() != null;
    }

    /**
     * Returns an AI player's attack starter territory.
     * The first attack starter with at least 4 troops on it will be chosen
     *
     * @return The chosen attack starter territory
     */
    public Territory findAttackStarter(){
        LinkedList<Territory> attackStarters = super.getAttackStarters();

        for (Territory t : attackStarters){
            if (t.getTroops()>=4 ) return t;
        }
        return null;
    }

    /**
     * Returns the chosen defender territory for a given attacker
     * The attackable neighbour with the lowest number of troops will be chosen
     *
     * @param attacker The territory that starts the attack
     * @return The selected defender of the attack
     */
    public Territory findAttackDefender(Territory attacker){
        LinkedList<Territory> attackables = attacker.getAttackableNeighbours();
        int minTroops = attackables.get(0).getTroops();
        Territory current = attacker.getAttackableNeighbours().get(0);

        for(int i = 1; i < attacker.getAttackableNeighbours().size() ; i++) {
            if ( minTroops > attackables.get(i).getTroops())
                current = attackables.get(i);
            }
        return current;
    }

    /**
     * Returns if the current AIPlayer wants to diceFight right now
     * Only want to dice fight if attacker has more troops than (defender troops)-3
     *
     * @param attacker The attacker territory
     * @param defender The defender territory
     * @return If the AI player wants to continue with the attack or not
     */
    public boolean wantToDiceFight(Territory attacker,Territory defender){
        return attacker.getTroops() > defender.getTroops() - 3;
    }

    public int chooseNumDice(Territory territory){
        if (territory.getTroops() >=4 ){
            return 3;
        } else if (territory.getTroops() == 3 ){
            return 2;
        } else {
            return 1;
        }
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
     * Receiver is whoever has the most attackable neighbours. This is the most vulnerable to attack
     *
     *
     * @param giver The fortify giver territory
     * @return The fortify receiver territory
     */
    public Territory findFortifyReceiver(Territory giver){
        LinkedList<Territory> fortifiables = super.getFortifiableTerritories(giver);
        int maxAttackableNeighbours = 0;
        Territory current = fortifiables.get(0);

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
