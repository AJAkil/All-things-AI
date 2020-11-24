import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileHandler handler = new FileHandler("E:\\ACADEMICS\\3-2\\16-batch" +
                "\\Sessionals\\AI lab\\OFFLINES\\OFFLINE 4\\1605079\\data\\d-10-01.txt.txt");

        LatinSquare ls = new LatinSquare();
        ls.setSize(handler.getBoardSize());
        ls.initiateLatinSquare(handler.readFile());
        ls.printBoard();
    }
}
