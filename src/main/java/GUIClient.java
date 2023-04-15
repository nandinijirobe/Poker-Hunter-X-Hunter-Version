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
import javafx.util.converter.IntegerStringConverter;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.regex.Pattern;


public class GUIClient extends Application {
    /*
    * light-blue: optionsButtons, ipAddressTextBG, portNumberTextBG, totalWinningsBG
    * hisoka-white: title, playButtonText
    * dark-blue: playbutton
    * light-pink: betBox
    * mid-blue: gameplay background
    *  *

     */
    final String[] blueTheme = new String[]{"#B8DADB", "#E6E6E6", "#40999D", "#FFB1BF", "#4EBFC3"};

    /* light-pink: optionsButtons, ipAddressTextBG, portNumberTextBG, totalWinningsBG
    * histoka-black: title, plabutton text
    * hisoka-hairpink:play button
    * light-blue:bet-card
    * mid-pink: gameplay background*/

    final String[] pinkTheme = new String[]{"#EBDBDE", "#010101", "#E64D69", "#4EBFC3", "#FFB1BF"};
    // welcome components
    BorderPane welcomePane;
    TextField ipAddressText;
    TextField portNumText;
    Button playButton;
    Button optionsButton1;
    Text gameTitle;
    String welcomeBackgroundLink = "src/main/java/hisokaStart1.jpg";
    // Gameplay
    Button dealButton;
    Button foldButton;
    Button submitButton;
    Button optionButton2;
    Text totalWinningInfo;
    BorderPane gamePane;
    ListView<String> gameInfo;
    TextField pairPlusNum, anteWageNum, playWagerNum;
    StackPane totalWinningInfoFlow;
    VBox betBox;
    // menu components
    Button freshStartButton;
    Button newLookButton;
    Button exitGameButton;
    Button closeOptionsButton;
    Text optionsTitle;
    // end components
    BorderPane endPane;
    Text endingText;
    Text endTotalWinningsText;
    Button endContinueButton;
    Button endExitButton;
    boolean wonCurrentGame = true;
    // new-look components
    Text newLookTitle;
    Text newLookDescription;
    Button blueModeButton;
    Button pinkModeButton;
    Button closeNewLookButton;
    boolean gameHasBegun = false;



    // map of all scenes
    HashMap<String, Scene> sceneMap;
    /**
     * TODO:
     * options-button/ip-address button/port-number button/play button
     * game title
     * prompt-text/end scene text
     * -------still need to add game scene colors--------------
     */
    boolean blueModeTurnedOn = true;
    private String dealerCardImagePath1 = "src/main/resources/clubs-2.png";
    private String dealerCardImagePath2 = "src/main/resources/clubs-3.png";
    private String dealerCardImagePath3 = "src/main/resources/clubs-4.png";
    private String playerCardImagePath1 = "src/main/resources/clubs-10.png";
    private String playerCardImagePath2 = "src/main/resources/clubs-12.png";
    private String playerCardImagePath3 = "src/main/resources/clubs-14.png";

