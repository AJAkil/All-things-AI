package Utility;

import Game.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    public static String prettifyBoard(int piece){
        return ( piece == 1 ) ? "W" : ( piece == 2) ? "B" : "-";
    }

    public  double findArea(int[][] board, int color, int rows, int columns){
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

    public double getTotalNumberOfMoves(Board board, int color){

        double moves=0;

        HashMap<Pair, ArrayList<Pair>> blackMovesMap = board.generateAllMove(color);

        for (Map.Entry<Pair, ArrayList<Pair>> entry: blackMovesMap.entrySet()){
            moves += entry.getValue().size();
        }

        return moves;
    }

}
