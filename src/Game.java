import java.io.*;
import java.util.*;

public class Game implements Serializable {
    public enum GameState {BEFORETURN,
        DRAFT,
        ATTACKORQUIT,
        ATTACKERSELECTION,
        DEFENDERSELECITON,
        DICEFIGHTORQUIT,
        DICEFIGHTATTACKERCHOICE,
        DICEFIGHTDEFENDERCHOICE,
        TAKEOVERTERRITORY,
        FORTIFYORQUIT,
        FORTIFYGIVER,
        FORTIFYRECEIVER,
        FORTIFYTROOPSTOMOVE,
        ELIMINATION,
        HASWINNER};

    private GameState gameState;
    private LinkedList<Player> players;
    private Player currentPlayer;
    private Dice dice;
    private GenericWorldMap genericWorldMap;
    private LinkedList<GameObserver> observers;
    private String[] currentTerritoriesOfInterest;
    private String currentMessage;



    /**
     * Constructor for the class  This will start playing the game
     */
    public Game(String customMapName){
        genericWorldMap = new GenericWorldMap(customMapName);
        players = new LinkedList<Player>();
        dice = new Dice();
        observers = new LinkedList<>();
    }

    /**
     * gets the player object given the player's name
     *
     * @param name name set to the player by the user
     * @return the player object corresponding to the given name
     */
    public Player getPlayerFromList(String name){
        for (Player player : players){
            if (player.getName().equals(name)){
                return player;
            }
        }
        return null; //This will never happen. Will always be found. Only called from the takeoverTerritory method
    }

    /**
     * @return genericWorldMap field
     */
    public GenericWorldMap getGenericWorldMap() {
        return genericWorldMap;
    }

    /**
     * @return the name of the current player
     */
    public String getCurrentPlayer(){
        return this.currentPlayer.getName();
    }

    /**
     * @return the player object of the current player
     */
    public Player getCurrentPlayerObject(){
        return this.currentPlayer;
    }

    /**
     * @return a linked list of the all the players not including the current player
     */
    public LinkedList<Player> getPlayersList(){
        return players;
    }

    /**
     * This method returns a reference to the the territory input
     *
     * @param territoryName name of the territory
     * @return the name of the current player
     */
    public Territory getTerritory(String territoryName){
        return genericWorldMap.getTerritory(territoryName);
    }

    /**
     * Completes the current players turn (Draft>attack cycle) and sets the current player to the next player in line
     */
    public void nextTurn(){
        players.add(currentPlayer);//Added to the back
        currentPlayer = players.pop();//Pull out the first player in line to go next
    }

    /**
     * @return the true if the game has a winner
     * false if there is no winner yet
     *
     */
    public boolean hasWinner(){
        return players.isEmpty();
    }

    /**
     * During a player's attack phase, after the attack starter and the defender are chosen,
     * this method is called to start a diceFight.
     *
     * Asks user (attacker) for how many dice to roll and verifies that it is a legitimate number
     * Defender will always roll 2 dice unless the defending territory only has 1 troop left. This will result in 1 dice
     *
     * @return the number of dice rolled by the attacker on this dice fight
     *
     */
    public String diceFight(String[] attackerDefender, int attackerDice, int defenderDice){

        Territory attacker = genericWorldMap.getTerritory(attackerDefender[0]);
        Territory defender = genericWorldMap.getTerritory(attackerDefender[1]);


        //Used to make a list of the different players different dice rolls
        LinkedList<Integer> attackerRolls = new LinkedList<>();
        LinkedList<Integer> defenderRolls = new LinkedList<>();


        //Add dice roll values to the lists. Number of dice rolls specified above
        for (int i = 0; i<attackerDice; i++){ attackerRolls.push((Integer) dice.rollDie());}
        Collections.sort(attackerRolls);//Sorting low to high
        Collections.reverse(attackerRolls);//Reverse to go high to low

        for (int i = 0; i<defenderDice; i++){ defenderRolls.push((Integer) dice.rollDie()); }
        Collections.sort(defenderRolls);//Sorting low to high
        Collections.reverse(defenderRolls);//Reverse to go high to low


        String diceFightResultMessage = "";

        //Pop out the dice rolls high to low to compare. Higher number wins. Ties means defender wins
        while (!attackerRolls.isEmpty() && !defenderRolls.isEmpty()){
            diceFightResultMessage += "Attacker rolls a " + attackerRolls.peek() +"\n";
            diceFightResultMessage += "Defender rolls a " + defenderRolls.peek() +"\n";
            if (attackerRolls.pop() <= defenderRolls.pop()){
                //Defender wins
                diceFightResultMessage += "Defender wins! Attacker loses a troop! RIP" +"\n";
                attacker.changeTroops(-1);
            } else {
                //Attacker wins
                diceFightResultMessage += "Attacker wins! Defender loses a troop! RIP" +"\n";
                defender.changeTroops(-1);
            }
        }
        //If the attacker takes over the territory, the number of troops moved in has to be greater or equal to
        //the number of dice rolled on the most recent diceFight
        return diceFightResultMessage;
    }

