/**
 * Command class get the value of the command which is of type String
 * and it'll be used to by the Parser class.
 */
public class Command {
    private String commandWord;
    /**
     * Create a command object. The word must be supplied, but
     * it can be null too.
     * @param command The word of the command. Null if the command
     *                  was not recognised.
     */
    public Command(String command)
    {
        commandWord = command;
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @return The command word.
     */
    public String getCommandWord()
    {
        return commandWord;
    }

    /**
     * @return true if this command was not understood or invalid.
     */
    public boolean isUnknown()
    {
        return (commandWord == null);
    }

}
