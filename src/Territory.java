import java.util.ArrayList;
import java.util.List;

/**
 * Territory class holds each territory name, how many troops on it, who owns it and it's neighbours.
 */
public class Territory {
    private int troops;//troops and armies are the same thing
    private String territoryName;// the name of the territory
    private String owner;// the name of the player who owns this territory
    private List<Territory> neighboursList;//each territory should know it's neighbours

    /**
     * Initializing the class states in this constructor
     * @param territoryName
     */
    public Territory(String territoryName){
        this.territoryName = territoryName;
        this.troops = 0;
        this.neighboursList = new ArrayList<>();
    }

    /**
     * this method would return the number of troops owned by this territory
     * @return troops
     */
    public int getTroops() {
        return troops;
    }

    /**
     * this method would set the number of troops owned by this territory
     * @param troops
     */
    public void setTroops(int troops){
        this.troops = troops;
    }

    /**
     * @param troops
     * the number of troops that is placed on each territory
     * would change in the draft and attack phase
     */
    public void changeTroops(int troops) {
        this.troops += troops;
    }

    /**
     * get the territory name
     * @return territoryName
     */
    public String getTerritoryName() {
        return territoryName;
    }

    /**
     * @return the owner's name
     */
    public String getOwner() {
        return owner;
    }

    /**
     * setting the name of the owner
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @param territoryName
     * @return a territory object that
     * it's name is equal to territoryName
     */
    public Territory getNeighbour(String territoryName) {
        for (Territory territory : neighboursList){
            if (territoryName.equals(territory.getTerritoryName())){
                return territory;
            }
        }
        System.out.println("No territory with this name");
        return null;
    }

    /**
     * adding a new territory neighbour to the neighboursList
     * @param newNeighbours
     */
    public void addNeighbours(Territory newNeighbours) {
        this.neighboursList.add(newNeighbours);
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
     * This method should return a list of territories that can be attacked
     * @return a list of territories
     */
    public List<Territory> getAttackableNeighbours(){
        if(this.getTroops() <= 1 ) return null; //Cannot start attack from terry with 1 troop

        List attackableNeighbours = new ArrayList();
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
     * this method print the information of each
     * possible attackable neighbours (territory)
     */
    public void printAttackableNeighbours(){
        for (Territory territory:getAttackableNeighbours()){
            territory.printInfo();
        }
    }

    /**
     * @param defender
     * @return true or false depends on the situation
     * true: in case the player can attack that territory
     * false: in case the player cannot attack that territory
     */
    public boolean canAttack(String defender){
        List<Territory> attackables = getAttackableNeighbours();
        for(Territory territory: attackables){
            if (territory.getTerritoryName().equals(defender)){
                return true;
            }
        }
        return false;
    }
}