    /**
     * this method returns a string representation of the result of the dice fight
     *
     * @param attackerDefender a string array of the draft result
     * @return string representation of the result of the dice fight
     *
     */
    public String diceFightInfo(String[] attackerDefender){
        return genericWorldMap.getTerritory(attackerDefender[0]).getInfoString() + "\n" + genericWorldMap.getTerritory(attackerDefender[1]).getInfoString();
    }

    /**
     * Switches the ownership of the killed defender territory to the player that won
     * Moves troops from the attackWinner territory into the killedDefender territory, based on user input
     * Number of troops moving in has to be >= number of dice rolled on most recent diceFight
     *
     * If the owner of the killedDefender territory lost their last territory, they will be eliminated from the game
     *
     * @param winner The player that conquered the territory
     * @param attackerWinner The territory that won the attack
     * @param killedDefender The defending territory that just lost it's last
     * @param numTroopsMovingIn Number of troops moving into the killed defender.
     */
    public boolean takeoverTerritory(Player winner,Territory attackerWinner, Territory killedDefender, int numTroopsMovingIn){

        killedDefender.setTroops(numTroopsMovingIn);
        attackerWinner.changeTroops(-numTroopsMovingIn);

        winner.addTerritory(killedDefender);

        String loserName = killedDefender.getOwner();//Name of the original owner.
        getPlayerFromList(loserName).removeTerritory(killedDefender);

        killedDefender.setOwner(winner.getName());

        //Check if the loser has been eliminated from the game
        if (getPlayerFromList(loserName).hasLost()){
            eliminatePlayer(getPlayerFromList(loserName));
            return true;
        }
        return false;
    }

    /**
     * Eliminates player from the game given pointer to their player object
     *
     * @param loser The player eliminated from the game
     */
    private void eliminatePlayer(Player loser){
        players.remove(loser);
    }

    /**
     * sitting the playerNames by getting array of strings from the view and
     * initialize each player with it's playerName
     * @param playerNames
     */
    public void makePlayers(LinkedList<String> playerNames){

        for(String s : playerNames) {
            if(s.contains("[B0T]")){
                players.add(new AIPlayer(s));
            }else {
                players.add(new Player(s));
            }
        }

        //RANDOM DISTRIBUTION OF TERRITORIES AND TROOPS
        //The territory list is always randomized in the DefaultWorldMap class
        for(Territory terry : genericWorldMap.getAllTerritories()){
            players.peek().addTerritory(terry); //Adding current territory to current player
            terry.setOwner(players.peek().getName()); //Setting owner of territory to current player
            players.add(players.pop()); //Sending current player to the back of the queue
        }

        Collections.shuffle(players);//Players are initialized and order is randomized.

        //Putting troops in all player's territories
        for (Player player: players){
            player.setupPlayer(playerNames.size());
        }
        currentPlayer = players.pop();//Establish the first player to go
    }

    /**
     * Saves the game to a file using serializable
     * @param fileName Name of the file
     */
    public void saveGame(String fileName){
        try {
            File file1 = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
        }catch (Exception e){
            System.err.println("Error when saving file");
        }
    }

