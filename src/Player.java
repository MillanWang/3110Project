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

    /*CONTROLLER MESSAGE
        Controller calls the setter method on controllerMessage
        ^^^Return this field to the game method that calls it
        This should hold stuff like wantToAttack, attackStarterChoice, wantToFortify, etc.

    */









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
     * Calculates the number of troops the player should receive in this turn.
     * It is based on the number of countries he occupies and any continents he fully controls
     * and the territories / 3
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
     * WORK IN PROGRESS
     *
     * @param game The game obj to be used to publish events
     */
    public void draftChoice(Game game){
        //Make an event with the currentPlayer's territories and the numTroops=Message
        //game.draftEvent
        game.draftEvent();
        game.displayMessage(this.getName() + " currently has " + this.getNumTroops());
    }





















    /**
     * WORK IN PROGRESS
     * Determines if the current player wants to attack or not. Asks the player directly
     *
     * @return if the player wants to attack or not
     */
    public boolean wantToAttack(){
        return true;
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

    public LinkedList<Territory> getFortifyGivers(){
        LinkedList<Territory> givers = new LinkedList<>();
        for (Territory t : territories){
            if (t.getTroops()>1) givers.add(t);
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

    public boolean equals(Object o){
        Player compared = (Player) o;

        //Comparing all owned territories
        for (int i = 0; i<this.getTerritories().size(); i++){
            if (!this.getTerritories().get(i).equals(compared.getTerritories().get(i))) return false;
        }

        return this.getName().equals(compared.getName());
    }

}
