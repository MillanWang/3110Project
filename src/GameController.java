import java.io.Serializable;
import java.util.LinkedList;

public class GameController implements Serializable {
    private Game game;

    /**
     * Constructor for the GameController class
     *
     * @param gameModel the model. Game class
     */
    public GameController(Game gameModel) {
        this.game = gameModel;
    }

    public String getImageFileName(){
        return game.getGenericWorldMap().getImageFileName();
    }

    public void startPlayersTurn(){
        game.startTurn();
        game.nextTurn();
    }

    public void makePlayers(LinkedList<String> playerNames){
        game.makePlayers(playerNames);
    }

    public String getCurrentPlayerInfo() {
        return "Current player: " + game.getCurrentPlayer()+" \n"+ game.getCurrentPlayerObject().getTerritoriesString();
    }

    public void doDraft(String numTroopsToSend, String territoryName){
        game.getCurrentPlayerObject().draftPhase(territoryName, numTroopsToSend);
    }

    public String[] getMapStringArray(){
        return game.getGenericWorldMap().getAllTerritoriesString().split("\n");
    }

    public void wantToAttack(boolean wantToAttack){
        if (wantToAttack) {
            game.setPlayerControllerMessage("attack");
        } else {
            game.setPlayerControllerMessage("");
        }
        //If this was something like diceFight, actually call the methods in game that do it
    }

    public void attackStarterChoice(String attackStarter){
        game.setPlayerControllerMessage(attackStarter);
    }

    public void attackDefenderChoice(String attackDefender){
        game.setPlayerControllerMessage(attackDefender);
    }

    public void wantToDiceFight(boolean wantToDiceFight) {
        if (wantToDiceFight) {
            game.setPlayerControllerMessage("diceFight");

        } else {
            game.setPlayerControllerMessage("");
        }
    }

    public void setAttackerDice(String dice){
        game.setPlayerControllerMessage(dice);
    }

    public void setDefenderDice(String dice){
        game.setCurrentMessage(dice);
    }

    public void setTakeoverTroops(String incomingTroops){
        game.setPlayerControllerMessage(incomingTroops);
    }

    public void wantToFortify(boolean wantToFortify){
        if (wantToFortify){
            game.setPlayerControllerMessage("fortify");
        }else{
            game.setPlayerControllerMessage("");
        }
    }

    public void chooseFortifyGiver(String fortifyGiver){
        game.setPlayerControllerMessage(fortifyGiver);
    }

    public void chooseFortifyReceiver(String fortifyReceiver){
        game.setPlayerControllerMessage(fortifyReceiver);
    }

    public void getFortifyTroops(String numTroops){
            game.setPlayerControllerMessage(numTroops);
    }

    public void saveGame(String fileName){game.saveGame(fileName);}
}
