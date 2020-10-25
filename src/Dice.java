import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * Dice class deals with the dice rolling process during battles in the attack stage of a turn
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

    /** A private function that rolls a single die.
     *  to required ints (from 1 to max+1)
     * @return A number from 1 to 6.
     */
    private int rollDie() {
        return this.random.nextInt(6) + 1;
    }

    /**
     * During a player's attack phase, after the attack starter and the defender are chosen,
     * this method is called to start a diceFight.
     *
     * Asks user (attacker) for how many dice to roll and verifies that it is a legitimate number
     * Defender will always roll 2 dice unless the defending territory only has 1 troop left. This will result in 1 dice
     *
     * @return the number of dice rolled by the attacker on this dice fight
     *
     */
    public int diceFight(Territory attacker, Territory defender){
        int attackerDice, defenderDice;

        //Defender rolls 2 unless 1 troop left on territory
        if(defender.getTroops() <= 1) {
            defenderDice=1;
        } else {
            defenderDice=2;
        }


        //Asking for user to choose how many dice to roll. Verify that the number is ok for the given territories
        Scanner input = new Scanner(System.in);
        int attackerMaxDice = Math.min(3, attacker.getTroops()-1);
        while(true) {
            try {
                System.out.println("Choose number of dice to roll (1 - " + attackerMaxDice + ")");
                attackerDice = input.nextInt();
                if (attackerDice < 1 || attackerDice > attackerMaxDice){
                    System.out.println("Invalid dice number. Try again");
                }else {
                    break;
                }
            }catch (Exception e){
                System.out.println("Please input a number between 1-" + attackerMaxDice);
                input.next();
            }
        }

        //Used to make a list of the different players different dice rolls
        LinkedList<Integer> attackerRolls = new LinkedList<>();
        LinkedList<Integer> defenderRolls = new LinkedList<>();


        //Add dice roll values to the lists. Number of dice rolls specified above
        for (int i = 0; i<attackerDice; i++){ attackerRolls.push((Integer) rollDie());}
        Collections.sort(attackerRolls);//Sorting low to high
        Collections.reverse(attackerRolls);//Reverse to go high to low

        for (int i = 0; i<defenderDice; i++){ defenderRolls.push((Integer) rollDie()); }
        Collections.sort(defenderRolls);//Sorting low to high
        Collections.reverse(defenderRolls);//Reverse to go high to low


        //Pop out the dice rolls high to low to compare. Higher number wins. Ties means defender wins
        while (!attackerRolls.isEmpty() && !defenderRolls.isEmpty()){
            System.out.println("Attacker rolls a " + attackerRolls.peek());
            System.out.println("Defender rolls a " + defenderRolls.peek());
            if (attackerRolls.pop() <= defenderRolls.pop()){
                //Defender wins
                System.out.println("Defender wins! Attacker loses a troop! RIP");
                attacker.changeTroops(-1);
            } else {
                //Attacker wins
                System.out.println("Attacker wins! Defender loses a troop! RIP");
                defender.changeTroops(-1);
            }
        }
        //If the attacker takes over the territory, the number of troops moved in has to be greater or equal to
        //the number of dice rolled on the most recent diceFight
        return attackerDice;
    }
}
