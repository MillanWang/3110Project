import java.util.*;

/**
 * A class representing a player in the game.
 */
public class Player {
    private String name; // The name of the player.
    private LinkedList<Territory> territories; // A list of the territories the player occupies.
    // private GenericWorldMap defaultWM;
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
                str += t.getInfoString();
            }
            return str;
        }else {
            return "Player owns no territory";
        }

    }

    public LinkedList getTerritoriesList(){
        return territories;
    }


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

    private void bonusTroops() {
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
     * The following continent methods are used to check if the player owns all territories in a continent
     * This will be used to determine the troop bonus for complete continent control
     *
     * @return True if the entire continent is owned by the player, false otherwise
    private Boolean hasNorthAmerica() { return territories.contains(defaultWM.getNorthAmerica()); }
    private Boolean hasSouthAmerica() { return territories.contains(defaultWM.getSouthAmerica()); }
    private Boolean hasAfrica() { return territories.contains(defaultWM.getAfrica()); }
    private Boolean hasAsia() { return territories.contains(defaultWM.getAsia()); }
    private Boolean hasEurope() {return territories.contains(defaultWM.getEurope()); }
    private Boolean hasAustralia() { return territories.contains(defaultWM.getAustralia()); }
     */


    /**
     *
     * this method prints out all territories owned by player
     *
     * @return string that contains all the territories owned by the player
     * This method would be used in the attack() stage
     */
    private void printAllPlayerTerritories() {
        for (Territory t : territories) {
            t.printInfo();
        }
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
    public void draftPhase(String location, String troops){
        bonusTroops();
        while (numTroops > 0) {
            /**
             Scanner input = new Scanner(System.in);
             System.out.println("You currently own the following territories");
             printAllPlayerTerritories();
             System.out.println("You have " + numTroops + " troops available to give out");
             System.out.println("Select territory in which you would like to send troops to.");
             System.out.println("Territory names are case sensitive ");
             */
            String territoryName = location;

            if(getTerritory(territoryName)==null){
                System.err.println("INVALID TERRITORY NAME");

            }else if (territoryName.equals(getTerritory(territoryName).getTerritoryName())) {
                System.out.println("Select amount of troops to send");
                //Sending troops to selected territory

                int troopNumber =  Integer.parseInt(troops);

                while(true) {
                    /**
                     try {
                     troopNumber = input.nextInt();
                     } catch (InputMismatchException e) {
                     System.err.println("Don't enter characters or strings. Numbers only");
                     input.next();
                     }*/

                    if (troopNumber > 0 && troopNumber <= numTroops) {
                        break;//User gave a valid number of troops
                    } else{
                        System.out.println("Number cannot be less than 1 and cannot be more than " + numTroops);
                    }
                }//End the ask and check for numTroops
                //Valid number of troops is chosen
                getTerritory(territoryName).changeTroops(troopNumber);
                numTroops -= troopNumber;
            } else {
                System.out.println("Invalid territory name! Try again!");
            }
        }//Completely emptied out the players troops
        System.out.println("You have run out of troops. Proceed to attack phase");
        System.out.println("******************************************");
    }

    /**
     * This method will only be called by the territory class to determine
     * if the player can attack neighbors
     *
     * @return a list of territories player can use to start an attack
     */
    public List<Territory> getAttackStarters() {
        List attackstarters = new LinkedList<Territory>();
        for (Territory ter : territories) {
            if ((ter.getTroops() > 1) && !(ter.getAttackableNeighbours() == null)) {
                attackstarters.add(ter);
            }
        }
        return attackstarters;
    }

    /**
     * this method prints out the list of territories player can use to start an attack
     */

    public void printAttackStarters(){
        List<Territory> attackStarters = getAttackStarters();
        for (Territory territory : attackStarters){
            territory.printInfo();
        }
    }

    /**
     * this method checks if a territory owned by player can be used to start an attack
     *
     * @param territoryName name of the territory to be checked
     * @return True if a territory can be used to start an attack
     */

    public boolean canStartAttack(String territoryName){
        return getTerritory(territoryName).getAttackableNeighbours() != null;
    }

}
