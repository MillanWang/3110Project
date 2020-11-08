import java.util.*;

/**
 * A class representing a player in the game.
 */
public class Player {
    private String name; // The name of the player.
    private LinkedList<Territory> territories; // A list of the territories the player occupies.
    private int numTroops;



    /**
     * A constructor that sets the player class variables.
     *
     * @ param String  the name of the player
     */
    public Player(String name) {
        this.name = name;
        territories = new LinkedList<>();
        //defaultWM = new GenericWorldMap();
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

    public String getTerritories(){
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

    public int getNumTroops() {
        return numTroops;
    }


    /**
     *
     * this method prints out all territories owned by player
     *
     * @return string that contains all the territories owned by the player
     * This method would be used in the attack() stage
    private void printAllPlayerTerritories() {
    for (Territory t : territories) {
    t.printInfo();
    }
    }*/



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
            //***********************************************************************************************
            //WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
            //numTroops = 5; //THIS IS FOR TESTING!!! VERY TEDIOUS WITH A LOT OF TROOPS
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
        //bonusTroops();
        getTerritory(territoryName).changeTroops(Integer.parseInt(troops));
        numTroops -= Integer.parseInt(troops);

        return territoryName + " now has " + getTerritory(territoryName).getTroops() + " troops after adding " + troops;
    }

    /**
     * This method will only be called by the territory class to determine
     * if the player can attack neighbors
     *
     * @return a list of territories player can use to start an attack
     */
    public String[] getAttackStarters() {
        LinkedList attackStarters = new LinkedList<Territory>();
        for (Territory ter : territories) {

            if ((ter.getTroops() > 1) && !(ter.getAttackableNeighbours() == null)) {
                attackStarters.add(ter);
            }
        }
        if (attackStarters.isEmpty()) return null;

        for (int i = 0; i < attackStarters.size(); i ++) System.out.println(attackStarters.get(i));

        return getTerritoryStringArray(attackStarters);


    }

    /**
     * this method prints out the list of territories player can use to start an attack
     public void printAttackStarters(){
     List<Territory> attackStarters = getAttackStarters();
     for (Territory territory : attackStarters){
     territory.printInfo();
     }
     }*/

    /**
     * this method checks if a territory owned by player can be used to start an attack
     *
     * @param territoryName name of the territory to be checked
     * @return True if a territory can be used to start an attack
     */

    public boolean canStartAttack(String territoryName){
        return getTerritory(territoryName).getAttackableNeighbours() != null;
    }

    public String[] getTerritoryStringArray(LinkedList<Territory> territories){
        String[] stringArray = new String[territories.size()];
        for (int i = 0; i < territories.size() ; i++){
            stringArray[i] = territories.get(i).getTerritoryName();
        }
        return stringArray;
    }
}
