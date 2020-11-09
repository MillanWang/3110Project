import org.junit.Test;

import static org.junit.Assert.*;

public class DiceTest {

    private Dice dice;
    
    /**
     *ensures that a dice roll is between 1 and 6
     */
    @Test
    public void rollDie() {
        dice = new Dice();
        assertTrue((dice.rollDie() >= 1) && (dice.rollDie() <= 6));
    }
}
