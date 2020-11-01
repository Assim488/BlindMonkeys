package blindmonkeys;

import java.util.Objects;
import javafx.scene.image.Image;

public class Card {

    private String cardType;  
    private int cardNumber;
    private Image cardImage;
    private int value;

    public Card(String cardType, int cardNumber) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        String currentDirectory = System.getProperty("user.dir");
        cardImage = new Image("file:///" + currentDirectory + "/src/resources/Cards/" + cardType + "/" + cardNumber + ".png");
        value = calculateCardValue(this.cardNumber);
    }

    private int calculateCardValue(int cardNumber) {
        if (cardNumber > 9) {  //10, J , Q ,K haben alle den Wert "10"
            return 10;
        }
        return cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) { 
        if (cardNumber > 13) { //K ist die 13. Karte eines jeden Symbols und auch die höchste, gibt keine weiteren
            return;
        }
        this.cardNumber = cardNumber;
    }

    public Image getCardImage() {
        return cardImage;
    }

    public void setCardImage(Image cardImage) {
        this.cardImage = cardImage;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {  //vorbereiten zur verwendung von equals und contains 
        boolean retVal = false;
        if (obj instanceof Card) {
            Card ptr = (Card) obj;
            retVal = (ptr.cardType == null ? this.cardType == null : ptr.cardType.equals(this.cardType)) && ptr.cardNumber == this.cardNumber;
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.cardType);
        hash = 89 * hash + this.cardNumber;
        return hash;
    }
}
