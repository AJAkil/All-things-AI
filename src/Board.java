import java.util.ArrayList;
import java.util.Scanner;

import Utility.*;

public class Board {

    private int[][] currentBoardState;
    private final int rows;
    private final int columns;
    private int totalBlackPieces;
    private int totalWhitePieces;
    private ArrayList<Pair> nextPossibleMoves = new ArrayList<Pair>();
    private final int BLACK = 2;
    private final int WHITE = 1;
    private final int empty = 0;


    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.totalBlackPieces = (rows - 2)*2;
        this.totalWhitePieces = (rows - 2)*2;
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

        System.out.println();
    }

    /**
     * initiates the board to the initial game state
     */
    public void initiateBoard(){

        currentBoardState = new int[rows][columns];

        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns ; j++) {
                currentBoardState[i][j] = empty;
            }
        }

        // filling up the white pieces
        for (int i = 1; i < rows - 1 ; i++) {
            currentBoardState[i][0] = WHITE;
            currentBoardState[i][columns-1] = WHITE;
        }

        // filling up the black pieces
        for (int i = 1; i < columns - 1 ; i++) {
            currentBoardState[0][i] = BLACK;
            currentBoardState[rows-1][i] = BLACK;
        }
    }

    public boolean isValidMove(int targetX, int targetY, int sourceX, int sourceY, LOA loa){

        int sourceColor = this.currentBoardState[sourceX][sourceY];

        // checking the bounding conditions
        if ((targetX < 0 || targetX >= rows) || (targetY < 0 || targetY >= columns)){
            return false;
        }

        // checking if the target coordinate is the same color as me
        if ( this.currentBoardState[targetX][targetY]!=0 &&
                (sourceColor == this.currentBoardState[targetX][targetY])){
            return false;
        }

        // checking if the path to the target has any piece of opposite color
        int start = -1;
        int end = -1;


        switch (loa){
            case HLOA:{

                start = sourceY;
                end = targetY;

                if (targetY < sourceY){
                    start = targetY;
                    end = sourceY;

                }

                for (int i = start+1; i < end ; i++) {
                    if (sourceColor!=0 && this.currentBoardState[targetX][i] != 0
                            && this.currentBoardState[targetX][i] != sourceColor){
                        return false;
                    }
                }

                break;
            }
            case VLOA:{

                start = sourceX;
                end = targetX;

                if (targetX < sourceX){
                    start = targetX; //0
                    end = sourceX; //5

                }

                for (int i = start+1; i < end ; i++) {
                    if (sourceColor!=0 && this.currentBoardState[i][targetY]!=0
                            && this.currentBoardState[i][targetY] != sourceColor){
                        return false;
                    }
                }

                break;
            }
            case DIAG1LOA:{
                 if(!checkDiagonally(LOA.DIAG1LOA, sourceX, sourceY, targetX, sourceColor)) return false;
                 break;
            }
            case DIAG2LOA:{
                if(!checkDiagonally(LOA.DIAG2LOA, sourceX, sourceY, targetX, sourceColor)) return false;
                break;
            }


        }

        return true;
    }

    public boolean checkDiagonally(LOA loa, int sourceX, int sourceY, int targetX, int sourceColor){

        //this.printBoard();

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
//            System.out.println(currentX);
//            System.out.println(currentY);
//            System.out.println(targetX);
        }


        while (currentX != targetX){

            int currentColor = this.currentBoardState[currentX][currentY];
//            System.out.println(currentColor);
//            System.out.println(sourceColor);

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

        this.nextPossibleMoves.clear();
        // generate possible vertical moves
        generateVerticalMoves(sourceX, sourceY);


        // generate possible horizontal moves
        generateHorizontalMoves(sourceX, sourceY);


        // generate possible diagonal-1/ left diagonal moves
        generateDiagonal1Moves(sourceX, sourceY);

        // generate possible diagonal-2/ right diagonal moves
        generateDiagonal2Moves(sourceX, sourceY);
        this.showAvailableMoves();

        // check for duplicate same moves

    }

    public void generateVerticalMoves(int sourceX, int sourceY){

        int pieces = 0;

        // count the number of pieces in the column of the piece in question
        for (int i = 0; i < rows ; i++) {
            if (this.currentBoardState[i][sourceY]!=0){
                pieces++;
            }
        }

        // moving downward from source
        if (isValidMove(sourceX + pieces, sourceY, sourceX, sourceY, LOA.VLOA)){
            nextPossibleMoves.add(new Pair(sourceX + pieces, sourceY));
        }

         // moving upward from source
        if (isValidMove(sourceX - pieces, sourceY, sourceX, sourceY, LOA.VLOA)){
            nextPossibleMoves.add(new Pair(sourceX - pieces, sourceY));
        }

    }

    public void generateHorizontalMoves(int sourceX, int sourceY){

        int pieces = 0;

        // count the number of pieces in the column of the piece in question
        for (int i = 0; i < columns ; i++) {
            if (this.currentBoardState[sourceX][i]!=0){
                pieces++;
            }
        }

        //System.out.println(pieces);

        // moving right from source
        if (isValidMove(sourceX, sourceY + pieces, sourceX, sourceY, LOA.HLOA)){
            nextPossibleMoves.add(new Pair(sourceX, sourceY + pieces));
        }

        // moving left from source
        if (isValidMove(sourceX, sourceY - pieces, sourceX, sourceY, LOA.HLOA)){
            nextPossibleMoves.add(new Pair(sourceX, sourceY - pieces));
        }

    }

    private void generateDiagonal2Moves(int sourceX, int sourceY) {

        int pieces = 0;

        int currentX = sourceX;
        int currentY = sourceY;

        // using this loop to count piece of the top right part including source itself
        while (currentX >= 0 && currentY < columns) {

            if (this.currentBoardState[currentX][currentY] != 0) pieces++;

            currentX--;
            currentY++;

        }

        currentX = sourceX + 1;
        currentY = sourceY - 1;


        // using this loop to count piece of the bottom left part including source itself
        while (currentX < rows && currentY >= 0) {

            if (this.currentBoardState[currentX][currentY] != 0) pieces++;

            currentX++;
            currentY--;

        }

//        System.out.println("wtf");
//        System.out.println(sourceX - pieces);
//        System.out.println(sourceY + pieces);
        // moving diagonally up right
        if (isValidMove(sourceX - pieces, sourceY + pieces, sourceX, sourceY, LOA.DIAG2LOA)){
            nextPossibleMoves.add(new Pair(sourceX - pieces, sourceY + pieces));
        }

        // moving diagonally bottom left
//        System.out.println("wtf");
//        System.out.println(sourceX+" "+sourceY);
        if (isValidMove(sourceX + pieces, sourceY - pieces, sourceX, sourceY, LOA.DIAG2LOA)){
            nextPossibleMoves.add(new Pair(sourceX + pieces, sourceY - pieces));
        }


    }

    public void generateDiagonal1Moves(int sourceX, int sourceY) {

        int pieces = 0;

        int currentX = sourceX;
        int currentY = sourceY;

        // using this loop to count piece of the top left part including source itself
        while (currentX >= 0 && currentY >= 0) {

            if (this.currentBoardState[currentX][currentY] != 0) pieces++;

            currentX--;
            currentY--;
        }

        currentX = sourceX + 1;
        currentY = sourceY + 1;

        // using this loop to count piece of the bottom right part including source itself
        while (currentX < rows && currentY < columns) {

            if (this.currentBoardState[currentX][currentY] != 0) pieces++;

            currentX++;
            currentY++;

        }

        // moving diagonally up left
        if (isValidMove(sourceX - pieces, sourceY - pieces, sourceX, sourceY, LOA.DIAG1LOA)){
            nextPossibleMoves.add(new Pair(sourceX - pieces, sourceY - pieces));
        }

        // moving diagonally bottom right
        if (isValidMove(sourceX + pieces, sourceY + pieces, sourceX, sourceY, LOA.DIAG1LOA)){
            nextPossibleMoves.add(new Pair(sourceX + pieces, sourceY + pieces));
        }

    }

    public void showAvailableMoves(){
        for (Pair move : nextPossibleMoves){
            System.out.println(move);
        }
    }

    public void capturePiece(int color){
        if ((color == 1)) {
            this.totalBlackPieces--;
        } else if(color == 2) {
            this.totalWhitePieces--;
        }
    }

    public void movePiece(Pair source, Pair destination){

        int sourceColor = this.currentBoardState[source.getX()][source.getY()];
        int destinationColor = this.currentBoardState[source.getX()][source.getY()];

        // moving the source piece and placing it in the destination
        this.currentBoardState[source.getX()][source.getY()] = 0;
        this.currentBoardState[destination.getX()][destination.getY()] = sourceColor;

        // if the destination has piece of opposite color, remove it from the board and place this color
        if (destinationColor != 0 && sourceColor!=destinationColor){
            // reduce the piece number of destination
            capturePiece(destinationColor);
        }

    }

    public int[][] getCurrentBoardState() {
        return currentBoardState;
    }

    /**
     * this method returns the player color that wins the match. If there is no winner yet, then it returns 0
     * @param lastMovingColor to keep track of the color that has made the final move before we call
     *                    this method
     * @return the value 0 if no one wins, else return the color value that wins
     */
    public int checkGameCompletion(int lastMovingColor){

        int x,y;

        // first check if there is player of a single color left, if so then that player won
        if ( this.totalBlackPieces == 1){
            return BLACK;
        }else if ( this.totalWhitePieces == 1 ){
            return WHITE;
        }

        String[] splitted = getFirstCoordinates(WHITE).split(",");
        x = Integer.parseInt(splitted[0]);
        y = Integer.parseInt(splitted[1]);

        GridGraph  g = new GridGraph(this.currentBoardState);

        boolean whiteChecker = g.bfsOnBoard(new Pair(x, y), this.totalWhitePieces);

        g.setVisited();
        g.cloneBoard(this.currentBoardState);

        splitted = getFirstCoordinates(BLACK).split(",");
        x = Integer.parseInt(splitted[0]);
        y = Integer.parseInt(splitted[1]);

        //System.out.println("("+x+","+y+") first black piece found");

        boolean blackChecker = g.bfsOnBoard(new Pair(x, y), this.totalBlackPieces);
        //System.out.println(blackChecker);

        if (whiteChecker && blackChecker){
            return lastMovingColor;
        }else if (blackChecker){
            return BLACK;
        }else if (whiteChecker){
            return WHITE;
        }

        return empty;
    }

    public String getFirstCoordinates(int pieceColor){
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (this.currentBoardState[i][j] == pieceColor){
                    return i + "," + j;
                }
            }
        }

        return "";
    }

    public int getTotalBlackPieces() {
        return totalBlackPieces;
    }

    public int getTotalWhitePieces() {
        return totalWhitePieces;
    }

    public ArrayList<Pair> getNextPossibleMoves() {
        return nextPossibleMoves;
    }

    public void setCustomBoardPiecesForTesting(){
        Scanner scan = new Scanner(System.in);
        int x;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                x = Integer.parseInt(scan.nextLine());
                this.currentBoardState[i][j] = x;
            }
        }
    }
}
