package AI;
import Game.*;
import Utility.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Heuristics {

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

        int [][] pieceSquareTable2 = {
                {-80, -25, -20, -20, -25, -80},
                {-25, 10, 10, 10, 10, -25},
                {-20, 10, 25, 25, 10, -20},
                {-20, 10, 25, 25, 10, -20},
                {-25, 10, 10, 10, 10, -25},
                {-80, -25, -20, -20, -25, -80}
        };

        int[][] currentBoardState = currentBoard.getCurrentBoardState();
        int blackPositionScoreSum = 0, whitePositionScoreSum = 0;

        boolean tracker = currentBoard.getRows() == 8;


        if (tracker){
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
        }else{
            // first get the calculation for BLACK
            for (int i = 0; i < currentBoard.getRows(); i++) {
                for (int j = 0; j < currentBoard.getColumns(); j++) {
                    if (currentBoardState[i][j] == currentBoard.getBLACK()){
                        blackPositionScoreSum += pieceSquareTable2[i][j];
                    }
                }
            }

            // then do the same for the white one
            for (int i = 0; i < currentBoard.getRows(); i++) {
                for (int j = 0; j < currentBoard.getColumns(); j++) {
                    if (currentBoardState[i][j] == currentBoard.getWHITE()){
                        whitePositionScoreSum += pieceSquareTable2[i][j];
                    }
                }
            }
        }


        return (AIColor == currentBoard.getBLACK() ? blackPositionScoreSum - whitePositionScoreSum : whitePositionScoreSum - blackPositionScoreSum);
    }

    public static double areaHeuristic(Board currentBoard,  int AIColor){

        int[][] currentBoardState = currentBoard.getCurrentBoardState();
        Utility utility = new Utility();

        // calculating area for black
        double blackArea = utility.findArea(currentBoardState, currentBoard.getBLACK(), currentBoard.getRows(), currentBoard.getColumns());

        // then we calculate the area for black piece
        double WhiteArea = utility.findArea(currentBoardState, currentBoard.getWHITE(), currentBoard.getRows(), currentBoard.getColumns());

        return (AIColor == currentBoard.getBLACK() ? WhiteArea - blackArea : blackArea - WhiteArea);
    }

    public static double mobilityHeuristic(Board currentBoard, int AIColor){

        double blackMoves = 0, whiteMoves = 0;
        Utility utility = new Utility();

        blackMoves = utility.getTotalNumberOfMoves(currentBoard, currentBoard.getBLACK());
        whiteMoves = utility.getTotalNumberOfMoves(currentBoard, currentBoard.getWHITE());

//        System.out.println(blackMoves);
//        System.out.println(whiteMoves);

        return (AIColor == currentBoard.getBLACK() ? blackMoves - whiteMoves : whiteMoves - blackMoves);

    }

    public static double finalEvaluator(Board currentBoard, int AIColor){

        double w1 = 1.0;
        double w2 = 1.0;
        double w3 = 1.0;

        return (w1 * pieceSquareTable(currentBoard, AIColor)) + (w2 * areaHeuristic(currentBoard, AIColor)) +
                (w3 * mobilityHeuristic(currentBoard, AIColor));

    }

}
