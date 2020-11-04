package Game;

import AI.AdversarialSearch;
import AI.Heuristics;
import Utility.*;

import java.util.Arrays;
import java.util.Scanner;

public class HumanVsAI extends GamePlay {
    @Override
    public void GameLoop() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the type of board for the game: ");
        int dimension = Integer.parseInt(scanner.nextLine());

        Board board = new Board(dimension,dimension);
        board.initiateBoard();
        //board.setCustomBoardPiecesForTesting();

        board.printBoard();
//        board.generateMove(1,0);
//        System.out.println(board.getNextPossibleMoves());
//        board.movePiece(new Pair(1,0), new Pair(1,2));
        //System.out.println(Heuristics.areaHeuristic(board,1));

        int player1Color, agentColor;
        System.out.print("Choose Color of the Human: 1 for white, 2 for black:  ");
        player1Color = Integer.parseInt(scanner.nextLine());

        agentColor = ( player1Color == 1) ? 2 : 1;
        String color1 = (player1Color == 1) ? "White" : "Black";
        String color2 = (agentColor == 1) ? "White" : "Black";

        System.out.println("Human is set to " + color1  + " AI is set to " + color2 + " ");

        this.setTurn(player1Color);
        AdversarialSearch searchAgent = new AdversarialSearch(board, agentColor, 10);

        while (true){

            int x,y;
            int turnColor;

            if (this.getTurn() == player1Color) {
                System.out.println("Human's turn");
                turnColor = player1Color;
            }
            else{
                System.out.println("Agent's turn");
                turnColor = agentColor;
            }

            if (turnColor == player1Color){

                while (true){
                    System.out.println("Enter your Source!");
                    x =  Integer.parseInt(scanner.nextLine());
                    y =  Integer.parseInt(scanner.nextLine());
                    if (board.getCurrentBoardState()[x][y] == 0 || board.getCurrentBoardState()[x][y] != turnColor){
                        System.out.println("No piece here or no piece of your color!! Try again");
                        continue;
                    }
                    board.generateMove(x,y);

                    System.out.println("Your Moves Are: ");
                    System.out.println(board.getNextPossibleMoves());
                    board.printBoard();

                    System.out.println("Do you want to give a move?");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("yes")) break;
                }

                System.out.println("Enter your Destination!");
                int a =  Integer.parseInt(scanner.nextLine());
                int b =  Integer.parseInt(scanner.nextLine());

                board.movePiece(new Pair(x,y), new Pair(a,b));
                board.printBoard();


            }else if (turnColor == agentColor){

                String[] moves= searchAgent.minimaxDecision().split("#");
                String[] sources = moves[0].split(",");
                String[] destinations = moves[1].split(",");

                System.out.println("AI has moved from "+ Arrays.toString(sources) +" to " + Arrays.toString(destinations));
//                System.out.println(Arrays.toString(sources));
//                System.out.println(Arrays.toString(destinations));
                
                board.movePiece(new Pair(Integer.parseInt(sources[0]), Integer.parseInt(sources[1])),
                        new Pair(Integer.parseInt(destinations[0]), Integer.parseInt(destinations[1])));
                board.printBoard();

            }

            int result = board.checkGameCompletion(turnColor);

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
