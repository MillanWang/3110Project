import java.io.Serializable;
import java.util.*;

/**
 * A class representing a player in the game.
 */
public class Player implements Serializable {
    private String name; // The name of the player.
    private LinkedList<Territory> territories; // A list of the territories the player occupies.
    protected int numTroops;
    private String controllerMessage;


    /**
     * A constructor that sets the player class variables.
     *
     * @ param String  the name of the player
     */
    public Player(String name) {
        this.name = name;
        territories = new LinkedList<>();
    }

    /**
     * this method returns the name of the player
     *
     * @ return  name players name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the territory object given the territory name
     *
     * @param territoryName The name of the territory
     * @return The territory object
     */
    public Territory getTerritory(String territoryName){
        for (Territory t : territories){
            if (territoryName.equals(t.getTerritoryName())){
                return t;
            }
        }
        System.out.println("No territory with this name");
        return null; //To compile
    }

    /**
     * Returns the description of player owned territories
     *
     * @return The description of all player owned territory
     */
    public String getTerritoriesString(){
        if (territories.size() > 0) {
            String str = "";
            for (Territory t : territories) {
                str +="\n"+ t.getInfoString();
            }
            return str;
        }else {
            return "Player owns no territory";
        }

    }

    /**
     * Returns the of player owned territories
     *
     * @return The list of all player owned territory
     */
    public LinkedList<Territory> getTerritories(){ return territories;}

    /**
     * Returns the  of player owned territories
     *
     * @return The description of all player owned territory
     */
    public String[] getTerritoriesList(){ return getTerritoryStringArray(territories); }


    /**
     * this method adds a territory to the list of territories owned by player
     *
     * @param territory the territory to be added
     **/
    public void addTerritory(Territory territory) {
        if (territory == null) {
            System.out.println(" Cannot add, invalid territory");
        } else {
            this.territories.add(territory);
        }
    }

    /**
     * this method removes a territory from the list of territories owned by player
     *
     * @param territory territory to be removed
     */
    public void removeTerritory(Territory territory) {
        //The current player has 1 or more territories before removal
        territories.remove(territory);
    }

    /**
     * checks if player has lost possession of all territories owned
     *
     * @return  true if the player has lost all territories.
     */
    public boolean hasLost() {
        return territories.isEmpty();
    }

    /**
     * Used by controller to get GUI information to the player
     *
     * @param controllerMessage The message from the GUI
     */
    public void setControllerMessage(String controllerMessage) {
        this.controllerMessage = controllerMessage;
    }

    /**
     * This method increases the number of troops the player has
     * with the appropriate bonus
     */
    public void bonusTroops() {
        numTroops += continentBonus() + (Math.max((int) Math.floor(this.territories.size() / 3), 3));
    }


    /**
     * calculates bonuses gotten from occupying an entire continent
     *
     * @return the bonus to be added to player's troops
     */
    private int continentBonus() {
        int bonus = 0;
        Map continentAndTimesSeen = new HashMap<String, Integer>();
        // the string is the continent name and the integer is how many territories from the continent the player has(counter)
        for (Territory t : territories) { // iterating through the player owned territories
            String cName = t.getContinentName(); // getting the continent the territory belongs to

            // check if we have seen a territory that belongs to the continent previously
            if (continentAndTimesSeen.containsKey(cName)) {
                // if we have seen the a territory in the continent previously increment our counter
                continentAndTimesSeen.put(cName, ((int) continentAndTimesSeen.get(cName) + 1));
            }else{
                // if we are yet to see the continent put it as a new key value pair
                continentAndTimesSeen.put(cName, 1);
            }

            // if the player has all the territories in the continent increment bonus
            if(t.getNumberOfTerritoriesInContinent() == (int)continentAndTimesSeen.get(cName)){
                bonus += t.getContinentControlBonus();
            }
        }
        return bonus;
    }

    /**
     * returns the number of troops the player can send
     * to player owned territories
     *
     * @return the number of troops player has to draft
     */
    public int getNumTroops() {
        return numTroops;
    }

