import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();

        FileHandler handler = new FileHandler("E:\\ACADEMICS\\3-2\\16-batch" +
                "\\Sessionals\\AI lab\\OFFLINES\\OFFLINE 4\\1605079\\data\\"+fileName);

        LatinSquare ls = new LatinSquare();
        ls.setSize(handler.getBoardSize());
        ls.initiateLatinSquare(handler.readFile());
        ls.printBoard();
        Search cspobj= new Search(ls);
        cspobj.setUnassignedVariables();
        cspobj.setAllDynamicDegrees();
        cspobj.setDomains(ls.getSize());
        ls.printBoard();
        //cspobj.printAllDynamicDegrees();
        //System.out.println(cspobj.checkConstraint(7,8,8));
        System.out.println(cspobj.backtracking("DynamicDegree"));
        System.out.println(cspobj.getNodeCounter());
        System.out.println(cspobj.getBacktracks());
    }
}
