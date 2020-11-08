import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController implements ActionListener{
    private Game game;
    private GameView gameView;

    public GameController(Game gameModel,  GameView gameView) {
        this.game = gameModel;
        this.gameView = gameView;
    }

    public String[] getPlayersTerritoriesForDraft(){
        return game.getCurrentPlayerObject().getTerritoriesList();
    }

    public void startPlayersTurn(){
        if (game.hasWinner()){
            //Cannot do next turn if a winner has already been found
            gameView.announceWinner();
            return;
        }

        //DRAFT STAGE
        //Retrieve model info to pass to view. Current player's dynamic territories list, how many troops can be sent
        //Send that info to GUI to be displayed. Get option from user
        //Send user input to model. Repeat until model says that player has no more troops to send out
        //gameView.showDraftOptions();
        gameView.displayMessage("Starting the draft phase for player: " + game.getCurrentPlayer());
        String[] draftInfoFromView;
        Player currentPlayer = game.getCurrentPlayerObject();
        currentPlayer.bonusTroops();
        System.out.println(currentPlayer.getNumTroops());

        while (currentPlayer.getNumTroops() > 0){
            draftInfoFromView = gameView.startDraft(currentPlayer.getNumTroops());
            gameView.displayMessage(game.getCurrentPlayerObject().draftPhase(draftInfoFromView[0],draftInfoFromView[1]));
        }

        //
        gameView.displayMessage("Draft stage complete, starting the attack phase for player: " + game.getCurrentPlayer());


        int endAttackStage = 0;
        String[] attackerDefender;
        while (true){
            //Ensure that the player has attack starters
            if (game.getCurrentPlayerObject().getAttackStarters() == null){
                gameView.displayMessage(game.getCurrentPlayer() + " has no territories that can start an attack. End attack phase");
                break;
            }

            //Getting the option from the player
            endAttackStage = gameView.attackOrQuitOption();
            if (endAttackStage!=0)break;//Ending attack phase

            //Player wants to start an attack
            attackerDefender = gameView.attackSelection();
            //^^Array of {attacker, defender}

            gameView.displayMessage(attackerDefender[0] + " is about to attack " + attackerDefender[1]);

            int[] diceFightChoice = new int[2];
            String diceFightResultString = "";
            while(true){//A PARTICULAR DICE FIGHT AFTER ATTACKER&DEFENDER ARE SELECTED
                //game.diceFightInfo(attackerDefender); //THIS IS A STRING[] OF THE INFO FOR THE VIEW
                diceFightChoice = gameView.diceFightView(game.getTerritory(attackerDefender[0]).maxDiceToRoll());
                if (diceFightChoice[0] != 0) break;

                diceFightResultString = game.diceFight(attackerDefender, diceFightChoice[1]);
                diceFightResultString += "\n" + game.diceFightInfo(attackerDefender);
                gameView.displayMessage(diceFightResultString);

                //DICE FIGHT
                //Retrieve number of troops in the attacker and defender terrys. Need to show info in the view
                //Also need to retrieve the maximum number of dice the attacker will roll

                //Get the player's option for how many dice they roll
                //Do the diceFight in the model. Get the string of the results to display by the view.
                //Check if a player has been eliminated. If eliminated, check if player has won the game totally.
                if (game.getTerritory(attackerDefender[0]).getTroops() <= 1){
                    //Attacker can no longer attack from the current selected territory
                    gameView.displayMessage("Only one troop left! You can no longer attack from " + attackerDefender[0]);
                    break;
                } else if (game.getTerritory(attackerDefender[1]).getTroops() <= 0){
                    //Defender just lost. Territory is given to the attacker. Ask how many troops to move in
                    gameView.displayMessage(attackerDefender[1] + " has just been conquered!");

                    int incomingTroops = gameView.troopsToMoveIn(game.getTerritory(attackerDefender[0]).getTroops(), diceFightChoice[1]);
                    if (game.takeoverTerritory(currentPlayer, game.getTerritory(attackerDefender[0]), game.getTerritory(attackerDefender[1]), incomingTroops)){
                        //A player has been eliminated
                        gameView.announceElimination(game.getTerritory(attackerDefender[1]).getOwner());

                        if(game.hasWinner()){
                            gameView.announceWinner();
                            return;
                        }
                    }
                    break;
                }
            }
        }
        game.nextTurn();//Switching to the next player
    }


    public String[] getNeighboursToAttack(Territory ter){
        return ter.attackableNeighbours();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