    /**
     * Sets up the players territories with troops
     * Number of troops is determined by the number of players
     * This method will only be called once by the Game class during the setup phase
     *
     * @param numPlayers The number of players in the game
     */
    public void setupPlayer(int numPlayers) {
        //Initialize the number of troops
        if (numPlayers == 2) {
            numTroops = 50;
        } else if (numPlayers == 3) {
            numTroops = 35;
        } else if (numPlayers == 4) {
            numTroops = 30;
        } else if (numPlayers == 5) {
            numTroops = 25;
        } else {//6 Players
            numTroops = 20;
            //numTroops = 5;//SMALLER TESTING VALUE
        }

        //Approximately evenly distribute troops to each owned territory
        while (numTroops > 0) {
            territories.peek().changeTroops(1);
            territories.add(territories.pop());
            numTroops--;
        }
        //numTroops will always be greater than number of territories
        //Each territory will now have at least one troop on it
    }

    /**
     * this method starts the draft phase of the game.
     * Player is given a certain number of troops that can be deployed to the territories player owns
     *
     */
    public String draftPhase(String territoryName, String troops){
        getTerritory(territoryName).changeTroops(Integer.parseInt(troops));
        numTroops -= Integer.parseInt(troops);

        return territoryName + " now has " + getTerritory(territoryName).getTroops() + " troops after adding " + troops;
    }

    /**
     * Determines the current players draft choice
     *
     * @param game The game obj to be used to publish events
     */
    public void draftChoice(Game game){
        game.draftEvent();
        game.displayMessage(this.getName() + " currently has " + this.getNumTroops() + " troops left to give");
    }

    /**
     * Determines if the current player wants to attack or not. Asks the player directly
     *
     * @return if the player wants to attack or not
     */
    public boolean wantToAttack(Game game){
        if (this.getAttackStarters()==null){
            //No attack starters. Cannot attack
            game.displayMessage("No available territories can start an attack");
            return false;
        }


        game.attackOrQuit();
        //^^Publishes event to GUI. GUI response to controller which changes the field in current player
        return controllerMessage.equals("attack");
    }

    /**
     * This method will only be called by the territory class to determine
     * if the player can attack neighbors
     *
     * @return a list of territories player can use to start an attack
     */
    public LinkedList<Territory> getAttackStarters() {
        LinkedList attackStarters = new LinkedList<Territory>();
        for (Territory ter : territories) {

            if ((ter.getTroops() > 1) && !(ter.getAttackableNeighbours() == null)) {
                attackStarters.add(ter);
            }
        }
        if (attackStarters.isEmpty()) return null;

        return attackStarters;
    }

    /**
     * This method gets an attack starter for the current player.
     *
     * @param game the current game
     * @return returns the name of the territory to start an attack from
     */
    public String chooseAttackStarter(Game game){
        game.chooseAttackStarter();
        //^^This event will change the string in controller message to the user selected attackStarter

        return this.controllerMessage;
    }

    /**
     * This method gets a defender from the other player.
     *
     * @param game the current game
     * @param attackStarter the current attack starter
     * @return returns the name of the territory to start an attack from
     */
    public String chooseAttackDefender(Game game, String attackStarter){
        game.chooseAttackDefender(attackStarter);
        //^^This event will change the string in controller message to the user selected attackStarter

        return this.controllerMessage;
    }

    /**
     * Returns if the current Player wants to diceFight right now
     * Only want to dice fight if attacker has more troops than 5 troops
     *
     * @param attackerCommaDefender Comma separated string of the attacker,defender
     * @return If the AI player wants to continue with the attack or not
     */
    public boolean wantToDiceFight(Game game, String attackerCommaDefender){
        game.wantToDiceFight(attackerCommaDefender);
        //^^This event will change the string in controllerMessage. Empty if no fight. diceFight if want to fight

        return this.controllerMessage.equals("diceFight");
    }

    /**
     * This method gets the dice roll for the current player.
     *
     * @param territory the current territory
     * @param game the current game
     *
     * @return returns the attacker dice roll
     */
    public int getAttackerDice(Game game, Territory territory){
        game.chooseAttackerDice(Math.min(3 , territory.getTroops() - 1), territory.getOwner(), territory.getTerritoryName());
        //^^The number of dice that the player wants to roll will be written into controller message

        return Integer.parseInt(controllerMessage);
    }

