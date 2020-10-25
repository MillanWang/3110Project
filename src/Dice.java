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


    // Default Constructor : A die

    public Dice() {
        random = new Random();
    }

    /** A private function that rolls a single die. Used for safety
     *  to avoid common error in forgetting to maps random ints (from 0 to max)
     *  to required ints (from 1 to max+1)
     * @return A number from 1 to 6.
     */
    private int rollDie() {
        return this.random.nextInt(6) + 1;
    }

    /**
     * During a player's attack phase, after the attack starter and the defender are chosen,
     * this method is called.
     *
     * @param attacker  Territory where the attack starts
     * @param defender  The defender territory. Victim of the attack
     *
     */
    public void diceFight(Territory attacker, Territory defender){
        int attackerDice, defenderDice;

        //Defender rolls 2 unless 1 troop left on territory
        if(defender.getTroops() <= 1) {
            defenderDice=1;
        } else {
            defenderDice=2;
        }


        //Asking for user to choose how many dice to roll
        Scanner input = new Scanner(System.in);
        while(true) {
            try {
                System.out.println("Choose number of dice to roll (1 - 3)");
                attackerDice = input.nextInt();
                if (attackerDice < 1 || attackerDice > 3){
                    System.out.println("1-3 dice only. Try again");
                } else if (attackerDice + 1 >= attacker.getTroops()){
                    System.out.println("Can't roll that many for the given territory. Try again");
                    input.next();
                }else {
                    //1 <= attackerDice <= min(3, attacker.getTroops()-1)
                    break;
                }
            }catch (Exception e){
                System.out.println("Please input a number between 1-3 ");
                input.next();
            }
        }


        LinkedList<Integer> attackerRolls = new LinkedList<>();
        LinkedList<Integer> defenderRolls = new LinkedList<>();

        for (int i = 0; i<attackerDice; i++){ attackerRolls.push((Integer) rollDie());}
        Collections.sort(attackerRolls);//Sorting low to high
        Collections.reverse(attackerRolls);//Reverse to go high to low

        for (int i = 0; i<defenderDice; i++){ defenderRolls.push((Integer) rollDie()); }
        Collections.sort(defenderRolls);//Sorting low to high
        Collections.reverse(defenderRolls);//Reverse to go high to low


        while (!attackerRolls.isEmpty() && !defenderRolls.isEmpty()){
            System.out.println("Attacker rolls a " + attackerRolls.peek());
            System.out.println("Defender rolls a " + defenderRolls.peek());
            if (attackerRolls.pop() <= defenderRolls.pop()){
                //Defender wins
                System.out.println("Attacker loses a troop! RIP");
                attacker.changeTroops(-1);
            } else {
                //Attacker wins
                System.out.println("Defender loses a troop! RIP");
                defender.changeTroops(-1);
            }
        }
    }
}