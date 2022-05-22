package Game;

public abstract class GamePlay {

    private int turn;

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    public void revertTurn(){
        //System.out.println("here");
        if (turn == 1){
            turn = 2;
        }else {
            turn = 1;
        }
    }

    public abstract void GameLoop();

}
