import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();

        FileHandler handler = new FileHandler("E:\\ACADEMICS\\3-2\\16-batch" +
                "\\Sessionals\\AI lab\\OFFLINES\\OFFLINE 4\\1605079\\data\\"+fileName);

        System.out.println("--------------------");
        System.out.println("----Backtracking----");
        System.out.println("--------------------");

//        LatinSquare ls = new LatinSquare();
//        ls.setSize(handler.getBoardSize());
//        ls.initiateLatinSquare(handler.readFile());
//        //ls.printBoard();
//        Search cspobj= new Search(ls);
//        cspobj.setUnassignedVariables();
//        cspobj.setAllDynamicDegrees();
//        cspobj.setDomains(ls.getSize());
//        //ls.printBoard();
//        //cspobj.printAllDynamicDegrees();
//        //System.out.println(cspobj.checkConstraint(7,8,8));
//        System.out.println(cspobj.backtracking("Domddeg"));
//        System.out.println(cspobj.getNodeCounter());
//        System.out.println(cspobj.getBacktracks());
//        //ls.printBoard();

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

        System.out.println(cspobj2.forwardChecking("Domddeg"));
        System.out.println(cspobj2.getNodeCounter());
        System.out.println(cspobj2.getBacktracks());
        //ls2.printBoard();
    }
}
