public class Command {
    private String commandWord;

    /**
     * Create a command object.
     * @param firstWord The first word of the command. Null if the command
     *                  was not recognised.
     */
    public Command(String firstWord)
    {
        commandWord = firstWord;

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
     * @return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        return (commandWord == null);
    }

}
