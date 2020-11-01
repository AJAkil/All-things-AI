package AI;
import Game.*;
import Utility.*;

public class Heuristics {

    public static double heuristic1(Board currentBoard, int depth){
        return 1.0;
    }

    public static double pieceSquareTable(Board currentBoard,  int AIColor){

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
}
