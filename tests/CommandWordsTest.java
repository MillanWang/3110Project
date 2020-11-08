import junit.framework.TestCase;

public class CommandWordsTest extends TestCase {
    CommandWords commandWords;

    protected void setUp() throws Exception {
        commandWords = new CommandWords();
    }

    public void testIsCommand() {
        assertTrue(commandWords.isCommand("quit"));
        assertTrue(commandWords.isCommand("showMap"));
        assertTrue(commandWords.isCommand("nextTurn"));
        assertTrue(commandWords.isCommand("help"));
    }
}