import java.util.ArrayList;
import java.util.List;
/**
 * Each territory should know it's name, how many troops on it, who owns it and it's neighbours.
 */

public class Territory {
    private int troops;// troops is basically the armies
    private String territoryName;// the territory name
    private String owner;// the owner name of the territory
    private List<Territory> neighboursList;// a list that contains all possible neighbours to the territory

    /**
     * @param territoryName
     * A constructor that will inilize the instance private variables
     */
    public Territory(String territoryName){
        this.territoryName = territoryName;
        this.troops = 0;
        this.neighboursList = new ArrayList<>();
    }

    /**
     * @return troops
     */
    public int getTroops() {
        return troops;
    }

    /**
     * @param troops
     * troops could be positive or negative
     */
    public void changeTroops(int troops) {
        this.troops += troops;
        if((this.troops-troops)<0){
            throw new IllegalArgumentException ("You can't deduct "+troops + "from the actual "+this.troops+" value");
        }
    }

    /**
     * @return territory name
     */
    public String getTerritoryName() {
        return territoryName;
    }

    /**
     * @param territoryName
     */
    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    /**
     * @return the owner's name
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @param index
     * @return a neighbour in the neighbour list
     */
    public Territory getNeighboursList(int index) {
        return this.neighboursList.get(index);
    }

    /**
     *
     * @param newNeighbours
     * this method add a new neighbour to the actual Territory
     */
    public void addNeighbours(Territory newNeighbours) {
        this.neighboursList.add(newNeighbours);
    }

    /**
     * this method will print each territory with how many troops on it and the owner name
     */
    public void printInfo(){
        String s = territoryName + "\t\tTroops: " + troops + "\t\tOwner: " + owner;
        System.out.println(s);
    }
    /**
     * This method should get the a list of neighbours owner can attack
     * */
    public List<Territory> getAttackableNeighbours(){
        if(this.getTroops() == 1 ) return null;
        List attackableNeighbours = new ArrayList();
        for (Territory ter : neighboursList) {
            if (!(this.owner.equals(ter.getOwner()))){
                attackableNeighbours.add(ter);
            }
        }
        return attackableNeighbours;
    }

    /**
     *
     * @return a string that contains all the attackable neighbours
     */
    public String printAttackableNeighbours(){
        String attackableNeighbours="{ ";
        for (Territory territory:getAttackableNeighbours()){
            attackableNeighbours += territory.getTerritoryName()+", ";
        }
        attackableNeighbours += " }";
        return attackableNeighbours;
    }

}