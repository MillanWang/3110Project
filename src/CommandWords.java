/**
 * CommandWords class holds all valid commands that will
 * be used in the game
 */
public class CommandWords {
    // a constant array that holds all valid command words
    /**
     * nextTurn: Starts the turn of the next player
     * showMap: Prints a complete list of all territories with the  #troops on them and the owner
     * quit: in case a player wanna quit the game
     * help: in case the player don't know what to do
     */
    private static final String[] validCommands = {
            "nextTurn", "showMap","help","quit"
    };


    /**
     * Constructor - initialise the command words.
     */
    public CommandWords() {}

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