    /**
     * Creates a game object given the fileName of the save
     * @param fileName name of the file to be loaded
     * @return Game object corresponding to the save
     */
    public static Game loadGame(String fileName){
        Game game = null;
        try {
            File file1 = new File(fileName);
            FileInputStream fileInputStream = new FileInputStream(file1);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            game = (Game) objectInputStream.readObject();
        }catch (Exception e){
            System.err.println("Error when loading file");
        }
        return game;
    }

    /**
     * Compares the contents of the game objects. True if all fields contain the same contents in the same order
     * @param o The game object to be compared
     * @return True if the games are equal, false otherwise
     */
    public boolean equals(Object o){
        Game comparedGame = (Game) o;

        //Comparing current player names
        if (!comparedGame.getCurrentPlayer().equals(this.getCurrentPlayer())) return false;

        //Comparing queued player order
        for (int i = 0; i<this.getPlayersList().size(); i++){
            if (!this.getPlayersList().get(i).equals(comparedGame.getPlayersList().get(i))) return false;
        }

        //Comparing all territories
        for(int i = 0; i<this.getGenericWorldMap().getAllTerritories().size(); i++){
            if (!this.getGenericWorldMap().getAllTerritories().get(i).equals(comparedGame.getGenericWorldMap().getAllTerritories().get(i)))return false;
        }

        return true;
    }

    public void setPlayerControllerMessage(String controllerMessage){
        currentPlayer.setControllerMessage(controllerMessage);
    }

    /**
     * Adds an observer object for the current game
     *
     * @param gameObserver
     */
    public void addObserver(GameObserver gameObserver){
        this.observers.add(gameObserver);
    }

    /**
     * Notifies all game observers of the current state of the game
     */
    public void notifyObservers(){
        for (GameObserver g: observers){
            g.handleUpdate(new GameEvent(this,
                    currentPlayer,
                    genericWorldMap,
                    gameState,
                    currentTerritoriesOfInterest,
                    currentMessage));
        }
    }

    /**
     * Used to tell the observers to display a message to the user.
     *
     * @param message The message to be displayed to the user
     */
    public void displayMessage(String message){
        currentMessage = message;
        notifyObservers();
        currentMessage = "";
    }

    /**
     * Announces a winner to the player
     */
    public void announceWinner(){
        gameState = GameState.HASWINNER;
        notifyObservers();
    }

    /**
     * Announces that a player has been eliminated from the game
     *
     * @param loserName The name of the player that is eliminated.
     */
    public void announceElimination(String loserName){
        currentMessage = loserName;
        gameState = GameState.ELIMINATION;
        notifyObservers();
        currentMessage = "";
    }

    public void draftEvent(){
        gameState = GameState.DRAFT;
        currentMessage = "" + currentPlayer.getNumTroops();
        currentTerritoriesOfInterest = currentPlayer.getTerritoriesList();
        notifyObservers();
        currentMessage = "";


        //CONTROLLER NEEDS TO DO SOME DRAFT

    }

    public void attackOrQuit(){
        gameState = GameState.ATTACKORQUIT;
        notifyObservers();



        //CONTROLLER NEEDS TO CHANGE THE FIELD INSIDE OF CURRENT PLAYER TO GET THE ANSWER
        //Set the field to the string "attack" if wants to attack. Clear the field otherwise
    }



    public void chooseAttackStarter(){
        gameState = GameState.ATTACKERSELECTION;
        currentTerritoriesOfInterest = Player.getTerritoryStringArray(currentPlayer.getAttackStarters());
        notifyObservers();



        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
        //Field will be set to the name of the chosen attack starter territory

    }

    public void chooseAttackDefender(String attackStarter){
        gameState = GameState.DEFENDERSELECITON;
        currentTerritoriesOfInterest = Player.getTerritoryStringArray(currentPlayer.getTerritory(attackStarter).getAttackableNeighbours());
        notifyObservers();

        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
        //Field will be set to the name of the chosen defender given the attackStarter
    }


    public void wantToDiceFight(){
        gameState = GameState.DICEFIGHTORQUIT;
        notifyObservers();


        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
        //Field will be set to the diceFight if want to fight. Clear the field otherwise
    }

    public void chooseAttackerDice(int maxDice){
        gameState = GameState.DICEFIGHTATTACKERCHOICE;
        currentMessage = "" + maxDice;
        notifyObservers();
        currentMessage = "";



        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
        //Field will be set to the chosen number of dice to roll
    }

