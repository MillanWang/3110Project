import java.util.EventObject;
import java.util.LinkedList;

public class GameEvent extends EventObject {

    //Fields of interest
    private Player currentPlayer;
    private GenericWorldMap genericWorldMap;
    private boolean hasWinner;
    private Game.GameState gameState;
    private String[] territoriesOfInterest;

    //Constructor
    public GameEvent(Game model,
                     Player currentPlayer,
                     GenericWorldMap genericWorldMap,
                     boolean hasWinner,
                     Game.GameState gameState,
                     String[] territoriesOfInterest){
        super(model);
        this.currentPlayer = currentPlayer;
        this.genericWorldMap = genericWorldMap;
        this.hasWinner = hasWinner;
        this.gameState = gameState;
        this.territoriesOfInterest = territoriesOfInterest;
    }

    //Getter methods for all fields
    public Player getCurrentPlayer() { return currentPlayer; }

    public GenericWorldMap getGenericWorldMap() { return genericWorldMap; }

    public boolean isHasWinner() { return hasWinner; }

    public Game.GameState getGameState() { return gameState; }

    public String[] getTerritoriesOfInterest() { return territoriesOfInterest; }

}