    Client clientConnection;
    PokerInfo pokerInfo;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Welcome to Poker");
        pokerInfo = new PokerInfo();
        {
            // initialize welcome components
            gameTitle = new Text("POKER");
            ipAddressText = new TextField("");
            ipAddressText.setPromptText("ENTER IP ADDRESS");
            portNumText = new TextField("");
            portNumText.setPromptText("ENTER PORT NUMBER");
            playButton = new Button("PLAY");
            optionsButton1 = new Button("OPTIONS");

            // initialize gameplay components

            optionButton2 = new Button("OPTIONS");
            totalWinningInfo = new Text("Total Winnings:\nXXXXX");
            pairPlusNum = new TextField();
            anteWageNum = new TextField();
            playWagerNum = new TextField();
            gameInfo = new ListView<>();
            dealButton = new Button("DEAL");
            foldButton = new Button("FOLD");
            submitButton = new Button("SUBMIT");
            gamePane = new BorderPane();

            // Textfield only accept integers
            TextFormatter<Integer> formatter1 = new TextFormatter<>(new IntegerStringConverter(), 1, c -> Pattern.matches("\\d*", c.getText()) ? c : null);  // copied code from https://stackoverflow.com/a/36749659
            TextFormatter<Integer> formatter2 = new TextFormatter<>(new IntegerStringConverter(), 1, c -> Pattern.matches("\\d*", c.getText()) ? c : null);
            TextFormatter<Integer> formatter3 = new TextFormatter<>(new IntegerStringConverter(), 1, c -> Pattern.matches("\\d*", c.getText()) ? c : null);
            pairPlusNum.setTextFormatter(formatter1);
            anteWageNum.setTextFormatter(formatter2);
            playWagerNum.setTextFormatter(formatter3);


            // initialize menu components
            freshStartButton = new Button("Fresh Start");
            newLookButton = new Button("New Look");
            exitGameButton = new Button("Exit the Game");
            optionsTitle = new Text("OPTIONS");
            closeOptionsButton = new Button("X");

            // initialize end components
            endingText = new Text("WINNER/LOSER YOU HAVE WON/LOST: $ XXXX");
            endTotalWinningsText = new Text("TOTAL WINNINGS: $XXXX");
            endContinueButton = new Button("Continue");
            endExitButton = new Button("Exit Game");

            // initialize new-look components
            newLookTitle = new Text("NEW L\uD83D\uDC40K");
            newLookDescription = new Text("Which theme do you prefer?");
            blueModeButton = new Button();
            pinkModeButton = new Button();
            closeNewLookButton = new Button("X");

            // style the welcome components
            gameTitle.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 120 Garamond;");
            ipAddressText.setStyle("-fx-background-color: #B8DADB; -fx-fill: #010101;  -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
            portNumText.setStyle("-fx-background-color: #B8DADB; -fx-fill: #010101;  -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
            playButton.setStyle("-fx-background-color: #40999D; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
            optionsButton1.setStyle("-fx-background-color: #B8DADB; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");

            // style the game components
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

            // style menu components
            optionsTitle.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 100 Garamond;");
            freshStartButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
            newLookButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
            exitGameButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
            closeOptionsButton.setStyle("-fx-background-color: #2D9BF0; -fx-text-fill:#E6E6E6; -fx-font: bold 50 Garamond;");

            // style end components
            endingText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #010101; -fx-text-alignment: center;");
            endTotalWinningsText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #010101; -fx-text-alignment: center;");
            endContinueButton.setStyle("-fx-background-color: #A2C255; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 200px;  -fx-font: bold 24 Calibri; ");
            endExitButton.setStyle("-fx-background-color: #E64D69; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 200px;  -fx-font: bold 24 Calibri; ");

            // style new look components
            newLookTitle.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 100 Garamond;");
            newLookDescription.setStyle("-fx-background-color:#1A1A1A; -fx-fill: #FFFFFF;-fx-font: bold 24 Calibri; ");
            blueModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#A2C255;-fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
            pinkModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#E6E6E6; -fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
            closeNewLookButton.setStyle("-fx-background-color: #BE83B1; -fx-text-fill:#E6E6E6; -fx-font: bold 50 Garamond;");
        }
        // welcome screen event handlers
        playButton.setOnAction(e -> {
            primaryStage.setScene(sceneMap.get("gameplay"));
            gameHasBegun = true;
            System.out.println("Has game begun? " + gameHasBegun);
            clientConnection = new Client(data->{
                Platform.runLater(()->{
                    pokerInfo = (PokerInfo) data;
                    gameInfo.getItems().add("Server: " + pokerInfo.message);
                });
            }, ipAddressText.getText(), Integer.valueOf(portNumText.getText()));
            clientConnection.start();
        });
        optionsButton1.setOnAction(e -> primaryStage.setScene(sceneMap.get("menuScreen")));
        newLookButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("newLookScreen")));
        closeOptionsButton.setOnAction(e -> {
            if (gameHasBegun) {
                primaryStage.setScene(sceneMap.get("gameplay"));
            } else {
                primaryStage.setScene(sceneMap.get("welcomeScreen"));
            }
        } );

        // menu screen event handlers
        //TODO:WRITE FRESH START EVENT HANDLER
        exitGameButton.setOnAction(e -> Platform.exit());

        // new-look screen event handlers
        closeNewLookButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("menuScreen")));
        pinkModeButton.setOnAction(e -> applyNewLook(pinkTheme));
        blueModeButton.setOnAction(e -> applyNewLook(blueTheme));

        // end screen event handlers
        //TODO: WRITE CONTINUE BUTTON EVENT HANDLER
        endExitButton.setOnAction(e -> Platform.exit());

        // TODO: ADD GAME SCREEN EVENT HANDLERS
        // TODO: Write the deal, fold, and submit event handlers
        submitButton.setOnAction(e->{
            pokerInfo.message = "I have made my bets";
            clientConnection.send(pokerInfo);
        });
        dealButton.setOnAction(e->{
            pokerInfo.message = "I want to deal!";
            clientConnection.send(pokerInfo);
        });
        foldButton.setOnAction(e->{
            pokerInfo.message = "I want to fold!";
            clientConnection.send(pokerInfo);
        });
        optionButton2.setOnAction(e -> primaryStage.setScene(sceneMap.get("menuScreen")));


        // Put scenes into a hashmap
        sceneMap = new HashMap<>();
        sceneMap.put("welcomeScreen", createWelcomeScreen());
        sceneMap.put("gameplay", createGameScene());
        sceneMap.put("endScreen", createEndScreen());
        sceneMap.put("newLookScreen", createNewLookScreen());
        sceneMap.put("menuScreen", createMenuScreen());

        // close all clients
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        // set screen
        primaryStage.setScene(sceneMap.get("welcomeScreen"));
        primaryStage.show();

    }

    Scene createWelcomeScreen() {
        welcomePane = new BorderPane();

        // set up background
        try {
            welcomePane.setBackground(new Background(new BackgroundImage(
                    new Image(new FileInputStream("src/main/java/hisokaStart1.jpg")),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
        } catch (Exception e) {
            System.out.println("Unable to load the background image for the intro scene");
        }
        ;

        // arrange components
        HBox justOneMenuBtn = new HBox(optionsButton1);
        justOneMenuBtn.setMargin(optionsButton1, new Insets(20, 20, 20, 20));
        VBox login = new VBox(gameTitle, ipAddressText, portNumText, playButton);
        login.setMargin(gameTitle, new Insets(30, 30, 5, 30));
        login.setMargin(ipAddressText, new Insets(5, 30, 0, 30));
        login.setMargin(portNumText, new Insets(5, 30, 0, 30));
        login.setMargin(playButton, new Insets(10, 30, 0, 30));

        welcomePane.setRight(login);
        welcomePane.setTop(justOneMenuBtn);
        login.setAlignment(Pos.CENTER);
        justOneMenuBtn.setAlignment(Pos.TOP_RIGHT);

        return new Scene(welcomePane, 1400, 790);
    }

    Scene createGameScene() {
        gamePane.setStyle("-fx-background-color: #4EBFC3;");  // TODO: Change the hexcode such that the new look can be applied

        //Right-side
        // winning total
        totalWinningInfoFlow = new StackPane(totalWinningInfo);
        totalWinningInfoFlow.setStyle("-fx-background-color: #B8DADB; -fx-min-width: 500; -fx-min-height: 85; -fx-background-radius: 5;");

        // Bet
        Text betTitle = new Text("MAKE YOUR BET!");
        betTitle.setStyle("-fx-font: bold 34 Calibri;");
        Text betDescription = new Text("Make your bet wagers between 5 and 25 bucks.\nEnter 0 if you do not want to bet on the pair plus.");
        betDescription.setStyle("-fx-font: 16 Calibri;");

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

        HBox betTextFields = new HBox(8, pairPlusBet, anteWageBet, playWageBet);
        betTextFields.setAlignment(Pos.CENTER);
        betBox = new VBox(10, betTitle, betDescription, betTextFields, submitButton);
        betBox.setStyle("-fx-background-color:#FFB1BF; -fx-background-radius: 5;");  // TODO: Change the hexcode
        betBox.setAlignment(Pos.CENTER);
        betBox.setPadding(new Insets(10));

        VBox paneRight = new VBox(20, totalWinningInfoFlow, betBox, gameInfo);
        paneRight.setAlignment(Pos.CENTER);

        // Dealer Hand
        Text dealerTitle = new Text("DEALER");
        dealerTitle.setStyle("-fx-font: bold 18 Calibri;");
        Rectangle dealerCard1 = createCard(dealerCardImagePath1);
        Rectangle dealerCard2 = createCard(dealerCardImagePath2);
        Rectangle dealerCard3 = createCard(dealerCardImagePath3);

        // Player Hand
        Text playerTitle = new Text("PLAYER (YOU)");
        playerTitle.setStyle("-fx-font: bold 18 Calibri;");
        Rectangle playerCard1 = createCard(playerCardImagePath1);
        Rectangle playerCard2 = createCard(playerCardImagePath2);
        Rectangle playerCard3 = createCard(playerCardImagePath3);

        // Deck of Card
        Rectangle deckCard = createCard(playerCardImagePath3);  // TODO: Need to create the back of the card. This is a placeholder.

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
        Rectangle card = new Rectangle(0, 0, 178, 250);
        card.setArcWidth(20);
        card.setArcHeight(20);

        try {
            ImagePattern pattern = new ImagePattern(new Image(new FileInputStream(cardImagePath)), 0, 0, 178, 250, false);  // position x, position y, width, height
            card.setFill(pattern);
            card.setStyle("-fx-background-radius: 5;");
        } catch (Exception e) {
            System.out.println("Image unable to get");
        }
        ;

        return card;
    }

    Scene createEndScreen() {
        endPane = new BorderPane();

        // set up background
        Image hisoka1;
        try {
            if (wonCurrentGame) {
                hisoka1 = new Image(new FileInputStream("src/main/java/hisokaEnd1.jpg"));
                endingText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #010101; -fx-text-alignment: center;");
                endTotalWinningsText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #010101; -fx-text-alignment: center;");
            } else {
                hisoka1 = new Image(new FileInputStream("src/main/java/hisokaEnd2.jpg"));
                endingText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #E6E6E6; -fx-text-alignment: center;");
                endTotalWinningsText.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 25 Garamond;  -fx-fill: #E6E6E6; -fx-text-alignment: center;");
            }


            endPane.setBackground(new Background(new BackgroundImage(hisoka1,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
        } catch (Exception e) {
            System.out.println("Unable to load the background image for the ending scene");
        }
        ;

        // assemble components
        HBox contORexit = new HBox(endContinueButton, endExitButton);
        VBox finalResults = new VBox(endingText, contORexit, endTotalWinningsText);

        contORexit.setMargin(endContinueButton, new Insets(10, 10, 20, 10));
        contORexit.setMargin(endExitButton, new Insets(10, 10, 20, 10));
        finalResults.setMargin(endingText, new Insets(20, 50, 20, 20));
        finalResults.setMargin(endTotalWinningsText, new Insets(20, 50, 20, 20));

        contORexit.setAlignment(Pos.CENTER);
        finalResults.setAlignment(Pos.CENTER);
        endPane.setRight(finalResults);

        return new Scene(endPane, 1400, 790);
    }

    Scene createNewLookScreen() {
        BorderPane pane = new BorderPane();

        try {
            // set up background
            Image newLookBg = new Image(new FileInputStream("src/main/java/newLookBg.jpg"));
            Image blueModePic = new Image(new FileInputStream("src/main/java/blueMode.jpg"));
            Image pinkModePic = new Image(new FileInputStream("src/main/java/pinkMode.jpg"));
            pane.setBackground(new Background(new BackgroundImage(newLookBg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));

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

        newLookBox.setMargin(newLookTitle, new Insets(30, 0, 0, 0));
        newLookBox.setMargin(newLookDescription, new Insets(30, 0, 0, 0));
        lookButtonsBox.setMargin(blueModeButton, new Insets(30, 30, 0, 30));
        lookButtonsBox.setMargin(pinkModeButton, new Insets(30, 30, 0, 30));

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
        allOptions.setMargin(optionsTitle, new Insets(30, 30, 5, 30));
        allOptions.setMargin(freshStartButton, new Insets(5, 30, 0, 30));
        allOptions.setMargin(newLookButton, new Insets(5, 30, 0, 30));
        allOptions.setMargin(exitGameButton, new Insets(5, 30, 0, 30));
        allOptions.setAlignment(Pos.CENTER);
        pane.setCenter(allOptions);

        HBox justOneExitBtn = new HBox(closeOptionsButton);
        pane.setTop(justOneExitBtn);
        justOneExitBtn.setAlignment(Pos.TOP_RIGHT);

        return new Scene(pane, 1400, 790);
    }

    //TODO: ADD GAME SCREEN FUNCTION

    public void applyNewLook(String[] themeStyle) {
        blueModeTurnedOn = ((blueModeTurnedOn == false) ? true : false);

        if (blueModeTurnedOn) {
            welcomeBackgroundLink = "src/main/java/hisokaStart1.jpg";
            blueModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#A2C255;-fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
            pinkModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#E6E6E6; -fx-pref-width: 300px;-fx-font: bold 24 Calibri;");

        } else {
            welcomeBackgroundLink = "src/main/java/hisokaStart2.jpg";
            blueModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#E6E6E6;-fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
            pinkModeButton.setStyle("-fx-background-color:#E6E6E6; -fx-pref-height: 300px; -fx-border-width:3; -fx-border-color:#A2C255; -fx-pref-width: 300px;-fx-font: bold 24 Calibri;");
        }

        try {
            welcomePane.setBackground(new Background(new BackgroundImage(
                    new Image(new FileInputStream(welcomeBackgroundLink)),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
        } catch (Exception e) {
            System.out.println("Unable to change the background image to the new look");
        };


        // update welcome components
        gameTitle.setStyle("-fx-fill:" + themeStyle[1] + ";-fx-font: bold 120 Garamond;");
        ipAddressText.setStyle("-fx-background-color:" + themeStyle[0] + "; -fx-fill:" + themeStyle[1] + "; -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        portNumText.setStyle("-fx-background-color:" + themeStyle[0] + "; -fx-fill:" + themeStyle[1] + "; -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        playButton.setStyle("-fx-background-color:" + themeStyle[2] + "; -fx-text-fill:" + themeStyle[1] + "; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionsButton1.setStyle("-fx-background-color: " + themeStyle[0] + "; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionButton2.setStyle("-fx-background-color: " + themeStyle[0] + "; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");

        // update end components
        endingText.setStyle("-fx-background-color: " + themeStyle[0] + "; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        totalWinningInfoFlow.setStyle("-fx-background-color:"+ themeStyle[0]+ ";-fx-min-width: 500; -fx-min-height: 85; -fx-background-radius: 5;");
        betBox.setStyle("-fx-background-color:"+ themeStyle[3]+ "; -fx-background-radius: 5;");  // TODO: Change the hexcode
        gamePane.setStyle("-fx-background-color:"+ themeStyle[4]+ ";");  // TODO: Change the hexcode such that the new look can be applied
    }


}