    public void chooseDefenderDice(int maxDice){
        gameState = GameState.DICEFIGHTDEFENDERCHOICE;
        currentMessage = "" + maxDice;
        notifyObservers();
        currentMessage = "";



        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
        //Field will be set to the chosen number of dice to roll

    }

    public void takeoverTerritoryEvent(int minMovingIn, int maxMovingIn){
        gameState = GameState.TAKEOVERTERRITORY;
        currentMessage = minMovingIn + "," + maxMovingIn;
        notifyObservers();
        currentMessage = "";

        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
    }

    public void fortifyOrQuit(){
        gameState = GameState.FORTIFYORQUIT;
        notifyObservers();

        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
    }

    public void chooseFortifyGivers(){
        gameState = GameState.FORTIFYGIVER;
        currentTerritoriesOfInterest = Player.getTerritoryStringArray(currentPlayer.getFortifyGivers());
        notifyObservers();

        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
    }

    public void chooseFortifyReceivers(String fortifyGiver){
        gameState = GameState.FORTIFYRECEIVER;
        Territory territory = currentPlayer.getTerritory(fortifyGiver);
        currentTerritoriesOfInterest = Player.getTerritoryStringArray(currentPlayer.getFortifiableTerritories(territory));
        notifyObservers();

        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
    }

    public void chooseFortifyTroops(int maxTroopsToMove){
        gameState = GameState.FORTIFYTROOPSTOMOVE;
        currentMessage = "" + maxTroopsToMove;

        notifyObservers();

        //CONTROLLER NEEDS TO CHANGE FIELD INSIDE OF CURRENT PLAYER TO GET THE CHOICE
    }

    public void gameDraft(){
        this.displayMessage("Starting the draft phase for player: " + this.getCurrentPlayer());

        currentPlayer.bonusTroops();//Calculates the number of troops to be distributed

        //Keep asking player to send troops to territories until there are no more troops to send
        while (currentPlayer.getNumTroops() > 0){
            currentPlayer.draftChoice(this);//Calls the player obj to get the choice
        }

        //Draft complete. Move on to attack
        this.displayMessage("Draft stage complete, starting the attack phase for player: " + this.getCurrentPlayer());
    }

    public void gameAttack(){
        while (currentPlayer.wantToAttack(this)){

            String[] attackerDefender = {"",""};
            //AttackStarterSelection
            attackerDefender[0] = currentPlayer.chooseAttackStarter(this);

            //AttackDefenderSelection
            attackerDefender[1] = currentPlayer.chooseAttackDefender(this, attackerDefender[0]);

            //DiceFightOrQuit
            int attackerDice, defenderDice;
            while (currentPlayer.wantToDiceFight(this, attackerDefender[0])){
                //DiceFightAttackerChoice
                attackerDice = currentPlayer.getAttackerDice(this, attackerDefender[0]);

                //DiceFightDefenderChoice
                Player defender = this.getPlayerFromList(this.getTerritory(attackerDefender[1]).getOwner());
                defenderDice = defender.getDefenderDice(this, this.getTerritory(attackerDefender[1]));

                //DiceFight results
                displayMessage(this.diceFight(attackerDefender, attackerDice,defenderDice));

                //Possible elimination and announcement of winner
                //Current diceFight ends if attacker has 1 troop left, or territory is conquered
                if (endDiceFight(attackerDefender, attackerDice)) break;

            }//End diceFightOrQuit


        }//End wantToAttack
        //Proceed to fortify stage of turn
    }//End gameAttack()

