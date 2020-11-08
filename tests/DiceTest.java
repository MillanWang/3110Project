import junit.framework.TestCase;

public class DiceTest extends TestCase {
    private Dice dice;

    public void setUp() throws Exception {
       dice = new Dice();
    }

    public void testRollDie() {
        assertTrue((dice.rollDie() >= 1) && (dice.rollDie() <= 6));
    }
}