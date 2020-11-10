import java.util.ArrayList;

//This classes are for creating territory objects and are madw with JSON files

public class JSONTerritoryListParser {
    private ArrayList<JSONTerritoryParser> territories;

    public ArrayList<JSONTerritoryParser> getTerritories() {
        return territories;
    }

    public void setTerritories(ArrayList<JSONTerritoryParser> territories) {
        this.territories = territories;
    }
}
