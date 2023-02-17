package Game;

import Utility.Pair;

import java.util.Scanner;

public class HumanVsHuman extends GamePlay {

    @Override
    public void GameLoop() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the type of board for the game: ");
        int dimension = Integer.parseInt(scanner.nextLine());

        Board board = new Board(dimension,dimension);
        board.initiateBoard();

        //board.setCustomBoardPiecesForTesting();
        board.printBoard();

        int player1Color, player2Color;
        System.out.print("Choose Color of the first player: 1 for white, 2 for black:  ");
        player1Color = Integer.parseInt(scanner.nextLine());

        player2Color = ( player1Color == 1) ? 2 : 1;
        String color1 = (player1Color == 1) ? "White" : "Black";
        String color2 = (player2Color == 1) ? "White" : "Black";

        System.out.println("Player 1 is set to " + color1  + " Player 2 is set to " + color2 + " ");

        this.setTurn(player1Color);
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

            int x,y,a=-1,b=-1;
            int turnColor;

            if (this.getTurn() == player1Color) {
                System.out.println("Player 1's turn");
                turnColor = player1Color;
            }
            else{
                System.out.println("Player 2's turn");
                turnColor = player2Color;
            }


            while (true){

                System.out.println("Enter your Source!");
                x =  Integer.parseInt(scanner.nextLine()) - 1;
                y =  Integer.parseInt(scanner.nextLine()) - 1;

                if (board.getCurrentBoardState()[x][y] == 0 || board.getCurrentBoardState()[x][y] != turnColor){
                    System.out.println("No piece here or no piece of your color!! Try again");
                    continue;
                }

                board.generateMove(x,y);

                System.out.println("Your Moves Are: ");
                board.showAvailableMoves();
                board.printBoard();

                System.out.println("Do you want to give a move?");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("yes")) break;

            }


            while (true) {

                System.out.println("Enter your Destination!");
                a =  Integer.parseInt(scanner.nextLine()) - 1;
                b =  Integer.parseInt(scanner.nextLine()) - 1;

                if (!board.getNextPossibleMoves().contains(new Pair(a,b))){
                    System.out.println("WRONG MOVE, TRY AGAIN.");
                }else{
                    break;
                }
            }

            board.movePiece(new Pair(x,y), new Pair(a,b));
            board.printBoard();

            int result = board.checkGameCompletion(board.getCurrentBoardState()[x][y]);

            if (result == 1){
                System.out.println("WHITE WINS");
                break;
            }else if(result == 2){
                System.out.println("BLACK WINS");
                break;
            }

            this.revertTurn();

        }
    }
}
