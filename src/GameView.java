import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GameView extends JFrame implements GameObserver{

    private JMenuBar menuBar;//the menu bar for the game
    private JMenuItem menuItemHelp, menuItemQuit, menuItemReset, menuItemCurrentPlayer, menuItemSaveGame, menuItemNextTurn;// the menuItems for the game
    private JPanel gamePanel,mapInfo,status;// the two JPanels that will be used in the game
    private GameController controller;// the controller of the game
    private ImageIcon map;// will store the path for the map picture

    /**
     * Constructor for the GameView class
     *
     * @param game The game object. Model for the game
     */
    public GameView(Game game){
        super("RISK: GLOBAL DOMINATION");
        this.setLayout(new BorderLayout());

        this.controller = new GameController(game);
        gamePanel = new JPanel(new BorderLayout());
        add(gamePanel);
        displayGame();
        setSize(1100, 825);
        // i changed resizable to true just in case the player wants it full screen
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Displays the game GUI
     */
    private void displayGame(){
        gamePanel.setVisible(true);
        addMenuItems();
        addMapPicture();
        addMapInfo();
        settingNumberOfPlayer();
        addGameStatus();
    }

    /**
     * Create a menu bar at the top of the window that will allow the user to choose if they want
     * help, to quit, to get the current Player info, endTurn, and showTerritory.
     */
    private void addMenuItems() {
        // Create menu bar
        menuBar = new JMenuBar();

        // Add help button
        menuItemHelp = new JMenuItem("Help");
        menuItemHelp.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemHelp.addActionListener(e -> {
            displayMessage("Welcome to RISK Global Domination\n"+
                    "The goal of the game is to take control of all territories on the map.\n"+
                    "Players who lose all of their territories are eliminated from the \n" +
                    "The last player standing is the ULTIMATE CHAMPION.\n" +
                    "To start the draft phase, click on the Start Next Turn JMenu Item (Top Right).");
        });
        menuBar.add(menuItemHelp);

        // Add reset button
        menuItemReset = new JMenuItem("Reset");
        menuItemReset.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemReset.addActionListener(e -> {
            dispose();//causes the JFrame window to be destroyed and cleaned up by the operating system
            new FirstView();//create a new GameVew, so new window
        });
        menuBar.add(menuItemReset);

        // Add quit button
        menuItemQuit = new JMenuItem("Quit");
        menuItemQuit.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemQuit.addActionListener(e -> {
            displayMessage("Thanks for playing. Goodbye");
            dispose();
        });
        menuBar.add(menuItemQuit);

        // Add show Turn button
        menuItemCurrentPlayer = new JMenuItem("Current-Player");
        menuItemCurrentPlayer.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemCurrentPlayer.addActionListener(e-> {
            displayMessage(controller.getCurrentPlayerInfo());
        });
        menuBar.add(menuItemCurrentPlayer);


        //Menu option to start the next turn. Turns options appear in windows
        menuItemNextTurn = new JMenuItem("Start Next Turn");
        menuItemNextTurn.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemNextTurn.addActionListener(e -> {
            controller.startPlayersTurn();
        });
        menuBar.add(menuItemNextTurn);

        // Add quit button
        menuItemSaveGame = new JMenuItem("Save and Quit");
        menuItemSaveGame.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemSaveGame.addActionListener(e -> {
            String uniqueFileName = "game session";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            uniqueFileName += format.format(new Date());
            controller.saveGame(uniqueFileName);
            updateGameStatus("Your current game session has been saved in : "+ uniqueFileName + "\n" +
                    "Thanks for playing. Goodbye");
            displayMessage("Your current game session has been saved in : "+ uniqueFileName + "\n" +
                    "Thanks for playing. Goodbye");
            dispose();
        });
        menuBar.add(menuItemSaveGame);

        gamePanel.add(menuBar, BorderLayout.NORTH);
    }


    /**
     * Sets the map image in the GUI
     */
    private void addMapPicture(){

        //NEEEDS A PARAMETER FOR THE PICTURE FILE NAME

        map = new ImageIcon(getClass().getResource("DefaultWorldMap.jpg"));
        JLabel MapLabel = new JLabel(map);
        gamePanel.setBackground(Color.LIGHT_GRAY);
        gamePanel.add(MapLabel,BorderLayout.CENTER);
    }

    /**
     * Sets the full game info on the GUI
     */
    private void addMapInfo(){
        mapInfo = new JPanel();
        mapInfo.setBackground(Color.LIGHT_GRAY);
        mapInfo.setLayout(new BoxLayout(mapInfo, BoxLayout.PAGE_AXIS));
        updateGameInfo();
        mapInfo.setVisible(true);
        gamePanel.add(mapInfo, BorderLayout.EAST);
    }

    /**
     * Sets the current game status on the GUI
     */
    private void addGameStatus(){
        status = new JPanel();
        status.setBackground(Color.LIGHT_GRAY);
        status.setBorder(BorderFactory.createLineBorder(Color.green));
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
        updateGameStatus("Welcome to RISK Global Domination\n"+
                "The goal of the game is to take control of all territories on the map.\n"+
                "Players who lose all of their territories are eliminated from the \n" +
                "The last player standing is the ULTIMATE CHAMPION.\n" +
                "To start the draft phase, click on the Start Next Turn JMenu Item (Top Right).");
        status.setVisible(true);
        gamePanel.add(status, BorderLayout.SOUTH);
        pack();
    }

    /**
     * updates the  current game status on the GUI
     */
    private void updateGameInfo(){
        mapInfo.removeAll();
        String[] arr = controller.getMapStringArray();
        for (int i = 0; i<arr.length; i++) {
            JLabel text = new JLabel( arr[i]);
            text.setFont(new Font("Arial", Font.BOLD, 12));
            text.setBorder(BorderFactory.createLineBorder(Color.orange));
            mapInfo.add(text);
        }
        mapInfo.revalidate();
    }

    /**
     * updates the current game status on the GUI
     */
    private void updateGameStatus(String newStatus){
        status.removeAll();
        String[] arr = newStatus.split("\n");
        for (int i = 0; i<arr.length; i++) {
            JLabel text = new JLabel(  arr[i]);
            text.setFont(new Font("Arial", Font.BOLD, 12));
            status.add(text);
        }
        status.revalidate();
    }

    /**
     * Setting the number of players in the game
     */
    private void settingNumberOfPlayer(){
        String[] possiblePlayers = { "2","3","4","5","6" };
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection of how many players would play the game"));
        JComboBox numPlayers = new JComboBox(possiblePlayers);
        panel.add(numPlayers);
        JOptionPane.showConfirmDialog(null,panel, "Number of Players", JOptionPane.DEFAULT_OPTION);

        int numberOfPlayers = Integer.parseInt((String) numPlayers.getSelectedItem());

        gettingNamesOfPlayers(numberOfPlayers);
    }

    /**
     * In this method all the players would input their names and
     * they would be saved in an arrayList of strings and then it'll be passed to
     * makePlayers() method in the game which will create the players object using the player names
     *
     * @param numberOfPlayers The number of players in the game
     */
    private void gettingNamesOfPlayers(int numberOfPlayers) {
        //Instantiating the GUI components
        LinkedList<String> playerNames = new LinkedList<>();
        JPanel namesPanel = new JPanel();
        JTextField jTextField = new JTextField(6);
        JLabel jLabel = new JLabel();
        namesPanel.add(jLabel);
        namesPanel.add(jTextField);
        JCheckBox aiPlayer = new JCheckBox("Make Player AI ?");
        namesPanel.add(aiPlayer);
        // checking for any invalid names as empty strings or duplicates
        for (int i = 0; i < numberOfPlayers; i++) {
            jLabel.setText("Enter Name of Player: " + (1 + i));
            aiPlayer.setSelected(false);
            int result = JOptionPane.showConfirmDialog(null, namesPanel, "Player Names", JOptionPane.OK_OPTION);
            if (result ==JOptionPane.OK_OPTION) {
                while (jTextField.getText().equals("") || playerNames.contains(jTextField.getText()) || jTextField.getText().contains("[B0T]")){
                    displayMessage("Player " + (1 + i) + " name: " + jTextField.getText() + " can't be empty, contain \"[B0T]\" or be the same as another player!!");
                    jTextField.setText("");
                    result = JOptionPane.showConfirmDialog(null, namesPanel, "Player Names", JOptionPane.OK_OPTION);
                    if (result != JOptionPane.OK_OPTION){
                        displayMessage("YOU HAVE TO START INPUTTING THE PLAYER NAMES AGAIN");
                        gettingNamesOfPlayers(numberOfPlayers);
                        return;
                    }
                }
                if(aiPlayer.isSelected()) {
                    playerNames.add(jTextField.getText() + "[B0T]");
                }else{
                    playerNames.add(jTextField.getText());
                }
                jTextField.setText("");
            } else {
                displayMessage("YOU HAVE TO START INPUTTING THE PLAYER NAMES AGAIN");
                gettingNamesOfPlayers(numberOfPlayers);
                return;
            }
        }

        controller.makePlayers(playerNames);//calling the makePlayers method and create the player in the Model
        updateGameInfo(); // updating the game info with updated territory info
    }

    /**
     * Draft phase for a particular territory. Send troops to the selected territory
     *
     * @param numTroopsString The maximum number of troops that can be sent
     * @return StringArray {nameOfSelectedTerritory, numberOfTroopsMovingIn}
     */
    public void startDraft(String numTroopsString, String[] draftTerritories){

        int numTroops = Integer.parseInt(numTroopsString);

        JPanel draftPanel = new JPanel();//creates panel to show list of draft territories
        draftPanel.add(new JLabel("Select territory to send troops to"));
        JComboBox draftComboBox = new JComboBox(draftTerritories);
        draftPanel.add(draftComboBox);
        JOptionPane.showConfirmDialog(null, draftPanel, "Draft Phase", JOptionPane.DEFAULT_OPTION);
        String territoryString = (String) draftComboBox.getItemAt(draftComboBox.getSelectedIndex());// gets the territory the player chose

        String[] troopNumbers = new String[numTroops];
        for(int i = 0; i<numTroops; i++){
            troopNumbers[i] = Integer.toString(i+1);
        }

        JPanel troopPanel = new JPanel();//creates panel to show list of draft territories
        troopPanel.add(new JLabel("Select number of troops to send"));
        JComboBox troopComboBox = new JComboBox(troopNumbers);
        troopPanel.add(troopComboBox);
        JOptionPane.showConfirmDialog(null, troopPanel, "Draft Phase", JOptionPane.DEFAULT_OPTION);
        String troopString = (String) troopComboBox.getItemAt(troopComboBox.getSelectedIndex());

        controller.doDraft(troopString, territoryString);
    }

    /**
     * Asks the player if they want to start an attack or move on to the next phase (ENDS TURN IN MILESTONE 2)
     *
     */
    public void attackOrQuitOption(){
        Object[] options = {"Start Attack",
                "End Attack"};
        int n = JOptionPane.showOptionDialog(this,
                "Choose to start an attack or to end attack stage",
                "Attack Stage",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]);

        //0 for starting attack
        //1 for end attack
        // -1 for top right X
        controller.wantToAttack(n == 0);
    }

    /**
     * Asks the player to select the attackStarterTerritory and the defender for an attack
     *
     * @return StringArray of {attackStarterTerritoryName, defenderTerritoryName}
     */
    public String[] attackSelection(String[] attackStartersStringArray){

        JPanel attackPanel = new JPanel();//creates panel to show list of attack starters
        attackPanel.add(new JLabel("Select country to attack from"));
        JComboBox attackStarters = new JComboBox(attackStartersStringArray);
        attackPanel.add(attackStarters);
        JOptionPane.showConfirmDialog(null, attackPanel, "Attack Starters", JOptionPane.DEFAULT_OPTION);

        String currentAttackStarter = (String) attackStarters.getItemAt(attackStarters.getSelectedIndex());
        // gets the territory that can start an attack

        //GETTING THE DEFENDER
        String[] defenderStrings = {};//controller.getNeighboursToAttack(game.getCurrentPlayerObject().getTerritory(currentAttackStarter));

        JPanel defendPanel = new JPanel();
        defendPanel.add(new JLabel("Select country to attack from " + currentAttackStarter));
        JComboBox defenders = new JComboBox(defenderStrings);
        defendPanel.add(defenders);
        JOptionPane.showConfirmDialog(null, defendPanel, "Defenders", JOptionPane.DEFAULT_OPTION);
        String currentDefender = (String) defenders.getItemAt(defenders.getSelectedIndex());
        // gets the territory to be attacked

        String[] attackerDefender = {currentAttackStarter, currentDefender};
        return attackerDefender;
    }
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^PROBABLY NOT NEEDED. BEHAVIOR MOVED INTO BELOW TWO METHODS

    public void attackStarterSelection(String[] attackStartersStringArray){
        JPanel attackPanel = new JPanel();//creates panel to show list of attack starters
        attackPanel.add(new JLabel("Select country to attack from"));
        JComboBox attackStarters = new JComboBox(attackStartersStringArray);
        attackPanel.add(attackStarters);
        JOptionPane.showConfirmDialog(null, attackPanel, "Attack Starters", JOptionPane.DEFAULT_OPTION);

        String currentAttackStarter = (String) attackStarters.getItemAt(attackStarters.getSelectedIndex());
        // gets the territory that can start an attack
        controller.attackStarterChoice(currentAttackStarter);
    }

    public void attackDefenderSelection(String[] defendersStringArray, String currentAttackStarter){
        //GETTING THE DEFENDER

        JPanel defendPanel = new JPanel();
        defendPanel.add(new JLabel("Select country to attack from " + currentAttackStarter));
        JComboBox defenders = new JComboBox(defendersStringArray);
        defendPanel.add(defenders);
        JOptionPane.showConfirmDialog(null, defendPanel, "Defenders", JOptionPane.DEFAULT_OPTION);
        String currentDefender = (String) defenders.getItemAt(defenders.getSelectedIndex());
        // gets the territory to be attacked

        controller.attackDefenderChoice(currentDefender);
    }

    /**
     * Asks the user if they want to roll dice or cancel.
     *
     * @param maxAttackerDefender Maximum number of dice that can be rolled by the attacker
     * @return int array of {PlayersOptionForRollOrCancel, NumberOfDiceRolledByAttacker}
     */
    public int[] diceFightView(int maxAttackerDefender){
        String[] diceNumbers = new String[maxAttackerDefender];
        for(int i = 0; i<maxAttackerDefender; i++){
            diceNumbers[i] = Integer.toString(1+i);
        }
        Collections.reverse(Arrays.asList(diceNumbers));
        Object[] options = {"Roll",
                "Cancel Dice Fight"};

        JPanel rollPanel = new JPanel();
        rollPanel.add(new JLabel("\nChoose how many dice to roll"));
        JComboBox rollComboBox = new JComboBox(diceNumbers);
        rollPanel.add(rollComboBox);

        int n = JOptionPane.showOptionDialog(this,
                rollPanel,
                "Dice Fight Stage",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]);
        String rollString = (String) rollComboBox.getItemAt(rollComboBox.getSelectedIndex());
        int[] resultRoll = {n, Integer.parseInt(rollString)};
        return resultRoll;
        //0 index : Clicked button
        //0 is roll, 1 is cancel dice fight, -1 is top right red X
        //1 index : number of dice rolled
    }
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^PROBABLY NOT NEEDED. BEHAVIOR MOVED INTO BELOW THREE METHODS

    public void diceFightOrQuit(String attackerCommaDefender){
        String[] attackerDefender = attackerCommaDefender.split(",");

        Object[] options = {"Start Dice Fight",
                "Cancel Dice Fight"};
        int n = JOptionPane.showOptionDialog(this,
                "Choose to start a dice fight with " + attackerDefender[0] + " attacking " + attackerDefender[1],
                "Dice Fight Stage",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]);

        //0 for starting attack
        //1 for end attack
        // -1 for top right X
        controller.wantToAttack(n == 0);
    }

    public void attackerDiceRoll(String numOwnerTerritory){
        String[] numThenOwnerThenTerritory = numOwnerTerritory.split(",");

        String[] diceNumbers = new String[Integer.parseInt(numThenOwnerThenTerritory[0])];
        for(int i = 0; i<Integer.parseInt(numThenOwnerThenTerritory[0]); i++){
            diceNumbers[i] = Integer.toString(1+i);
        }

        Collections.reverse(Arrays.asList(diceNumbers));//This makes 2 first option

        JPanel dicePanel = new JPanel();//creates panel to show list of draft territories
        dicePanel.add(new JLabel("Player: " + numThenOwnerThenTerritory[1]
                + " choose number of dice to roll for attacking from " + numThenOwnerThenTerritory[2]));

        JComboBox troopComboBox = new JComboBox(diceNumbers);
        dicePanel.add(troopComboBox);
        JOptionPane.showConfirmDialog(null, dicePanel, "Draft Phase", JOptionPane.DEFAULT_OPTION);

        controller.setAttackerDice((String) troopComboBox.getItemAt(troopComboBox.getSelectedIndex()));

    }

    /**
     * Asks the defender of the current dice fight how many dice to roll
     *
     * @param numOwnerTerritory Comma separated string of (MaxRoll),(OwnerName),(TerritoryName)
     */
    public void defenderDiceRoll(String numOwnerTerritory){
        String[] numThenOwnerThenTerritory = numOwnerTerritory.split(",");
        String[] diceNumbers = Integer.parseInt(numThenOwnerThenTerritory[0])>=2 ? new String[2] : new String[1];
        //Options of 1 and 2 when there are more than 1 troops on the terry
        //Can only roll 1 dice if there is only one troop on the terry

        for(int i = 0; i<diceNumbers.length; i++){
            diceNumbers[i] = Integer.toString(i+1);
        }
        Collections.reverse(Arrays.asList(diceNumbers));//This makes 2 first option

        JPanel dicePanel = new JPanel();//creates panel to show list of draft territories
        dicePanel.add(new JLabel("Player: " + numThenOwnerThenTerritory[1]
                + " choose number of dice to roll for defending " + numThenOwnerThenTerritory[2]));

        JComboBox troopComboBox = new JComboBox(diceNumbers);
        dicePanel.add(troopComboBox);
        JOptionPane.showConfirmDialog(null, dicePanel, "Draft Phase", JOptionPane.DEFAULT_OPTION);

        controller.setDefenderDice((String) troopComboBox.getItemAt(troopComboBox.getSelectedIndex()));

    }

    /**
     * Asks the user how many troops to move into the newly conquered territory
     *
     * @param minCommaMax Comma separated integers for minimum and maximum number of troops that can move in
     * @return The number of troops moving into the conquered territory
     */
    public void troopsToMoveIn(String minCommaMax){

        int diceWonWith = Integer.parseInt(minCommaMax.split(",")[0]);
        int attackerNumTroops = Integer.parseInt(minCommaMax.split(",")[1]);


        String[] troopNumbers = new String[attackerNumTroops - diceWonWith];
        for(int i = 0; i<attackerNumTroops-diceWonWith; i++){
            troopNumbers[i] = Integer.toString(attackerNumTroops - 1 - i);
        }
        Collections.reverse(Arrays.asList(troopNumbers));

        Object[] options = {"Confirm Troops Moving In"};

        JPanel troopsPanel = new JPanel();
        troopsPanel.add(new JLabel("\nChoose how many troops to move into conquered territory"));
        JComboBox troopComboBox = new JComboBox(troopNumbers);
        troopsPanel.add(troopComboBox);

        JOptionPane.showOptionDialog(this,
                troopsPanel,
                "Territory Takeover!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]);
        String rollString = (String) troopComboBox.getItemAt(troopComboBox.getSelectedIndex());
        controller.setTakeoverTroops(rollString);
    }

    /**
     * Makes message box for when a player has been eliminated from the game.
     * @param loser The name of the eliminated player from the game
     */
    public void announceElimination(String loser){
        JOptionPane.showMessageDialog(this, loser + " has lost their last territory. Please accept defeat...", "A player has been eliminated! RIP", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Message dialog box announcing the winner
     */
    public void announceWinner(String winner){
        JOptionPane.showMessageDialog(this, winner + " IS THE ULTIMATE RISK CHAMPION!!!", "GAME OVER!", JOptionPane.WARNING_MESSAGE);
    }

    public void fortifyOrQuitOption(){
        Object[] options = {"Start Fortify",
                "End Turn"};
        int n = JOptionPane.showOptionDialog(this,
                "Choose to fortify a territory or end the current turn",
                "Fortify Stage",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]);

        //0 for starting attack
        //1 for end attack
        // -1 for top right X
        controller.wantToAttack(n == 0);
    }

    /**
     * Starts the fortify phase of the current players turn. Choose the territory to start the fortify or to end turn
     *
     * @param fortifyStarterTerritories String array of fortify starters
     */
    public void chooseFortifyGiver(String[] fortifyStarterTerritories){

        Object[] options = {"Take troops from this territory"};

        JPanel fortifyPanel = new JPanel();//creates panel to show list of draft territories
        fortifyPanel.add(new JLabel("Select territory to take troops from to send to another territory"));
        JComboBox fortifyComboBox = new JComboBox(fortifyStarterTerritories);
        fortifyPanel.add(fortifyComboBox);
        int response = JOptionPane.showOptionDialog(this,
                fortifyPanel,
                "Fortify Stage",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]);

        String territoryString = (String) fortifyComboBox.getItemAt(fortifyComboBox.getSelectedIndex());// gets the territory the player chose
        controller.chooseFortifyGiver(territoryString);
    }

    /**
     * During fortify phase, choose who to receive troops
     *
     * @param territories String array of territories that can receive troops in the fortify stage
     * @return The chosen territory to receive troops
     */
    public void chooseFortifyReceiver(String[] territories){

        JPanel fortifyPanel = new JPanel();//creates panel to show list of attack starters
        fortifyPanel.add(new JLabel("Select territory to send troops to"));
        JComboBox fortifiables = new JComboBox(territories);
        fortifyPanel.add(fortifiables);
        JOptionPane.showConfirmDialog(null, fortifyPanel, "Fortifiables", JOptionPane.DEFAULT_OPTION);

        controller.chooseFortifyReceiver( (String) fortifiables.getItemAt(fortifiables.getSelectedIndex()));
    }

    /**
     * Asks the player how many troops to move from the fortify starter to the fortified territory
     *
     * @param maxTroopsToMove maximum number of troops that can be moved
     * @return selected number of troops to move
     */
    public void numTroopsToFortify(String maxTroopsToMove){

        String[] troopNumbers = new String[Integer.parseInt(maxTroopsToMove)];
        for(int i = 0; i<Integer.parseInt(maxTroopsToMove); i++){
            troopNumbers[i] = Integer.toString(i+1);
        }

        JPanel troopPanel = new JPanel();//creates panel to show list of draft territories
        troopPanel.add(new JLabel("Select number of troops to send"));
        JComboBox troopComboBox = new JComboBox(troopNumbers);
        troopPanel.add(troopComboBox);
        JOptionPane.showConfirmDialog(null, troopPanel, "Fortify Phase", JOptionPane.DEFAULT_OPTION);

        controller.getFortifyTroops( (String) troopComboBox.getItemAt(troopComboBox.getSelectedIndex()));
    }

    /**
     * Will display a message to the user. Expect message to be on of the following:
     * (a) invalid move (b) quit goodbye message (c) help instructions message
     *
     * @param message
     */
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void handleUpdate(GameEvent event) {
        if (event.getGameState().equals(Game.GameState.MESSAGE)) {
            displayMessage(event.getMessage());

        }else if (event.getGameState().equals(Game.GameState.DRAFT)){
            System.out.println(event.getMessage());
            startDraft(event.getMessage(), event.getTerritoriesOfInterest());

        }else if (event.getGameState().equals(Game.GameState.ATTACKORQUIT)){
            attackOrQuitOption();

        }else if (event.getGameState().equals(Game.GameState.ATTACKERSELECTION)){
            attackStarterSelection(event.getTerritoriesOfInterest());

        }else if (event.getGameState().equals(Game.GameState.DEFENDERSELECITON)){
            attackDefenderSelection(event.getTerritoriesOfInterest(), event.getMessage());

        }else if (event.getGameState().equals(Game.GameState.DICEFIGHTORQUIT)){
            diceFightOrQuit(event.getMessage());

        }else if (event.getGameState().equals(Game.GameState.DICEFIGHTATTACKERCHOICE)){
            attackerDiceRoll(event.getMessage());

        }else if (event.getGameState().equals(Game.GameState.DICEFIGHTDEFENDERCHOICE)){
            defenderDiceRoll(event.getMessage());

        }else if (event.getGameState().equals(Game.GameState.TAKEOVERTERRITORY)){
            troopsToMoveIn(event.getMessage());

        }else if (event.getGameState().equals(Game.GameState.FORTIFYORQUIT)){
            fortifyOrQuitOption();

        }else if (event.getGameState().equals(Game.GameState.FORTIFYGIVER)){
            chooseFortifyGiver(event.getTerritoriesOfInterest());

        }else if (event.getGameState().equals(Game.GameState.FORTIFYRECEIVER)){
            chooseFortifyReceiver(event.getTerritoriesOfInterest());

        }else if (event.getGameState().equals(Game.GameState.FORTIFYTROOPSTOMOVE)){
            numTroopsToFortify(event.getMessage());

        }else if (event.getGameState().equals(Game.GameState.ELIMINATION)){
            announceElimination(event.getMessage());

        }else if (event.getGameState().equals(Game.GameState.HASWINNER)){
            announceWinner(event.getCurrentPlayer().getName());

        }else if (!event.getMessage().equals("")){
            displayMessage(event.getMessage());
            updateGameStatus(event.getMessage());
        }


    }

}
