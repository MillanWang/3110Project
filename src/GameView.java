import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class GameView extends JFrame implements GameObserver{
    private Game game;// the model of the game
    private JMenuBar menuBar;//the menu bar for the game
    private JMenuItem menuItemHelp, menuItemQuit, menuItemReset, menuItemCurrentPlayer, menuItemShowTerritories, menuItemNextTurn;// the menuItems for the game
    private JPanel gamePanel,startPage;// the two JPanels that will be used in the game
    private JButton newGameBtn;// the first button will be appeard to the player
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
        this.game=game;
        this.controller = new GameController(game,this);
        createStartPage();
        setSize(800, 580);
        // i changed resizable to true just in case the player wants it full screen
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Creates red start page before starting the game
     */
    private void createStartPage() {
        gamePanel = new JPanel(new BorderLayout());
        add(gamePanel);
        
        startPage = new JPanel();
        startPage.setLayout(null);
        startPage.setBackground(new Color(204, 0, 24));

        newGameBtn = new JButton("Start a New Game");
        newGameBtn.setBounds(280, 350, 210, 50);
        newGameBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        newGameBtn.setBackground(Color.WHITE);
        newGameBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // when the user click the button, then these methods would be called and the startPage would be removed
        newGameBtn.addActionListener(e-> {
            displayGame();
            gamePanel.remove(startPage);
            settingNumberOfPlayer();
        });

        // Adding game title
        JLabel title = new JLabel("RISK GAME");
        title.setFont(new Font("Monospaced", Font.BOLD, 100));
        title.setBounds(150, 200, 900, 100);
        title.setForeground(Color.WHITE);

        // Adding all components to panel
        startPage.add(title);
        startPage.add(newGameBtn);
        gamePanel.add(startPage,BorderLayout.CENTER);
    }

    /**
     * Displays the game GUI
     */
    private void displayGame(){
        gamePanel.setVisible(true);
        addMenuItems();
        addMapPicture();
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
            new GameView(new Game());//create a new GameVew, so new window
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
            displayMessage("Current player: " + game.getCurrentPlayer()+" \n"+ game.getCurrentPlayerObject().getTerritoriesString());
        });
        menuBar.add(menuItemCurrentPlayer);

        //Option to show all of the territories with their owners and troops
        menuItemShowTerritories = new JMenuItem("Show-Territories");
        menuItemShowTerritories.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemShowTerritories.addActionListener(e-> {
            displayMessage(game.getGenericWorldMap().getAllTerritoriesString());
        });
        menuBar.add(menuItemShowTerritories);


        //Menu option to start the next turn. Turns options appear in windows
        menuItemNextTurn = new JMenuItem("Start Next Turn");
        menuItemNextTurn.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemNextTurn.addActionListener(e -> {
            controller.startPlayersTurn();
        });
        menuBar.add(menuItemNextTurn);

        gamePanel.add(menuBar, BorderLayout.NORTH);
    }


    /**
     * Sets the map image in the GUI
     */
    private void addMapPicture(){
        map = new ImageIcon(getClass().getResource("DefaultWorldMap.jpg"));
        JLabel MapLabel = new JLabel(map);
        gamePanel.add(MapLabel,BorderLayout.CENTER);
        pack();
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

        this.game.makePlayers(playerNames);//calling the makePlayers method and create the player in the Model
    }

    /**
     * Draft phase for a particular territory. Send troops to the selected territory
     *
     * @param numTroops The maximum number of troops that can be sent
     * @return StringArray {nameOfSelectedTerritory, numberOfTroopsMovingIn}
     */
    public String[] startDraft(int numTroops, String[] draftTerritories){

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

        String[] returnValues = {territoryString,troopString};
        return returnValues;
    }

    /**
     * Asks the player if they want to start an attack or move on to the next phase (ENDS TURN IN MILESTONE 2)
     *
     * @return Players choice. Start attack or to move on
     */
    public int attackOrQuitOption(){
        Object[] options = {"Start Attack",
                "End Attack"};
        int n = JOptionPane.showOptionDialog(this,
                "Choose to start an attack or to end attack stage",
                "Attack Stage",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options, options[0]);
        return n;
        //0 for starting attack
        //1 for end attack
        // -1 for top right X
    }

    /**
     * Asks the player to select the attackStarterTerritory and the defender for an attack
     *
     * @return StringArray of {attackStarterTerritoryName, defenderTerritoryName}
     */
    public String[] attackSelection(){
        String[] attackStartersStringArray = Player.getTerritoryStringArray(game.getCurrentPlayerObject().getAttackStarters());

        JPanel attackPanel = new JPanel();//creates panel to show list of attack starters
        attackPanel.add(new JLabel("Select country to attack from"));
        JComboBox attackStarters = new JComboBox(attackStartersStringArray);
        attackPanel.add(attackStarters);
        JOptionPane.showConfirmDialog(null, attackPanel, "Attack Starters", JOptionPane.DEFAULT_OPTION);

        String currentAttackStarter = (String) attackStarters.getItemAt(attackStarters.getSelectedIndex());
        // gets the territory that can start an attack

        //GETTING THE DEFENDER
        String[] defenderStrings = controller.getNeighboursToAttack(game.getCurrentPlayerObject().getTerritory(currentAttackStarter));

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

    /**
     * Asks the defender of the current dice fight how many dice to roll
     *
     * @param territory Defending territory object
     * @return The choice of the player
     */
    public int defenderDiceRoll(Territory territory){

        String[] diceNumbers = territory.getTroops()>=2 ? new String[2] : new String[1];
        //Options of 1 and 2 when there are more than 1 troops on the terry
        //Can only roll 1 dice if there is only one troop on the terry

        for(int i = 0; i<diceNumbers.length; i++){
            diceNumbers[i] = Integer.toString(i+1);
        }
        Collections.reverse(Arrays.asList(diceNumbers));//This makes 2 first option

        JPanel dicePanel = new JPanel();//creates panel to show list of draft territories
        dicePanel.add(new JLabel("Player: " + territory.getOwner() + " choose number of dice to roll for defending " + territory.getTerritoryName()));
        JComboBox troopComboBox = new JComboBox(diceNumbers);
        dicePanel.add(troopComboBox);
        JOptionPane.showConfirmDialog(null, dicePanel, "Draft Phase", JOptionPane.DEFAULT_OPTION);
        return  Integer.parseInt((String) troopComboBox.getItemAt(troopComboBox.getSelectedIndex()));
    }

    /**
     * Asks the user how many troops to move into the newly conquered territory
     *
     * @param attackerNumTroops  The number of troops in the attacker's territory
     * @param diceWonWith   The number of dice rolled on the most recent win
     * @return The number of troops moving into the conquered territory
     */
    public int troopsToMoveIn(int attackerNumTroops, int diceWonWith){
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
        return Integer.parseInt(rollString);
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

    /**
     * Starts the fortify phase of the current players turn. Choose the territory to start the fortify or to end turn
     *
     * @return String array {Player's choice to end attack or fortify, the chosen territory to start the fortify}
     */
    public String[] startFortify(String[] fortifyStarterTerritories){

        Object[] options = {"Fortify", "End turn"};

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
        String[] results = {Integer.toString(response), territoryString};
        return results;
    }

    /**
     * During fortify phase, choose who to receive troops
     *
     * @param territories String array of territories that can receive troops in the fortify stage
     * @return The chosen territory to receive troops
     */
    public String chooseFortified(String[] territories){

        JPanel fortifyPanel = new JPanel();//creates panel to show list of attack starters
        fortifyPanel.add(new JLabel("Select territory to send troops to"));
        JComboBox fortifiables = new JComboBox(territories);
        fortifyPanel.add(fortifiables);
        JOptionPane.showConfirmDialog(null, fortifyPanel, "Fortifiables", JOptionPane.DEFAULT_OPTION);

        return (String) fortifiables.getItemAt(fortifiables.getSelectedIndex());
    }

    /**
     * Asks the player how many troops to move from the fortify starter to the fortified territory
     *
     * @param maxTroopsToMove maximum number of troops that can be moved
     * @return selected number of troops to move
     */
    public int numTroopsToFortify(int maxTroopsToMove){

        String[] troopNumbers = new String[maxTroopsToMove];
        for(int i = 0; i<maxTroopsToMove; i++){
            troopNumbers[i] = Integer.toString(i+1);
        }

        JPanel troopPanel = new JPanel();//creates panel to show list of draft territories
        troopPanel.add(new JLabel("Select number of troops to send"));
        JComboBox troopComboBox = new JComboBox(troopNumbers);
        troopPanel.add(troopComboBox);
        JOptionPane.showConfirmDialog(null, troopPanel, "Fortify Phase", JOptionPane.DEFAULT_OPTION);

        return Integer.parseInt( (String) troopComboBox.getItemAt(troopComboBox.getSelectedIndex()));
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
        if (event.getGameState().equals(Game.GameState.ELIMINATION)) {
            announceElimination(event.getCurrentMessage());

        } else if (event.getGameState().equals(Game.GameState.HASWINNER)) {
            announceWinner(event.getCurrentPlayer().getName());

        }else if (event.getGameState().equals(Game.GameState.DRAFT)){
            startDraft(Integer.parseInt(event.getCurrentMessage()), event.getTerritoriesOfInterest());

        } else if (!event.getCurrentMessage().equals("")) {
            displayMessage(event.getCurrentMessage());
        }
    }


    /**
     * Main method of the game. Run to start the GUI
     * @param args
     */
    public static void main(String[] args) {
        new GameView(new Game());//Starts the game
    }
}
