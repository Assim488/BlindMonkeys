package blindmonkeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cardList;

    public Deck() {
        cardList = new ArrayList();
        createDeck();
    }

    private void createDeck() { //Konstruktor erzeugt ein Deck indem jeweils 13 Karten eines Symbols kreiert werden
        for (int i = 1; i < 14; i++) {
            addCard(new Card("Clubs", i));
            addCard(new Card("Diamonds", i));
            addCard(new Card("Hearts", i));
            addCard(new Card("Spades", i));
        }
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public void addCard(Card card) {
        cardList.add(card);
    }

    public Card drawCard() {
        if (cardList.size() > 0) { //Wenn die Liste nicht leer ist
            return cardList.remove(0); //kann man ziehen und es wird eine removed
        } else {
            return null;
        }
    }

    public void removeCard(Card card) {
        if (cardList.contains(card)) {
            cardList.remove(card);
        }
    }

    public void shuffleDeck() { //Das Kartendeck vor Benutzung shuffeln
        Collections.shuffle(cardList);
    }

}
