import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();

        FileHandler handler = new FileHandler("E:\\ACADEMICS\\3-2\\16-batch" +
                "\\Sessionals\\AI lab\\OFFLINES\\OFFLINE 4\\1605079\\data\\"+fileName);

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
//        System.out.println(cspobj.backtracking("DomainSize"));
//        System.out.println(cspobj.getNodeCounter());
//        System.out.println(cspobj.getBacktracks());
        //ls.printBoard();

        LatinSquare ls2 = new LatinSquare();
        ls2.setSize(handler.getBoardSize());
        ls2.initiateLatinSquare(handler.readFile());
        //ls.printBoard();
        Search cspobj2= new Search(ls2);
        cspobj2.setUnassignedVariables();
        cspobj2.setAllDynamicDegrees();
        cspobj2.setDomains(ls2.getSize());

        System.out.println(cspobj2.forwardChecking("DomainSize"));
        System.out.println(cspobj2.getNodeCounter());
        System.out.println(cspobj2.getBacktracks());
        System.out.println("track="+cspobj2.getTrack());
        ls2.printBoard();
    }
}
