import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        for (int i = 6; i <= 6; i++) {
            FileHandler handler = new FileHandler("E:\\ACADEMICS\\3-2\\16-batch" +
                    "\\Sessionals\\AI lab\\OFFLINES\\OFFLINE 4\\1605079\\data\\"+i+".txt");

            System.out.println("--------------------");
            System.out.println("----Forward Checking----");
            System.out.println("--------------------");
            LatinSquare ls2 = new LatinSquare();
            ls2.setSize(handler.getBoardSize());
            ls2.initiateLatinSquare(handler.readFile());
            //ls.printBoard();
            Search cspobj2= new Search(ls2);
            cspobj2.setUnassignedVariables();
            cspobj2.setAllDynamicDegrees();
            cspobj2.setDomains(ls2.getSize());

            System.out.println(cspobj2.forwardChecking("Breluz"));
            System.out.println(cspobj2.getNodeCounter());
            System.out.println(cspobj2.getBacktracks());
            System.out.println(ls2.isLatinSquareComplete());
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
            //System.out.println(cspobj.checkConstraint(7,8,8));
            System.out.println(cspobj.backtracking("Breluz"));
            System.out.println(cspobj.getNodeCounter());
            System.out.println(cspobj.getBacktracks());
            System.out.println(ls.isLatinSquareComplete());
//        ls.printBoard();
        }


    }
}