    public boolean endDiceFight(String[] attackerDefender, int attackerDice){
        //Checking if another dice fight can happen or not.
        if (this.getTerritory(attackerDefender[0]).getTroops() <= 1){
            //Attacker can no longer dice fight from the current selected territory
            this.displayMessage("Only one troop left! You can no longer attack from " + attackerDefender[0]);
            return true;


        } else if (this.getTerritory(attackerDefender[1]).getTroops() <= 0){
            //Defender just lost. Territory is given to the attacker. Ask how many troops to move in
            this.displayMessage(attackerDefender[1] + " has just been conquered!");




            //Asking user for how many troops to move into newly conquered territory
            //int incomingTroops = gameView.troopsToMoveIn(this.getTerritory(attackerDefender[0]).getTroops(), attackerDice);


            int incomingTroops = currentPlayer.getTakeoverTroops(this, attackerDice, this.getTerritory(attackerDefender[0]).getTroops());

            //Checking if the defender owner is eliminated
            String loserName = this.getTerritory(attackerDefender[1]).getOwner();
            if (this.takeoverTerritory(currentPlayer, this.getTerritory(attackerDefender[0]), this.getTerritory(attackerDefender[1]), incomingTroops)){
                //A player has been eliminated
                this.announceElimination(loserName);

                //Checking if the player has won. Announce it if so
                if (this.hasWinner()) this.announceWinner();
            }
            return true;//No more dice fight for this attackDefender pair after territory takeover
        }
        return false;
    }

    public void gameFortify(){
        if (!currentPlayer.wantToFortify(this) || this.hasWinner()) return;
        //Want to fortify if made it to here

        Territory fortifyGiver = this.getTerritory(currentPlayer.chooseFortifyGiver(this));
        Territory fortifyReceiver = this.getTerritory("");
        int troopsToMove = currentPlayer.getFortifyTroops(this, fortifyGiver);

        fortifyGiver.changeTroops(-troopsToMove);
        fortifyReceiver.changeTroops(troopsToMove);

        displayMessage("Fortify stage complete");
    }

    public void startTurn(){
        gameDraft();
        gameAttack();
        gameFortify();
        displayMessage("End of " + currentPlayer.getName() + "'s turn!");
    }



    /**
     * Getting a string array of all of the players territories. Used during the draft phase in the view
     *
     * @return String array of all the territories that the current player has
     */
    public String[] getPlayersTerritories(){
        return this.getCurrentPlayerObject().getTerritoriesList();
    }

    /**
     * Starts the draft,attack,(Fortify on next milestone),endTurn sequence for the current player.
     *
     */
    public void startPlayersTurn() {
   /*     if (this.hasWinner()) {
            //Cannot do next turn if a winner has already been found
            this.announceWinner();
            return;//Immediately get out the method.
        }

        //If the current player is an AI player, handle turn in different method
        if (this.getCurrentPlayerObject() instanceof AIPlayer){
            startAIPlayersTurn((AIPlayer) this.getCurrentPlayerObject());
        } else{
            //Currently a human player
            humanPlayerDraft(this.getCurrentPlayerObject());
            humanPlayerAttack(this.getCurrentPlayerObject());
            if (!this.hasWinner() && this.getCurrentPlayerObject().getFortifyGivers() != null && this.getCurrentPlayerObject().getTerritories().size() > 2){
                //Can only fortify when there is no winner and when the current player has more than one territory
                humanPlayerFortify(this.getCurrentPlayerObject());
            }
        }
        this.displayMessage(this.getCurrentPlayerObject().getName() + " has finished their turn!");
        this.nextTurn();//Switching to the next player
        */

    }

    /**
     * Starts the draft phase for a human player
     *
     * @param currentPlayer The current player
     */
    private void humanPlayerDraft(Player currentPlayer){
       /* this.displayMessage("Starting the draft phase for player: " + this.getCurrentPlayer());
        String[] draftInfoFromView;

        currentPlayer.bonusTroops();

        //Keep asking player to send troops to territories until there are no more troops to send
        while (currentPlayer.getNumTroops() > 0){
            //draftInfoFromView = this.startDraft();//ONLY FOR HUMANS
            this.displayMessage(this.getCurrentPlayerObject().draftPhase(draftInfoFromView[0],draftInfoFromView[1]));
        }

        //Draft complete. Move on to attack
        this.displayMessage("Draft stage complete, starting the attack phase for player: " + this.getCurrentPlayer());
        */

    }

