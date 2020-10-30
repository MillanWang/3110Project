import java.util.*;

public class Game {
    private Parser parser;
    private LinkedList<Player> players;
    private Player currentPlayer;
    private Dice dice;
    private DefaultWorldMap defaultWorldMap;
    private boolean gameEnds;

    /**
     * Constructor for the class game. This will start playing the game
     */
    public Game(){
        defaultWorldMap = new DefaultWorldMap();
        parser = new Parser();
        players = new LinkedList<Player>();
        gameEnds = false;
        dice = new Dice();
        play();
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

    /**
     * Prints a welcome message introducing the players to the game Risk
     */
    private void welcomeMessage(){
        System.out.println("Welcome to Risk\n");
        System.out.println("The goal of the game is to take control of all territories on the map");
        System.out.println("Players who lose all of their territories are eliminated from the game");
        System.out.println("The last player standing is the ULTIMATE CHAMPION");
        System.out.println("Type 'Help' if you don't know what to do.");
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
     * Starts the game. The game will end when the player chooses to quit or when a winner is found
     */
    private void play(){
        welcomeMessage();
        System.out.println("******************************");
        makePlayers();

        while (!gameEnds){
            Command command = parser.getCommand();
            gameEnds = processCommand(command);
        }
    }

    /**
     * Completes the current players turn (Draft>attack cycle) and sets the current player to the next player in line
     */
    private void nextTurn(){
        //START THIS PLAYER'S TURN (Draft>Attack>End cycle)
        System.out.println("\n\nCurrent Player: " + currentPlayer.getName());
        currentPlayer.draftPhase();
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
            defaultWorldMap.printAllTerritories();
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
        currentPlayer.printAttackStarters();

        Territory attackStarterTerritory, defenderTerritory;// Needed for diceFight
        boolean endAttack = false;

        while(!endAttack){
            System.out.println("Type \"attack\" to start an attack or type \"skip\" to move on");
            //Ask player to start attack or skip attack phase and move on to (NEXT TURN). Verify input
            String attackOrSkip = "";
            if (currentPlayer.getAttackStarters().isEmpty()) {
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
                    currentPlayer.printAttackStarters(); //shows territories current player is neighbours with
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
                        int diceWonWith = dice.diceFight(attackStarterTerritory,defenderTerritory);
                        if (defenderTerritory.getTroops() <=0 ){
                            //Defender has been killed
                            takeoverTerritory(currentPlayer,attackStarterTerritory, defenderTerritory, diceWonWith);
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

    /**
     * Asks for how many players this game will have and verifies that it is 2-6
     * Then asks user to choose unique player names for each player
     * Randomly distributes the territories to the players
     * Initiates process for players to distribute troops throughout their given territories
     */
    private void makePlayers(){
        Scanner input = new Scanner(System.in);
        int numPlayers = 0;
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

        for(int i = 0 ; i < numPlayers ; i++) {
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
                        players.add(new Player(currName));
                        break;
                    }
                }//End check for legal name
            }//End while
        }

        //RANDOM DISTRIBUTION OF TERRITORIES AND TROOPS
        //The territory list is always randomized in the DefaultWorldMap class
        for(Territory terry : defaultWorldMap.getAllTerritories()){
            players.peek().addTerritory(terry); //Adding current territory to current player
            terry.setOwner(players.peek().getName()); //Setting owner of territory to current player
            players.add(players.pop()); //Sending current player to the back of the queue
        }
        
        Collections.shuffle(players);//Players are initialized and order is randomized.

        //Putting troops in all player's territories
        int pNumber = 1;
        for (Player player: players){
            player.setupPlayer(numPlayers);
            System.out.println("Player " + pNumber+" : " + player.getName());
            pNumber++;
        }
        currentPlayer = players.pop();//Establish the first player to go
        nextTurn();
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
