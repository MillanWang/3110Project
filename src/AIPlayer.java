/**
 * This class represents an AI player in the game RISK
 */
public class AIPlayer extends Player{

    /**
     * A constructor that sets the player class variables.
     *
     * @param name
     * @ param String  the name of the player
     */
    public AIPlayer(String name) {
        super(name);
    }

    /**
     * Does the draft part of a phase for the current AI player's turn
     *
     * @param territoryName
     * @param troops
     * @return
     */
    @Override
    public String draftPhase(String territoryName, String troops){

        return "";
    }
}
