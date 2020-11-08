import junit.framework.TestCase;

public class ParserTest extends TestCase {
    private Parser parser;

    public void setUp() throws Exception {
        parser = new Parser();
    }

    public void testGetCommand() {
        assert(((parser.getCommand()).getCommandWord()).equalsIgnoreCase("quit"));
    }
}