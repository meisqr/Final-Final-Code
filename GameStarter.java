

public class GameStarter{
    
    public static void main(String[] args){
        GameFrame gf = new GameFrame(Game.GAME_WIDTH,Game.GAME_HEIGHT);
        gf.connectToServer();
        gf.setUpGUI();
        
    }
}
