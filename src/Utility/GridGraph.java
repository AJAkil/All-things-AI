package Utility;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GridGraph {

    private int [][] operationalBoard;
    private boolean [][] visited;
    private int rows;
    private int columns;

    public GridGraph(int[][] board) {
        this.rows = board.length;
        this.columns = board[0].length;
        this.operationalBoard = new int[rows][columns];
        this.visited = new boolean[rows][columns];
        this.setVisited();
        this.cloneBoard(board);
    }

    public void setVisited(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.visited[i][j] = false;
            }
        }
    }

    public void printBoard(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(this.operationalBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printVisited(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(this.visited[i][j] + " ");
            }
            System.out.println();
        }
    }


    public boolean bfsOnBoard(Pair startingCoordinates, int totalPieces){


        int x = -1;
        int y = -1;
        int color = this.operationalBoard[startingCoordinates.getX()][startingCoordinates.getY()];
        ArrayList<Pair> possibleNeighbors = new ArrayList<Pair>();
        ArrayList<Pair> neighbors = new ArrayList<Pair>();
        int visited = 0;

        // declaring the Queue
        Queue<String> queue = new LinkedList<>();

        // Add the starting coordinate to the queue
        queue.add(startingCoordinates.getX() + "," + startingCoordinates.getY());
        this.visited[startingCoordinates.getX()][startingCoordinates.getY()] = true;

        while (!queue.isEmpty()){

            String popped = queue.remove();
            String [] splitted = popped.split(",");
            x = Integer.parseInt(splitted[0]);
            y = Integer.parseInt(splitted[1]);

            visited++;

            System.out.println("(" + x + "," + y  + ")");
            //this.printVisited();


            // coordinate validity
            possibleNeighbors.clear();

            possibleNeighbors.add(new Pair(x, y - 1));
            possibleNeighbors.add(new Pair(x, y + 1));
            possibleNeighbors.add(new Pair(x - 1, y));
            possibleNeighbors.add(new Pair(x + 1, y));
            possibleNeighbors.add(new Pair(x - 1, y - 1));
            possibleNeighbors.add(new Pair(x + 1, y + 1));
            possibleNeighbors.add(new Pair(x - 1, y + 1));
            possibleNeighbors.add(new Pair(x + 1, y - 1));

            neighbors = validateNeighbors(possibleNeighbors, color);

            for (Pair neighbor: neighbors){
                queue.add(neighbor.getX() + "," + neighbor.getY());
            }

       }

        System.out.println(visited);
        return visited == totalPieces;
    }

    private ArrayList<Pair> validateNeighbors(ArrayList<Pair> possibleNeighbors, int currentNodeColor) {
        ArrayList<Pair> neighbors = new ArrayList<Pair>();

        for (Pair node: possibleNeighbors){
            int x = node.getX();
            int y = node.getY();

            if (checkBoardBoundary(x, y) && !this.visited[x][y] && this.operationalBoard[x][y] == currentNodeColor){
//                System.out.println("Adding " + "(" + x + "," + y  + ") ");
                this.visited[x][y] = true;
                neighbors.add(node);
            }
        }

        return neighbors;
    }

    public boolean checkBoardBoundary(int x, int y){

        return (x >= 0 && x < rows) && (y >= 0 && y < columns);
    }

    public void cloneBoard(int[][] board){

        for (int i = 0; i < rows; i++) {
            if (columns >= 0) System.arraycopy(board[i], 0, this.operationalBoard[i], 0, columns);
        }
        
    }

}
