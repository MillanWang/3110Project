import java.util.LinkedList;
import java.util.List;

/**
 * Territory class holds each territory name, how many troops on it, who owns it and it's neighbours.
 */
public class Territory {

    /**
     * Builder pattern to create territory classes
     */
    public static class Builder{

        private String territoryName, continentName;
        private int numberOfTerritoriesInContinent, continentControlBonus;

        /**
         * Constructor for the Builder
         *
         * @param territoryName Name of the territory
         */
        public Builder(String territoryName){
            this.territoryName = territoryName;
        }

        public Builder setContinentName(String continentName){
            this.continentName = continentName;
            return this;
        }
        public Builder setNumTerritoriesInContinent(int num){
            this.numberOfTerritoriesInContinent = num;
            return this;
        }
        public Builder setContinentControlBonus(int bonus){
            this.continentControlBonus = bonus;
            return this;
        }

        public Territory build(){
            Territory terry = new Territory();
            terry.territoryName = this.territoryName;
            terry.continentName = this.continentName;
            terry.numberOfTerritoriesInContinent = this.numberOfTerritoriesInContinent;
            terry.continentControlBonus = this.continentControlBonus;
            return terry;
        }
    }


    private int troops;//troops and armies are the same thing
    private String territoryName;// the name of the territory
    private String owner;// the name of the player who owns this territory
    private List<Territory> neighboursList;//each territory should know it's neighbours
    private String continentName;
    private int numberOfTerritoriesInContinent, continentControlBonus;

    /**
     * Initializing the class states in this constructor
     * Private due to builder pattern
     */
    private Territory(){
        this.troops = 0;
        this.neighboursList = new LinkedList<>();
    }

    /**
     * Getter method for troops
     * @return troops
     */
    public int getTroops() {
        return troops;
    }

    /**
     * Sets the number of troops
     *
     * @param troops The new number of troops to be added to the troops field
     */
    public void setTroops(int troops){
        this.troops = troops;
    }

    /**
     * Changes the number of troops in the troops field
     *
     * @param troops The number of troops to be added to the troops field
     */
    public void changeTroops(int troops) {
        this.troops += troops;
    }

    /**
     * Getter method for numberOfTerritoriesInContinent
     *
     * @return field value numberOfTerritoriesInContinent
     */
    public int getNumberOfTerritoriesInContinent() {
        return numberOfTerritoriesInContinent;
    }

    /**
     * Getter method for the continent control bonus field
     *
     * @return field value continentControlBonus
     */
    public int getContinentControlBonus() {
        return continentControlBonus;
    }

    /**
     * Returns the string of this territory's name
     *
     * @return territoryName
     */
    public String getTerritoryName() {
        return territoryName;
    }

    /**
     * Returns the continent of the current territory
     *
     * @return the name of the Continent
     */
    public String getContinentName(){
        return continentName;
    }

    /**
     * Getter method for the owner
     *
     * @return the owner string
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Setter for the owner field
     *
     * @param owner The name of the owner of this territory
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Getter method for the neighboursList field
     * @return neighboursList field
     */
    public List<Territory> getNeighboursList() {
        return neighboursList;
    }

    /**
     * adding a new territory neighbour to the neighboursList
     * This operation is two directional
     *
     * Setting one neighbour automatically sets a neighbour relationship in the other direction.
     *
     * @param newNeighbours The neighbour to be added.
     */
    public void addNeighbours(Territory newNeighbours) {
        this.neighboursList.add(newNeighbours);
        newNeighbours.neighboursList.add(this);
    }

    /**
     * this method would print out each territory name with
     * the number of troops and who owns it
     */
    public void printInfo(){
        StringBuilder sb = new StringBuilder(territoryName);
        sb.append("                                                                     ");
        sb.insert(25,"\tTroops: " + troops + "\t\tOwner: " + owner );
        System.out.println(sb);
    }

    /**
     * Returns a string of the territory name, number of troops, and the owner
     *
     * Example
     * Ontario   Troops: 5   Owner: Player1
     *
     * @return String of the territory's info
     */
    public String getInfoString(){
        StringBuilder sb = new StringBuilder(territoryName);
        sb.append("      ");
        sb.append("\tTroops: " + troops);
        sb.append("        ");
        sb.append("\tOwner: " + owner );
        return sb.toString();
    }

    /**
     * This method should return a list of territories that can be attacked
     *
     * @return a list of attackable territories. Null if no attackable neighbours
     */
    public List<Territory> getAttackableNeighbours(){
        if(this.getTroops() <= 1 ) return null; //Cannot start attack from terry with 1 troop
        List attackableNeighbours = new LinkedList();
        for (Territory ter : neighboursList) {

            if (!(this.owner.equals(ter.getOwner()))){
                attackableNeighbours.add(ter);
            }
        }
        if (attackableNeighbours.isEmpty()){
            return null;
        }else {
            return attackableNeighbours;
        }
    }

    /**
     * returns the maximum number of dice that can be rolled for this territory during a diceFight
     *
     * @return Max number of dice for attacker to roll
     */
    public int maxDiceToRoll(){
        return Math.min(3, this.getTroops()-1);
    }

    /**
     * Returns a String array of the given list of territories
     *
     * @param territories List of territories to be converted
     * @return String array of all territories in the parameter list
     */
    public String[] getNeighbourStringArray(List<Territory> territories){
        String[] stringArray = new String[territories.size()];
        for (int i = 0; i < territories.size() ; i++){
            stringArray[i] = territories.get(i).getTerritoryName();
        }
        return stringArray;
    }

    /**
     * Returns a string array of all attackable neighbour territories
     *
     * @return String array of attackable neighbour territories
     */
    public String[] attackableNeighbours() {
        LinkedList attackableNeighbours = new LinkedList<Territory>();
        for (Territory ter : neighboursList) {
            if (!(ter.getOwner().equals(this.owner))) {
                attackableNeighbours.add(ter);
            }
        }
        return getNeighbourStringArray(attackableNeighbours);
    }

    /**
     * Determines if the territory is surrounded by friendly territories. Used by AI player
     *
     * @return boolean of if the territory is surrounded by friendlies
     */
    public boolean surroundedByFriendlies(){
        for (Territory t : neighboursList){
            if (!t.getOwner().equals(owner)) return false;
        }
        return true;
    }
}
