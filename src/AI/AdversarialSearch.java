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

        // for the current state of the board and the current color of the agent, get all the possible moves
        HashMap<Pair, ArrayList<Pair>> AllMoves = this.board.generateAllMove(this.agentColor);
        int sourceColor, destinationColor;
        int c = 0;
        double value;
        double resultValue = Double.MIN_VALUE;
        String move = "";
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        for (Map.Entry<Pair, ArrayList<Pair>> entry: AllMoves.entrySet()){

            // we take all the moves possible from a given source coordinate
            Pair sourceCoordinate = entry.getKey();
            ArrayList<Pair> possibleMoves= entry.getValue();

            for (Pair destinationCoordinate : possibleMoves) {

                //System.out.println(destinationCoordinate);

                // saving the source and destination color for undoing the move
                sourceColor = board.getCurrentBoardState()[sourceCoordinate.getX()][sourceCoordinate.getY()];
                destinationColor = board.getCurrentBoardState()[destinationCoordinate.getX()][destinationCoordinate.getY()];

                // we perform the move
                this.board.movePiece(sourceCoordinate, destinationCoordinate);

                // we call the minValue method next
                value = minValue(this.depth-1, alpha, beta, this.agentColor);
                // we then undo the move for the next iteration
                this.board.undoMove(sourceCoordinate,destinationCoordinate,sourceColor,destinationColor);

                if (value > resultValue){
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
        if (depth == 0 || completion == 1 || completion == 2){
            return Heuristics.heuristic1(this.board, depth);
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

            for (Pair destinationCoordinate : possibleMoves) {

                //System.out.println(destinationCoordinate);

                // saving the source and destination color for undoing the move
                sourceColor = board.getCurrentBoardState()[sourceCoordinate.getX()][sourceCoordinate.getY()];
                destinationColor = board.getCurrentBoardState()[destinationCoordinate.getX()][destinationCoordinate.getY()];

                // we perform the move
                this.board.movePiece(sourceCoordinate, destinationCoordinate);

                // we call the minValue method next
                value = Math.max(value, minValue(depth-1, alpha, beta, color));

                // we then undo the move for the next iteration
                this.board.undoMove(sourceCoordinate,destinationCoordinate,sourceColor,destinationColor);

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
        if (depth == 0 || completion == 1 || completion == 2){
            return Heuristics.heuristic1(this.board, depth);
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

            for (Pair destinationCoordinate : possibleMoves) {

                //System.out.println(destinationCoordinate);

                // saving the source and destination color for undoing the move
                sourceColor = board.getCurrentBoardState()[sourceCoordinate.getX()][sourceCoordinate.getY()];
                destinationColor = board.getCurrentBoardState()[destinationCoordinate.getX()][destinationCoordinate.getY()];

                // we perform the move
                this.board.movePiece(sourceCoordinate, destinationCoordinate);

                // we call the minValue method next
                value = Math.min(value, maxValue(depth-1, alpha, beta, color));

                // we then undo the move for the next iteration
                this.board.undoMove(sourceCoordinate,destinationCoordinate,sourceColor,destinationColor);

                if (value <= alpha)
                    return value;

                beta = Math.min(value, beta);
            }

        }


        return value;
    }
}
