import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * Dice class deals with the dice rolling process during battles in the attack stage of a turn
 * Important for the diceFight section of an attack
 *
 */
public class Dice {
    private Random random;


    /**
     * Constructor for the dice class. Used during the attack phase
     */
    public Dice() {
        random = new Random();
    }

    /** A function that rolls a single die.
     *  to required ints (from 1 to max+1)
     * @return A number from 1 to 6.
     */
    public int rollDie() {
        return this.random.nextInt(6) + 1;
    }

}
