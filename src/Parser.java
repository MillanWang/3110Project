import java.util.Scanner;

/**
 * the Parser class reads the inputs on the command window
 */
public class Parser {
    private CommandWords commands;// holds all valid command words
    private Scanner reader;// source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser()
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand()
    {
        String inputLine;// will hold the full input line
        System.out.print("> ");
        inputLine = reader.nextLine();// Find word on the line.
        Scanner scanner = new Scanner(inputLine);
        if(scanner.hasNext()) {
            inputLine = scanner.next();
        }
        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        if(commands.isCommand(inputLine)) {
            return new Command(inputLine);
        }
        else if(inputLine.contains(" ") || inputLine.equals("")){
            System.out.println("command cannot contain a space");
            return new Command(null);
        } else {
            return new Command(null);
        }
    }
}
