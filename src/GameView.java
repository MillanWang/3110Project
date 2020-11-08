import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GameView extends JFrame {
    private Game game;
    private JMenuBar menuBar;
    private JMenuItem menuItemHelp, menuItemQuit, menuItemReset, menuItemCurrentPlayer, menuItemShowTerritories, menuItemNextTurn;
    private JPanel gamePanel,startPage;
    private JButton newGameBtn;
    private JComboBox territoryList;
    private Stack<JPanel> previousPanels;
    private GameController controller;
    ImageIcon map;

    //*********Make sure to comment out play() METHOD IN THE GAME CONSTRUCTOR BEFORE RUNNING THIS CLASS!!
    public GameView(Game game){
        super();
        this.setLayout(new BorderLayout());
        this.game=game;
        this.controller = new GameController(game,this);
        //previousPanels = new Stack<>();
        createStartPage();
        setTitle("RISK Global Domination");
        setSize(800, 580);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Still working on this method
     * It's almost finished but the only problem is when the user click on Start a New Game,
     * we'll not see the Map picture and the JmenuItem, but instead nothing would happen.
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
        //newGameBtn.setForeground(Color.WHITE);
        newGameBtn.addActionListener(e-> {
            displayGame();
            gamePanel.remove(startPage);
            sittingNumberOfPlayer();
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

        //previousPanels.add(startPage);
    }

    /**
     * adding the gamePanel to the jframe window and calling the addMenuItems method
     */
    private void displayGame(){
        gamePanel.setVisible(true);
        addMenuItems();
        setDropDownList(null);
        addMapPicture();
    }

    /**
     * Create a menu that will allow the user to choose if they want help or quit, know the currentPlayerTurn,
     * endTurn and showTerritory.
     */
    private void addMenuItems() {
        // Create menu bar
        menuBar = new JMenuBar();

        // Add help button
        menuItemHelp = new JMenuItem("Help");
        menuItemHelp.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemHelp.addActionListener(e -> {
            displayMessage(game.welcomeMessage());
        });
        menuBar.add(menuItemHelp);

        // Add reset button
        menuItemReset = new JMenuItem("Reset");
        menuItemReset.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemReset.addActionListener(e -> {
            dispose();//causes the JFrame window to be destroyed and cleaned up by the operating system
            //game.main(null);
            new GameView(new Game());
        });
        menuBar.add(menuItemReset);

        // Add quit button
        menuItemQuit = new JMenuItem("Quit");
        menuItemQuit.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemQuit.addActionListener(e -> {
            displayMessage(game.quitMessage());
            dispose();
        });
        //menuItemQuit.setAccelerator(KeyStroke.getKeyStroke('q')); // can activate quit by pressing q
        menuBar.add(menuItemQuit);

        // Add show Turn button
        menuItemCurrentPlayer = new JMenuItem("Current-Player");
        menuItemCurrentPlayer.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemCurrentPlayer.addActionListener(e-> {
            displayMessage(game.getCurrentPlayer());
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
     * This method would be responsible for the two drop down list at the SOUTH of the JPanel
     */
    private void setDropDownList(String[] arrayOfTerritoryNames){
        LinkedList<Player> players = new LinkedList<>(game.getPlayersList());
        players.add(game.getCurrentPlayerObject());

        for (Player p : players){

        }

        String[] TerritoriesStrings = { "Ottawa", "Quebec", "Egypt", "China", "South Africa" };

        territoryList = new JComboBox(TerritoriesStrings);
        territoryList.setName("Select Territories");

        Dimension preferredSize = territoryList.getPreferredSize();
        preferredSize.width = 10;
        territoryList.setPreferredSize(preferredSize);
        gamePanel.add(territoryList,BorderLayout.SOUTH);
        territoryList.addActionListener(e->{
            JComboBox cb = (JComboBox)e.getSource();
            displayMessage((String)cb.getSelectedItem());
        });
    }

    /**
     * This method would show the png image
     */
    private void addMapPicture(){
        map = new ImageIcon(getClass().getResource("DefaultWorldMap.jpg"));
        JLabel MapLabel = new JLabel(map);
        gamePanel.add(MapLabel,BorderLayout.CENTER);
        pack();
    }

    private void sittingNumberOfPlayer(){
        String[] possiblePlayers = { "2","3","4","5","6" };
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection of how many players would play the game"));
        JComboBox numPlayers = new JComboBox(possiblePlayers);
        panel.add(numPlayers);
        int results = JOptionPane.showConfirmDialog(null,panel, "Number of Players", JOptionPane.OK_CANCEL_OPTION);
        if (results == JOptionPane.OK_OPTION) {
            int numberOfPlayers = Integer.parseInt((String) numPlayers.getSelectedItem());
            System.out.println(numberOfPlayers);
            gettingNamesOfPlayers(numberOfPlayers);
        }
    }

    /**
     * In this method all the players would input their names and
     * they would be saved in an array of strings and then it'll passed to
     * makePlayers() method in the game which will create the player object using the player names
     * @param numberOfPlayers
     */
    private void gettingNamesOfPlayers(int numberOfPlayers) {
        //Instantiating the GUI components
        LinkedList<String> playerNames = new LinkedList<String>();
        JPanel namesPanel = new JPanel();
        JTextField jTextField = new JTextField(6);
        JLabel jLabel = new JLabel();
        namesPanel.add(jLabel);
        namesPanel.add(jTextField);

        for (int i=0; i< numberOfPlayers;i++) {
            //Showing JOption panel to get the player input
            jLabel.setText("Enter Name of Player: "+(1+i));
            jTextField.setText("");

            int result = JOptionPane.showConfirmDialog(null, namesPanel, "Player Names", JOptionPane.OK_OPTION);
            if (result ==JOptionPane.OK_OPTION){
                while (jTextField.getText().equals("") || playerNames.contains(jTextField.getText())){
                    displayMessage("Player " + (1 + i) + " name: " + jTextField.getText() + " can't be empty or the same as another player!!");
                    break;//TO PREVENT INFINITE LOOPS
                }
                playerNames.add(jTextField.getText());
            }
        }
        this.game.makePlayers(playerNames);
    }

    public boolean checkForDuplicates(String[] array) {
        /*List<String> list = new ArrayList<String>(Arrays.asList(array));
        for (int i=0; i < array.length;i++){
            if (list.get(i).equals("")){
                list.remove(i);
            }
        }*/

        Set<String> set = new HashSet<>(Arrays.asList(array));

        return array.length != set.size();
    }

    public String[] startDraft(int numTroops){
        String[] draftTerritories = controller.getPlayersTerritoriesForDraft();

        JPanel draftPanel = new JPanel();//creates panel to show list of draft territories
        draftPanel.add(new JLabel("Select territory to send troops to"));
        JComboBox draftComboBox = new JComboBox(draftTerritories);
        draftPanel.add(draftComboBox);
        JOptionPane.showConfirmDialog(null, draftPanel, "Draft Phase", JOptionPane.OK_CANCEL_OPTION);
        String territoryString = (String) draftComboBox.getItemAt(draftComboBox.getSelectedIndex());// gets the territory the player chose

        String[] troopNumbers = new String[numTroops];
        for(int i = 0; i<numTroops; i++){
            troopNumbers[i] = Integer.toString(i+1);
        }



        JPanel troopPanel = new JPanel();//creates panel to show list of draft territories
        troopPanel.add(new JLabel("Select number of troops to send"));
        JComboBox troopComboBox = new JComboBox(troopNumbers);
        troopPanel.add(troopComboBox);
        JOptionPane.showConfirmDialog(null, troopPanel, "Draft Phase", JOptionPane.OK_CANCEL_OPTION);
        String troopString = (String) troopComboBox.getItemAt(troopComboBox.getSelectedIndex());

        String[] returnValues = {territoryString,troopString};
        return returnValues;


    }

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
     * this method displays messages to the player, prompting what to do during the attack phase
     */
    public String[] attackSelection(){
        String[] attackStartersStringArray = game.getCurrentPlayerObject().getAttackStarters();

        JPanel attackPanel = new JPanel();//creates panel to show list of attack starters
        attackPanel.add(new JLabel("Select country to attack from"));
        JComboBox attackStarters = new JComboBox(attackStartersStringArray);
        attackPanel.add(attackStarters);
        JOptionPane.showConfirmDialog(null, attackPanel, "Attack Starters", JOptionPane.OK_CANCEL_OPTION);

        String currentAttackStarter = (String) attackStarters.getItemAt(attackStarters.getSelectedIndex());// gets the territory that can start an attack
        //displayMessage(currentAttackStarter + " is attacking"); //Kinda redundant if the player just picked this

        //GETTING THE DEFENDER

        String[] defenderStrings = controller.getNeighboursToAttack(game.getCurrentPlayerObject().getTerritory(currentAttackStarter));
        //System.out.println(Arrays.toString(defenderStrings));


        JPanel defendPanel = new JPanel();
        defendPanel.add(new JLabel("Select country to attack from " + currentAttackStarter));
        JComboBox defenders = new JComboBox(defenderStrings);
        defendPanel.add(defenders);
        JOptionPane.showConfirmDialog(null, defendPanel, "Defenders", JOptionPane.OK_CANCEL_OPTION);
        String currentDefender = (String) defenders.getItemAt(defenders.getSelectedIndex());// gets the territory to be attacked(need to work on checking if player owns territory)


        String[] attackerDefender = {currentAttackStarter, currentDefender};
        return attackerDefender;

    }

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
        //0 is roll
        //1 is cancel dice fight
        //-1 is top right red X

        //1 index : number of dice rolled
    }

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

    public void announceElimination(String loser){
        JOptionPane.showMessageDialog(this, loser + " has lost their last territory. Please accept defeat...", "A player has been eliminated! RIP", JOptionPane.WARNING_MESSAGE);
    }

    public void announceWinner(){
        JOptionPane.showMessageDialog(this, game.getCurrentPlayer() + " IS THE ULTIMATE RISK CHAMPION!!!", "GAME OVER!", JOptionPane.WARNING_MESSAGE);
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

    public static void main(String[] args) {
        new GameView(new Game());//Starts the game
    }
}
