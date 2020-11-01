package Game;

import AI.AdversarialSearch;

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

        AdversarialSearch searchAgent = new AdversarialSearch(board, 2, 4);

        System.out.println("THE MOVE IS : " + searchAgent.minimaxDecision());

    }
}
