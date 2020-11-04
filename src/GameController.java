import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

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
    
    public LinkedList<Territory> getPlayersTerritoriesForDraft(){
        return game.getCurrentPlayerObject().getTerritoriesList();
    }

}
