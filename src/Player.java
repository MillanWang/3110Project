import java.util.ArrayList;

/**
 * A class representing a player in the game.
 */
public class Player {
    private String name; // The name of the player.
    private ArrayList<Teritory> territories; // A list of the territories the player occupies.
    private int troops; // number of troops a player has
    
//Mention the bonus troops too

    /**
     * A constructor that sets the player class variables.
     */
    public Player(String name) {
        this.name = name;
        clearTerritory();
    }

    public String getName() {
        return this.name;
    }

    /**
     * Clears the countries list.
     */
    public void clearTerritory() {
        territories = new ArrayList<>();
    }


    public void removeTerritory(Teritory teritory) {
        territories.remove(teritory);
    }

    public int getTroops() {
        return troops;
    }
    
    public void bonusTroops(int bonus){
        if (true){ // will be updated once we review the condition for adding bonus troops
            troops +=  bonus;
            }
    }
}