    /**
     * Starts the attack phase for a human player
     *
     * @param currentPlayer The current player
     */
   /* private void humanPlayerAttack(Player currentPlayer){
        int endAttackStage = 0; //Non zero when player hits "End Attack" or window X
        String[] attackerDefender; //{AttackingTerritoryName , DefenderTerritoryName}
        while (true){//Continue attacking until player decides to stop, or no more territories can start the attack
            //Ensure that the player has attack starters
            if (this.getCurrentPlayerObject().getAttackStarters() == null){
                this.displayMessage(this.getCurrentPlayer() + " has no territories that can start an attack. End attack phase");
                break;
                //End turn and move on if no attack starters
            }

            //Getting the option from the player
            //endAttackStage = gameView.attackOrQuitOption();

            if (endAttackStage!=0)break;//Player chooses to end attack. Moving on to next phase

            //Player wants to start an attack
            //attackerDefender = gameView.attackSelection();
            //^^Array of {attacker, defender}

            this.displayMessage(attackerDefender[0] + " is about to attack " + attackerDefender[1]);

            int[] diceFightChoice;//{diceFightOrBackToAttackSelection, numDiceRolls}
            String diceFightResultString = "";


            while(true){//A PARTICULAR DICE FIGHT AFTER ATTACKER&DEFENDER ARE SELECTED

                diceFightChoice = gameView.diceFightView(this.getTerritory(attackerDefender[0]).maxDiceToRoll());
                if (diceFightChoice[0] != 0) break;//Player wants to end current diceFight.

                diceFightResultString = this.diceFight(attackerDefender, diceFightChoice[1], getDefenderDiceRoll(attackerDefender[1]));
                diceFightResultString += "\n" + this.diceFightInfo(attackerDefender);
                this.displayMessage(diceFightResultString);//Telling the player the rolls and results of the dice fight

                //Checking if another dice fight can happen or not.
                if (this.getTerritory(attackerDefender[0]).getTroops() <= 1){
                    //Attacker can no longer dice fight from the current selected territory
                    this.displayMessage("Only one troop left! You can no longer attack from " + attackerDefender[0]);
                    break;
                } else if (this.getTerritory(attackerDefender[1]).getTroops() <= 0){
                    //Defender just lost. Territory is given to the attacker. Ask how many troops to move in
                    this.displayMessage(attackerDefender[1] + " has just been conquered!");

                    //Asking user for how many troops to move into newly conquered territory
                    int incomingTroops = gameView.troopsToMoveIn(this.getTerritory(attackerDefender[0]).getTroops(), diceFightChoice[1]);

                    //Checking if the defender owner is eliminated
                    String loserName = this.getTerritory(attackerDefender[1]).getOwner();
                    if (this.takeoverTerritory(currentPlayer, this.getTerritory(attackerDefender[0]), this.getTerritory(attackerDefender[1]), incomingTroops)){
                        //A player has been eliminated
                        this.announceElimination(loserName);

                        //Checking if the player has won. If so,
                        if(this.hasWinner()){
                            this.announceWinner();
                            return;
                        }
                    }
                    break;//No more dice fight for this attackDefender pair after territory takeover
                }
            }
        }
    }*/

    /**
     * Starts the fortify phase for a human player
     *
     * @param currentPlayer The current player
     */
    /*private void humanPlayerFortify(Player currentPlayer){
        this.displayMessage("Attack stage complete, starting the Fortify stage for player: " + this.getCurrentPlayer());
        //Ask player to choose any one of their owned terrys
        String[] results = gameView.startFortify(Player.getTerritoryStringArray(currentPlayer.getFortifyGivers()));
        if (!results[0].equals("0")) return; //Player decides to skip fortify. Ends turn

        //Once selection is made, get the fortifiable terry list

        //Player chooses one of the fortifiable territories
        String fortified = gameView.chooseFortified(Player.getTerritoryStringArray(currentPlayer.getFortifiableTerritories(this.getTerritory(results[1]))));

        int movedTroops = gameView.numTroopsToFortify(this.getTerritory(results[1]).getTroops() - 1 );
        //Max troops to send is numTroops on fortifyStarter-1
        //Adjust the troop numbers appropriately
        this.getTerritory(results[1]).changeTroops(-movedTroops);
        this.getTerritory(fortified).changeTroops(movedTroops);
    }*/

