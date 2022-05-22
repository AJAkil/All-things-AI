import Utility.Pair;
import Game.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("WELCOME TO LOA");
        System.out.println("CHOOSE MODE TO PLAY: (ENTER THE MODE NUMBER)");
        System.out.println("1. Human VS Human ");
        System.out.println("2. Human VS AI ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("1")){

            GamePlay gamePlay = new HumanVsHuman();
            gamePlay.GameLoop();

        }else if(choice.equalsIgnoreCase("2")){

            GamePlay gamePlay = new HumanVsAI();
            gamePlay.GameLoop();

        }

    }
}
