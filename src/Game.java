import java.io.*;
import java.util.*;

public class Game implements Serializable {
    public enum GameState {LOAD,
        MESSAGE,
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
    public Game(String customMapName) {
        genericWorldMap = new GenericWorldMap(customMapName);
        players = new LinkedList<Player>();
        dice = new Dice();
        observers = new LinkedList<>();
        gameState = GameState.MESSAGE;
    }

    /**
     * Alternate constructor for creating Game with a custom map that verifies that the loaded map is good
     *
     * @param customMapName Name of the custom map file
     * @return game object if the custom map is good. Null if the custom map is illegal
     */
    public static Game makeGameVerifyMap(String customMapName){
        Game game = new Game(customMapName);

        if (!game.getGenericWorldMap().verifyMap()){
            //When the custom loaded map is invalid
            return null;
        }else{
            //Custom map is valid and playable
            return game;
        }
    }

    /**
     * Creates a new GUI view object to for the current game.
     */
    public void showView(){
        this.addObserver(GameView.GameViewNewGame(this));
    }

    /**
     * Replaces the previous GUI view with a newly generated one. Used when loading games
     */
    public void replaceView(){
        ((GameView) observers.get(0)).dispose();
        observers.remove(0);
        this.addObserver(new GameView(this));
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
     * Sends the current player to the back of the line and the next player in line becomes current player
     */
    public void nextTurn(){
        players.add(currentPlayer);//Added to the back
        currentPlayer = players.pop();//Pull out the first player in line to go next
    }

    /**
     * Completes the current players turn (Draft>attack cycle)
     */
    public void startTurn(){
        if (hasWinner()){
            announceWinner();
            return;
        }

        gameDraft();
        gameAttack();
        if (hasWinner()) return; //Skipping fortify if the game is won
        gameFortify();
        displayMessage("End of " + currentPlayer.getName() + "'s turn!");
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
     * Sets the message field in this class. For use in defender diceFight dice selection
     * @param currentMessage String to occupy the currentMessage field
     */
    public void setCurrentMessage(String currentMessage) { this.currentMessage = currentMessage; }

    /**
     * Getter method for the currentMessage field. Used in defender diceFight dice selection
     * @return currentMessage field
     */
    public String getCurrentMessage() { return currentMessage; }

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

        Territory attacker = getTerritory(attackerDefender[0]);
        Territory defender = getTerritory(attackerDefender[1]);


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


        diceFightResultMessage += "\n\n" + "Attacker has " + attacker.getTroops() + " troops remaining";
        diceFightResultMessage += "\n" + "Defender has " + defender.getTroops() + " troops remaining";

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
            game.makeGUIVisible();
        }catch (Exception e){
            System.err.println("Error when loading file");
        }
        return game;
    }

