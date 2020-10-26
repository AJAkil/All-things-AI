public class Board {

    private int[][] currentBoardState;
    private int rows;
    private int columns;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        initiateBoard();
    }

    public void printBoard(){
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns ; j++) {
                System.out.print(utility.prettifyBoard(currentBoardState[i][j]) + "   ");
            }
            System.out.println();
        }
    }

    public void initiateBoard(){

        currentBoardState = new int[rows][columns];

        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns ; j++) {
                currentBoardState[i][j] = 0;
            }
        }

        for (int i = 1; i < rows - 1 ; i++) {
            currentBoardState[i][0] = 1;
            currentBoardState[i][columns-1] = 1;
        }

        for (int i = 1; i < columns - 1 ; i++) {
            currentBoardState[0][i] = 2;
            currentBoardState[rows-1][i] = 2;
        }
    }
}
