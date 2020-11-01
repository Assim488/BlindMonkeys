package blindmonkeys;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Deck deck;
    private List<Player> playerList;
    private int turn;
    private Card cardOnTable;

    public Game(int playerCount) { //bekommt die eingabe, wv spieler spielen und initialisiert dann Spiel
        turn = 0;
        playerList = new ArrayList();
        deck = new Deck();
        deck.shuffleDeck();
        for (int i = 1; i < playerCount + 1; i++) {
            addPlayer(createNewPlayer(i));
        }
        cardOnTable = deck.drawCard();
    }

    public Game(List<Player> playerList) { //In den folgenden Runden wird Spiel nur mit denen Initialisiert, die auch noch spielen dürfen
        turn = 0;
        this.playerList = new ArrayList();
        deck = new Deck();
        deck.shuffleDeck();
        for (int i = 0; i < playerList.size(); i++) {
            addPlayer(refreshPlayersCards(playerList.get(i)));
        }
        cardOnTable = deck.drawCard();
    }

    private Player refreshPlayersCards(Player player) {
        List<Card> playerCards = new ArrayList();
        for (int i = 0; i < 4; i++) {
            playerCards.add(deck.drawCard());
        }
        player.setCards(playerCards);
        boolean visibility[] = {true, true, true, true}; //Nach throw card immediately, wird die sichtbarkeit (hinterseite) der karte verhindert
        player.setCardVisibility(visibility);
        return player;
    }

    private Player createNewPlayer(int id) {
        List<Card> playerCards = new ArrayList();
        for (int i = 0; i < 4; i++) {
            playerCards.add(deck.drawCard());
        }
        Player player = new Player(id, playerCards);
        boolean visibility[] = {true, true, true, true}; //Throw Card Logik
        player.setCardVisibility(visibility);
        return player;
    }

    private void addPlayer(Player player) {
        playerList.add(player);
    }

    public Player getCurrentPlayer() { //Ermittelt, wer am Zuge ist
        int playerCount = playerList.size();
        if (turn % playerCount == 0) {
            return playerList.get(playerCount - 1);
        }
        return playerList.get((turn % playerCount) - 1);
    }

    public Player nextPlayer() {
        int playerCount = playerList.size();
        int id = getCurrentPlayer().getId();
        if (id < playerCount) {
            return playerList.get(id);
        } else {
            return playerList.get(0); //Um vom letzten Player, wieder zum Player 1 zu gelangen
        }
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void nextTurn() {
        turn++;
    }

    public Card getCardOnTable() {
        return cardOnTable;
    }

    public void setCardOnTable(Card cardOnTable) {
        this.cardOnTable = cardOnTable;
    }
}
