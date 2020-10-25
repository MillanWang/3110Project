/**
 * CommandWords class holds all valid commands that will
 * be used in the game
 */
public class CommandWords {
    /**
     * VALID_COMMANDS holds all the valid commands that the user can input.
     * nextTurn: Starts the turn of the next player
     * showMap: Prints a complete list of all territories with the  #troops on them and the owner
     * quit: in case a player wanna quit the game
     * help: in case the player don't know what to do
     */
    private static final String[] VALID_COMMANDS = {
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
        for(int i = 0; i < VALID_COMMANDS.length; i++) {
            if(VALID_COMMANDS[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }
}
