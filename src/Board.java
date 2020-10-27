import java.util.ArrayList;
import Utility.*;

public class Board {

    private int[][] currentBoardState;
    private int rows;
    private int columns;
    private int totalBlackPieces;
    private int totalWhitePieces;
    private ArrayList<Pair> nextPossibleMoves = new ArrayList<Pair>();


    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.totalBlackPieces = rows - 2;
        this.totalWhitePieces = rows - 2;
        initiateBoard();
    }

    /**
     * prints the board to the console
     */
    public void printBoard(){
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns ; j++) {
                System.out.print(Utility.prettifyBoard(currentBoardState[i][j]) + "   ");
            }
            System.out.println();
        }
    }

    /**
     * initiates the board to the initial game state
     */
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


    public boolean isValidMove(int targetX, int targetY, int sourceX, int sourceY, LOA loa){

        // checking the bounding conditions
        if ((targetX < 0 || targetX >= rows) || (targetY < 0 || targetY >= columns)){
            return false;
        }

        // checking if the target coordinate is the same color as me
        if ( this.currentBoardState[targetX][targetY]!=0 &&
                (this.currentBoardState[sourceX][sourceY] == this.currentBoardState[targetX][targetY])){
            return false;
        }

        // checking if the path to the target has any piece of opposite color

        int start = -1;
        int end = -1;
        int sourceColor = this.currentBoardState[sourceX][sourceY];

        switch (loa){
            case HLOA:{

                start = sourceY;
                end = targetY;

                if (targetY < sourceY){
                    start = targetY;
                    end = sourceY;

                }

                for (int i = start+1; i < end ; i++) {
                    if (sourceColor!=0 && this.currentBoardState[targetX][i] != sourceColor){
                        return false;
                    }
                }

                break;
            }
            case VLOA:{

                start = sourceX;
                end = targetX;

                if (targetX < sourceX){
                    start = targetX;
                    end = sourceX;

                }

                for (int i = start+1; i < end ; i++) {
                    if (sourceColor!=0 && this.currentBoardState[i][targetY] != sourceColor){
                        return false;
                    }
                }

                break;
            }
            case DIAG1LOA:{
                checkDiagonally(LOA.DIAG1LOA, sourceX, sourceY, targetX, sourceColor);
                break;
            }
            case DIAG2LOA:{
                checkDiagonally(LOA.DIAG2LOA, sourceX, sourceY, targetX, sourceColor);
                break;
            }


        }

        return true;
    }

    public boolean checkDiagonally(
             LOA loa, int sourceX, int sourceY, int targetX, int sourceColor){

        int currentX = -1;
        int currentY = -1;

        if (loa == LOA.DIAG1LOA && targetX < sourceX){
            currentX = sourceX - 1;
            currentY = sourceY - 1;
        } else if (loa == LOA.DIAG1LOA && targetX > sourceX){
            currentX = sourceX + 1;
            currentY = sourceY + 1;
        }else if (loa == LOA.DIAG2LOA && targetX < sourceX){
            currentX = sourceX - 1;
            currentY = sourceY + 1;
        }else if (loa == LOA.DIAG2LOA && targetX > sourceX){
            currentX = sourceX + 1;
            currentY = sourceY - 1;
        }

        while (currentX != targetX){

            int currentColor = this.currentBoardState[currentX][currentY];

            if (currentColor!=0 && currentColor!=sourceColor){
                return false;
            }

            if (loa == LOA.DIAG1LOA && targetX < sourceX){
                currentX--;
                currentY--;
            } else if (loa == LOA.DIAG1LOA && targetX > sourceX){
                currentX++;
                currentY++;
            }else if (loa == LOA.DIAG2LOA && targetX < sourceX){
                currentX--;
                currentY++;
            }else if (loa == LOA.DIAG2LOA && targetX > sourceX){
                currentX++;
                currentY--;
            }
        }

        return true;
    }


    public void generateMove(int sourceX, int sourceY){

    }

    public void makeMove(){

    }

    public boolean checkGameCompletion(){
        return false;
    }
}
