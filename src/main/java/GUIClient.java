import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

public class GUIClient extends Application {
    /* List of colors and the components that use them:
     * Light-Blue: option buttons, ipAddressText background, portNumberText background, totalWinnings background
     * Hisoka-white: title, playButtonText text color
     * Dark-Blue: playbutton background color
     * Light-Pink: betBox background color
     * Medium-Blue: gameplay background
     */
    final String[] blueTheme = new String[]{
            "#B8DADB",
            "#E6E6E6",
            "#40999D",
            "#FFB1BF",
            "#4EBFC3"
    };

    /* Light-Pink: optionsButtons, ipAddressTextBG, portNumberTextBG, totalWinningsBG
     * Hisoka-Black: title, playbutton text color
     * Hisoka-Hair Pink :play button
     * Light Blue :bet-card
     * Medium Pink: gameplay background*/
    final String[] pinkTheme = new String[]{
            "#EBDBDE",
            "#010101",
            "#E64D69",
            "#4EBFC3",
            "#FFB1BF"
    };


    // Welcome components
    BorderPane welcomePane;
    TextField ipAddressText;
    TextField portNumText;
    Button playButton;
    Button optionsButton1;
    Text gameTitle;
    String welcomeBackgroundLink = "src/main/resources/Backgrounds/hisokaStart1.jpg";


    // Gameplay components
    Button dealButton;
    Button foldButton;
    Button submitButton;
    Button optionButton2;
    Text totalWinningInfo;
    BorderPane gamePane;
    ListView<String> gameInfo;
    TextField pairPlusNum, anteWageNum, playWagerNum;
    StackPane totalWinningInfoFlow;
    Rectangle dealerCard1, dealerCard2, dealerCard3;
    Rectangle playerCard1, playerCard2, playerCard3;
    Rectangle deckCard;
    VBox betBox;


    // Menu components
    Button freshStartButton;
    Button newLookButton;
    Button exitGameButton;
    Button closeOptionsButton;
    Text optionsTitle;


    // End components
    BorderPane endPane;
    Text endingText;
    Text endTotalWinningsText;
    Button endContinueButton;
    Button endExitButton;
    boolean wonCurrentGame = false;


    // New-look components
    Text newLookTitle;
    Text newLookDescription;
    Button blueModeButton;
    Button pinkModeButton;
    Button closeNewLookButton;

    // Helper variables
    boolean gameHasBegun = false;
    boolean havePlacedBets = false;
    HashMap<String, Scene> sceneMap;
    boolean blueModeTurnedOn = true;
    Client clientConnection;
    PokerInfo pokerInfo;
    PauseTransition pause = new PauseTransition(Duration.seconds(12));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Welcome to Poker");
        pokerInfo = new PokerInfo();

        pause.setOnFinished(event -> {
            // Switch to the ending scene
            primaryStage.setScene(sceneMap.get("endScreen"));
        });

        // Initalize all welcome components
        gameTitle = new Text("POKER");
        ipAddressText = new TextField("");
        ipAddressText.setPromptText("ENTER IP ADDRESS");
        portNumText = new TextField("");
        portNumText.setPromptText("ENTER PORT NUMBER");
        playButton = new Button("PLAY");
        optionsButton1 = new Button("OPTIONS");

        //  Initialize all gameplay components
        optionButton2 = new Button("OPTIONS");
        totalWinningInfo = new Text("Total Winnings:\n$ XXXXX");
        pairPlusNum = new TextField();
        anteWageNum = new TextField();
        playWagerNum = new TextField();
        gameInfo = new ListView<>();
        dealButton = new Button("DEAL");
        foldButton = new Button("FOLD");
        submitButton = new Button("SUBMIT");
        gamePane = new BorderPane();
        dealButton.setDisable(true);
        foldButton.setDisable(true);

        // Textfields can only accept integers
        TextFormatter<Integer> formatter1 = new TextFormatter<>(new IntegerStringConverter(), 1, c -> Pattern.matches("\\d*", c.getText()) ? c : null); // copied code from https://stackoverflow.com/a/36749659
        TextFormatter<Integer> formatter2 = new TextFormatter<>(new IntegerStringConverter(), 1, c -> Pattern.matches("\\d*", c.getText()) ? c : null);
        pairPlusNum.setTextFormatter(formatter1);
        anteWageNum.setTextFormatter(formatter2);


