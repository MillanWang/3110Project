import java.util.*;

public class Game {
    private LinkedList<Player> players;
    private Player currentPlayer;
    private Dice dice;
    private GenericWorldMap genericWorldMap;

    /**
     * Constructor for the class game. This will start playing the game
     */
    public Game(){
        genericWorldMap = new GenericWorldMap();
        players = new LinkedList<Player>();
        dice = new Dice();
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
     * Prints a welcome message introducing the players to the game Risk
     */
    public String welcomeMessage(){
        String gameRules = "Welcome to RISK Global Domination\n"+
                "The goal of the game is to take control of all territories on the map.\n"+
                "Players who lose all of their territories are eliminated from the game.\n" +
                "The last player standing is the ULTIMATE CHAMPION.\n" +
                "To start the draft phase, click on the Start Next Turn JMenu Item (Top Right).";
        return gameRules;
    }


    /**
     * Return a message to the user if they want to quit.
     *
     * @return String message
     */
    public String quitMessage() {
        return "Thanks for playing. Goodbye";
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
            players.add(new Player(s));
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
}
