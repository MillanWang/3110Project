import java.util.*;

/**
 * A class representing a player in the game.
 */
public class Player {
    private String name; // The name of the player.
    private List<Territory> territories; // A list of the territories the player occupies.
    private DefaultWorldMap defaultWM;


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
    private Boolean hasNorthAmerica(){
        if (territories.contains(defaultWM.getNorthAmerica())){
            return true;
        }else{
            return false;
        }
    }

    private Boolean hasSouthAmerica(){
        if (territories.contains(defaultWM.getSouthAmerica())){
            return true;
        }else{
            return false;
        }
    }

    private Boolean hasAfrica(){
        if (territories.contains(defaultWM.getAfrica())){
            return true;
        }else{
            return false;
        }
    }

    private Boolean hasAsia(){
        if (territories.contains(defaultWM.getAsia())){
            return true;
        }else{
            return false;
        }
    }

    private Boolean hasEurope(){
        if (territories.contains(defaultWM.getEurope()))
        {
            return true;
        }else{
            return false;
        }
    }

    private Boolean hasOceania(){
        if (territories.contains(defaultWM.getOceania())){
            return true;
        }else{
            return false;
        }
    }
    /**
     * this method removes the a territory from the list of territories owned by the player
     * @ param territory the territory to be removed
     **/

    //TERRITORY HAS TWO R'S IN IT

    public void addTerritory(Territory territory){
        if (territory =! null) {
            this.territories.add(territory);
        }
        else {
            System.out.println(" Cannot add invalid territory");
        }
    }
    public void removeTerritory(Territory territory) {
        if (!(hasLost())) {
            territories.remove(territory);
            if(hasLost()){
                handleDeadPlayer();
            }
        }else{
            handleDeadPlayer();
        }
    }

    private int decideBonus(){
        if (hasAfrica()) {
            return 3;
        }else if (hasAsia()) {
            return 7;
        }else if (hasEurope()) {
            return 5;
        }else if (hasNorthAmerica()) {
            return 5;
        }else if (hasOceania()) {
            return 2;
        }else if (hasSouthAmerica()){
            return 2;
        }
        return 0;
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
        System.out.println("THE KING IS DEAD");
    }



    /**
     * Calculates the number of troops the player should receive in this turn.
     * It is based on the number of countries he occupies and any continents he fully controls
     */
    public void  bonusTroops(){
        Math.max(decideBonus (), (int) Math.floor(this.territories.size() /  3));
    }


}
