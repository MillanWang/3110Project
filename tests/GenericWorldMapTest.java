import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class GenericWorldMapTest {

    GenericWorldMap gwm;

    /**
     * ensures all territories appear on the map by checking the number
     * of territories currently on the map
     */
    @Test
    public void getAllTerritories() {
        gwm = new GenericWorldMap("DefaultMap.txt");
        assertEquals(42, gwm.getAllTerritories().size());
    }


    /**
     * ensures all territories gotten are properly converted to strings
     */
    @Test
    public void getAllTerritoriesString() {
        gwm = new GenericWorldMap("DefaultMap.txt");
        LinkedList<Territory> territories = gwm.getAllTerritories();
        assertTrue(gwm.getAllTerritoriesString().contains("Alaska"));
        assertTrue(gwm.getAllTerritoriesString().contains("Congo"));
        assertTrue(gwm.getAllTerritoriesString().contains("Egypt"));
        assertTrue(gwm.getAllTerritoriesString().contains("Ontario"));
    }


    /**
     * ensures that a specified territory is properly retrieved
     */
    @Test
    public void getTerritory() {
        gwm = new GenericWorldMap("DefaultMap.txt");
        Territory ter = new Territory.Builder("Alaska").setContinentName("NorthAmerica")
                .setNumTerritoriesInContinent(9).setContinentControlBonus(5).build();
        assertTrue(gwm.getAllTerritories().contains(gwm.getTerritory("Alaska")));
    }
}