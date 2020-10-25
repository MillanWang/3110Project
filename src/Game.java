import java.util.*;

public class Game {
    private Parser parser;
    private LinkedList<Player> players;
    private Player currentPlayer;
    private Dice dice;
    private int troopNumber;
    private DefaultWorldMap defaultWorldMap;
    private boolean gameEnds = false;

    public Game(){
        defaultWorldMap = new DefaultWorldMap();
        parser = new Parser();
        players = new LinkedList<Player>();
        play();
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
     * Print out some help information.
     * Here we print a cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You don't know what to do, Right?");
        System.out.println("Make sure to choose from the following commands.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("showMap, nextTurn ,quit ,help");
    }

    /**
     * would play the game
     */
    public void play(){
        welcomeMessage();
        System.out.println("********************************");
        makePlayers();
        defaultWorldMap.printAllTerritories();

        while (!gameEnds){
            Command command = parser.getCommand();
            gameEnds = processCommand(command);
        }
    }

    /**
     * Ends the current player's turn and set's the current player to the next player in the list
     */
    private void nextTurn(){
        //START THE NEXT PLAYERS TURN Draft,Attack, end
        System.out.println("\n\nCurrent Player: " + currentPlayer.getName());
        currentPlayer.draftPhase();

        //ATTACK PHASE

        System.out.println("Player " + currentPlayer.getName() + " has finished their turn");

        players.add(currentPlayer);//Added to the back
        currentPlayer = players.pop();//Pull out the first player in line to go next
    }

    private void eliminatePlayer(Player loser){
        players.remove(loser);
        if (players.isEmpty()){
            System.out.println(currentPlayer.getName()+ " is the ULTIMATE RULER OF THE WORLD!!!");
            gameEnds = true;
        }
    }

    /**
     * TAHER'S WORK
     * @param command
     * @return
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        } else if (command.getCommandWord().equals("help")) {
            printHelp();
        }else if (command.getCommandWord().equals("quit")){
            wantToQuit = true;
        }else if (command.getCommandWord().equals("nextTurn")){
            nextTurn();
        }else if (command.getCommandWord().equals("showMap")) {
            defaultWorldMap.printAllTerritories();
        }
        return wantToQuit;
    }


    /*
    private void attackPhase(){
        Scanner input = new Scanner (System.in);
        System.out.println("Select territory to attack from");
        System.out.println("You currently own " + "\n" + currentPlayer.getAllTerritories());
        String c1 = input.nextLine();
        if(c1.equals(currentPlayer.getTerritory(c1).getTerritoryName())){ //check if user input equals a territory to attack from
            System.out.println("Select country to attack");
            System.out.println("You are currently able to attack " +"\n" +
                    currentPlayer.getTerritory(c1).printAttackableNeighbours()); //shows territories currentplayer is neighbours with
        }
        String c2 = input.nextLine();
        Territory defender = defaultWorldMap.getTerritory(c2);
        if (c2.equals(defender.getTerritoryName())){ //if user input equals a territory to attack
            System.out.println("proceed to roll dice");
        }

        //Dice Roll Phase
        System.out.println("You have the option to roll 1, 2, or 3 dice");
        System.out.println("Select how many you'd like to roll");

        int c3 = input.nextInt();
        dice = new Dice();
        System.out.println(dice.diceFight(c3, defender.getTroops()));
        */
        /**
         dice.roll();// roll amount of dice user inputs
         int attackerValue = dice.rollDie();
         System.out.println("You rolled " + attackerValue);
         Dice dice2;
         int defenderValue;
         if(defender.getTroops() >= 2){
         dice2 = new Dice(2); //roll 2 dice by default if defender's troops are >= 2
         dice2.roll();
         defenderValue = dice2.rollDie();
         System.out.println("Defender rolled " + defenderValue);
         }else{
         dice2 =new Dice(1); //roll 1 dice by default if defender's troops are < 2
         dice2.roll();
         defenderValue = dice2.rollDie();
         System.out.println("Defender rolled " + defenderValue);
         }}
         */
        //Dice Fight


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
        int pNumber = 1;
        for(Player p : players){
            System.out.println("Player " + pNumber+" : " + p.getName());
            pNumber++;
        }
        Collections.shuffle(players);//Players are initialized and order is randomized.

        //RANDOM DISTRIBUTION OF TERRITORIES AND TROOPS
        //The territory list is always randomized in the DefaultWorldMap class
        for(Territory terry : defaultWorldMap.getAllTerritories()){
            players.peek().addTerritory(terry); //Adding current territory to current player
            terry.setOwner(players.peek().getName()); //Setting owner of territory to current player
            players.add(players.pop()); //Sending current player to the back of the queue
        }
        //Putting troops in all player's territories
        for (Player player: players){
            player.setupPlayer(numPlayers);
        }
        currentPlayer = players.pop();//Establish the first player to go
        nextTurn();
    }


    public static void main(String[] args) {
        Game game = new Game();


    }
}
