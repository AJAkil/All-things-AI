import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        long startTime, endTime;

        String[] Heuristics = {"DomainSize", "Breluz", "Domddeg"};

        for (String h: Heuristics){

            System.out.println("HEURISTIC: "+h+"\n");

            for (int i = 1; i <= 4; i++) {
                FileHandler handler = new FileHandler("E:\\ACADEMICS\\3-2\\16-batch" +
                        "\\Sessionals\\AI lab\\OFFLINES\\OFFLINE 4\\1605079\\data\\"+i+".txt");

                System.out.println("INPUT FILE: "+i+"\n");

                System.out.println("------------------------");
                System.out.println("----Forward Checking----");
                System.out.println("------------------------");
                LatinSquare ls2 = new LatinSquare();
                ls2.setSize(handler.getBoardSize());
                ls2.initiateLatinSquare(handler.readFile());
                //ls.printBoard();
                Search cspobj2= new Search(ls2);
                cspobj2.setUnassignedVariables();
                cspobj2.setAllDynamicDegrees();
                cspobj2.setDomains(ls2.getSize());

                cspobj2.forwardChecking(h);
                System.out.println("Nodes: "+cspobj2.getNodeCounter());
                System.out.println("Backtracks: "+cspobj2.getBacktracks());
                System.out.println("Is Latin Square Complete?: "+ls2.isLatinSquareComplete()+"\n");
                //ls2.printBoard();

                System.out.println("--------------------");
                System.out.println("----Backtracking----");
                System.out.println("--------------------");

                LatinSquare ls = new LatinSquare();
                ls.setSize(handler.getBoardSize());
                ls.initiateLatinSquare(handler.readFile());
                //ls.printBoard();
                Search cspobj= new Search(ls);
                cspobj.setUnassignedVariables();
                cspobj.setAllDynamicDegrees();
                cspobj.setDomains(ls.getSize());
                //ls.printBoard();
                //cspobj.printAllDynamicDegrees();
                cspobj.backtracking(h);
                System.out.println("Nodes: "+cspobj.getNodeCounter());
                System.out.println("Backtracks: "+cspobj.getBacktracks());
                System.out.println("Is Latin Square Complete?: "+ls.isLatinSquareComplete()+"\n");
//        ls.printBoard();
            }
        }


    }
}
