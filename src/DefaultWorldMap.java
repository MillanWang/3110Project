import java.util.ArrayList;
import java.util.Collections;

/**
 * Class representing the complete map of the world in the game RISK
 */
public class DefaultWorldMap {
    /**
     * This class is to represent the entire playing map in the game RISK for
     * the group project in SYSC 3110
     *
     * @author J.Anyia, F.Olotu, T.Shabaan, M.Wang
     * 
     */
    //List of all territories
    private ArrayList<Territory> allTerritories;

    //These represent the continents. Important for calculating troop bonuses for complete continental control
    private ArrayList<Territory> northAmerica, southAmerica, africa, europe, asia, australia;

    /**
     * Constructor for the DefaultWorldMap class
     * This map is modeled after the real world and
     */
    public DefaultWorldMap() {
        allTerritories = new ArrayList<>();
        northAmerica = new ArrayList<>();
        southAmerica = new ArrayList<>();
        africa = new ArrayList<>();
        europe = new ArrayList<>();
        asia = new ArrayList<>();
        australia = new ArrayList<>();

        this.setupMap();
    }

    /**
     * Returns the list consisting of all territories that make up NorthAmerica
     * This will be used to calculate complete continental control bonuses (Implemented in Milestone 3)
     *
     * @return list containing all territories in North America
     */
    public ArrayList<Territory> getNorthAmerica(){
        return northAmerica;
    }

    /**
     * Returns the list consisting of all territories that make up SouthAmerica
     * This will be used to calculate complete continental control bonuses (Implemented in Milestone 3)
     *
     * @return list containing all territories in South America
     */
    public ArrayList<Territory> getSouthAmerica(){
        return southAmerica;
    }

    /**
     * Returns the list consisting of all territories that make up Africa
     * This will be used to calculate complete continental control bonuses (Implemented in Milestone 3)
     *
     * @return list containing all territories in Africa
     */
    public ArrayList<Territory> getAfrica(){
        return africa;
    }

    /**
     * Returns the list consisting of all territories that make up Europe
     * This will be used to calculate complete continental control bonuses (Implemented in Milestone 3)
     *
     * @return list containing all territories in Europe
     */
    public ArrayList<Territory> getEurope(){
        return europe;
    }

    /**
     * Returns the list consisting of all territories that make up Asia
     * This will be used to calculate complete continental control bonuses (Implemented in Milestone 3)
     *
     * @return list containing all territories in Asia
     */
    public ArrayList<Territory> getAsia(){
        return asia;
    }

