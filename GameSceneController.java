package blindmonkeys;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GameSceneController implements Initializable {

    private Map<Player, Integer> scoreBoard;
    private Game game;
    private boolean isCardSelect = false;
    private boolean throwSameCard = false;
    private Card selectedCard;
    private int remainingEndGame = -1;
    private Player gameEnder;
    private boolean specialMove8 = false;
    private boolean specialMove9 = false;
    private boolean specialMove10 = false;
    private int pickedCardNumber;

    @FXML
    public Label playerName;

    @FXML
    private Button throwButton;

    @FXML
    private Button startButton;

    @FXML
    private Pane gameSettingsPane;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;

    @FXML
    private RadioButton radioButton4;

    @FXML
    private ImageView cardOnTable;

    @FXML
    private ImageView remainingDeck;

    @FXML
    private ImageView drawedCard;

    @FXML
    private Label warningLabel;

    @FXML
    private ImageView card1;

    @FXML
    private ImageView card2;

    @FXML
    private ImageView card3;

    @FXML
    private ImageView card4;

    @FXML
    private Pane scoreBoardPane;

    @FXML
    private Label score1;

    @FXML
    private Label score2;

    @FXML
    private Label score3;

    @FXML
    private Label score4;

    @FXML
    private Button endThrowButton;

    @FXML
    private Pane nextPlayerPane;

    @FXML
    private ImageView nextPlayer1;

    @FXML
    private ImageView nextPlayer2;

    @FXML
    private ImageView nextPlayer3;

    @FXML
    private ImageView nextPlayer4;

    public void nextTurnInGame() {
        if (remainingEndGame > 0) { //Damit nach "End Game" jeder noch ne Runde spielen kann
            remainingEndGame--;
        } else if (remainingEndGame == 0) {
            endGame();
            return;
        }
        int playerCount = playerCount();
        game.nextTurn();
        refreshCardVisibility();  //Wv Karten werden gezeigt
        Player player = game.getPlayerList().get(calculatePlayerTurn(game.getTurn(), playerCount));
        playerName.setText("PLAYER " + player.getId());
        if (game.getTurn() <= playerCount) {  //überprüft, ob es der erste Zug ist
            Image cardBackGround = card2.getImage();
            card3.setImage(player.getCards().get(2).getCardImage());
            card4.setImage(player.getCards().get(3).getCardImage());
            System.out.println("Karten offen");
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {  //Nach 5 Sekunden werden die Hinterseiten wieder geladen
                    card3.setImage(cardBackGround);
                    card4.setImage(cardBackGround);
                    System.out.println("Karten verdeckt");
                    t.cancel();
                }
            }, 5000);
        }

    }

    private void specialMoveEight(int cardNumber) {
        ImageView cardView = null;
        switch (cardNumber) {
            case 1:
                cardView = card1;
                break;
            case 2:
                cardView = card2;
                break;
            case 3:
                cardView = card3;
                break;
            case 4:
                cardView = card4;
                break;
        }
        if (cardView != null) {
            cardView.setImage(game.getCurrentPlayer().getCards().get(cardNumber - 1).getCardImage());
        }
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (cardNumber) {
                    case 1:
                        card1.setImage(card2.getImage());
                        break;
                    case 2:
                        card2.setImage(card1.getImage());
                        break;
                    case 3:
                        card3.setImage(card2.getImage());
                        break;
                    case 4:
                        card4.setImage(card2.getImage());
                        break;
                }
                Platform.runLater(() -> {
                    warningLabel.setVisible(false);
                    warningLabel.setText("WÄHLEN SIE EINE KARTE ZUM TAUSCHEN ODER WEGLEGEN");
                    nextTurnInGame();
                    throw new UnsupportedOperationException("Not supported yet."); 
                });

                t.cancel();
            }
        }, 5000);
    }

    private void specialMoveNine(int cardNumber) {

        ImageView cardView = null;
        switch (cardNumber) {
            case 1:
                cardView = nextPlayer1;
                break;
            case 2:
                cardView = nextPlayer2;
                break;
            case 3:
                cardView = nextPlayer3;
                break;
            case 4:
                cardView = nextPlayer4;
                break;
        }
        if (cardView != null) {
            cardView.setImage(game.nextPlayer().getCards().get(cardNumber - 1).getCardImage());
        }
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (cardNumber) {
                    case 1:
                        nextPlayer1.setImage(card2.getImage());
                        break;
                    case 2:
                        nextPlayer2.setImage(card2.getImage());
                        break;
                    case 3:
                        nextPlayer3.setImage(card2.getImage());
                        break;
                    case 4:
                        nextPlayer4.setImage(card2.getImage());
                        break;
                }
                warningLabel.setVisible(false);
                warningLabel.setText("WÄHLEN SIE EINE KARTE ZUM TAUSCHEN ODER WEGLEGEN");
                Platform.runLater(() -> {
                    nextPlayerPane.setVisible(false);
                    nextTurnInGame();
                    throw new UnsupportedOperationException("Not supported yet."); 
                });

                t.cancel();
            }
        }, 5000);
    }

    private void prepareSpecialMoveTen(int pickedCardNumber) {
        nextPlayerPane.setVisible(true);
        this.pickedCardNumber = pickedCardNumber;
    }

    private void specialMoveTen(int cardNumber) {

        ImageView cardView = null;
        switch (cardNumber) {
            case 1:
                cardView = nextPlayer1;
                break;
            case 2:
                cardView = nextPlayer2;
                break;
            case 3:
                cardView = nextPlayer3;
                break;
            case 4:
                cardView = nextPlayer4;
                break;
        }
        if (cardView != null) {
            Card temp = game.nextPlayer().getCards().get(cardNumber - 1);
            game.nextPlayer().getCards().set(cardNumber - 1, game.getCurrentPlayer().getCards().get(cardNumber - 1));
            game.getCurrentPlayer().getCards().set(pickedCardNumber - 1, temp);
            warningLabel.setVisible(false);
            warningLabel.setText("WÄHLEN SIE EINE KARTE ZUM TAUSCHEN ODER WEGLEGEN");
            nextPlayerPane.setVisible(false);
            nextTurnInGame();
        }
    }

    private int calculatePlayerTurn(int turn, int playerCount) {
        if (turn % playerCount == 0) {
            return playerCount - 1;
        }
        return (turn % playerCount) - 1;
    }

    @FXML
    void handleStartButtonAction(ActionEvent event) {
        game = new Game(playerCount());
        scoreBoard = new HashMap<>();
        firstRound();
    }

    @FXML
    void handleThrowSameCardButtonAction(ActionEvent event) {
        throwSameCard = true;
    }

    @FXML
    void handleNextPlayer1Action(MouseEvent event) {
        if (specialMove10) {
            specialMoveTen(1);
            specialMove10 = false;
        } else if (specialMove9) {
            specialMoveNine(1);
            specialMove9 = false;
        }
    }

    @FXML
    void handleNextPlayer2Action(MouseEvent event) {
        if (specialMove10) {
            specialMoveTen(2);
            specialMove10 = false;
        } else if (specialMove9) {
            specialMoveNine(2);
            specialMove9 = false;
        }
    }

    @FXML
    void handleNextPlayer3Action(MouseEvent event) {
        if (specialMove10) {
            specialMoveTen(3);
            specialMove10 = false;
        } else if (specialMove9) {
            specialMoveNine(3);
            specialMove9 = false;
        }
    }

    @FXML
    void handleNextPlayer4Action(MouseEvent event) {
        if (specialMove10) {
            specialMoveTen(4);
            specialMove10 = false;
        } else if (specialMove9) {
            specialMoveNine(4);
            specialMove9 = false;
        }
    }

    private void resetGame() {
        resetCardVisibility();
        isCardSelect = false;
        remainingEndGame = -1;
        scoreBoardPane.setVisible(false);
    }

    private void firstRound() {
        gameSettingsPane.setVisible(false);
        cardOnTable.setImage(game.getCardOnTable().getCardImage());
        nextTurnInGame();
    }

    private void refreshCardOnTable(Card card) {
        game.setCardOnTable(card);
        cardOnTable.setImage(card.getCardImage());
    }

    private void cardExchange(int cardNumber) {
        if (isCardSelect) {
            warningLabel.setVisible(false); //kırmızı yazıyı kapatıyor
            refreshCardOnTable(game.getCurrentPlayer().getCards().get(cardNumber - 1)); //masadaki kartı degistiriyor
            game.getCurrentPlayer().getCards().set(cardNumber - 1, selectedCard); // oyuncunun kartını degistiriyor
            drawedCard.setVisible(false);
            throwButton.setVisible(false);
            isCardSelect = false;
            if (!specialMoves()) { //Wenn kein Special-Move folgt, nächster Zug
                nextTurnInGame();
            }
        }
    }

    private boolean specialMoves() {
        int cardNumber = game.getCardOnTable().getCardNumber();
        switch (cardNumber) {
            case 8: 
                specialMove8 = true;
                warningLabel.setText("WÄHLEN SIE EINE KARTE ZUM ANSEHEN");
                warningLabel.setVisible(true);

                return true;
            case 9: 
                specialMove9 = true;
                nextPlayerPane.setVisible(true);
                warningLabel.setText("WÄHLEN SIE EINE KARTE ZUM ANSEHEN");
                warningLabel.setVisible(true);
                return true;
            case 10:  
                specialMove10 = true;
                warningLabel.setText("WÄHLEN SIE EINE KARTE ZUM TAUSCHEN");
                warningLabel.setVisible(true);
                return false;
            default:
                return false;
        }
    }

    private void cardThrow(int cardNumber) {
        if (game.getCardOnTable().getCardNumber() == game.getCurrentPlayer().getCards().get(cardNumber - 1).getCardNumber()) {
            refreshCardOnTable(game.getCurrentPlayer().getCards().get(cardNumber - 1));
            game.getCurrentPlayer().getCards().set(cardNumber - 1, new Card("remove", -1)); //Um nach Remove kein OutOfBound zu bekommen
            disableCardVisibility(cardNumber - 1);
            refreshCardVisibility();
            throwSameCard = false;
            endThrowButton.setVisible(true);
        } // else +1 Card Penalty Case 
        else {
            game.getCurrentPlayer().getCards().add(new Card("penalty", 5)); // 5 Strafpunkte
        }
    }

    private void resetCardVisibility() {
        boolean visibility[] = {true, true, true, true};
        game.getPlayerList().forEach((player) -> {
            player.setCardVisibility(visibility);
        });
    }

    private void disableCardVisibility(int index) { //Logik zur verdeckten Kartenansicht
        boolean visibility[] = game.getCurrentPlayer().getCardVisibility();
        visibility[index] = false;
        game.getCurrentPlayer().setCardVisibility(visibility);
    }

    private void refreshCardVisibility() {
        boolean visibility[] = game.getCurrentPlayer().getCardVisibility();

        card1.setVisible(visibility[0]);
        card2.setVisible(visibility[1]);
        card3.setVisible(visibility[2]);
        card4.setVisible(visibility[3]);
    }

    private void enableCardVisibility(int index) {  //Logik zu offenen Kartenansicht
        boolean visibility[] = game.getCurrentPlayer().getCardVisibility();
        visibility[index] = true;
        game.getCurrentPlayer().setCardVisibility(visibility);
    }

    @FXML
    void handleContinueButtonAction(ActionEvent event) {
        List<Player> nextRoundPlayers = new ArrayList();
        game.getPlayerList().stream().filter((player) -> (scoreBoard.get(player) < 100)).forEachOrdered((player) -> {
            nextRoundPlayers.add(player);
        });
        if (nextRoundPlayers.size() > 1) {  //Bis 1 Spieler übrig bleibt
            game = new Game(nextRoundPlayers);
            resetGame();
            firstRound();
            System.out.println("Nächste Runde...");
        } else {
            warningLabel.setText("GLÜCKWUNSCH PLAYER " + nextRoundPlayers.get(0).getId());
            warningLabel.setVisible(true);
        }

    }

    @FXML
    void handleEndGameButtonAction(ActionEvent event) {
        System.out.println("Runde beenden geklickt...");
        remainingEndGame = playerCount() - 1;  //Um zu wissen wv Gesamtzüge verbleiben
        gameEnder = game.getCurrentPlayer();  //Spieler, der die Runde beendet
        nextTurnInGame();
    }

    private List<Integer> calculatePlayersScore() {
        int gameEnderIndex = -1;
        List<Integer> scoreList = new ArrayList();
        List<Player> playerList = game.getPlayerList();
        for (int i = 0; i < game.getPlayerList().size(); i++) {
            scoreList.add(playerList.get(i).getScore());
            if (playerList.get(i).getId() == gameEnder.getId()) {
                gameEnderIndex = i;
            }
        }

        int minScore = Collections.min(scoreList);

        if (minScore < gameEnder.getScore() && gameEnderIndex != -1) {
            scoreList.set(gameEnderIndex, 25);
        } else {
            int times = Collections.frequency(scoreList, minScore);
            if (times > 1 && gameEnderIndex != -1) {  
                scoreList.set(gameEnderIndex, 25);
            } else if (gameEnderIndex != -1) {
                scoreList.set(gameEnderIndex, 0);
            }
        }
        return scoreList;
    }

    private List<Integer> scoreBoardFiller(List<Player> player, List<Integer> scoreList) {
        List<Integer> updatedScoreList = new ArrayList();
        for (int i = 0; i < player.size(); i++) {
            if (scoreBoard.get(player.get(i)) != null) {
                updatedScoreList.add(scoreBoard.get(player.get(i)) + scoreList.get(i));
                scoreBoard.put(player.get(i), scoreBoard.get(player.get(i)) + scoreList.get(i));
            } else {
                updatedScoreList.add(scoreList.get(i));
                scoreBoard.put(player.get(i), scoreList.get(i));
            }
        }
        return updatedScoreList;
    }

    private void endGame() {
        scoreBoardPane.setVisible(true);
        int playerCount = game.getPlayerList().size();
        List<Integer> scoreList = calculatePlayersScore();
        scoreList = scoreBoardFiller(game.getPlayerList(), scoreList);

        switch (playerCount) {
            case 1:
                score1.setText("" + scoreList.get(0));
                break;
            case 2:
                score1.setText("" + scoreList.get(0));
                score2.setText("" + scoreList.get(1));
                break;
            case 3:
                score1.setText("" + scoreList.get(0));
                score2.setText("" + scoreList.get(1));
                score3.setText("" + scoreList.get(2));
                break;
            case 4:
                score1.setText("" + scoreList.get(0));
                score2.setText("" + scoreList.get(1));
                score3.setText("" + scoreList.get(2));
                score4.setText("" + scoreList.get(3));
                break;
            default:
                break;
        }
    }

    @FXML
    void handleThrowButtonAction(ActionEvent event) {
        warningLabel.setVisible(false);
        refreshCardOnTable(selectedCard);
        drawedCard.setVisible(false);
        throwButton.setVisible(false);
        isCardSelect = false;
        if (!specialMoves()) {
            nextTurnInGame();
        }
    }

    @FXML
    void handleCard1Action(MouseEvent event) {
        if (specialMove8) {
            specialMoveEight(1);
            specialMove8 = false;
        } else if (specialMove10) {
            prepareSpecialMoveTen(1);
        } else if (!throwSameCard) {
            cardExchange(1);
        } else {
            cardThrow(1);
        }

    }

    @FXML
    void handleCard2Action(MouseEvent event) {
        if (specialMove8) {
            specialMoveEight(2);
            specialMove8 = false;
        } else if (specialMove10) {
            prepareSpecialMoveTen(2);
        } else if (!throwSameCard) {
            cardExchange(2);
        } else {
            cardThrow(2);
        }
    }

    @FXML
    void handleCard3Action(MouseEvent event) {
        if (specialMove8) {
            specialMoveEight(3);
            specialMove8 = false;
        } else if (specialMove10) {
            prepareSpecialMoveTen(3);
        } else if (!throwSameCard) {
            cardExchange(3);
        } else {
            cardThrow(3);
        }
    }

    @FXML
    void handleCard4Action(MouseEvent event) {
        if (specialMove8) {
            specialMoveEight(4);
            specialMove8 = false;
        } else if (specialMove10) {
            prepareSpecialMoveTen(4);
        } else if (!throwSameCard) {
            cardExchange(4);
        } else {
            cardThrow(4);
        }
    }

    @FXML
    void handleEndThrowButtonAction(ActionEvent event) {
        nextTurnInGame();
        endThrowButton.setVisible(false);
    }

    @FXML
    void handleRemainingDeckAction(MouseEvent event) {
        if (!isCardSelect) {
            warningLabel.setVisible(true);
            selectedCard = game.getDeck().drawCard();
            if (selectedCard == null) {
                endGame();
            }
            drawedCard.setImage(selectedCard.getCardImage());
            drawedCard.setVisible(true);
            throwButton.setVisible(true);
            isCardSelect = true;
        }
    }

    @FXML
    void handleCardOnTableAction(MouseEvent event) {
        warningLabel.setVisible(true);
        selectedCard = game.getCardOnTable();
        isCardSelect = true;
    }

    private int playerCount() {
        if (radioButton2.isSelected()) {
            return 2;
        } else if (radioButton3.isSelected()) {
            return 3;
        } else if (radioButton4.isSelected()) {
            return 4;
        } else {
            return 0;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
