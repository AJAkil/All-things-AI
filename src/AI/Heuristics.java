package AI;
import Game.*;
import Utility.*;

public class Heuristics {

    public static double heuristic1(Board currentBoard, int depth){
        return 1.0;
    }

    public static double pieceSquareTable(Board currentBoard,  int AIColor){

        //currentBoard.printBoard();

        int [][] pieceSquareTable = {
                {-80, -25, -20, -20, -20, -20, -25, -80},
                {-25, 10, 10, 10, 10, 10, 10, -25},
                {-20, 10, 25, 25, 25, 25, 10, -20},
                {-20, 10, 25, 50, 50, 25, 10, -20},
                {-20, 10, 25, 50, 50, 25, 10, -20},
                {-20, 10, 25, 25, 25, 25, 10, -20},
                {-25, 10, 10, 10, 10, 10, 10, -25},
                {-80, -25, -20, -20, -20, -20, -25, -80}
        };

//        int [][] pieceSquareTable = {
//                {-80, -25, -20, -20, -25, -80},
//                {-25, 10, 10, 10, 10, -25},
//                {-20, 10, 25, 25, 10, -20},
//                {-20, 10, 50, 50, 10, -20},
//                {-20, 10, 50, 50, 10, -20},
//                {-20, 10, 25, 25, 10, -20},
//                {-25, 10, 10, 10, 10, -25},
//                {-80, -25, -20, -20, -25, -80}
//        };

        int[][] currentBoardState = currentBoard.getCurrentBoardState();
        int blackPositionScoreSum = 0, whitePositionScoreSum = 0;

        // first get the calculation for BLACK
        for (int i = 0; i < currentBoard.getRows(); i++) {
            for (int j = 0; j < currentBoard.getColumns(); j++) {
                if (currentBoardState[i][j] == currentBoard.getBLACK()){
                    blackPositionScoreSum += pieceSquareTable[i][j];
                }
            }
        }

        // then do the same for the white one
        for (int i = 0; i < currentBoard.getRows(); i++) {
            for (int j = 0; j < currentBoard.getColumns(); j++) {
                if (currentBoardState[i][j] == currentBoard.getWHITE()){
                    whitePositionScoreSum += pieceSquareTable[i][j];
                }
            }
        }

        return (AIColor == currentBoard.getBLACK() ? blackPositionScoreSum - whitePositionScoreSum : whitePositionScoreSum - blackPositionScoreSum);
    }

    public static double areaHeuristic(Board currentBoard,  int AIColor){

        int[][] currentBoardState = currentBoard.getCurrentBoardState();

        // calculating area for black
        double blackArea = findArea(currentBoardState, currentBoard.getBLACK(), currentBoard.getRows(), currentBoard.getColumns());

        // then we calculate the area for black piece
        double WhiteArea = findArea(currentBoardState, currentBoard.getWHITE(), currentBoard.getRows(), currentBoard.getColumns());

        return (AIColor == currentBoard.getBLACK() ? WhiteArea - blackArea : blackArea - WhiteArea);
    }

    public static double findArea(int[][] board, int color, int rows, int columns){
        int minColumn = 0, maxColumn = 0, minRow = 0, maxRow = 0;

        // find the minColumn
        for (int i = 0; i< columns; i++){
            for (int j = 0; j < rows; j++) {
                if (board[j][i] == color){
                    minColumn = i;
                    break;
                }
            }
        }

        // find the maxColumn
        for (int i = columns - 1; i>=0; i--){
            for (int j = 0; j < rows; j++) {
                if (board[j][i] == color){
                    maxColumn = i;
                    break;
                }
            }
        }

        // find the minRow
        for (int i = 0; i< rows; i++){
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == color){
                    minRow= i;
                    break;
                }
            }
        }

        //maxRow
        for (int i = rows - 1; i>=0; i--){
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == color){
                    maxRow = i;
                    break;
                }
            }
        }

        double width = Math.abs(minColumn - maxColumn);
        double height = Math.abs(minRow - maxRow);

        return width * height;
    }


}
