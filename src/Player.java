import java.util.*;

/**
 * A class representing a player in the game.
 */
public class Player {
    private String name; // The name of the player.
    private LinkedList<Territory> territories; // A list of the territories the player occupies.
    //^^^^^^^^^^^Switched to linked list because of the cycling of territories during setup phase. No longer requires complete shifting
    private DefaultWorldMap defaultWM;
    private int numTroops;


    /**
     * A constructor that sets the player class variables.
     *
     * @ param String  the name of the player
     */
    public Player(String name) {
        this.name = name;
        territories = new LinkedList<>();
        defaultWM = new DefaultWorldMap();
    }

    /**
     * this method returns the name of the player
     *
     * @ return  name players name
     */
    public String getName() {
        return this.name;
    }

    public void changeNumTroops(int changeAmount){
        numTroops += changeAmount;
    }

    public List getTerritories() {
        return territories;
    }

    public Territory getTerritory(String territoryName) {
        for (Territory territory : territories) {
            if (territory.getTerritoryName().equals(territoryName)) return territory;
        }
        return null;
    }



    /**
     * this method removes the a territory from the list of territories owned by the player
     *
     * @ param territory the territory to be removed
     **/

    public void addTerritory(Territory territory) {
        if (territory == null) {
            System.out.println(" Cannot add, invalid territory");
        } else {
            this.territories.add(territory);
        }
    }

    /**
     *
     *
     * @param territory
     */
    public void removeTerritory(Territory territory) {
        //The current player has 1 or more territories before removal
        territories.remove(territory);
    }

    /**
     * @return Whether the player has lost all territories.
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

    private int continentBonus() {
        int bonus = 0;
        if (hasAfrica()) bonus += 3;
        if (hasAsia()) bonus += 7;
        if (hasEurope()) bonus += 5;
        if (hasNorthAmerica()) bonus += 5;
        if (hasAustralia()) bonus += 2;
        if (hasSouthAmerica()) bonus += 2;

        return bonus;
    }
    /**
     * The following continent methods are used to check if the player owns all territories in a continent
     * This will be used to determine the troop bonus for complete continent control
     *
     * @return True if the entire continent is owned by the player, false otherwise
     */
    private Boolean hasNorthAmerica() { return territories.contains(defaultWM.getNorthAmerica()); }
    private Boolean hasSouthAmerica() { return territories.contains(defaultWM.getSouthAmerica()); }
    private Boolean hasAfrica() { return territories.contains(defaultWM.getAfrica()); }
    private Boolean hasAsia() { return territories.contains(defaultWM.getAsia()); }
    private Boolean hasEurope() {return territories.contains(defaultWM.getEurope()); }
    private Boolean hasAustralia() { return territories.contains(defaultWM.getAustralia()); }

    /**
     * TAHER'S WORK
     *
     * @return string that contains all the territories owned by the player
     * This method would be used in the attack() stage
     */
    public void printAllTerritories() {
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

    public void draftPhase(){
        bonusTroops();
        while (numTroops > 0) {
            Scanner input = new Scanner(System.in);
            System.out.println("You currently own the following territories");
            printAllTerritories();
            System.out.println("You have " + numTroops + " troops available to give out");
            System.out.println("Select territory in which you would like to send troops to.");
            String territoryName = input.nextLine();

            if(getTerritory(territoryName)==null){
                System.err.println("INVALID TERRITORY NAME");

            }else if (territoryName.equals(getTerritory(territoryName).getTerritoryName())) {
                System.out.println("Select amount of troops to send");
                //Sending troops to selected territory

                int troopNumber = 0;
                while(true) {
                    try {
                        troopNumber = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.err.println("Don't enter characters or strings. Numbers only");
                        input.next();
                    }

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

    public void printAttackStarters(){
        List<Territory> attackStarters = getAttackStarters();
        for (Territory territory : attackStarters){
            territory.printInfo();
        }
    }

    public boolean canStartAttack(String territoryName){
        return getTerritory(territoryName).getAttackableNeighbours() != null;
    }
}