    /**
     * Processes and handles an AI player's turn.
     * This will only be called by the startPlayersTurn() method if the current player is an AI
     *
     */
    /*private void startAIPlayersTurn(AIPlayer aiPlayer){
        // Draft phase
        this.displayMessage(aiPlayer.aiDraftPhase());

        //Attack phase
        AIPlayerAttack(aiPlayer);

        // Fortify
        if (!this.hasWinner() && aiPlayer.getFortifyGivers() != null) {
            AIPlayerFortify(aiPlayer);
        }
    }*/

    /**
     * Runs the attack phase of an AI players turn
     *
     * @param aiPlayer the current AIPlayer
     */
   /* private void AIPlayerAttack(AIPlayer aiPlayer){
        // Attack phase
        Territory attacker, defender;
        while(aiPlayer.wantToAttack()){

            //Selecting the attacker and defender for this particular dice fight
            attacker = aiPlayer.findAttackStarter();
            defender = aiPlayer.findAttackDefender(attacker);

            if (defender==null || attacker == null || !aiPlayer.wantToDiceFight(attacker)) break;

            //DiceFight
            while(aiPlayer.wantToDiceFight(attacker)){
                //Need how many dice the attacker rolled
                int attackerDice = aiPlayer.chooseNumDice(attacker);
                //Need to "ask" defender how many to roll
                int defenderDice = getDefenderDiceRoll(defender.getTerritoryName());

                //Do the diceFight and the GUI show the dice fight results
                String[] attackerDefender = {attacker.getTerritoryName(), defender.getTerritoryName()};
                String diceFightResultString = this.diceFight(attackerDefender, attackerDice, defenderDice);
                diceFightResultString += "\n" + this.diceFightInfo(attackerDefender);
                this.displayMessage(diceFightResultString);

                //Checking if another dice fight can happen or not.
                if (attacker.getTroops() <= 1){
                    break;
                } else if (defender.getTroops() <= 0){
                    //Defender just lost. Territory is given to the attacker. Ask how many troops to move in
                    this.displayMessage(defender.getTerritoryName() + " has just been conquered!");

                    //Checking if the defender owner is eliminated

                    if (this.takeoverTerritory(aiPlayer, attacker, defender, attackerDice)){
                        //A player has been eliminated
                        this.announceElimination(defender.getOwner());

                        //Checking if the player has won. If so,
                        if(this.hasWinner()){
                            this.announceWinner();
                            return;
                        }
                    }
                    break;//No more dice fight for this attackDefender pair after territory takeover
                }
            }
        }
    }*/

    /**
     * Runs the fortify phase of an AI players turn
     *
     * @param aiPlayer the current AIPlayer
     */
    private void AIPlayerFortify(AIPlayer aiPlayer){
        //Need to check if the AI wants to fortify or not
        if (aiPlayer.aiWantToFortify() && aiPlayer.getTerritories().size()>2){
            Territory giver = aiPlayer.findFortifyGiver();
            Territory receiver = aiPlayer.findFortifyReceiver(giver);

            Random rando = new Random();
            if (giver != null && receiver != null){
                int movedTroops = rando.nextInt(giver.getTroops()-1) + 1;
                giver.changeTroops(-movedTroops);
                receiver.changeTroops(movedTroops);
                this.displayMessage("Fortify phase complete: " + movedTroops + " troops moved from " + giver.getTerritoryName() + " to " + receiver.getTerritoryName());
            }

        }
    }

    /**
     * Gets the number of dice to roll form the defending territory's owner
     *
     * @param defender The name of the defending territory's owner
     * @return How many dice the defender wants to roll
     */
  /*  private int getDefenderDiceRoll(String defender){
        int defenderDiceFightChoice=1;
        if (this.getPlayerFromList(this.getTerritory(defender).getOwner()) instanceof AIPlayer){
            //Always roll 2 unless down to last troop. Roll 1
            defenderDiceFightChoice= (this.getTerritory(defender).getTroops() >=2) ? 2 : 1;
        } else {
            //Human player is the defender. Ask for how many dice to roll
            defenderDiceFightChoice = gameView.defenderDiceRoll(this.getTerritory(defender));
        }
        //GOTTA ASK DEFENDER HOW MANY DICE TO ROLL IF DEFENDER IS HUMAN
        //AI DEFENDERS AUTO ROLL 2 UNLESS FORCED TO ROLL 1
        return defenderDiceFightChoice;
    }    */

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
