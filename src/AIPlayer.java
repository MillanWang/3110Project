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

    /**
     * This method performs all the draft behavior for the ai player.
     *
     * @param game the current game
     */
    @Override
    public void draftChoice(Game game){

        Random rando = new Random();
        LinkedList<Territory> territories = this.getTerritories();
        LinkedList<Territory> drafted = new LinkedList<>();
        Collections.shuffle(territories);

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


    /**
     * This method checks if the ai player wants to attack.
     *
     * @param game the current game
     * @return True or false if the AI player has an attack starter
     */
    @Override
    public boolean wantToAttack(Game game){
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
     * The attachable neighbour with the lowest number of troops will be chosen
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
     * This method gets an attack starter for the ai player.
     *
     * @param game the current game
     * @return returns the name of the territory to start an attack from
     */
    @Override
    public String chooseAttackStarter(Game game){
        return this.findAttackStarter().getTerritoryName();
    }

    /**
     * This method gets a defender from the other player.
     *
     * @param game the current game
     * @param attackStarter the current attack starter
     * @return returns the name of the territory to start an attack from
     */
    @Override
    public String chooseAttackDefender(Game game, String attackStarter){
        return this.findAttackDefender(getTerritory(attackStarter)).getTerritoryName();
    }

    /**
     * Returns if the current AIPlayer wants to diceFight right now
     * Only want to dice fight if attacker has more troops than 5 troops
     *
     * @param attackerCommaDefender Comma separated string of the attacker,defender
     * @return If the AI player wants to continue with the attack or not
     */
    @Override
    public boolean wantToDiceFight(Game game, String attackerCommaDefender){
        return this.getTerritory(attackerCommaDefender.split(",")[0]).getTroops() > 5;
    }

    /**
     * This method gets the dice roll for the AI player.
     *
     * @param territory the current territory
     * @return returns the name of the territory to start an attack from
     */
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
     * This method gets the dice roll for the AI player.
     *
     * @param territory the current territory
     * @param game the current game
     *
     * @return returns the attacker dice roll
     */
    @Override
    public int getAttackerDice(Game game, Territory territory){
        return this.chooseNumDice(territory);
    }

    /**
     * This method gets the defender dice roll.
     *
     * @param territory the current territory
     * @param game the current game
     *
     * @return returns the defender dice roll
     */
    @Override
    public int getDefenderDice(Game game, Territory territory){
        if (territory.getTroops() >= 2){
            return 2;
        } else{
            return 1;
        }
    }

    /**
     * This method gets the number for troops to take over.
     *
     * @param game the current game
     * @param attackerDice attacker's dice roll
     * @param numTroops the current number of troops
     *
     * @return returns the take over troops
     */
    @Override
    public int getTakeoverTroops(Game game, int attackerDice, int numTroops){
        return attackerDice;
    }

    /**
     * This method checks if the AI player is able to fortify.
     *
     * @param game the current game
     *
     * @return returns true or false if the AI player is able to fortify
     */
    @Override
    public boolean wantToFortify(Game game){
        //BEHAVIOR
        //Only want to fortify iff the player has a territory that has 1 or 2 troops
        return this.findFortifyGiver() != null;
    }

    /**
     * Determines if the current AI player wants to fortify
     *
     * @return Boolean of if the AI player wants to fortify or not
     */
    public boolean aiWantToFortify(){
        //BEHAVIOR
        //Only want to fortify iff the player has a territory that has 1 or 2 troops
        for (Territory t : super.getTerritories()){
            if(t.getTroops() < 3) return true;
        }
        return false;
    }

    /**
     * This method chooses the fortify giver for the AI player
     *
     * @param game the current game
     *
     * @return returns name of the fortify giver
     */
    public String chooseFortifyGiver(Game game){
        return this.findFortifyGiver().getTerritoryName();
    }

    /**
     * This method chooses the fortify reciever for the AI player
     *
     * @param game the current game
     *
     * @return returns the name of the fortify receiver
     */
    @Override
    public String chooseFortifyReceiver(Game game, String fortifyGiver){
        return this.findFortifyReceiver(this.getTerritory(fortifyGiver)).getTerritoryName();
    }

    /**
     * This method gets the number of troops the AI player can fortify with
     *
     * @param game the current game
     * @param territory the current territory
     *
     * @return returns the number of troops for the fortify stage
     */
    @Override
    public int getFortifyTroops(Game game, Territory territory){
        Random rando = new Random();
        return rando.nextInt(territory.getTroops()-1) + 1;
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
