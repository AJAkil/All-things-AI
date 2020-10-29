import Utility.GridGraph;
import Utility.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(8,8);
        board.initiateBoard();
        //board.setCustomBoardPiecesForTesting();
        board.printBoard();
        //HashMap<Pair, ArrayList<Pair>> generateAllMove = board.generateAllMove(1);

//        int [][] grid = new int[][] {
//                {0, 2, 0, 2, 2, 1,0,0},
//                {0, 1, 1, 0, 2, 2,0,0},
//                {0, 1, 2, 0, 2, 2,0,0},
//                {0, 1, 1, 2, 0, 1,2,0},
//                {0, 1, 2, 0, 2, 2,0,2},
//                {0, 2, 1, 0, 2, 1,0,1},
//                {2, 1, 1, 1, 2, 2,1,0},
//                {0, 1, 1, 0, 1, 2,0,1},
//        };
//
        //GridGraph g = new GridGraph(board.getCurrentBoardState());
        //g.printBoard();
        //g.bfsOnBoard(new Pair(6,0), 12);
//
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
            int x,y;

            while (true){
                System.out.println("Enter your Source!");
                x =  Integer.parseInt(scanner.nextLine());
                y =  Integer.parseInt(scanner.nextLine());
                if (board.getCurrentBoardState()[x][y] == 0){
                    System.out.println("No piece here! Try again");
                    continue;
                }
                board.generateMove(x,y);
                System.out.println("Do you want to give a move?");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("yes")) break;
            }


            System.out.println("Enter your Destination!");
            int a =  Integer.parseInt(scanner.nextLine());
            int b =  Integer.parseInt(scanner.nextLine());

            int sourceColor = board.getCurrentBoardState()[x][y];
            int destinationColor = board.getCurrentBoardState()[a][b];

            board.movePiece(new Pair(x,y), new Pair(a,b));
            board.printBoard();

            System.out.println("DO you want to undo the move?");
            if (scanner.nextLine().equalsIgnoreCase("yes")){
                board.undoMove(new Pair(x,y), new Pair(a,b),sourceColor, destinationColor );
                board.printBoard();
            }

            int result = board.checkGameCompletion(board.getCurrentBoardState()[x][y]);

            if (result == 1){
                System.out.println("WHITE WINS");
            }else if(result == 2){
                System.out.println("BLACK WINS");
            }else if(result == 0){
                System.out.println("GO ON!!");
            }

        }

    }
}
