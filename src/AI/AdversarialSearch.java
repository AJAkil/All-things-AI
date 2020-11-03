package AI;
import Game.*;
import Utility.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdversarialSearch {

    private Board board;
    private int agentColor;
    private int depth;
    private final int MAX_VALUE = 100000;
    private final int MIN_VALUE = -100000;

    public AdversarialSearch(Board board, int agentColor, int depth) {
        this.board = board;
        this.agentColor = agentColor;
        this.depth = depth;
    }

    public int switchColor(int color){
        return 2-color+1;
    }

    public String minimaxDecision(){

        System.out.println("Here in the AI ");
        this.board.printBoard();

        // for the current state of the board and the current color of the agent, get all the possible moves
        HashMap<Pair, ArrayList<Pair>> AllMoves = this.board.generateAllMove(this.agentColor);
        int sourceColor, destinationColor;
        int c = 0;
        double value;
        double resultValue = Double.NEGATIVE_INFINITY;
        String move = "";
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

//        for (Map.Entry<Pair, ArrayList<Pair>> entry: AllMoves.entrySet()){
//            System.out.println("Source = " + entry.getKey() +
//                    ", Moves = " + entry.getValue());
//        }

        for (Map.Entry<Pair, ArrayList<Pair>> entry: AllMoves.entrySet()){

            // we take all the moves possible from a given source coordinate
            Pair sourceCoordinate = entry.getKey();
            ArrayList<Pair> possibleMoves= entry.getValue();

//            System.out.println("SOURCE = "+sourceCoordinate);
//            System.out.println("DESTINATIONs = "+possibleMoves);

            for (int i = 0; i < possibleMoves.size(); i++) {
                Pair destinationCoordinate = possibleMoves.get(i);

                //System.out.println(destinationCoordinate);

                // saving the source and destination color for undoing the move
                sourceColor = board.getCurrentBoardState()[sourceCoordinate.getX()][sourceCoordinate.getY()];
                destinationColor = board.getCurrentBoardState()[destinationCoordinate.getX()][destinationCoordinate.getY()];

                // we perform the move
                this.board.movePiece(sourceCoordinate, destinationCoordinate);

                // we call the minValue method next
                value = minValue(this.depth - 1, alpha, beta, this.agentColor);
                //System.out.println("VALUE" + value);
                // we then undo the move for the next iteration
                this.board.undoMove(sourceCoordinate, destinationCoordinate, sourceColor, destinationColor);

                if (value > resultValue) {
                    resultValue = value;
                    move = sourceCoordinate.getX() + "," + sourceCoordinate.getY() + "#" + destinationCoordinate.getX() +
                            "," + destinationCoordinate.getY();
                }

                alpha = Math.max(alpha, value);
            }

        }

        return move;
    }

    public double maxValue(int depth, double alpha, double beta, int prevColor){

        // first check if this is the terminal state or not or if the depth has been reached to 0, then return a heuristic value
        int completion = this.board.checkGameCompletion(prevColor);

        if (completion == this.agentColor){
            return Double.POSITIVE_INFINITY;
        }else if (completion != 0){
            return Double.NEGATIVE_INFINITY;
        }

        if (depth == 0){
            //return Heuristics.pieceSquareTable(this.board, this.agentColor);
            return Heuristics.finalEvaluator(this.board, this.agentColor);
        }

        // if that's not the case, then we need have some work to do. First we alternate the color of the piece to move
        int color = this.switchColor(prevColor);

        // if that's not the case, then we need have some work to do. First we alternate the color of the piece to move
        HashMap<Pair, ArrayList<Pair>> AllMoves = this.board.generateAllMove(color);
        int sourceColor, destinationColor;
        double value = Double.NEGATIVE_INFINITY;

        for (Map.Entry<Pair, ArrayList<Pair>> entry: AllMoves.entrySet()){

            // we take all the moves possible from a given source coordinate
            Pair sourceCoordinate = entry.getKey();
            ArrayList<Pair> possibleMoves= entry.getValue();

            //System.out.println(possibleMoves.size());

            for (int i = 0; i < possibleMoves.size(); i++) {
                Pair destinationCoordinate = possibleMoves.get(i);

                //System.out.println(destinationCoordinate);

                // saving the source and destination color for undoing the move
                sourceColor = board.getCurrentBoardState()[sourceCoordinate.getX()][sourceCoordinate.getY()];
                destinationColor = board.getCurrentBoardState()[destinationCoordinate.getX()][destinationCoordinate.getY()];

                // we perform the move
                this.board.movePiece(sourceCoordinate, destinationCoordinate);

                // we call the minValue method next
                value = Math.max(value, minValue(depth - 1, alpha, beta, color));

                // we then undo the move for the next iteration
                this.board.undoMove(sourceCoordinate, destinationCoordinate, sourceColor, destinationColor);

                if (value >= beta)
                    return value;

                alpha = Math.max(alpha, value);
            }

        }

        return value;

    }

    public double minValue(int depth, double alpha, double beta, int prevColor){

        // first check if this is the terminal state or not or if the depth has been reached to 0, then return a heuristic value
        int completion = this.board.checkGameCompletion(prevColor);

        if (completion == this.agentColor){
            return Double.POSITIVE_INFINITY;
        }else if (completion != 0){
            return Double.NEGATIVE_INFINITY;
        }

        if (depth == 0){
            //return Heuristics.pieceSquareTable(this.board, this.agentColor);
            return Heuristics.finalEvaluator(this.board, this.agentColor);
        }

        // if that's not the case, then we need have some work to do. First we alternate the color of the piece to move
        int color = this.switchColor(prevColor);

        // then the same thing again
        // for the current state of the board and the current color of the agent, get all the possible moves
        HashMap<Pair, ArrayList<Pair>> AllMoves = this.board.generateAllMove(color);
        int sourceColor, destinationColor;
        double value = Double.POSITIVE_INFINITY;

        for (Map.Entry<Pair, ArrayList<Pair>> entry: AllMoves.entrySet()){

            // we take all the moves possible from a given source coordinate
            Pair sourceCoordinate = entry.getKey();
            ArrayList<Pair> possibleMoves= entry.getValue();

            for (int i = 0; i < possibleMoves.size(); i++) {
                Pair destinationCoordinate = possibleMoves.get(i);

                //System.out.println(destinationCoordinate);

                // saving the source and destination color for undoing the move
                sourceColor = board.getCurrentBoardState()[sourceCoordinate.getX()][sourceCoordinate.getY()];
                destinationColor = board.getCurrentBoardState()[destinationCoordinate.getX()][destinationCoordinate.getY()];

                // we perform the move
                this.board.movePiece(sourceCoordinate, destinationCoordinate);

                // we call the minValue method next
                value = Math.min(value, maxValue(depth - 1, alpha, beta, color));

                // we then undo the move for the next iteration
                this.board.undoMove(sourceCoordinate, destinationCoordinate, sourceColor, destinationColor);

                if (value <= alpha)
                    return value;

                beta = Math.min(value, beta);
            }

        }


        return value;
    }
}
