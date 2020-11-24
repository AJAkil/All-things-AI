import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class FileHandler {

    FileReader fr;
    BufferedReader br;
    String fileName;

    public FileHandler(String fileName)  {
        this.fileName = fileName;
    }

    public int getBoardSize() throws IOException {
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);

        String size= br.readLine().split("=")[1].split(";")[0];

        br.close();
        fr.close();

        return Integer.parseInt(size);
    }

    public String readFile() throws IOException {

        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        String board = "";

        br.readLine();
        br.readLine();
        br.readLine();

        String strCurrentLine;

        while ((strCurrentLine = br.readLine()) != null) {

            String[] splittedLine = strCurrentLine.split(",");
            board += splittedLine[0]+",";

            for (int i = 1; i <splittedLine.length ; i++) {
                board +=  splittedLine[i].split(" ")[1]+",";
            }
            board+="$";

        }

        br.close();
        fr.close();

        return board;
    }
}
