import org.codehaus.jackson.map.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is to represent the entire playing map in the game RISK for
 * the group project in SYSC 3110
 *
 * Loads territory information about the map from external JSON file.
 */
public class GenericWorldMap {

    //List of all territories
    private ArrayList<Territory> allTerritories;

    /**
     * Constructor for the DefaultWorldMap class
     * This map is modeled after the real world and
     */
    public GenericWorldMap() {
        allTerritories = new ArrayList<>();


        //Loading in the custom map from file. Need to ask for user path. SMALL EXAMPLE FOR NOW.
        String jsonText = "";
        try {
            jsonText = new String(Files.readAllBytes(Paths.get(".//src//DefaultMap.riskmap")));
        } catch (Exception e){
            System.out.println("Invalid file path");
        }


        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONTerritoryListParser jsonTerritoryListParser = mapper.readValue(jsonText, JSONTerritoryListParser.class);
            //^^Arraylist containing info for individual territory objects

            //Creating new territories from all elements in the list
            Territory currentTerritoryToAdd;
            for (JSONTerritoryParser t : jsonTerritoryListParser.getTerritories()){
                currentTerritoryToAdd = new Territory.Builder(t.getTerritoryName())
                                                        .setContinentName(t.getContinentName())
                                                        .setNumTerritoriesInContinent(t.getNumberOfTerritoriesInContinent())
                                                        .setContinentControlBonus(t.getContinentControlBonus())
                                                        .build();

                //Setting up neighbour relationships
                // addNeighbours(terry) if neighbour currently exists. If not, relationship will be made when the terry is initialized
                //addNeighbours is reflective so this process should always work

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
    public ArrayList<Territory> getAllTerritories(){
        return allTerritories;
    }

    /**
     * Prints out all territories with the number of troops inside and the owner
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

    public static void main(String[] args){
        GenericWorldMap g = new GenericWorldMap();
        System.out.println(g.getAllTerritoriesString());

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
    private Territory getTerritory(String territoryName){
        for (Territory t : allTerritories){
            if (t.toString().equals(territoryName)){
                return t;
            }
        }
        return null;
    }
}
