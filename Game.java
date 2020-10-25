import java.util.*;

public class Game {
    private Parser parser;
    private LinkedList<Player> players;
    private Player currentPlayer;
    private Dice dice;
    private int troopNumber;
    DefaultWorldMap defaultWorldMap;

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
        System.out.println("attack, fortify, draft, passTurn, endAttack, roll1, roll2, roll3, autoAttack,quit,help");
    }

    /**
     * would play the game
     */
    public void play(){
        welcomeMessage();
        System.out.println("********************************");
        makePlayers();

        boolean gameEnds = false;
        while (!gameEnds){
            Command command = parser.getCommand();
            gameEnds = processCommand(command);
        }
    }

    private void showAllTerritories(){
        defaultWorldMap.printAllTerritories();
    }

    /**
     * Ends the current player's turn and set's the current player to the next player in the list
     */
    private void nextTurn(){
        players.add(currentPlayer);//Added to the back
        currentPlayer = players.pop();//Pull out the first player in line to go next
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
        } else if (command.equals("help")) {
            printHelp();
        }else if (command.equals("quit")){
            wantToQuit = true;
        }else if (command.equals("passTurn")){
            nextTurn();
        }else if (command.equals("attack")){
            attackPhase(); // THIS METHOD NEEDS TO BE IMPLEMENTED
        }else if(command.equals("draft")){
            draftPhase(); // THIS METHOD NEEDS TO BE IMPLEMENTED
        }
        return wantToQuit;
    }

    private void draftPhase(){
        while (currentPlayer.getNumTroops() != 0) {
            Scanner input = new Scanner(System.in);
            System.out.println("Select territory in which you would like to send troops to.");
            System.out.println("You currently own " + "\n" +
                    currentPlayer.getAllTerritories());

            String t = input.nextLine();
            if (t.equals(currentPlayer.getTerritory(t).getTerritoryName())) {
                System.out.println("Select amount of troops to send");
            }

            troopNumber = input.nextInt();
            if (troopNumber < 0) {
                System.out.println("Number cannot be less than 1!");
            }

            currentPlayer.getTerritory(t).changeTroops(troopNumber);
            //currentPlayer.setNumTroops(currentPlayer.getNumTroops() - troopNumber);
            System.out.println("You now have " + currentPlayer.getNumTroops() + " troops");
        }
        System.out.println("You have run out of troops. Proceed to attack phase");
    }

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
         }
         */
        //Dice Fight
    }

    private void makePlayers(){
        Scanner input = new Scanner(System.in);
        int numPlayers = 0;
        while(true) {
            System.out.println("Choose number of players (2-6)");
            numPlayers = input.nextInt();  //HAVE TO FIGURE OUT WHAT TO DO IF THE USER INPUTS STRING INSTEAD OF INT
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
    }


    public static void main(String[] args) {
        Game game = new Game();

    }
}