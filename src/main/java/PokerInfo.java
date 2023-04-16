import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    int anteBet = 0;
    int pairPlusBet = 0;
    int pairPlusEarnings = 0;
    ArrayList<String> dealerCards;
    ArrayList<String> playerCards;
    int totalGameMoney = 0;
    int roundWinnings = 0;
    String winner = "";
    String message = "";
    boolean hasConnectedToServerFirstTime = false;

    public PokerInfo() {
        dealerCards = new ArrayList<String>(3);
        playerCards = new ArrayList<String>(3);
    }
}
