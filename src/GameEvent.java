import java.io.Serializable;
import java.util.EventObject;
import java.util.LinkedList;

public class GameEvent extends EventObject implements Serializable {

    //Fields of interest
    private Player currentPlayer;
    private GenericWorldMap genericWorldMap;
    private Game.GameState gameState;
    private String[] territoriesOfInterest;
    private String message;

    //Constructor
    public GameEvent(Game model,
                     Player currentPlayer,
                     GenericWorldMap genericWorldMap,
                     Game.GameState gameState,
                     String[] territoriesOfInterest,
                     String message){
        super(model);
        this.currentPlayer = currentPlayer;
        this.genericWorldMap = genericWorldMap;
        this.gameState = gameState;
        this.territoriesOfInterest = territoriesOfInterest;
        this.message = message;
    }

    //Getter methods for all fields
    public Player getCurrentPlayer() { return currentPlayer; }

    public GenericWorldMap getGenericWorldMap() { return genericWorldMap; }

    public Game.GameState getGameState() { return gameState; }

    public String[] getTerritoriesOfInterest() { return territoriesOfInterest; }

    public String getMessage() {return message;}

}
