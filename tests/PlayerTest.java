import junit.framework.TestCase;

import java.util.LinkedList;

public class PlayerTest extends TestCase {

    private Player playerA;

    public void setUp() throws Exception {
        playerA = new Player("Mo");
    }

    
  
    @org.junit.Test
    // tests addTerritory, removeTerritory and getTerritoriesList
    public void testAddRemoveGetTerritories() {
        Territory territory = new Territory.Builder("Congo").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territory.setOwner("Mo");
        String[] test = playerA.getTerritoriesList();
        assertEquals(0, test.length);

        playerA.addTerritory(territory);
        test = playerA.getTerritoriesList();
        assertEquals(1,test.length);

        playerA.removeTerritory(territory);
        test = playerA.getTerritoriesList();
        assertEquals(0,test.length);

    }


    @org.junit.Test
    public void testHasLost() {
        Territory territory = new Territory.Builder("Congo").setContinentName("Africa")
            .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territory.setOwner("Mo");
         playerA.addTerritory(territory);
         playerA.removeTerritory(territory);
         assertTrue(playerA.hasLost());
    }

    @org.junit.Test
    // testing for continentBonus and getNumTroops
    public void testBonusTroops() {
        Territory territoryA = new Territory.Builder("Egypt").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territoryA.setOwner("Mo");
        Territory territoryB = new Territory.Builder("Congo").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territoryB.setOwner("Mo");
        playerA.addTerritory(territoryA);
        playerA.addTerritory(territoryB);
        playerA.bonusTroops();
        assertEquals(10,playerA.getNumTroops());
    }


    @org.junit.Test
    public void testDraftPhase() {
        Territory territoryA = new Territory.Builder("Egypt").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territoryA.setOwner("Mo");
        territoryA.changeTroops(2);
        playerA.addTerritory(territoryA);
        assertEquals("Egypt now has 12 troops after adding 10",playerA.draftPhase("Egypt", "10"));
    }

     @org.junit.Test
    public void testGetAttackStarters() {
        Territory territoryA = new Territory.Builder("Egypt").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territoryA.setOwner("Mo");
        territoryA.setTroops(2);

        Territory territoryB = new Territory.Builder("Congo").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territoryB.setOwner("Nemo");

        playerA.addTerritory(territoryA);
       // assertNull(playerA.getAttackStarters());

        territoryA.addNeighbours(territoryB);

        assertEquals(1, playerA.getAttackStarters().length);
    }

    @org.junit.Test
    public void testGetTerritoryStringArray() {
        Territory territoryA = new Territory.Builder("Egypt").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territoryA.setOwner("Mo");

        Territory territoryB = new Territory.Builder("Congo").setContinentName("Africa")
                .setNumTerritoriesInContinent(2).setContinentControlBonus(7).build();
        territoryB.setOwner("Mo");

        LinkedList list = new LinkedList<Territory>();
        list.add(territoryA);
        list.add(territoryB);
        assertEquals(2, playerA.getTerritoryStringArray(list).length);
    }

}
