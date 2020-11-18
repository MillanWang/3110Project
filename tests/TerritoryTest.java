import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class TerritoryTest {
    Territory territory;


    /**
     *change troops test. Ensures the troops are properly set
     */
    @Test
    public void changeTroops() {
        territory = new Territory.Builder("Congo").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();//
        territory.changeTroops(2); //number of troops to set
        assertEquals(2, territory.getTroops());
    }



    /**
     * checks for the key info that a territory has in the returned string namely; territoty name,
     * territory owner, and number of troops, to ensure a territory info is correct
     */
    @Test
    public void getInfoString() {
        territory = new Territory.Builder("Congo").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territory.setOwner("Josh");
        territory.changeTroops(2);
        String info = territory.getInfoString();
        assertTrue(info.contains("Congo")); // territory name check
        assertTrue(info.contains("Josh")); // territory owner check
        assertTrue(info.contains("2")); // number of troops check
    }

    /**
     * ensures that the territory contains the right neighbours that can be attacked
     */
    @Test
    public void getAttackableNeighbours() {
        territory = new Territory.Builder("Congo")
                                          .setNumTerritoriesInContinent(4).setContinentControlBonus(7).build();
        territory.setOwner("Josh");
        territory.changeTroops(5);
        Territory territory1 = new Territory.Builder("Egypt").setContinentName("Africa")
                                                     .setNumTerritoriesInContinent(4).setContinentControlBonus(7).build();
        territory1.setOwner("Kyle");
        territory.addNeighbours(territory1);
        assertTrue(territory.getAttackableNeighbours().contains(territory1));
    }



    /**
     * ensures the correct maximum dice roll is returned
     */
    @Test
    public void maxDiceToRoll() {
        territory = new Territory.Builder("Congo").setContinentName("Africa")
                .setNumTerritoriesInContinent(4).setContinentControlBonus(7).build();
        territory.changeTroops(2);
        assertEquals(1, territory.maxDiceToRoll());
    }


    /**
     * ensures that a particular territory neighbour is properly converted into a string array
     */
    @Test
    public void getNeighbourStringArray() {
        territory = new Territory.Builder("Congo")
                .setNumTerritoriesInContinent(4).setContinentControlBonus(7).build();
        territory.setOwner("Josh");
        territory.changeTroops(5);
        Territory territory1 = new Territory.Builder("Egypt").setContinentName("Africa")
                .setNumTerritoriesInContinent(4).setContinentControlBonus(7).build();
        territory1.setOwner("Kyle");
        territory.addNeighbours(territory1);
        List<Territory> territories = territory.getAttackableNeighbours();
        String[] neighbours = territory.getNeighbourStringArray(territories);// converts territories into a string array
        assertTrue(Arrays.asList(neighbours).contains("Egypt"));
    }


    /**
     * ensures that the neighbours a territory can attack are properly converted into a string
     */
    @Test
    public void attackableNeighbours() {
        territory = new Territory.Builder("Congo")
                .setNumTerritoriesInContinent(4).setContinentControlBonus(7).build();
        territory.setOwner("Josh");
        territory.changeTroops(5);
        Territory territory1 = new Territory.Builder("Egypt").setContinentName("Africa")
                .setNumTerritoriesInContinent(4).setContinentControlBonus(7).build();
        territory1.setOwner("Kyle");
        territory.addNeighbours(territory1);
        String[] neighbours = territory.attackableNeighbours();
        assertTrue(Arrays.asList(neighbours).contains("Egypt"));
    }
}