        //  Initialize all  menu components
        freshStartButton = new Button("Fresh Start");
        newLookButton = new Button("New Look");
        exitGameButton = new Button("Exit the Game");
        optionsTitle = new Text("OPTIONS");
        closeOptionsButton = new Button("X");

        //  Initialize all end components
        endingText = new Text("");
        endTotalWinningsText = new Text("");
        endContinueButton = new Button("Continue");
        endExitButton = new Button("Exit Game");

        //  Initialize all new-look components
        newLookTitle = new Text("NEW L\uD83D\uDC40K");
        newLookDescription = new Text("Which theme do you prefer?");
        blueModeButton = new Button();
        pinkModeButton = new Button();
        closeNewLookButton = new Button("X");

        // Style all welcome components
        gameTitle.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 120 Garamond;");
        ipAddressText.setStyle("-fx-background-color: #B8DADB; -fx-fill: #010101;  -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        portNumText.setStyle("-fx-background-color: #B8DADB; -fx-fill: #010101;  -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        playButton.setStyle("-fx-background-color: #40999D; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionsButton1.setStyle("-fx-background-color: #B8DADB; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");

        // Style game components
        optionButton2.setStyle("-fx-background-color: #B8DADB; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        dealButton.setStyle("-fx-background-color: #A2C255; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 200px;  -fx-font: bold 22 Calibri; ");
        foldButton.setStyle("-fx-background-color: #E64D69; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 200px;  -fx-font: bold 22 Calibri;");
        submitButton.setStyle("-fx-background-color: #E64D69; -fx-text-fill: #FFFFFF; -fx-pref-height: 13px; -fx-pref-width: 200px;  -fx-font: bold 18 Calibri;");
        gameInfo.setStyle("-fx-background-color: #FDFB97; -fx-min-height: 250px; -fx-max-height: 250px; -fx-background-radius: 5;");
        totalWinningInfo.setStyle("-fx-font: bold 26 Calibri;");
        totalWinningInfo.setTextAlignment(TextAlignment.CENTER);
        pairPlusNum.setAlignment(Pos.CENTER);
        anteWageNum.setAlignment(Pos.CENTER);
        playWagerNum.setAlignment(Pos.CENTER);
        playWagerNum.setDisable(true);

        // Disable until client is connected
        anteWageNum.setDisable(true);
        pairPlusNum.setDisable(true);
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        submitButton.setDisable(true);

        // Disable until the client reaches to the gameplay
        freshStartButton.setDisable(true);

        // Style all the menu components
        optionsTitle.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 100 Garamond;");
        freshStartButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
        newLookButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
        exitGameButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
        closeOptionsButton.setStyle("-fx-background-color: #2D9BF0; -fx-text-fill:#E6E6E6; -fx-font: bold 50 Garamond;");

        // Style end components
        endingText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #010101; -fx-text-alignment: center;");
        endTotalWinningsText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #010101; -fx-text-alignment: center;");
        endContinueButton.setStyle("-fx-background-color: #A2C255; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 200px;  -fx-font: bold 24 Calibri; ");
        endExitButton.setStyle("-fx-background-color: #E64D69; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 200px;  -fx-font: bold 24 Calibri; ");

        // Style new-look components
        newLookTitle.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 100 Garamond;");
        newLookDescription.setStyle("-fx-background-color:#1A1A1A; -fx-fill: #FFFFFF;-fx-font: bold 24 Calibri; ");
        blueModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#A2C255;-fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
        pinkModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#E6E6E6; -fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
        closeNewLookButton.setStyle("-fx-background-color: #BE83B1; -fx-text-fill:#E6E6E6; -fx-font: bold 50 Garamond;");

        // Welcome screen event handlers
        playButton.setOnAction(e -> {

            // Switch to gamePlay screen
            primaryStage.setScene(sceneMap.get("gameplay"));
            gameHasBegun = true;

            // Create a client socket
            clientConnection = new Client(data -> {
                Platform.runLater(() -> {

                    // Update listview and this class's global object
                    pokerInfo = (PokerInfo) data;
                    gameInfo.getItems().add("Server: " + pokerInfo.message);

                    // Ensure the client has connected to the server
                    if (pokerInfo.hasConnectedToServerFirstTime) {
                        pairPlusNum.setDisable(false);
                        anteWageNum.setDisable(false);
                        submitButton.setDisable(false);
                        freshStartButton.setDisable(false);
                        totalWinningInfo.setText("Total Winnings:\n$ 0");
                        pokerInfo.hasConnectedToServerFirstTime = false;
                    }

                    // Update cards if bets have been made
                    if (havePlacedBets) {
                        playerCard1 = changeCard(playerCard1, pokerInfo.playerCards.get(0));
                        playerCard2 = changeCard(playerCard2, pokerInfo.playerCards.get(1));
                        playerCard3 = changeCard(playerCard3, pokerInfo.playerCards.get(2));
                    }

                    // Display results if Server requests to
                    if (pokerInfo.message.equals("Showing dealer cards and calculating results...")) {
                        // Go to end screen
                        endScreenHelper();
                        dealerCard1 = changeCard(dealerCard1, pokerInfo.dealerCards.get(0));
                        dealerCard2 = changeCard(dealerCard2, pokerInfo.dealerCards.get(1));
                        dealerCard3 = changeCard(dealerCard3, pokerInfo.dealerCards.get(2));

                        // Display results
                        if (pokerInfo.winner.equals("Dealer Do Not Qualify")) {
                            gameInfo.getItems().add("Results: The dealer do not have a Queen or higher. So here are your ante bet and play bet money.");
                        } else if (pokerInfo.winner.equals("Draw")) {
                            gameInfo.getItems().add("Results: There is a draw. No one wins!");
                        } else if (pokerInfo.winner.equals("Player")) {
                            gameInfo.getItems().add("Results: Congratulations! You have won against the dealer.");
                        } else if (pokerInfo.winner.equals("Dealer")) {
                            gameInfo.getItems().add("Results: You lost against the dealer. Better luck next time!");
                        }
                        totalWinningInfo.setText("Total Winnings:\n$" + pokerInfo.totalGameMoney);

                        // Change comment depending on results
                        if (Integer.valueOf(pokerInfo.pairPlusEarnings) > 0) {
                            gameInfo.getItems().add("You won the Pair Plus!");
                        } else {
                            gameInfo.getItems().add("You lost the Pair Plus!");
                        }

                        sceneMap.put("endScreen", createEndScreen());
                        pause.play();

                    } else if (pokerInfo.message.equals("Game over. Going to win/lose screen...")) {
                        totalWinningInfo.setText("Total Winnings:\n$" + pokerInfo.totalGameMoney);
                        // Change comment depending on results
                        if (Integer.valueOf(pokerInfo.pairPlusEarnings) > 0) {
                            gameInfo.getItems().add("You won the Pair Plus!");
                        } else {
                            gameInfo.getItems().add("You lost the Pair Plus!");
                        }
                        // Go to end screen
                        endScreenHelper();
                        sceneMap.put("endScreen", createEndScreen());
                        pause.play();

                    } else if (pokerInfo.message.equals("No problem! Starting new Game!")) {
                        // Restart the Game
                        freshStartHelper();
                        primaryStage.setScene(sceneMap.get("gameplay")); // Switch to the ending scene
                    }
                });
            }, ipAddressText.getText(), Integer.valueOf(portNumText.getText()));

            // Begin the client connection
            clientConnection.start();
        });

        // Menu screen event handlers
        optionsButton1.setOnAction(e -> primaryStage.setScene(sceneMap.get("menuScreen")));
        newLookButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("newLookScreen")));

