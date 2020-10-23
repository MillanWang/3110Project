import java.util.*;

/**
 * A class representing a player in the game.
 */
public class Player {
    private String name; // The name of the player.
    private List<Teritory> territories; // A list of the territories the player occupies.
    private final Map<String, Integer> continentBonuses= new HashMap<String, Integer>() {{
        put("Africa", 3);
        put("Asia", 7);
        put("Europe", 5);
        put("North America", 5);
        put("Oceania", 2);
        put("South America", 2);
    }}; // A map with the troop bonuses for every continent, by its name.


    /**
     * A constructor that sets the player class variables.
     * @ param String  the name of the player
     */
    public Player(String name) {
        this.name = name;
        territories = new ArrayList<>();
    }

    /**
     * this method returns the name of the player
     * @ return  name players name
     */
    public String getName() {
        return this.name;
    }

    /**
     * this method removes the a territory from the list of territories owned by the player
     * @ param territory the territory to be removed
     **/
    
    //TERRITORY HAS TWO R'S IN IT
    
    // YOU SHOULD ADD addTerritory method TOO
    public void removeTerritory(Teritory teritory) {
        if (!(hasLost())) {
            territories.remove(teritory);
            if(hasLost()){
                handleDeadPlayer();
            }
        }else{
            handleDeadPlayer();
        }
    }

     /**
     * TAHER'S WORK
     * @return string that contains all the territories owned by the player
     * This method would be used in the attack() stage
     */
    public String getAllTerritories(){
        String allTerritories=" ";
        for (Territory t: territories){
            allTerritories += t.getTerritoryName()+", ";
        }
        return allTerritories;
    }

    /**
     * @return Whether the player has lost all territories.
     */
    private  boolean hasLost() {
        return territories.isEmpty();
    }

    /**
     * this method handles the dead player state
     */
    private  void handleDeadPlayer() {
        // additional implementation may be needed.
        System.out.println("THE KING IS DEAD");
    }



    /**
     * this method handles the bonus troops stage of the risk game from owning all territories in a continent
     * @ param bonus the amount of bonus troops player received
     */
    public int bonusTroops(String cName){ 
        // i need more information to complete this
        return 1;
    }

    /**
     * Calculates the number of troops the player should receive in this turn.
     * It is based on the number of countries he occupies and any continents he fully controls.
     * @param continents The list of continents in the game
     * @return The number of new troops the player should receive in this turn.
     */
    public int getBonusTroops(Collection<Continent> continents) {
        int continentBonuses = 0;
        for (Continent continent : continents) {
            if (this.territories.containsAll(continent.getTerritories()))
                continentBonuses += bonusTroops(continent.getName());

        }
        return this.territories.size() / 3 + continentBonuses;
    }

}
