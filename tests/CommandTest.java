import junit.framework.TestCase;

public class CommandTest extends TestCase {
    private Command command;
    private Command command2;
    private Command command3;
    private Command command4;

    protected void setUp() throws Exception {
        command = new Command("quit");
        command2 = new Command("help");
        command3 = new Command("showMap");
        command4 = new Command("nextTurn");

    }

    public void testGetCommandWord() {
        assertEquals("quit", command.getCommandWord());
        assertEquals("help", command2.getCommandWord());
        assertEquals("showMap", command3.getCommandWord());
        assertEquals("nextTurn", command4.getCommandWord());
    }

    public void testIsUnknown() {
        assertFalse(command.isUnknown());
        assertFalse(command2.isUnknown());
        assertFalse(command3.isUnknown());
        assertFalse(command4.isUnknown());
    }
}