    /**
     * Makes the GUI visible. Used for loaded games
     */
    private void makeGUIVisible(){
        gameState = GameState.LOAD;
        notifyObservers();
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

    /**
     * Sets the message field in the current player object. This is to get the results from the GUI
     * @param controllerMessage
     */
    public void setPlayerControllerMessage(String controllerMessage){
        currentPlayer.setControllerMessage(controllerMessage);
    }

    /**
     * Adds an observer object for the current game
     *
     * @param gameObserver The observer to be added
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
        gameState = GameState.MESSAGE;
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

    /**
     * Publishes event to GUI to get the user input for a draft
     */
    public void draftEvent(){
        gameState = GameState.DRAFT;
        currentMessage = "" + currentPlayer.getNumTroops();
        currentTerritoriesOfInterest = currentPlayer.getTerritoriesList();
        notifyObservers();
    }

    /**
     * Publishes event to GUI asking player to start attack or to move on to fortify
     */
    public void attackOrQuit(){
        gameState = GameState.ATTACKORQUIT;
        notifyObservers();
    }

    /**
     * Publishes event to GUI asking the player for where to start an attack from
     */
    public void chooseAttackStarter(){
        gameState = GameState.ATTACKERSELECTION;
        currentTerritoriesOfInterest = Player.getTerritoryStringArray(currentPlayer.getAttackStarters());
        notifyObservers();
    }

    /**
     * Publishes event to GUI asking the player where to attack
     *
     * @param attackStarter Name of territory starting the attack
     */
    public void chooseAttackDefender(String attackStarter){
        gameState = GameState.DEFENDERSELECITON;
        currentTerritoriesOfInterest = Player.getTerritoryStringArray(currentPlayer.getTerritory(attackStarter).getAttackableNeighbours());
        currentMessage = attackStarter;
        notifyObservers();

        currentMessage = "";
    }

    /**
     * Publishes event to GUI asking user if they want to dice fight or not
     *
     * @param attackerCommaDefender String of attacker,defender.
     */
    public void wantToDiceFight(String attackerCommaDefender){
        gameState = GameState.DICEFIGHTORQUIT;
        currentMessage = attackerCommaDefender;
        notifyObservers();
        currentMessage = "";
    }

    /**
     * Publishes event to GUI asking how many dice to roll
     *
     * @param maxDice maximum number of dice to roll
     * @param owner Name of the owner
     * @param territoryName Name of the attacking territory
     */
    public void chooseAttackerDice(int maxDice, String owner, String territoryName){
        gameState = GameState.DICEFIGHTATTACKERCHOICE;
        currentMessage = "" + maxDice + "," + owner + "," + territoryName;
        notifyObservers();
        currentMessage = "";
    }

    /**
     * Publishes event to GUI asking the defending player how many dice to roll
     *
     * @param maxDice maximum number of dice to roll
     * @param owner Name of the owner of the defending territory
     * @param territoryName Name of the defending territory
     */
    public void chooseDefenderDice(int maxDice, String owner, String territoryName){
        gameState = GameState.DICEFIGHTDEFENDERCHOICE;
        currentMessage = "" + maxDice + "," + owner + "," + territoryName;
        notifyObservers();
    }

    /**
     * Publishes event to GUI asking user how many troops to move into a conquered territory
     *
     * @param minMovingIn minimum number of troops moving in
     * @param maxMovingIn maximum number of troops moving in
     */
    public void takeoverTerritoryEvent(int minMovingIn, int maxMovingIn){
        gameState = GameState.TAKEOVERTERRITORY;
        currentMessage = minMovingIn + "," + maxMovingIn;
        notifyObservers();
        currentMessage = "";
    }

    /**
     * Publishes event to GUI asking the player if they want to fortify or not
     */
    public void fortifyOrQuit(){
        gameState = GameState.FORTIFYORQUIT;
        notifyObservers();
    }

    /**
     * Publishes event to GUI asking the player asking where to take troops from during fortify
     */
    public void chooseFortifyGivers(){
        gameState = GameState.FORTIFYGIVER;
        currentTerritoriesOfInterest = Player.getTerritoryStringArray(currentPlayer.getFortifyGivers());
        notifyObservers();
    }

    /**
     * Publishes event to GUI asking the player where to sent troops to during fortify
     *
     * @param fortifyGiver The troop giver in the fortify stage
     */
    public void chooseFortifyReceivers(String fortifyGiver){
        gameState = GameState.FORTIFYRECEIVER;
        Territory territory = currentPlayer.getTerritory(fortifyGiver);
        currentTerritoriesOfInterest = Player.getTerritoryStringArray(currentPlayer.getFortifiableTerritories(territory));
        notifyObservers();
    }

    /**
     * Publishes event to GUI asking the player how many troops to move between them
     *
     * @param maxTroopsToMove maximum number of troops to move
     */
    public void chooseFortifyTroops(int maxTroopsToMove){
        gameState = GameState.FORTIFYTROOPSTOMOVE;
        currentMessage = "" + maxTroopsToMove;

        notifyObservers();
        currentMessage = "";
    }

    /**
     * Conducts the draft process for the current player
     */
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

    /**
     * Conducts the attack process for the current player
     */
    public void gameAttack(){
        int counter = 0;
        while (currentPlayer.wantToAttack(this)){
            counter++;
            if (counter > 100) break;
            /*In rare cases, AI players will repeatedly select the same attackers and defenders
            while also not wanting to dice fight. That will infinitely loop here and this counter will prevent it
            Under normal circumstances, no reasonable player will ever try to do more than 100 separate attacks in the same turn
            */

            String[] attackerDefender = {"",""};
            //AttackStarterSelection
            attackerDefender[0] = currentPlayer.chooseAttackStarter(this);

            //AttackDefenderSelection
            attackerDefender[1] = currentPlayer.chooseAttackDefender(this, attackerDefender[0]);

            //DiceFightOrQuit
            int attackerDice, defenderDice;
            while (currentPlayer.wantToDiceFight(this, attackerDefender[0]+ "," + attackerDefender[1])){
                //DiceFightAttackerChoice
                attackerDice = currentPlayer.getAttackerDice(this, getTerritory(attackerDefender[0]));

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

    /**
     * Checks if another dice fight is possible for the current attacker & defender selection
     *
     * @param attackerDefender
     * @param attackerDice Number of dice rolled
     * @return if another dice fight is possible
     */
    public boolean endDiceFight(String[] attackerDefender, int attackerDice){
        //Checking if another dice fight can happen or not.
        if (this.getTerritory(attackerDefender[0]).getTroops() <= 1){
            //Attacker can no longer dice fight from the current selected territory
            this.displayMessage("Only one troop left! You can no longer attack from " + attackerDefender[0]);
            return true;


        } else if (this.getTerritory(attackerDefender[1]).getTroops() <= 0){
            //Defender just lost. Territory is given to the attacker. Ask how many troops to move in
            this.displayMessage(attackerDefender[1] + " has just been conquered!");

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

    /**
     * Conducts the fortify process for the current player
     */
    public void gameFortify(){
        if (!currentPlayer.wantToFortify(this) || this.hasWinner()) return;
        //Want to fortify if made it to here

        Territory fortifyGiver = this.getTerritory(currentPlayer.chooseFortifyGiver(this));
        Territory fortifyReceiver = this.getTerritory(currentPlayer.chooseFortifyReceiver(this, fortifyGiver.getTerritoryName()));
        int troopsToMove = currentPlayer.getFortifyTroops(this, fortifyGiver);

        fortifyGiver.changeTroops(-troopsToMove);
        fortifyReceiver.changeTroops(troopsToMove);

        displayMessage("Fortify stage complete");
        gameState = GameState.MESSAGE;
    }

}
