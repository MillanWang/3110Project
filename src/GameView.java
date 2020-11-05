import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameView extends JFrame {
    private Game game;
    private JMenuBar menuBar;
    private JMenuItem menuItemHelp, menuItemQuit, menuItemReset, menuItemCurrentPlayer, menuItemShowTerritories;
    private JPanel gamePanel,startPage;
    private JButton newGameBtn;
    private JComboBox territoryList;
    private Stack<JPanel> previousPanels;
    ImageIcon map;

    //*********Make sure to comment out play() METHOD IN THE GAME CONSTRUCTOR BEFORE RUNNING THIS CLASS!!
    public GameView(Game game){
        super();
        this.setLayout(new BorderLayout());
        this.game=game;
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
        addDropDownList();
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

        menuItemShowTerritories = new JMenuItem("Show-Territories");
        menuItemShowTerritories.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        menuItemShowTerritories.addActionListener(e-> {
            // displayMessage(game.);
        });
        menuBar.add(menuItemShowTerritories);

        gamePanel.add(menuBar, BorderLayout.NORTH);
    }



    /**
     * This method would be responsible for the two drop down list at the SOUTH of the JPanel
     */
    private void addDropDownList(){
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
            startAttack();
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
        int results = JOptionPane.showConfirmDialog(null,panel);
        int numberOfPlayers = Integer.parseInt((String)numPlayers.getSelectedItem());
        System.out.println(numberOfPlayers);
        gettingNamesOfPlayers(numberOfPlayers);
    }

    /**
     * In this method all the players would input their names and
     * they would be saved in an array of strings and then it'll passed to
     * makePlayers() method in the game which will create the player object using the player names
     * @param numberOfPlayers
     */
    private void gettingNamesOfPlayers(int numberOfPlayers) {
        String[] playerNames= new String[numberOfPlayers];
        JPanel namesPanel = new JPanel();
        List<JTextField> jTextFieldList = new ArrayList<>();
        for (int i=0; i < numberOfPlayers;i++){
            jTextFieldList.add(new JTextField(7));
            namesPanel.add(new JLabel("Name of Player "+(i+1)));
            namesPanel.add(jTextFieldList.get(i));
        }

        int result = JOptionPane.showConfirmDialog(null,namesPanel, "Player Names", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION){
            for (int i=0; i < jTextFieldList.size();i++){
                playerNames[i]= jTextFieldList.get(i).getText();
                System.out.println("Player "+(i+1)+":" + playerNames[i]);
            }
        }
        this.game.makePlayers(playerNames);

    }

    public void startAttack(){
        //game.getPlayerFromList(game.getCurrentPlayer()).stringAttackStarters()
        String[] TerritoriesStrings = {"Ottawa", "Quebec", "Egypt", "China", "South Africa" };
        displayMessage("You're done with drafting! Let's attack some bitches");
        JPanel attackPanel = new JPanel();
        attackPanel.add(new JLabel("Select country to attack from"));
        JComboBox attackStarters = new JComboBox(TerritoriesStrings);
        attackPanel.add(attackStarters);
        int result = JOptionPane.showConfirmDialog(null, attackPanel, "Attack Starters", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            displayMessage(TerritoriesStrings[result] + " is attacking");
        }
        JPanel defendPanel = new JPanel();
        defendPanel.add(new JLabel("Select country to attack"));
        JComboBox defenders = new JComboBox(TerritoriesStrings);
        defendPanel.add(defenders);
        int result2 = JOptionPane.showConfirmDialog(null, defendPanel, "Defenders", JOptionPane.OK_CANCEL_OPTION);
        if (result2 == JOptionPane.OK_OPTION) {
            displayMessage("Alright! Lets attack "+TerritoriesStrings[result2]);
        }
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
        Game game = new Game();
        new GameView(game);
    }
}
