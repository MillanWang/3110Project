public class GameController {
    private Game game;
    private GameView gameView;

    /**
     * Constructor for the GameController class
     *
     * @param gameModel the model. Game class
     * @param gameView the view. GameView class
     */
    public GameController(Game gameModel,  GameView gameView) {
        this.game = gameModel;
        this.gameView = gameView;
    }

    /**
     * Getting a string array of all of the players territories. Used during the draft phase in the view
     *
     * @return String array of all the territories that the current player has
     */
    public String[] getPlayersTerritoriesForDraft(){
        return game.getCurrentPlayerObject().getTerritoriesList();
    }

    /**
     * Starts the draft,attack sequence for the current player.
     *
     */
    public void startPlayersTurn(){
        if (game.hasWinner()){
            //Cannot do next turn if a winner has already been found
            gameView.announceWinner();
            return;//Immediately get out the method.
        }

        //DRAFT
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


        //ATTACK

        int endAttackStage = 0;
        String[] attackerDefender;
        while (true){
            //Ensure that the player has attack starters
            if (game.getCurrentPlayerObject().getAttackStarters() == null){
                gameView.displayMessage(game.getCurrentPlayer() + " has no territories that can start an attack. End attack phase");
                break;
                //End turn and move on if no attack starters
            }

            //Getting the option from the player
            endAttackStage = gameView.attackOrQuitOption();

            if (endAttackStage!=0)break;//Player chooses to end attack. Moving on to next phase

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
}
