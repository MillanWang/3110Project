/**
 * A class representing a player in the game.
 */
public class Player {
    private String name; // The name of the player.
    private ArrayList<Teritory> teritories; // A list of the teritories the player occupies.
    
//Mention how many troops does each player have
//Mention the bonus troops too    
    /**
     * A constructor that sets the player class variables.
     */
    public Player(String name) {
        this.name = name;
        clearTeritory();
    }

    public String getName() {
        return this.name;
    }

    /**
     * Clears the countries list.
     */
    public void clearTeritory() {
        territories = new ArrayList<>();
    }


    public void removeTerritory(Teritory teritory) {
        teritories.remove(teritory);
    }
