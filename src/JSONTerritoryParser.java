import java.util.ArrayList;

public class JSONTerritoryParser {
    private String territoryName;
    private String continentName;
    private int numberOfTerritoriesInContinent;
    private int continentControlBonus;
    private ArrayList<String> neighbours;

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public int getNumberOfTerritoriesInContinent() {
        return numberOfTerritoriesInContinent;
    }

    public void setNumberOfTerritoriesInContinent(int numberOfTerritoriesInContinent) {
        this.numberOfTerritoriesInContinent = numberOfTerritoriesInContinent;
    }

    public int getContinentControlBonus() {
        return continentControlBonus;
    }

    public void setContinentControlBonus(int continentControlBonus) {
        this.continentControlBonus = continentControlBonus;
    }

    public ArrayList<String> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(ArrayList<String> neighbours) {
        this.neighbours = neighbours;
    }
}
