import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    int clientNum;
    int anteBet;
    int pairPlusBet;
    int playBet;
    ArrayList<String> dealerCards;
    ArrayList<String> playerCards;
    String dealOrFold;
    int totalGameMoney;
    int roundWinnings;
    boolean playGameAgain;
    String message;
}
