
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class GameController implements ActionListener{
    private Game game;
    private GameView gameView;

    public GameController(Game gameModel,  GameView gameView) {
        this.game = gameModel;
        this.gameView = gameView;
    }

    public LinkedList<Territory> getPlayersTerritoriesForDraft(){
        return game.getCurrentPlayerObject().getTerritoriesList();
    }

    public void startPlayersTurn(){
        System.out.println("Initiating process for next turn");

        //DRAFT STAGE
        //Retrieve model info to pass to view. Current player's dynamic territories list, how many troops can be sent
        //Send that info to GUI to be displayed. Get option from user
        //Send user input to model. Repeat until model says that player has no more troops to send out
        //gameView.showDraftOptions();


        //ATTACK STAGE

        //AttackerSelection phase.
        //Retrieve from model the possible attack starters for the current player
        //Option to quit(endTurn), or choose terry from list of possible attack starters(defenderSelection phase)
        //Get a direct reference to the territory that is starting the attack

        //DefenderSelection phase
        //Retrieve from model the possible defenders. Pass those to GUI
        //Option to go back to AttackerSelection phase or choose from a list who will be the victim of the attack
        //Get direct reference to the defender territory

        //DiceFight phase
        //Get from attacker territory the max number of dice that can be rolled with attackStarterTerritory.maxDiceToRoll()


        //Return to main menu view. Click on the "Next Turn" Button to do the next player's turn
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
