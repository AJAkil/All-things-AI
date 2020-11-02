package Game;

import AI.AdversarialSearch;
import AI.Heuristics;

import java.util.Scanner;

public class HumanVsAI extends GamePlay {
    @Override
    public void GameLoop() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the type of board for the game: ");
        int dimension = Integer.parseInt(scanner.nextLine());

        Board board = new Board(dimension,dimension);
        //board.initiateBoard();
        board.setCustomBoardPiecesForTesting();

        board.printBoard();
        System.out.println(Heuristics.areaHeuristic(board,1));

        //AdversarialSearch searchAgent = new AdversarialSearch(board, 2, 4);

        //System.out.println("THE MOVE IS : " + searchAgent.minimaxDecision());

    }
}