        closeOptionsButton.setOnAction(e -> {
            if (gameHasBegun) {
                primaryStage.setScene(sceneMap.get("gameplay"));
            } else {
                primaryStage.setScene(sceneMap.get("welcomeScreen"));
            }
        });
        freshStartButton.setOnAction(e -> {
            pokerInfo.message = "The client have restarted the game.";
            clientConnection.send(pokerInfo);
        });
        exitGameButton.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        // New-look screen event handlers
        closeNewLookButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("menuScreen")));
        pinkModeButton.setOnAction(e -> applyNewLook(pinkTheme));
        blueModeButton.setOnAction(e -> applyNewLook(blueTheme));

        // End screen event handlers
        endExitButton.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        endContinueButton.setOnAction(e -> {
            pokerInfo.message = "I will continue playing";
            pokerInfo.roundWinnings = 0;
            clientConnection.send(pokerInfo);
            havePlacedBets = false;

            pairPlusNum.setDisable(false);
            submitButton.setDisable(false);
            pairPlusNum.setText("");
            playWagerNum.setText("");

            if (pokerInfo.winner.equals("Draw")) {
                anteWageNum.setDisable(false);
                anteWageNum.setText("");
            } else if (pokerInfo.winner.equals("Player")) {
                anteWageNum.setDisable(false);
                anteWageNum.setText("");
            } else if (pokerInfo.winner.equals("Dealer") || pokerInfo.winner.equals("Fold")) {
                anteWageNum.setDisable(false);
                anteWageNum.setText("");
            }


            // Change the cards to the back (change based on styles)
            if (blueModeTurnedOn) {
                dealerCard1 = changeCard(dealerCard1, "src/main/resources/Cards/back-card1.png");
                dealerCard2 = changeCard(dealerCard2, "src/main/resources/Cards/back-card1.png");
                dealerCard3 = changeCard(dealerCard3, "src/main/resources/Cards/back-card1.png");
                playerCard1 = changeCard(playerCard1, "src/main/resources/Cards/back-card1.png");
                playerCard2 = changeCard(playerCard2, "src/main/resources/Cards/back-card1.png");
                playerCard3 = changeCard(playerCard3, "src/main/resources/Cards/back-card1.png");
            } else {
                dealerCard1 = changeCard(dealerCard1, "src/main/resources/Cards/back-card2.png");
                dealerCard2 = changeCard(dealerCard2, "src/main/resources/Cards/back-card2.png");
                dealerCard3 = changeCard(dealerCard3, "src/main/resources/Cards/back-card2.png");
                playerCard1 = changeCard(playerCard1, "src/main/resources/Cards/back-card2.png");
                playerCard2 = changeCard(playerCard2, "src/main/resources/Cards/back-card2.png");
                playerCard3 = changeCard(playerCard3, "src/main/resources/Cards/back-card2.png");
            }

            primaryStage.setScene(sceneMap.get("gameplay")); // Switch to the ending scene
        });

        // Gameplay screen event handlers
        submitButton.setOnAction(e -> {
            // check if the pairplus and ante bets are betweeen 5 and 25 bucks
            int pairPlusBetVal = Integer.valueOf(pairPlusNum.getText());
            int anteBetVal = Integer.valueOf(anteWageNum.getText());
            if (((pairPlusBetVal >= 5 && pairPlusBetVal <= 25) || pairPlusBetVal == 0) && anteBetVal >= 5 && anteBetVal <= 25) {
                // everything in here
                pokerInfo.anteBet = anteBetVal;
                pokerInfo.pairPlusBet = pairPlusBetVal;
                pokerInfo.pairPlusBet = pairPlusBetVal;
                pokerInfo.message = "I have made my bets";
                havePlacedBets = true;
                clientConnection.send(pokerInfo); // send server info here
                anteWageNum.setDisable(true);
                pairPlusNum.setDisable(true);
                submitButton.setDisable(true);
                dealButton.setDisable(false);
                foldButton.setDisable(false);

                // Change the player cards
            } else if (anteBetVal < 5 || pairPlusBetVal < 5) {
                gameInfo.getItems().add("You made one of your bets too low. Make sure it's at least $5.");
            } else if (anteBetVal > 25 || pairPlusBetVal > 25) {
                gameInfo.getItems().add("You made one of your bets too high. Make sure it's at most $25");
            }
        });

        dealButton.setOnAction(e -> {
            pokerInfo.message = "I want to deal!";
            clientConnection.send(pokerInfo);
            dealButton.setDisable(true);
            foldButton.setDisable(true);
            playWagerNum.setText(anteWageNum.getText());
        });

        foldButton.setOnAction(e -> {
            pokerInfo.message = "I want to fold!";
            clientConnection.send(pokerInfo);
            foldButton.setDisable(true);
            dealButton.setDisable(true);
            playWagerNum.setText("0");
        });

        optionButton2.setOnAction(e -> primaryStage.setScene(sceneMap.get("menuScreen")));

        // Put scenes into a hashmap
        sceneMap = new HashMap<>();
        sceneMap.put("welcomeScreen", createWelcomeScreen());
        sceneMap.put("gameplay", createGameScene());
        sceneMap.put("endScreen", createEndScreen());
        sceneMap.put("newLookScreen", createNewLookScreen());
        sceneMap.put("menuScreen", createMenuScreen());

        // Close clients after application closes
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        // Begin with screen
        primaryStage.setScene(sceneMap.get("welcomeScreen"));
        primaryStage.show();
    }

    Scene createWelcomeScreen() {
        welcomePane = new BorderPane();

        // Set background image
        try {
            welcomePane.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream("src/main/resources/Backgrounds/hisokaStart1.jpg")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
        } catch (Exception e) {
            System.out.println("Unable to load the background image for the intro scene");
        }

        // Arrange all components
        HBox justOneMenuBtn = new HBox(optionsButton1);
        HBox.setMargin(optionsButton1, new Insets(20, 20, 20, 20));
        VBox login = new VBox(gameTitle, ipAddressText, portNumText, playButton);
        VBox.setMargin(gameTitle, new Insets(30, 30, 5, 30));
        VBox.setMargin(ipAddressText, new Insets(5, 30, 0, 30));
        VBox.setMargin(portNumText, new Insets(5, 30, 0, 30));
        VBox.setMargin(playButton, new Insets(10, 30, 0, 30));
        welcomePane.setRight(login);
        welcomePane.setTop(justOneMenuBtn);
        login.setAlignment(Pos.CENTER);
        justOneMenuBtn.setAlignment(Pos.TOP_RIGHT);

        return new Scene(welcomePane, 1400, 790);
    }

    Scene createGameScene() {
        gamePane.setStyle("-fx-background-color: #4EBFC3;");

        // Right Hand Side of gameplay screen
        totalWinningInfoFlow = new StackPane(totalWinningInfo);
        totalWinningInfoFlow.setStyle("-fx-background-color: #B8DADB; -fx-min-width: 500; -fx-min-height: 85; -fx-background-radius: 5;");

        // Style bet card
        Text betTitle = new Text("MAKE YOUR BET!");
        betTitle.setStyle("-fx-font: bold 34 Calibri;");
        Text betDescription = new Text("Make your bet wagers between 5 and 25 bucks.\nEnter 0 if you do not want to bet on the pair plus.");
        betDescription.setStyle("-fx-font: 16 Calibri;");

        // Style pair-plus, ante, and play wager titles
        Text pairPlusTitle = new Text("PAIR PLUS");
        pairPlusTitle.setStyle("-fx-font: bold 16 Calibri;");
        VBox pairPlusBet = new VBox(2, pairPlusTitle, pairPlusNum);
        pairPlusBet.setAlignment(Pos.CENTER);

        Text anteTitle = new Text("ANTE");
        anteTitle.setStyle("-fx-font: bold 16 Calibri;");
        VBox anteWageBet = new VBox(2, anteTitle, anteWageNum);
        anteWageBet.setAlignment(Pos.CENTER);

        Text playTitle = new Text("PLAY");
        playTitle.setStyle("-fx-font: bold 16 Calibri;");
        VBox playWageBet = new VBox(2, playTitle, playWagerNum);
        playWageBet.setAlignment(Pos.CENTER);

        // Arrange the bet boxes
        HBox betTextFields = new HBox(8, pairPlusBet, anteWageBet, playWageBet);
        betTextFields.setAlignment(Pos.CENTER);
        betBox = new VBox(10, betTitle, betDescription, betTextFields, submitButton);
        betBox.setStyle("-fx-background-color:#FFB1BF; -fx-background-radius: 5;"); // TODO: Change the hexcode
        betBox.setAlignment(Pos.CENTER);
        betBox.setPadding(new Insets(10));

        VBox paneRight = new VBox(20, totalWinningInfoFlow, betBox, gameInfo);
        paneRight.setAlignment(Pos.CENTER);

        // Dealer Hand Cards
        Text dealerTitle = new Text("DEALER");
        dealerTitle.setStyle("-fx-font: bold 18 Calibri;");
        dealerCard1 = createCard("src/main/resources/Cards/back-card1.png");
        dealerCard2 = createCard("src/main/resources/Cards/back-card1.png");
        dealerCard3 = createCard("src/main/resources/Cards/back-card1.png");

        // Player Hand Cards
        Text playerTitle = new Text("PLAYER (YOU)");
        playerTitle.setStyle("-fx-font: bold 18 Calibri;");
        playerCard1 = createCard("src/main/resources/Cards/back-card1.png");
        playerCard2 = createCard("src/main/resources/Cards/back-card1.png");
        playerCard3 = createCard("src/main/resources/Cards/back-card1.png");

        // Deck of Card
        deckCard = createCard("src/main/resources/Cards/back-card1.png");

        // Arrange cards, buttons, info boxes on screen
        HBox dealerHand = new HBox(15, dealerCard1, dealerCard2, dealerCard3);
        dealerHand.setAlignment(Pos.CENTER);

        VBox dealer = new VBox(15, dealerTitle, dealerHand);
        dealer.setAlignment(Pos.CENTER);

        HBox playerHand = new HBox(15, playerCard1, playerCard2, playerCard3);
        playerHand.setAlignment(Pos.CENTER);

        VBox player = new VBox(15, playerHand, playerTitle);
        player.setAlignment(Pos.CENTER);

        HBox foldOrDeal = new HBox(20, dealButton, foldButton);
        foldOrDeal.setAlignment(Pos.CENTER);

        HBox paneTop = new HBox(optionButton2);
        VBox gameBox = new VBox(25, dealer, foldOrDeal, player);
        HBox paneCenter = new HBox(70, deckCard, gameBox, paneRight);

        HBox.setMargin(optionButton2, new Insets(20));
        gameBox.setAlignment(Pos.CENTER);
        paneTop.setAlignment(Pos.TOP_RIGHT);
        paneCenter.setAlignment(Pos.CENTER);

        gamePane.setCenter(paneCenter);
        gamePane.setTop(paneTop);

        return new Scene(gamePane, 1400, 750);
    }

    Rectangle createCard(String cardImagePath) {
        // Create a box for a poker card
        Rectangle card = new Rectangle(0, 0, 178, 250);
        card.setArcWidth(20);
        card.setArcHeight(20);

        try {
            ImagePattern pattern = new ImagePattern(new Image(new FileInputStream(cardImagePath)), 0, 0, 178, 250, false); // position x, position y, width, height
            card.setFill(pattern);
            card.setStyle("-fx-background-radius: 5;");
        } catch (Exception e) {
            System.out.println("Image unable to get");
        }

        return card;
    }

    Rectangle changeCard(Rectangle card, String cardImagePath) {
        // Update a card with a given image path
        try {
            ImagePattern pattern = new ImagePattern(new Image(new FileInputStream(cardImagePath)), 0, 0, 178, 250, false); // position x, position y, width, height
            card.setFill(pattern);
            card.setStyle("-fx-background-radius: 5;");
        } catch (Exception e) {
            System.out.println("Image unable to get");
        }

        return card;

    }

    Scene createEndScreen() {
        endPane = new BorderPane();
        // set up background
        Image hisoka1;
        try {
            if (wonCurrentGame) {
                hisoka1 = new Image(new FileInputStream("src/main/resources/Backgrounds/hisokaEnd1.jpg"));
                endingText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #010101; -fx-text-alignment: center;");
                endTotalWinningsText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #010101; -fx-text-alignment: center;");
            } else {
                hisoka1 = new Image(new FileInputStream("src/main/resources/Backgrounds/hisokaEnd2.jpg"));
                endingText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #E6E6E6; -fx-text-alignment: center;");
                endTotalWinningsText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #E6E6E6; -fx-text-alignment: center;");
            }
            endPane.setBackground(new Background(new BackgroundImage(hisoka1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
        } catch (Exception e) {
            System.out.println("Unable to load the background image for the ending scene");
        }

        // assemble components
        HBox contORexit = new HBox(endContinueButton, endExitButton);
        VBox finalResults = new VBox(endingText, contORexit, endTotalWinningsText);

        HBox.setMargin(endContinueButton, new Insets(10, 10, 20, 10));
        HBox.setMargin(endExitButton, new Insets(10, 10, 20, 10));
        VBox.setMargin(endingText, new Insets(20, 50, 20, 20));
        VBox.setMargin(endTotalWinningsText, new Insets(20, 50, 20, 20));

        contORexit.setAlignment(Pos.CENTER);
        finalResults.setAlignment(Pos.CENTER);
        endPane.setRight(finalResults);

        return new Scene(endPane, 1400, 790);
    }

    Scene createNewLookScreen() {
        BorderPane pane = new BorderPane();

        try {
            // set up background
            Image newLookBg = new Image(new FileInputStream("src/main/resources/Backgrounds/newLookBg.jpg"));
            Image blueModePic = new Image(new FileInputStream("src/main/resources/NewLookButtonBGs/blueMode.jpg"));
            Image pinkModePic = new Image(new FileInputStream("src/main/resources/NewLookButtonBGs/pinkMode.jpg"));
            pane.setBackground(new Background(new BackgroundImage(newLookBg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));

            // add images for buttons' background
            ImageView pinkView = new ImageView(pinkModePic);
            ImageView blueView = new ImageView(blueModePic);
            pinkView.setPreserveRatio(true);
            pinkView.setFitHeight(300);
            blueView.setPreserveRatio(true);
            blueView.setFitHeight(300);
            pinkModeButton.setGraphic(pinkView);
            blueModeButton.setGraphic(blueView);
        } catch (Exception e) {
            System.out.println("Unable to load images for the look choices");
        }

        // arrange components
        HBox lookButtonsBox = new HBox(blueModeButton, pinkModeButton);
        VBox newLookBox = new VBox(newLookTitle, newLookDescription, lookButtonsBox);

        VBox.setMargin(newLookTitle, new Insets(30, 0, 0, 0));
        VBox.setMargin(newLookDescription, new Insets(30, 0, 0, 0));
        HBox.setMargin(blueModeButton, new Insets(30, 30, 0, 30));
        HBox.setMargin(pinkModeButton, new Insets(30, 30, 0, 30));

        HBox justOneExitBtn = new HBox(closeNewLookButton);
        pane.setTop(justOneExitBtn);
        justOneExitBtn.setAlignment(Pos.TOP_RIGHT);

        lookButtonsBox.setAlignment(Pos.CENTER);
        newLookBox.setAlignment(Pos.CENTER);
        pane.setCenter(newLookBox);

        return new Scene(pane, 1400, 790);
    }

    Scene createMenuScreen() {
        BorderPane pane = new BorderPane();

        // set up background
        pane.setStyle("-fx-background-color: #2D9BF0");

        // arrange components
        VBox allOptions = new VBox(optionsTitle, freshStartButton, newLookButton, exitGameButton);
        VBox.setMargin(optionsTitle, new Insets(30, 30, 5, 30));
        VBox.setMargin(freshStartButton, new Insets(5, 30, 0, 30));
        VBox.setMargin(newLookButton, new Insets(5, 30, 0, 30));
        VBox.setMargin(exitGameButton, new Insets(5, 30, 0, 30));
        allOptions.setAlignment(Pos.CENTER);
        pane.setCenter(allOptions);

        HBox justOneExitBtn = new HBox(closeOptionsButton);
        pane.setTop(justOneExitBtn);
        justOneExitBtn.setAlignment(Pos.TOP_RIGHT);

        return new Scene(pane, 1400, 790);
    }


    public void applyNewLook(String[] themeStyle) {
        // Switch the color mode
        blueModeTurnedOn = (!blueModeTurnedOn);

        // Update mode buttons, welcome background, and the back of the cards
        if (blueModeTurnedOn) {
            welcomeBackgroundLink = "src/main/resources/Backgrounds/hisokaStart1.jpg";
            blueModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#A2C255;-fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
            pinkModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#E6E6E6; -fx-pref-width: 300px;-fx-font: bold 24 Calibri;");

            // Change the back of the cards
            deckCard = changeCard(deckCard, "src/main/resources/Cards/back-card1.png");

            if (!havePlacedBets) {
                playerCard1 = changeCard(playerCard1, "src/main/resources/Cards/back-card1.png");
                playerCard2 = changeCard(playerCard2, "src/main/resources/Cards/back-card1.png");
                playerCard3 = changeCard(playerCard3, "src/main/resources/Cards/back-card1.png");
            }

            dealerCard1 = changeCard(dealerCard1, "src/main/resources/Cards/back-card1.png");
            dealerCard2 = changeCard(dealerCard2, "src/main/resources/Cards/back-card1.png");
            dealerCard3 = changeCard(dealerCard3, "src/main/resources/Cards/back-card1.png");

        } else {
            welcomeBackgroundLink = "src/main/resources/Backgrounds/hisokaStart2.jpg";
            blueModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#E6E6E6;-fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
            pinkModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#A2C255; -fx-pref-width: 300px;-fx-font: bold 24 Calibri;");

            // Change the back of the cards
            deckCard = changeCard(deckCard, "src/main/resources/Cards/back-card2.png");

            if (!havePlacedBets) {
                playerCard1 = changeCard(playerCard1, "src/main/resources/Cards/back-card2.png");
                playerCard2 = changeCard(playerCard2, "src/main/resources/Cards/back-card2.png");
                playerCard3 = changeCard(playerCard3, "src/main/resources/Cards/back-card2.png");
            }

            dealerCard1 = changeCard(dealerCard1, "src/main/resources/Cards/back-card2.png");
            dealerCard2 = changeCard(dealerCard2, "src/main/resources/Cards/back-card2.png");
            dealerCard3 = changeCard(dealerCard3, "src/main/resources/Cards/back-card2.png");
        }

        // Set new welcome background
        try {
            welcomePane.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(welcomeBackgroundLink)), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
        } catch (Exception e) {
            System.out.println("Unable to change the background image to the new look");
        }

        // update welcome components
        gameTitle.setStyle("-fx-fill:" + themeStyle[1] + ";-fx-font: bold 120 Garamond;");
        ipAddressText.setStyle("-fx-background-color:" + themeStyle[0] + "; -fx-fill:" + themeStyle[1] + "; -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        portNumText.setStyle("-fx-background-color:" + themeStyle[0] + "; -fx-fill:" + themeStyle[1] + "; -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        playButton.setStyle("-fx-background-color:" + themeStyle[2] + "; -fx-text-fill:" + themeStyle[1] + "; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionsButton1.setStyle("-fx-background-color: " + themeStyle[0] + "; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionButton2.setStyle("-fx-background-color: " + themeStyle[0] + "; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");

        // update end components
        endingText.setStyle("-fx-background-color: " + themeStyle[0] + "; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        totalWinningInfoFlow.setStyle("-fx-background-color:" + themeStyle[0] + ";-fx-min-width: 500; -fx-min-height: 85; -fx-background-radius: 5;");
        betBox.setStyle("-fx-background-color:" + themeStyle[3] + "; -fx-background-radius: 5;"); // TODO: Change the hexcode
        gamePane.setStyle("-fx-background-color:" + themeStyle[4] + ";"); // TODO: Change the hexcode such that the new look can be applied
    }

    public void endScreenHelper() {
        // Update total winnings
        endTotalWinningsText.setText("TOTAL WINNINGS:\n$ " + pokerInfo.totalGameMoney);

        // Update comments depending on results
        if (pokerInfo.winner.equals("Dealer Do Not Qualify")) {
            wonCurrentGame = false;
            endingText.setText("DEALER DID NOT QUALIFY! YOU HAVE EARNED:\n$" + pokerInfo.roundWinnings);
        } else if (pokerInfo.winner.equals("Draw")) {
            wonCurrentGame = true;
            endingText.setText("IT'S A TIE! YOU HAVE EARNED:\n$" + pokerInfo.roundWinnings);
        } else if (pokerInfo.winner.equals("Player")) {
            wonCurrentGame = true;
            endingText.setText("CONGRATULATIONS WINNER! YOU HAVE EARNED:\n$" + pokerInfo.roundWinnings);
        } else if (pokerInfo.winner.equals("Dealer") || pokerInfo.winner.equals("Fold")) {
            wonCurrentGame = false;
            endingText.setText("BETTER LUCK NEXT TIME LOSER! YOU HAVE EARNED:\n$" + pokerInfo.roundWinnings);
        }
    }


    public void freshStartHelper() {
        // Start new game
        havePlacedBets = false;
        pairPlusNum.setDisable(false);
        anteWageNum.setDisable(false);
        submitButton.setDisable(false);
        dealButton.setDisable(true);
        foldButton.setDisable(true);
        pairPlusNum.setText("");
        playWagerNum.setText("");
        anteWageNum.setText("");
        totalWinningInfo.setText("Total Winnings:\n$ 0");

        // Change the cards to the back (change based on styles)
        if (blueModeTurnedOn) {
            dealerCard1 = changeCard(dealerCard1, "src/main/resources/Cards/back-card1.png");
            dealerCard2 = changeCard(dealerCard2, "src/main/resources/Cards/back-card1.png");
            dealerCard3 = changeCard(dealerCard3, "src/main/resources/Cards/back-card1.png");
            playerCard1 = changeCard(playerCard1, "src/main/resources/Cards/back-card1.png");
            playerCard2 = changeCard(playerCard2, "src/main/resources/Cards/back-card1.png");
            playerCard3 = changeCard(playerCard3, "src/main/resources/Cards/back-card1.png");
        } else {
            dealerCard1 = changeCard(dealerCard1, "src/main/resources/Cards/back-card2.png");
            dealerCard2 = changeCard(dealerCard2, "src/main/resources/Cards/back-card2.png");
            dealerCard3 = changeCard(dealerCard3, "src/main/resources/Cards/back-card2.png");
            playerCard1 = changeCard(playerCard1, "src/main/resources/Cards/back-card2.png");
            playerCard2 = changeCard(playerCard2, "src/main/resources/Cards/back-card2.png");
            playerCard3 = changeCard(playerCard3, "src/main/resources/Cards/back-card2.png");
        }

    }
}