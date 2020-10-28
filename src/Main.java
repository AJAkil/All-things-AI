import Utility.Pair;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(8,8);
        board.initiateBoard();
        board.printBoard();

        Scanner scanner = new Scanner(System.in);
        board.movePiece(new Pair(0,6), new Pair(2,4));
        board.movePiece(new Pair(7,6), new Pair(5,4));
        board.movePiece(new Pair(4,7), new Pair(4,5));
        board.printBoard();

        //board.generateMove(4,5);
        board.movePiece(new Pair(4,5), new Pair(4,3));
        board.printBoard();
        board.movePiece(new Pair(7,3), new Pair(4,3));

        board.printBoard();
        board.movePiece(new Pair(5,7), new Pair(5,4));
        board.printBoard();

        board.movePiece(new Pair(7,4), new Pair(5,2));
        board.printBoard();

        while (true){
            int x =  Integer.parseInt(scanner.nextLine());
            int y =  Integer.parseInt(scanner.nextLine());
            board.generateMove(x,y);
        }

    }
}
