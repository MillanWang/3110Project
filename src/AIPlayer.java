import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class represents an AI player in the game RISK
 */
public class AIPlayer extends Player implements Serializable {

    /**
     * A constructor that sets the player class variables.
     *
     * @param name
     * @ param String  the name of the player
     */
    public AIPlayer(String name) {
        super(name);
    }

    @Override
    public void draftChoice(Game game){
        //Do all the draft behavior. Distribute all troops
        //game.displayMessage("Whatever happened during the draft phase")
        Random rando = new Random();
        LinkedList<Territory> territories = this.getTerritories();
        LinkedList<Territory> drafted = new LinkedList<>();
        Collections.shuffle(territories);

        //this.bonusTroops();

        while (super.numTroops > 0){
            int troopToDraft =  rando.nextInt(super.numTroops) + 1;
            //Get ONE terry from shuffled territories list
            territories.peek().changeTroops(troopToDraft);
            //Send random number of troops there
            super.numTroops -= troopToDraft;
            //Add the terry to the list of drafted
            if (!drafted.contains(territories.peek())) drafted.add(territories.peek());
            //Send that terry to the back of the linked list
            territories.add(territories.pop());

        }

        //Build string from the drafted territories list
        String str = "";
        for (Territory territory: drafted){
            str += territory.getTerritoryName() + " now has " + territory.getTroops() + " troops" + "\n";
        }

        game.displayMessage(str);
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

        if (attackStarters!=null && !attackStarters.isEmpty()) Collections.shuffle(attackStarters);

        for (Territory t : attackStarters){
            if (t.getTroops()>=2 ) return t;
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
        Territory current = attackables.get(0);

        for(int i = 0; i < attacker.getAttackableNeighbours().size() ; i++) {
            if ( minTroops > attackables.get(i).getTroops() && attackables.get(i).getTroops() <= attacker.getTroops())
                current = attackables.get(i);
            }
        return current;
    }

    /**
     * Returns if the current AIPlayer wants to diceFight right now
     * Only want to dice fight if attacker has more troops than 5 troops
     *
     * @param attacker The attacker territory
     * @return If the AI player wants to continue with the attack or not
     */
    public boolean wantToDiceFight(Territory attacker){
        return attacker.getTroops() > 5;
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

            if (currentHighestTroops < t.getTroops() && t.hasFriendlyNeighbour()){
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
        Territory current = null;

        for (Territory t : fortifiables){

            if (t.getAttackableNeighbours()!= null && maxAttackableNeighbours < t.getAttackableNeighbours().size()){
                current = t;
                maxAttackableNeighbours = t.getAttackableNeighbours().size();
            }
        }
        return current;
    }

}
