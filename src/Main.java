import Utility.Pair;
import Game.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        GamePlay gamePlay = new HumanVsAI();
        gamePlay.GameLoop();

    }
}
