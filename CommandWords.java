public class CommandWords {
    // a constant array that holds all valid command words
    /**
     * attack: enables a player to attack a territory
     * fortify: allows a player to transfer troops to other territories owned by the player
     * draft: increase the number of troops in a certain territory owned by player
     * passTurn: enables a player to skip turn
     * endAttack: allows a player to end his/her attack stage
     * roll1: a player can roll one die during attack stage
     * roll2: a player can roll two dice during attack stage
     * roll3: a player can roll three dice during attack stage
     * autoAttack: a player can roll a default amount of dice
     * quit: in case a player wanna quit the game
     * help: in case the player don't know what to do
     *
     */
    private static final String[] validCommands = {
            //I ADDED "quit" command
            "attack", "fortify", "draft", "passTurn",
            "endAttack", "roll1", "roll2", "roll3", "autoAttack","quit","help"
    };

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid command word.
     * @return true if a given string is a valid command,
     * false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }
}