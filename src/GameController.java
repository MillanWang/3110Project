import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private Game game;
    private GameView gameView;

    public GameController(Game game,  GameView gameView) {
        this.game = game;
        this.gameView = gameView;
        this.gameView.addGameListner(new GameListner());
    }

    class GameListner implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Player player;
            
        }
    }

}
