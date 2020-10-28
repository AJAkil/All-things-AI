import Utility.GridGraph;
import Utility.Pair;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(8,8);
        board.initiateBoard();
        board.printBoard();

        int [][] grid = new int[][] {
                {0, 2, 0, 2, 2, 1,0,0},
                {0, 1, 1, 0, 2, 2,0,0},
                {0, 1, 2, 0, 2, 2,0,0},
                {0, 1, 1, 2, 0, 1,2,0},
                {0, 1, 2, 0, 2, 2,0,2},
                {0, 2, 1, 0, 2, 1,0,1},
                {2, 1, 1, 1, 2, 2,1,0},
                {0, 1, 1, 0, 1, 2,0,1},
        };

        GridGraph g = new GridGraph(grid);
        g.printBoard();
        g.bfsOnBoard(new Pair(0,7), 12);

        Scanner scanner = new Scanner(System.in);
//        board.movePiece(new Pair(0,6), new Pair(2,4));
//        board.movePiece(new Pair(7,6), new Pair(5,4));
//        board.movePiece(new Pair(4,7), new Pair(4,5));
//        board.printBoard();
//
//        //board.generateMove(4,5);
//        board.movePiece(new Pair(4,5), new Pair(4,3));
//        board.printBoard();
//        board.movePiece(new Pair(7,3), new Pair(4,3));
//
//        board.printBoard();
//        board.movePiece(new Pair(5,7), new Pair(5,4));
//        board.printBoard();
//
//        board.movePiece(new Pair(7,4), new Pair(5,2));
//        board.printBoard();

        while (true){
            System.out.println("Enter your Source!");
            int x =  Integer.parseInt(scanner.nextLine());
            int y =  Integer.parseInt(scanner.nextLine());
            board.generateMove(x,y);


            System.out.println("Enter your Destination!");
            int a =  Integer.parseInt(scanner.nextLine());
            int b =  Integer.parseInt(scanner.nextLine());

            board.movePiece(new Pair(x,y), new Pair(a,b));
            board.printBoard();


        }

    }
}
