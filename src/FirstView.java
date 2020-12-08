import javax.swing.*;
import java.awt.*;

public class FirstView extends JFrame {
    private JButton newGameBtn, loadGameBtn;
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

        newGameBtn = new JButton("Start a New Game");
        newGameBtn.setBounds(280, 350, 210, 50);
        newGameBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        newGameBtn.setBackground(Color.WHITE);
        newGameBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // when the user click the button, then these methods would be called and the startPage would be removed
        newGameBtn.addActionListener(e-> {
            //displayGame();
            gamePanel.remove(startPage);
            //settingNumberOfPlayer();
        });

        loadGameBtn = new JButton("Load Previous Game");
        loadGameBtn.setBounds(280, 460, 240, 40);
        loadGameBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        loadGameBtn.setBackground(Color.WHITE);
        loadGameBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // when the user click the button, then these methods would be called and the startPage would be removed
        loadGameBtn.addActionListener(e-> {

            try{
                chooseFile();
            } catch (Exception event) {
                event.printStackTrace();
            }
            gamePanel.remove(startPage);
        });

        // Adding game title
        JLabel title = new JLabel("RISK GAME");
        title.setFont(new Font("Monospaced", Font.BOLD, 100));
        title.setBounds(150, 200, 900, 100);
        title.setForeground(Color.WHITE);

        // Adding all components to panel
        startPage.add(title);
        startPage.add(newGameBtn);
        startPage.add(loadGameBtn);
        gamePanel.add(startPage,BorderLayout.CENTER);
    }

    public String chooseFile(){
        String fileName="";
        JFileChooser fileChooser = new JFileChooser();
        StringBuilder sb = new StringBuilder();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            //get the file
            fileName = fileChooser.getSelectedFile().getName();
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
