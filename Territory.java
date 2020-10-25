import java.util.ArrayList;
import java.util.List;

public class Territory {
    private int troops;
    private String territoryName;
    private String owner;
    private List<Territory> neighboursList;

    //MILLAN CONSTRUCTOR
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
     */
    //NAME CHANGED. NOT ADDING A LIST, ADDING AN INDIVIDUAL NEIGHBOUR
    public void addNeighbours(Territory newNeighbours) {
        this.neighboursList.add(newNeighbours);
    }

    public void printInfo(){
        String s = territoryName + "\t\tTroops: " + troops + "\t\tOwner: " + owner;
        System.out.println(s);
    }
    /**
     *Favour
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
    public String printAttackableNeighbours(){
        String attackableNeighbours="{ ";
        for (Territory territory:getAttackableNeighbours()){
            attackableNeighbours += territory.getTerritoryName()+", ";
        }
        attackableNeighbours += " }";
        return attackableNeighbours;
    }

}
