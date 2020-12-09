import java.util.ArrayList;

//This class is used for creating territory objects and is made with JSON files

public class JSONTerritoryListParser {
    private ArrayList<JSONTerritoryParser> territories;
    private String imageFileName;

    public String getImageFileName() { return imageFileName; }

    public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }

    public ArrayList<JSONTerritoryParser> getTerritories() {
        return territories;
    }

    public void setTerritories(ArrayList<JSONTerritoryParser> territories) {
        this.territories = territories;
    }
}