    /**
     * This method gets the defender dice roll.
     *
     * @param territory the current territory
     * @param game the current game
     *
     * @return returns the defender dice roll
     */
    public int getDefenderDice(Game game, Territory territory){
        game.chooseDefenderDice(Math.min(territory.getTroops(), 2), territory.getOwner(), territory.getTerritoryName());
        //^^The number of dice that the player wants to roll will be written into controller message

        return Integer.parseInt(game.getCurrentMessage());
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
    public int getTakeoverTroops(Game game, int attackerDice, int numTroops){
        game.takeoverTerritoryEvent(attackerDice, numTroops);
        return Integer.parseInt(controllerMessage);
    }

    /**
     * This method checks if the current player is able to fortify.
     *
     * @param game the current game
     *
     * @return returns true or false if the AI player is able to fortify
     */
    public boolean wantToFortify(Game game){
        //Must have 2 or more territories to fortify

        LinkedList<Territory> fortifyGivers = this.getFortifyGivers();



        if(fortifyGivers==null || fortifyGivers.size() < 2){
            game.displayMessage("No territories available to fortify");
            return false;
        }

        game.fortifyOrQuit();
        return controllerMessage.equals("fortify");
    }

    /**
     * This method chooses the fortify giver for the current player
     *
     * @param game the current game
     *
     * @return returns name of the fortify giver
     */
    public String chooseFortifyGiver(Game game){
        game.chooseFortifyGivers();

        return this.controllerMessage;
    }

    /**
     * This method chooses the fortify receiver for the current player
     *
     * @param game the current game
     *
     * @return returns the name of the fortify receiver
     */
    public String chooseFortifyReceiver(Game game, String fortifyGiver){
        game.chooseFortifyReceivers(fortifyGiver);

        return this.controllerMessage;
    }

    /**
     * This method gets the number of troops the current player can fortify with
     *
     * @param game the current game
     * @param territory the current territory
     *
     * @return returns the number of troops for the fortify stage
     */
    public int getFortifyTroops(Game game, Territory territory){
        game.chooseFortifyTroops(territory.getTroops() - 1);

        return Integer.parseInt(this.controllerMessage);
    }

    /**
     * Returns a LIST/STRINGARRAY of territories that can be fortified from the given territory
     *
     * @param rootTerritory The territory that supplies the fortified troops
     */
    public LinkedList<Territory> getFortifiableTerritories(Territory rootTerritory){

        LinkedList<Territory> fortifiables = new LinkedList<>();
        fortifiables.add(rootTerritory);
        int current = 0;

        while(current < fortifiables.size()){
            for (Territory t : fortifiables.get(current).getNeighboursList()){
                if (t.getOwner().equals(rootTerritory.getOwner()) && !fortifiables.contains(t)){
                    //Only add to fortifiables if owners match and not in list already
                    fortifiables.add(t);
                }
            }
            current++;
        }
        fortifiables.pop();//Removes the rootTerritory. Cannot fortify to self. Always at front of list
        return fortifiables;
    }

    /**
     * Returns a list of fortify givers the current player has
     *
     * @return A linked list of  fortify givers
     */
    public LinkedList<Territory> getFortifyGivers(){
        LinkedList<Territory> givers = new LinkedList<>();
        for (Territory t : territories){
            if (t.getTroops()>1 && t.hasFriendlyNeighbour()) givers.add(t);
        }
        return !givers.isEmpty()? givers : null;
    }

    /**
     * This returns a string array containing the names of the territories
     *
     * @param territories linked list of territories
     * @return String array containing territory names
     */
    public static String[] getTerritoryStringArray(LinkedList<Territory> territories){
        String[] stringArray = new String[territories.size()];
        for (int i = 0; i < territories.size() ; i++){
            stringArray[i] = territories.get(i).getTerritoryName();
        }
        return stringArray;
    }

    /**
     * The equals method for the player class
     *
     * @return The true or false if the object is equal to this object
     */
    public boolean equals(Object o){
        Player compared = (Player) o;

        //Comparing all owned territories
        for (int i = 0; i<this.getTerritories().size(); i++){
            if (!this.getTerritories().get(i).equals(compared.getTerritories().get(i))) return false;
        }

        return this.getName().equals(compared.getName());
    }

}
