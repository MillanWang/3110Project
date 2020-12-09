import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Map;

/**
 * This class is to represent the entire playing map in the game RISK for
 * the group project in SYSC 3110
 *
 * Loads territory information about the map from external JSON file.
 */
public class GenericWorldMap implements Serializable {

    private LinkedList<Territory> allTerritories;
    private String imageFileName;

    /**
     * Constructor for the DefaultWorldMap class
     * This map is modeled after the real world and
     */
    public GenericWorldMap(String mapName) {
        allTerritories = new LinkedList<>();

        //Loading in the custom map from file. Need to ask for user path. SMALL EXAMPLE FOR NOW.
        String jsonText = "";
        try {
            //InputStream in = getClass().getResourceAsStream("/TestSmallWorldMap.txt"); //SMALLER MAP FOR EASIER TESTING
            //InputStream in = getClass().getResourceAsStream("/DefaultMap.txt");
            InputStream in = getClass().getResourceAsStream(mapName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = reader.readLine();
            }
            jsonText = sb.toString();

        } catch (Exception e){
            System.out.println("Invalid file path");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONTerritoryListParser jsonTerritoryListParser = mapper.readValue(jsonText, JSONTerritoryListParser.class);
            //^^Linkedlist containing info for individual territory objects

            imageFileName = jsonTerritoryListParser.getImageFileName();

            //Creating new territories from all elements in the list
            Territory currentTerritoryToAdd;
            for (JSONTerritoryParser t : jsonTerritoryListParser.getTerritories()){
                currentTerritoryToAdd = new Territory.Builder(t.getTerritoryName())
                        .setContinentName(t.getContinentName())
                        .setNumTerritoriesInContinent(t.getNumberOfTerritoriesInContinent())
                        .setContinentControlBonus(t.getContinentControlBonus())
                        .build();

                for (String territoryName : t.getNeighbours()){
                    if (getTerritory(territoryName)!=null){
                        currentTerritoryToAdd.addNeighbours(getTerritory(territoryName));
                        //^^Adding the neighbour to the current territory
                    }//Do nothing if neighbour does not exist yet.
                }
                allTerritories.add(currentTerritoryToAdd);
            }
        } catch (Exception e){
            System.out.println(e);
            System.out.println("Invalid map file given. Use official maps only that follow the strict JSON map making template");
        }
        //Randomization of the order of territories.
        Collections.shuffle(allTerritories);
    }

    /**
     * Returns the list consisting of all territories
     * This will be used to distribute territories to players in the Game class
     *
     * @return list containing all territories in Africa
     */
    public LinkedList<Territory> getAllTerritories(){
        return allTerritories;
    }

    /**
     * Prints out all territories with the number of troops inside and the owner
     *
     * Example:
     * Ontario   Troops: 5   Owner: Player1
     *
     */
    public String getAllTerritoriesString(){
        String allTerritoriesString = "";
        for (Territory t: allTerritories){
            allTerritoriesString += t.getInfoString() + "\n";
        }
        return allTerritoriesString;
    }

    /**
     * Returns the territory with a matching name to the given string.
     * Returns null if there is no matching name found.
     * Only used in initial setup process
     *
     * @param territoryName     Name of the territory
     * @return The territory object, null if there is not one currently
     */
    public Territory getTerritory(String territoryName){
        for (Territory t : allTerritories){
            if (t.getTerritoryName().equals(territoryName)){
                return t;
            }
        }
        return null;
    }

    /**
     * Prints out all territories with the number of troops inside and the owner
     * Mostly useful for testing new maps and ensureing that all territories are established properly
     *
     * Example:
     * Ontario   Troops: 5   Owner: Player1
     *
     */
    public void printAllTerritories(){
        for (Territory t: allTerritories){
            t.printInfo();
        }
    }

    /**
     * Verifies if the loaded map can be played or not
     *
     * @return True if the map can be played, false otherwise
     */
    public boolean verifyMap(){

        if (this.getAllTerritories().size() < 6 ) return false; //Map must have at least 6 territories to be legitimate

        Map continentAndTimesSeen = new HashMap<String, Integer>();
        Map continentAndContinentBonus = new HashMap<String, Integer>();

        //Iterate through all territories.
        for(Territory t : allTerritories){
            if (t.getNeighboursList().size() == 0) return false; //Illegal to have no neighbours

            //Ensuring that all neighbour relationships are two directional
            for (Territory neighbour : t.getNeighboursList()){
                if (!verifyTwoWayNeighbour(t,neighbour)) return false;
            }

            String cName = t.getContinentName(); // getting the continent the territory belongs to

            // check if we have seen a territory that belongs to the continent previously
            if (continentAndTimesSeen.containsKey(cName)) {
                // if we have seen the a territory in the continent previously increment our counter
                continentAndTimesSeen.put(cName, ((int) continentAndTimesSeen.get(cName) + 1));

                //Verifies that the continent control bonus will be consistent between territories
                if ((int) continentAndContinentBonus.get(cName) != t.getContinentControlBonus()) return false;
            }else{
                // if we are yet to see the continent put it as a new key value pair
                continentAndTimesSeen.put(cName, 1);
                continentAndContinentBonus.put(cName, t.getContinentControlBonus());
            }

            //Cannot see territories in a given continent more times than there are territories in that continent
            if(t.getNumberOfTerritoriesInContinent() < (int)continentAndTimesSeen.get(cName))return false;

        }

        LinkedList<Territory> seenTerritories = new LinkedList<>();
        seenTerritories.add(this.getAllTerritories().get(0));
        int current = 0;

        while(current < seenTerritories.size()){
            for (Territory t : seenTerritories.get(current).getNeighboursList()){
                if (!seenTerritories.contains(t)){
                    //Only add to seen if not in list already
                    seenTerritories.add(t);
                }
            }
            current++;
        }

        return seenTerritories.size() == this.getAllTerritories().size();
    }

    private boolean verifyTwoWayNeighbour(Territory territory1, Territory territory2){
        return territory1.getNeighboursList().contains(territory2) && territory2.getNeighboursList().contains(territory1);
    }

    public String getImageFileName() { return imageFileName; }
}
