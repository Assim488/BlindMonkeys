package blindmonkeys;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player {

    private int id;
    private int score;
    private List<Card> cards;
    private boolean cardVisibility[];

    public Player(int id, List<Card> cards) {
        this.id = id;
        this.cards = cards;
        score = 0;
        cardVisibility = new boolean[cards.size()];
    }

    public int getScore() {
        score = calculatePlayerScore();
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean[] getCardVisibility() {
        for (int i = 0; i < cards.size(); i++) {
            if ("remove".equals(cards.get(i).getCardType())) {
                cardVisibility[i] = false;
            }
        }
        return cardVisibility;
    }

    public void setCardVisibility(boolean[] cardVisibility) {
        this.cardVisibility = cardVisibility;
    }

    public int calculatePlayerScore() {
        int totalScore = 0;
        List<Integer> valueList = new ArrayList();
        for (Card c : cards) {
            valueList.add(c.getValue());
        }
        valueList = valueList.stream().distinct().collect(Collectors.toList());  //Keine Mehrfachzählung  
        return valueList.stream().map((c) -> c).reduce(totalScore, Integer::sum);  //Punktzahl
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        if (cards.contains(card)) {
            cards.remove(card);
        }
    }
}
