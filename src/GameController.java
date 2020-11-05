
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class GameController implements ActionListener{
    private Game game;
    private GameView gameView;

    public GameController(Game game,  GameView gameView) {
        this.game = game;
        this.gameView = gameView;
    }

    public LinkedList<Territory> getPlayersTerritoriesForDraft(){
        return game.getCurrentPlayerObject().getTerritoriesList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