    /**
     * Returns the list consisting of all territories that make up Australia
     * This will be used to calculate complete continental control bonuses (Implemented in Milestone 3)
     *
     * @return list containing all territories in Australia
     */
    public ArrayList<Territory> getAustralia(){
        return australia;
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

    /**
     * Sets up the complete game map as a graph and initializes the continents & neighbours
     * The map is modelled after the real world map (Mercator projection used in image)
     * There are 42 total territories and 7 continents.
     *
     * This is almost 350 lines. No methods below this one
     */
    private void setupMap(){

        //NORTH AMERICA CREATION
        Territory alaska = new Territory("Alaska");
        allTerritories.add(alaska);
        northAmerica.add(alaska);
        Territory northWestTerritories = new Territory("NorthwestTerritories");
        northAmerica.add(northWestTerritories);
        allTerritories.add(northWestTerritories);
        Territory alberta = new Territory("Alberta");
        allTerritories.add(alberta);
        northAmerica.add(alberta);
        Territory ontario = new Territory("Ontario");
        allTerritories.add(ontario);
        northAmerica.add(ontario);
        Territory quebec = new Territory("Quebec");
        allTerritories.add(quebec);
        northAmerica.add(quebec);
        Territory greenland = new Territory("Greenland");
        allTerritories.add(greenland);
        northAmerica.add(greenland);
        Territory westUSA = new Territory("WestUSA");
        allTerritories.add(westUSA);
        northAmerica.add(westUSA);
        Territory eastUSA = new Territory("EastUSA");
        allTerritories.add(eastUSA);
        northAmerica.add(eastUSA);
        Territory centralAmerica = new Territory("CentralAmerica");
        allTerritories.add(centralAmerica);
        northAmerica.add(centralAmerica);

        //SOUTH AMERICA CREATION
        Territory venezuela = new Territory("Venezuela");
        allTerritories.add(venezuela);
        southAmerica.add(venezuela);
        Territory peru = new Territory("Peru");
        allTerritories.add(peru);
        southAmerica.add(peru);
        Territory argentina = new Territory("Argentina");
        allTerritories.add(argentina);
        southAmerica.add(argentina);
        Territory brazil = new Territory("Brazil");
        allTerritories.add(brazil);
        southAmerica.add(brazil);

        //AFRICA CREATION
        Territory southAfrica = new Territory("SouthAfrica");
        allTerritories.add(southAfrica);
        africa.add(southAfrica);
        Territory madagascar = new Territory("Madagascar");
        allTerritories.add(madagascar);
        africa.add(madagascar);
        Territory congo = new Territory("Congo");
        allTerritories.add(congo);
        africa.add(congo);
        Territory eastAfrica = new Territory("EastAfrica");
        allTerritories.add(eastAfrica);
        africa.add(eastAfrica);
        Territory northAfrica = new Territory("NorthAfrica");
        allTerritories.add(northAfrica);
        africa.add(northAfrica);
        Territory egypt = new Territory("Egypt");
        allTerritories.add(egypt);
        africa.add(egypt);

        //EUROPE CREATION
        Territory northEurope = new Territory("NorthEurope");
        allTerritories.add(northEurope);
        europe.add(northEurope);
        Territory southEurope = new Territory("SouthEurope");
        allTerritories.add(southEurope);
        europe.add(southEurope);
        Territory westEurope = new Territory("WestEurope");
        allTerritories.add(westEurope);
        europe.add(westEurope);
        Territory greatBritain = new Territory("GreatBritain");
        allTerritories.add(greatBritain);
        europe.add(greatBritain);
        Territory iceland = new Territory("Iceland");
        allTerritories.add(iceland);
        europe.add(iceland);
        Territory scandinavia = new Territory("Scandinavia");
        allTerritories.add(scandinavia);
        europe.add(scandinavia);
        Territory ukraine = new Territory("Ukraine");
        allTerritories.add(ukraine);
        europe.add(ukraine);

        //ASIA CREATION
        Territory middleEast = new Territory("MiddleEast");
        allTerritories.add(middleEast);
        asia.add(middleEast);
        Territory afghanistan = new Territory("Afghanistan");
        allTerritories.add(afghanistan);
        asia.add(afghanistan);
        Territory ural = new Territory("Ural");
        allTerritories.add(ural);
        asia.add(ural);
        Territory siberia = new Territory("Siberia");
        allTerritories.add(siberia);
        asia.add(siberia);
        Territory india = new Territory("India");
        allTerritories.add(india);
        asia.add(india);
        Territory siam = new Territory("Siam");
        allTerritories.add(siam);
        asia.add(siam);
        Territory china = new Territory("China");
        allTerritories.add(china);
        asia.add(china);
        Territory mongolia = new Territory("Mongolia");
        allTerritories.add(mongolia);
        asia.add(mongolia);
        Territory irkutsk = new Territory("Irkutsk");
        allTerritories.add(irkutsk);
        asia.add(irkutsk);
        Territory yakutsk = new Territory("Yakutsk");
        allTerritories.add(yakutsk);
        asia.add(yakutsk);
        Territory kamchatka = new Territory("Kamchatka");
        allTerritories.add(kamchatka);
        asia.add(kamchatka);
        Territory japan = new Territory("Japan");
        allTerritories.add(japan);
        asia.add(japan);

        //AUSTRALIA CREATION
        Territory indonesia = new Territory("Indonesia");
        allTerritories.add(indonesia);
        australia.add(indonesia);
        Territory newGuinea = new Territory("NewGuinea");
        allTerritories.add(newGuinea);
        australia.add(newGuinea);
        Territory westAustralia = new Territory("WestAustralia");
        allTerritories.add(westAustralia);
        australia.add(westAustralia);
        Territory eastAustralia = new Territory("EastAustralia");
        allTerritories.add(eastAustralia);
        australia.add(eastAustralia);

        //NORTH AMERICA NEIGHBOUR SETUP vvv
        alaska.addNeighbours(kamchatka);
        alaska.addNeighbours(northWestTerritories);
        alaska.addNeighbours(alberta);

        northWestTerritories.addNeighbours(alaska);
        northWestTerritories.addNeighbours(alberta);
        northWestTerritories.addNeighbours(ontario);
        northWestTerritories.addNeighbours(greenland);

        alberta.addNeighbours(alaska);
        alberta.addNeighbours(northWestTerritories);
        alberta.addNeighbours(ontario);
        alberta.addNeighbours(westUSA);

        ontario.addNeighbours(alberta);
        ontario.addNeighbours(northWestTerritories);
        ontario.addNeighbours(westUSA);
        ontario.addNeighbours(eastUSA);
        ontario.addNeighbours(quebec);
        ontario.addNeighbours(greenland);

        quebec.addNeighbours(ontario);
        quebec.addNeighbours(greenland);
        quebec.addNeighbours(eastUSA);

        greenland.addNeighbours(northWestTerritories);
        greenland.addNeighbours(ontario);
        greenland.addNeighbours(quebec);
        greenland.addNeighbours(iceland);

        westUSA.addNeighbours(alberta);
        westUSA.addNeighbours(eastUSA);
        westUSA.addNeighbours(ontario);
        westUSA.addNeighbours(centralAmerica);

        eastUSA.addNeighbours(ontario);
        eastUSA.addNeighbours(quebec);
        eastUSA.addNeighbours(westUSA);
        eastUSA.addNeighbours(centralAmerica);

        centralAmerica.addNeighbours(westUSA);
        centralAmerica.addNeighbours(eastUSA);
        centralAmerica.addNeighbours(venezuela);
        //NORTH AMERICA NEIGHBOUR SETUP ^^^

        //SOUTH AMERICA NEIGHBOUR SETUP vvv
        venezuela.addNeighbours(centralAmerica);
        venezuela.addNeighbours(peru);
        venezuela.addNeighbours(brazil);

        peru.addNeighbours(venezuela);
        peru.addNeighbours(brazil);
        peru.addNeighbours(argentina);

        brazil.addNeighbours(venezuela);
        brazil.addNeighbours(peru);
        brazil.addNeighbours(argentina);
        brazil.addNeighbours(northAfrica);

        argentina.addNeighbours(peru);
        argentina.addNeighbours(brazil);
        //SOUTH AMERICA NEIGHBOUR SETUP ^^^

        //AFRICA NEIGHBOUR SETUP vvv
        northAfrica.addNeighbours(brazil);
        northAfrica.addNeighbours(congo);
        northAfrica.addNeighbours(eastAfrica);
        northAfrica.addNeighbours(egypt);
        northAfrica.addNeighbours(southEurope); //Assuming traversal across strait of Gibraltar
        northAfrica.addNeighbours(westEurope);

        congo.addNeighbours(northAfrica);
        congo.addNeighbours(eastAfrica);
        congo.addNeighbours(southAfrica);

        southAfrica.addNeighbours(congo);
        southAfrica.addNeighbours(eastAfrica);
        southAfrica.addNeighbours(madagascar);

        madagascar.addNeighbours(southAfrica);
        madagascar.addNeighbours(eastAfrica);

        eastAfrica.addNeighbours(madagascar);
        eastAfrica.addNeighbours(southAfrica);
        eastAfrica.addNeighbours(congo);
        eastAfrica.addNeighbours(northAfrica);
        eastAfrica.addNeighbours(egypt);
        eastAfrica.addNeighbours(middleEast);

        egypt.addNeighbours(northAfrica);
        egypt.addNeighbours(eastAfrica);
        egypt.addNeighbours(middleEast);
        egypt.addNeighbours(southEurope);
        //AFRICA NEIGHBOUR SETUP ^^^

        //EUROPE NEIGHBOUR SETUP vvv
        westEurope.addNeighbours(greatBritain);
        westEurope.addNeighbours(northAfrica);
        westEurope.addNeighbours(northEurope);
        westEurope.addNeighbours(southEurope);

        greatBritain.addNeighbours(iceland);
        greatBritain.addNeighbours(scandinavia);
        greatBritain.addNeighbours(northEurope);
        greatBritain.addNeighbours(westEurope);

        iceland.addNeighbours(greenland);
        iceland.addNeighbours(greatBritain);
        iceland.addNeighbours(scandinavia);

        southEurope.addNeighbours(northAfrica);
        southEurope.addNeighbours(egypt);
        southEurope.addNeighbours(middleEast);
        southEurope.addNeighbours(ukraine);
        southEurope.addNeighbours(northEurope);
        southEurope.addNeighbours(westEurope);

        northEurope.addNeighbours(southEurope);
        northEurope.addNeighbours(ukraine);
        northEurope.addNeighbours(scandinavia);
        northEurope.addNeighbours(greatBritain);
        northEurope.addNeighbours(westEurope);

        scandinavia.addNeighbours(northEurope);
        scandinavia.addNeighbours(ukraine);
        scandinavia.addNeighbours(iceland);
        scandinavia.addNeighbours(greatBritain);

        ukraine.addNeighbours(scandinavia);
        ukraine.addNeighbours(northEurope);
        ukraine.addNeighbours(southEurope);
        ukraine.addNeighbours(middleEast);
        ukraine.addNeighbours(afghanistan);
        ukraine.addNeighbours(ural);
        //EUROPE NEIGHBOUR SETUP ^^^

        //ASIA NEIGHBOUR SETUP vvv
        middleEast.addNeighbours(eastAfrica);
        middleEast.addNeighbours(egypt);
        middleEast.addNeighbours(southEurope);
        middleEast.addNeighbours(ukraine);
        middleEast.addNeighbours(afghanistan);
        middleEast.addNeighbours(india);

        afghanistan.addNeighbours(middleEast);
        afghanistan.addNeighbours(ukraine);
        afghanistan.addNeighbours(ural);
        afghanistan.addNeighbours(china);
        afghanistan.addNeighbours(india);

        ural.addNeighbours(afghanistan);
        ural.addNeighbours(ukraine);
        ural.addNeighbours(siberia);
        ural.addNeighbours(china);

        siberia.addNeighbours(ural);
        siberia.addNeighbours(yakutsk);
        siberia.addNeighbours(irkutsk);
        siberia.addNeighbours(mongolia);
        siberia.addNeighbours(china);

        india.addNeighbours(middleEast);
        india.addNeighbours(afghanistan);
        india.addNeighbours(china);
        india.addNeighbours(siam);

        siam.addNeighbours(india);
        siam.addNeighbours(china);
        siam.addNeighbours(indonesia);

        china.addNeighbours(siam);
        china.addNeighbours(india);
        china.addNeighbours(afghanistan);
        china.addNeighbours(ural);
        china.addNeighbours(siberia);
        china.addNeighbours(mongolia);

        mongolia.addNeighbours(china);
        mongolia.addNeighbours(siberia);
        mongolia.addNeighbours(irkutsk);
        mongolia.addNeighbours(kamchatka);
        mongolia.addNeighbours(japan);

        japan.addNeighbours(mongolia);
        japan.addNeighbours(kamchatka);

        irkutsk.addNeighbours(mongolia);
        irkutsk.addNeighbours(siberia);
        irkutsk.addNeighbours(yakutsk);
        irkutsk.addNeighbours(kamchatka);

        yakutsk.addNeighbours(irkutsk);
        yakutsk.addNeighbours(siberia);
        yakutsk.addNeighbours(kamchatka);

        kamchatka.addNeighbours(japan);
        kamchatka.addNeighbours(irkutsk);
        kamchatka.addNeighbours(yakutsk);
        kamchatka.addNeighbours(alaska);
        //ASIA NEIGHBOUR SETUP ^^^

        //AUSTRALIA NEIGHBOUR SETUP vvv
        indonesia.addNeighbours(siam);
        indonesia.addNeighbours(newGuinea);
        indonesia.addNeighbours(westAustralia);

        newGuinea.addNeighbours(eastAustralia);
        newGuinea.addNeighbours(westAustralia);
        newGuinea.addNeighbours(eastAustralia);

        westAustralia.addNeighbours(indonesia);
        westAustralia.addNeighbours(newGuinea);
        westAustralia.addNeighbours(eastAustralia);

        eastAustralia.addNeighbours(newGuinea);
        eastAustralia.addNeighbours(westAustralia);
        //AUSTRALIA NEIGHBOUR SETUP ^^^

        //Territories are randomly distributed.
        //Shuffle the elements to do so
        Collections.shuffle(allTerritories);
    }

}
