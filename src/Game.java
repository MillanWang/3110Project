import java.util.*;

public class Game {
    private Parser parser;
    private LinkedList<Player> players;
    private Player currentPlayer;
    private Dice dice;



    private GenericWorldMap genericWorldMap;
    private boolean gameEnds;

    /**
     * Constructor for the class game. This will start playing the game
     */
    public Game(){
        genericWorldMap = new GenericWorldMap();
        parser = new Parser();
        players = new LinkedList<Player>();
        gameEnds = false;
        dice = new Dice();
        //play(); no need for it anymore
    }

    /**
     * gets the player object given the player's name
     *
     * @param name name set to the player by the user
     * @return the player object corresponding to the given name
     */
    private Player getPlayerFromList(String name){
        for (Player player : players){
            if (player.getName().equals(name)){
                return player;
            }
        }
        return null; //This will never happen. Will always be found. Only called from the takeoverTerritory method
    }

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
                "The last player standing is the ULTIMATE CHAMPION";
        return gameRules;
    }

    /**
     * Print out the legal commands of the game for the user
     */
    private void printHelp()
    {
        System.out.println("******************************");
        System.out.println("Make sure to choose from the following commands.");
        System.out.println("showMap : Prints a list of all territories on the map with number of troops on them and owner");
        System.out.println("nextTurn : Starts the Draft>Attack turn cycle for the next player");
        System.out.println("quit : To end the game");
        System.out.println("help : To show this message\n");
        System.out.println("Your command words are:");
        System.out.println("showMap, nextTurn ,quit ,help");
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
     * Starts the game. The game will end when the player chooses to quit or when a winner is found
     */
    private void play(){
        welcomeMessage();
        System.out.println("******************************");
        //makePlayers();// It'll be called by GameView

        while (!gameEnds){
            Command command = parser.getCommand();
            gameEnds = processCommand(command);
        }
    }
    public String getCurrentPlayer(){
        return this.currentPlayer.getName();
    }
    public Player getCurrentPlayerObject(){
        return this.currentPlayer;
    }


    /**
     * Completes the current players turn (Draft>attack cycle) and sets the current player to the next player in line
     */
    private void nextTurn(){
        //START THIS PLAYER'S TURN (Draft>Attack>End cycle)
        System.out.println("\n\nCurrent Player: " + currentPlayer.getName());
        //currentPlayer.draftPhase();

        //PASSING CONTINENT HASHMAP TO THE DRAFT PHASE TO GET THE CONTINENT BONUS!



        attackPhase();
        System.out.println("Player " + currentPlayer.getName() + " has finished their turn");
        System.out.println("******************************");
        printHelp();
        players.add(currentPlayer);//Added to the back
        currentPlayer = players.pop();//Pull out the first player in line to go next
    }

    /**
     * Processes the commands from the user in the main menu interface section
     * @param command The command given by the player
     * @return True if the player wants to end the game. False otherwise
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        } else if (command.getCommandWord().equals("help")) {
            printHelp();
        }else if (command.getCommandWord().equals("quit")){
            System.out.println("Thank you for playing RISK\nHave a nice day!!");
            wantToQuit = true;
        }else if (command.getCommandWord().equals("nextTurn")){
            nextTurn();
        }else if (command.getCommandWord().equals("showMap")) {
            genericWorldMap.printAllTerritories();
            printHelp();
        }
        return wantToQuit;
    }


    /**
     * The attack phase of a player's turn
     * If player has territories that can start an attack, the player has the option to choose if they want to
     * start the attack or move on to the next phase (ends turn for now, fortify stage in the future)
     *
     * If a player decides to start an attack, ask where to start the attack
     * Once attack starter is determined, choose where to attack (the defender)
     *
     * Initiates a diceFight process (Dice class)
     * This may lead to a territory being conquered by the attacker. This would be a call to takeoverTerritory
     *
     * The attack phase is forced to end if the current player no longer has any territories that can start an attack
     */

    private void attackPhase() {
        Scanner input = new Scanner(System.in);
        System.out.println("******************************");
        System.out.println("Starting attack phase of turn for player " + currentPlayer.getName());
        System.out.println("******************************");
        //currentPlayer.printAttackStarters();

        Territory attackStarterTerritory, defenderTerritory;// Needed for diceFight
        boolean endAttack = false;

        while(!endAttack){
            System.out.println("Type \"attack\" to start an attack or type \"skip\" to move on");
            //Ask player to start attack or skip attack phase and move on to (NEXT TURN). Verify input
            String attackOrSkip = "";
            if (currentPlayer.getAttackStarters().length == 0) {
                endAttack = true;
                System.out.println("No territories can start an attack");
                break;
            }else {
                attackOrSkip = input.nextLine();
            }

            if (attackOrSkip.equals("skip")) {
                endAttack = true;
                System.out.println("Ending attack phase of turn. Moving on...");


                //AttackChoiceStage begins here
            } else if (attackOrSkip.equals("attack") ){
                System.out.println("Starting attack");
                //Player wants to attack. Give options of attack starters

                //Ask for attack starter. Verify it is legit
                while (true){
                    System.out.println("\nYou are currently able to start an attack from the following territories" );
                    //currentPlayer.printAttackStarters(); //shows territories current player is neighbours with
                    System.out.println("Choose from above territories where to start an attack from ");

                    String attackStarter = input.nextLine();

                    if (currentPlayer.getTerritory(attackStarter)==null){
                        System.out.println("Territory name does not exist. Try again.");
                    } else if (currentPlayer.canStartAttack(attackStarter)){
                        //Start attack
                        attackStarterTerritory = currentPlayer.getTerritory(attackStarter);
                        System.out.println("Starting attack from " + attackStarter);
                        break;
                    } else {
                        System.out.println("Cannot start an attack from that territory");
                    }
                }//At this point we have a pointer to the attackStarterTerritory
                List<Territory> attackableNeighbours = attackStarterTerritory.getAttackableNeighbours();

                while (true){
                    System.out.println("\nYou are currently able to attack the following territories" );
                    attackStarterTerritory.printAttackableNeighbours(); //shows territories that the current player can attack

                    String defender = input.nextLine();

                    //Get input from the user
                    //Check if input corresponds to a name of a terry that is attackable from attackStarterTerritory

                    if (attackStarterTerritory.canAttack(defender)){
                        //Valid defender is chosen
                        defenderTerritory = attackStarterTerritory.getNeighbour(defender);
                        break;
                    } else {
                        System.out.println("Invalid defender chosen. Try again!");
                    }
                }
                //Dice fight stage
                while(true){
                    System.out.print("******************************\nAttack starting from: ");
                    attackStarterTerritory.printInfo();
                    System.out.print("\nAttacking: ");
                    defenderTerritory.printInfo();

                    System.out.println("Type \"dice\" to start a battle or \"endBattle\" to end a battle");
                    String diceFightChoice = input.nextLine();

                    if (diceFightChoice.equals("dice")){
                        //int diceWonWith = diceFight(attackStarterTerritory,defenderTerritory, attackStarterTerritory.maxDiceToRoll());
                        if (defenderTerritory.getTroops() <=0 ){
                            //Defender has been killed
                            takeoverTerritory(currentPlayer,attackStarterTerritory, defenderTerritory, 3);//diceWonWith); Controller should know this
                            break;
                        } else if ( attackStarterTerritory.getTroops() <= 1){
                            //Attacker can no longer attack from this territory
                            System.out.println("Can no longer attack from " + attackStarterTerritory.getTerritoryName());
                            System.out.println("Only one troop left!");
                            break;
                        }
                    } else if (diceFightChoice.equals("endBattle")){
                        break;
                    } else {
                        System.out.println("Unknown command. Try again!");
                    }
                }
            } else {
                System.out.println("Invalid command!");
            }//End check for valid command
        }//End while(!endAttack)
    }//End attackPhase()

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
    public String diceFight(Territory attacker, Territory defender, int attackerDice){

        //Defender rolls 2 unless 1 troop left on territory
        int defenderDice;
        if(defender.getTroops() <= 1) {
            defenderDice=1;
        } else {
            defenderDice=2;
        }

        /*

        CONTROLLER SHOULD CALL THIS METHOD AND ONLY PROVIDE LEGAL OPTIONS FOR THE NUMBER OF DICE TO ROLL

        //Asking for user to choose how many dice to roll. Verify that the number is ok for the given territories
        Scanner input = new Scanner(System.in);
        int attackerMaxDice = Math.min(3, attacker.getTroops()-1);
        while(true) {
            try {
                System.out.println("Choose number of dice to roll (1 - " + attackerMaxDice + ")");
                attackerDice = input.nextInt();
                if (attackerDice < 1 || attackerDice > attackerMaxDice){
                    System.out.println("Invalid dice number. Try again");
                }else {
                    break;
                }
            }catch (Exception e){
                System.out.println("Please input a number between 1-" + attackerMaxDice);
                input.next();
            }
        }


        */


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
            diceFightResultMessage += "Attacker rolls a " + attackerRolls.peek();
            diceFightResultMessage += "Defender rolls a " + defenderRolls.peek();
            if (attackerRolls.pop() <= defenderRolls.pop()){
                //Defender wins
                diceFightResultMessage += "Defender wins! Attacker loses a troop! RIP";
                attacker.changeTroops(-1);
            } else {
                //Attacker wins
                diceFightResultMessage += "Attacker wins! Defender loses a troop! RIP";
                defender.changeTroops(-1);
            }
        }
        //If the attacker takes over the territory, the number of troops moved in has to be greater or equal to
        //the number of dice rolled on the most recent diceFight
        return diceFightResultMessage;
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
     * @param diceWonWith The number of dice that the winner rolled on the most recent diceFight
     */
    private void takeoverTerritory(Player winner,Territory attackerWinner, Territory killedDefender, int diceWonWith){
        System.out.println(winner.getName() + " has taken control over " + killedDefender.getTerritoryName());

        int numTroopsMovingIn=0;
        Scanner input = new Scanner(System.in);
        while(true) {//Asking user for how many troops are moving in
            System.out.println("Select between " + diceWonWith+ " - " + (attackerWinner.getTroops()-1) + " troops to move into " + killedDefender.getTerritoryName());
            try {
                numTroopsMovingIn = input.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Don't enter characters or strings. Numbers only");
                input.next();
                continue;
            }
            if ( numTroopsMovingIn<0 || numTroopsMovingIn < diceWonWith || numTroopsMovingIn >= attackerWinner.getTroops()) {
                System.out.println("Illegal number of troops.");
            } else{
                break;//Legal number troops moving in
            }
        }//A legal number of troops moving in is stored in numTroopsMovingIn

        killedDefender.setTroops(numTroopsMovingIn);
        attackerWinner.changeTroops(-numTroopsMovingIn);

        winner.addTerritory(killedDefender);

        String loserName = killedDefender.getOwner();//Name of the original owner.
        getPlayerFromList(loserName).removeTerritory(killedDefender);

        killedDefender.setOwner(winner.getName());

        //Check if the loser has been eliminated from the game
        if (getPlayerFromList(loserName).hasLost()){
            eliminatePlayer(getPlayerFromList(loserName));
            gameEnds = true;
        }
    }

    /**\
     * Eliminates player from the game given pointer to their player object
     *
     * @param loser The player eliminated from the game
     */
    private void eliminatePlayer(Player loser){
        players.remove(loser);
        System.out.println("Rest in peace "+ loser.getName() + ", you have been eliminated");
        if (players.isEmpty()){
            System.out.println(currentPlayer.getName()+ " is the ULTIMATE RULER OF THE WORLD!!!");
            gameEnds = true;
        }
    }


    //OLD DOCUMENTATION FOR makePlayers()
    /**
     * Asks for how many players this game will have and verifies that it is 2-6
     * Then asks user to choose unique player names for each player
     * Randomly distributes the territories to the players
     * Initiates process for players to distribute troops throughout their given territories
     */
    // NEW DOCUMENTATION FOR makePlayers()
    /**
     * sitting the playerNames by getting array of strings from the view and
     * initialize each player with it's playerName
     * @param playerNames
     */
    public void makePlayers(String[] playerNames){
        String [] namesOfPlayer = playerNames;
        //Scanner input = new Scanner(System.in);
        int numPlayers = playerNames.length;
        /*
        while(true) {
            System.out.println("Choose number of players (2-6)");
            try {
                numPlayers = input.nextInt();
            } catch (InputMismatchException e){
                System.err.println("Don't enter characters or strings");
                input.next();
            }
            if (numPlayers < 2 || numPlayers > 6){
                System.out.println("2-6 players only. Try again");
            } else{
                break;
            }
        }
         */

        for(int i = 0 ; i < numPlayers ; i++) {
            players.add(new Player(playerNames[i]));
        }
            /*
            while (true){ //Loop to ensure that a proper name is selected
                System.out.println("Set name for Player " + (i+1));
                String currName  = input.nextLine();

                //Ensure no spaces and no duplicate names
                if (currName.contains(" ") || currName.equals("")) {
                    System.out.println("Name cannot contain spaces and cannot be empty string");
                } else {
                    boolean hasDupe = false;
                    //Checking for duplicates
                    for (Player p : players){
                        if (p.getName().equals(currName)){
                            hasDupe = true;
                            break;
                        }
                    }//End dupe checking for loop
                    if (hasDupe){
                        System.out.println("Duplicate names are not allowed. Try another name");
                    } else {

                        //players.add(new Player(currName));
                        //break;

                    }
                }//End check for legal name
            }//End while
        }

         */

        //RANDOM DISTRIBUTION OF TERRITORIES AND TROOPS
        //The territory list is always randomized in the DefaultWorldMap class
        for(Territory terry : genericWorldMap.getAllTerritories()){
            players.peek().addTerritory(terry); //Adding current territory to current player
            terry.setOwner(players.peek().getName()); //Setting owner of territory to current player
            players.add(players.pop()); //Sending current player to the back of the queue
        }

        Collections.shuffle(players);//Players are initialized and order is randomized.

        //Putting troops in all player's territories
        int pNumber = 1;
        for (Player player: players){
            player.setupPlayer(numPlayers);
            //System.out.println("Player " + pNumber+" : " + player.getName());
            pNumber++;
        }
        currentPlayer = players.pop();//Establish the first player to go
        //nextTurn();
    }


    /**
     * Creates a new game object and starts the game
     *
     * @param args Formality
     */
    public static void main(String[] args) {
        Game game = new Game();

    }

}
