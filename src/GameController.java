import java.util.Random;

public class GameController {
    private Game game;


    /**
     * Constructor for the GameController class
     *
     * @param gameModel the model. Game class
     */
    public GameController(Game gameModel) {
        this.game = gameModel;
    }

    public void wantToAttack(boolean wantToAttack){
        if (wantToAttack) {
            game.setPlayerControllerMessage("attack");
        } else {
            game.setPlayerControllerMessage("");
        }
        //If this was something like diceFight, actually call the methods in game that do it
    }

}
