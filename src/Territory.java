import java.util.ArrayList;
import java.util.List;

public class Territory {
    private int troops;
    private String territoryName;
    private String owner;
    private List<Territory> neighboursList;


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

    public void setTroops(int troops){
        this.troops = troops;
    }

    /**
     * @param troops
     * troops could be positive or negative
     */
    public void changeTroops(int troops) {
        this.troops += troops;
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
     * @param
     * @return a neighbour in the neighbour list
     */
    public Territory getNeighbour(String territoryName) {
        for (Territory t : neighboursList){
            if (territoryName.equals(t.getTerritoryName())){
                return t;
            }
        }
        System.out.println("No territory with this name");
        return null; //To compile
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
        StringBuilder sb = new StringBuilder(territoryName);
        sb.append("                                                                     ");
        sb.insert(25,"\tTroops: " + troops + "\t\tOwner: " + owner );
        System.out.println(sb);
    }
    /**
     *Favour
     * This method should get the a list of neighbours owner can attack
     * */
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
    public void printAttackableNeighbours(){
        for (Territory territory:getAttackableNeighbours()){
            territory.printInfo();
        }
    }

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
