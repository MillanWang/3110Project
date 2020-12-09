import javax.swing.*;
import java.awt.*;

public class FirstView extends JFrame {
    private JButton newGamebtnDefaultMap, newGamebtnCustomMaps, loadGameBtn;
    private JPanel gamePanel,startPage;// the two JPanels that will be used in the game

    public FirstView(){
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

        newGamebtnDefaultMap = new JButton("Start a New Game With Default Map");
        newGamebtnDefaultMap.setBounds(180, 350, 420, 50);
        newGamebtnDefaultMap.setFont(new Font("Monospaced", Font.BOLD, 20));
        newGamebtnDefaultMap.setBackground(Color.WHITE);
        newGamebtnDefaultMap.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // when the user click the button, then these methods would be called and the startPage would be removed
        newGamebtnDefaultMap.addActionListener(e-> {
            gamePanel.remove(startPage);
            new Game("/DefaultMap.txt");
            dispose();
        });

        newGamebtnCustomMaps = new JButton("Start a New Game With Custom Map");
        newGamebtnCustomMaps.setBounds(180, 410, 420, 50);
        newGamebtnCustomMaps.setFont(new Font("Monospaced", Font.BOLD, 20));
        newGamebtnCustomMaps.setBackground(Color.WHITE);
        newGamebtnCustomMaps.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // when the user click the button, then these methods would be called and the startPage would be removed
        newGamebtnCustomMaps.addActionListener(e-> {
            gamePanel.remove(startPage);
            String filePath = chooseFile();
            new Game(filePath);
            dispose();
        });

        loadGameBtn = new JButton("Load Previous Game");
        loadGameBtn.setBounds(250, 470, 240, 40);
        loadGameBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        loadGameBtn.setBackground(Color.WHITE);
        loadGameBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // when the user click the button, then these methods would be called and the startPage would be removed
        loadGameBtn.addActionListener(e-> {
            try{
                String fileName = chooseFile();
                Game.loadGame(fileName);
            } catch (Exception event) {
                event.printStackTrace();
            }
            gamePanel.remove(startPage);
        });

        // Adding game title
        JLabel title = new JLabel("RISK GAME");
        title.setFont(new Font("Monospaced", Font.BOLD, 100));
        title.setBounds(140, 180, 900, 100);
        title.setForeground(Color.WHITE);

        // Adding all components to panel
        startPage.add(title);
        startPage.add(newGamebtnDefaultMap);
        startPage.add(newGamebtnCustomMaps);
        startPage.add(loadGameBtn);
        gamePanel.add(startPage,BorderLayout.CENTER);
    }

    /**
     * Return the choosen file name
     * @return fileName
     */
    public String chooseFile(){
        String fileName="";
        JFileChooser fileChooser = new JFileChooser();
        StringBuilder sb = new StringBuilder();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            //get the file
            fileName = fileChooser.getSelectedFile().getPath();
            System.out.println(fileName);
        }
        else {
            JOptionPane.showMessageDialog(this,"No file was selected!");
        }
        return fileName;
    }
    public static void main(String[] args) {
        new FirstView();
    }
}