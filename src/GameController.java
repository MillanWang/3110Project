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
     * Starts the draft,attack,(Fortify on next milestone),endTurn sequence for the current player.
     *
     */
    public void startPlayersTurn() {
        if (game.hasWinner()) {
            //Cannot do next turn if a winner has already been found
            gameView.announceWinner();
            return;//Immediately get out the method.
        }

        //If the current player is an AI player, handle turn in different method
        if (game.getCurrentPlayerObject() instanceof AIPlayer){
            startAIPlayersTurn();
        return;
        }

        //DRAFT
        gameView.displayMessage("Starting the draft phase for player: " + game.getCurrentPlayer());
        String[] draftInfoFromView;
        Player currentPlayer = game.getCurrentPlayerObject();
        currentPlayer.bonusTroops();

        //Keep asking player to send troops to territories until there are no more troops to send
        while (currentPlayer.getNumTroops() > 0){
            draftInfoFromView = gameView.startDraft(currentPlayer.getNumTroops());//ONLY FOR HUMANS
            gameView.displayMessage(game.getCurrentPlayerObject().draftPhase(draftInfoFromView[0],draftInfoFromView[1]));
        }

        //Draft complete. Move on to attack
        gameView.displayMessage("Draft stage complete, starting the attack phase for player: " + game.getCurrentPlayer());


        //ATTACK

        int endAttackStage = 0; //Non zero when player hits "End Attack" or window X
        String[] attackerDefender; //{AttackingTerritoryName , DefenderTerritoryName}
        while (true){//Continue attacking until player decides to stop, or no more territories can start the attack
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

            int[] diceFightChoice;//{diceFightOrBackToAttackSelection, numDiceRolls}
            String diceFightResultString = "";


            while(true){//A PARTICULAR DICE FIGHT AFTER ATTACKER&DEFENDER ARE SELECTED

                diceFightChoice = gameView.diceFightView(game.getTerritory(attackerDefender[0]).maxDiceToRoll());
                if (diceFightChoice[0] != 0) break;//Player wants to end current diceFight.


                int defenderDiceFightChoice=0;
                if (game.getPlayerFromList(game.getTerritory(attackerDefender[1]).getOwner()) instanceof AIPlayer){
                    //Always roll 2 unless down to last troop. Roll 1
                    defenderDiceFightChoice= (game.getTerritory(attackerDefender[1]).getTroops() >=2) ? 2 : 1;
                } else {
                    //Human player is the defender. Ask for how many dice to roll
                    defenderDiceFightChoice = gameView.defenderDiceRoll(game.getTerritory(attackerDefender[1]));
                }
                //GOTTA ASK DEFENDER HOW MANY DICE TO ROLL IF DEFENDER IS HUMAN
                //AI DEFENDERS AUTO ROLL 2 UNLESS FORCED TO ROLL 1


                diceFightResultString = game.diceFight(attackerDefender, diceFightChoice[1], defenderDiceFightChoice);
                diceFightResultString += "\n" + game.diceFightInfo(attackerDefender);
                gameView.displayMessage(diceFightResultString);//Telling the player the rolls and results of the dice fight

                //Checking if another dice fight can happen or not.
                if (game.getTerritory(attackerDefender[0]).getTroops() <= 1){
                    //Attacker can no longer dice fight from the current selected territory
                    gameView.displayMessage("Only one troop left! You can no longer attack from " + attackerDefender[0]);
                    break;
                } else if (game.getTerritory(attackerDefender[1]).getTroops() <= 0){
                    //Defender just lost. Territory is given to the attacker. Ask how many troops to move in
                    gameView.displayMessage(attackerDefender[1] + " has just been conquered!");

                    //Asking user for how many troops to move into newly conquered territory
                    int incomingTroops = gameView.troopsToMoveIn(game.getTerritory(attackerDefender[0]).getTroops(), diceFightChoice[1]);

                    //Checking if the defender owner is eliminated
                    String loserName = game.getTerritory(attackerDefender[1]).getOwner();
                    if (game.takeoverTerritory(currentPlayer, game.getTerritory(attackerDefender[0]), game.getTerritory(attackerDefender[1]), incomingTroops)){
                        //A player has been eliminated
                        gameView.announceElimination(loserName);

                        //Checking if the player has won. If so,
                        if(game.hasWinner()){
                            gameView.announceWinner();
                            return;
                        }
                    }
                    break;//No more dice fight for this attackDefender pair after territory takeover
                }
            }
        }
        game.nextTurn();//Switching to the next player
    }

    /**
     * Processes and handles an AI player's turn.
     * This will only be called by the startPlayersTurn() method if the current player is an AI
     *
     */
    private void startAIPlayersTurn(){
        //BIG WORK IN PROGRESS


        game.nextTurn();//Switching to the next player
    }


    /**
     * Getting the attackable neighbours string array for the specified territory
     *
     * @param ter The territory object starting the attack
     * @return  String array of names of territories that can be attacked from ter
     */
    public String[] getNeighboursToAttack(Territory ter){
        return ter.attackableNeighbours();
    }
}
