import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.print.attribute.standard.Media;
import java.io.FileInputStream;
import java.util.HashMap;



public class GUIClient extends Application {

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
    Text gameInfo, totalWinningInfo;
    Text pairPlusNum, anteWageNum, playWagerNum;

    Rectangle dealerCard1, dealerCard2, dealerCard3;
    Rectangle playerCard1, playerCard2, playerCard3;

    // menu components
    Button freshStartButton;
    Button newLookButton;
    Button exitGameButton;
    Button closeOptionsButton1;
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

    // map of all scenes
    HashMap<String, Scene> sceneMap;

    /** TODO:
     * options-button/ip-address button/port-number button/play button
     * game title
     * prompt-text/end scene text
     * -------still need to add game scene colors--------------
     */
    boolean blueModeTurnedOn = true;
    // blue, hisoka-black, hisoka-#010101
    final String[] blueTheme = new String[]{"#B8DADB", "#E6E6E6", "#010101"};

    // pink, hisoka-black, hisoka-#E6E6E6
    final String[] pinkTheme = new String[]{"#BE83B1", "#010101", "#E6E6E6",};


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Welcome to Poker");

        // initialize welcome components
        gameTitle = new Text("POKER");
        ipAddressText = new TextField("");
        ipAddressText.setPromptText("ENTER IP ADDRESS");
        portNumText = new TextField("");
        portNumText.setPromptText("ENTER PORT NUMBER");
        playButton = new Button ("PLAY");
        optionsButton1 = new Button("OPTIONS");

        // initialize gameplay components
        optionButton2 = new Button("OPTIONS");
        optionButton2.setStyle("-fx-background-color: #E6E6E6; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionButton2.setOnAction(e -> {
            primaryStage.setScene(sceneMap.get("menuScreen"));
        });


        // initialize menu components
        freshStartButton = new Button ("Fresh Start");
        newLookButton = new Button ("New Look");
        exitGameButton = new Button ("Exit the Game");
        optionsTitle = new Text("OPTIONS");
        closeOptionsButton1 = new Button("X");

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
        ipAddressText.setStyle("-fx-background-color: #E6E6E6; -fx-fill: #010101;  -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        portNumText.setStyle("-fx-background-color: #E6E6E6; -fx-fill: #010101;  -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        playButton.setStyle("-fx-background-color: #3D9295; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionsButton1.setStyle("-fx-background-color: #E6E6E6; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");

        // style menu components
        optionsTitle.setStyle("-fx-fill: #FFFFFF;-fx-font: bold 100 Garamond;");
        freshStartButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
        newLookButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
        exitGameButton.setStyle("-fx-background-color: #1F405A; -fx-text-fill: #E6E6E6; -fx-pref-height: 20px; -fx-pref-width: 450px;  -fx-font: bold 24 Calibri; ");
        closeOptionsButton1.setStyle("-fx-background-color: #2D9BF0; -fx-text-fill:#E6E6E6; -fx-font: bold 50 Garamond;");

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

        // welcome screen event handlers
        // TODO: WRITE PLAY GAME EVENT HANDLER
        optionsButton1.setOnAction(e->primaryStage.setScene(sceneMap.get("menuScreen")));
        closeOptionsButton1.setOnAction(e->primaryStage.setScene(sceneMap.get("welcomeScreen")));

        // menu screen event handlers
        //TODO:WRITE FRESH START EVENT HANDLER
        exitGameButton.setOnAction(e -> Platform.exit());
        newLookButton.setOnAction(e->primaryStage.setScene(sceneMap.get("newLookScreen")));

        // new-look screen event handlers
        closeNewLookButton.setOnAction(e->primaryStage.setScene(sceneMap.get("menuScreen")));
        pinkModeButton.setOnAction(e->applyNewLook(pinkTheme));
        blueModeButton.setOnAction(e->applyNewLook(blueTheme));

        // end screen event handlers
        //TODO: WRITE CONTINUE BUTTON EVENT HANDLER
        endExitButton.setOnAction(e -> Platform.exit());

        // TODO: ADD GAME SCREEN EVENT HANDLERS


        // Put scenes into a hashmap
        sceneMap = new HashMap<>();
        sceneMap.put("welcomeScreen", createWelcomeScreen());
        sceneMap.put("gameplay",createGameScene());
        sceneMap.put("endScreen", createEndScreen());
        sceneMap.put("newLookScreen", createNewLookScreen());
        sceneMap.put("menuScreen", createMenuScreen());

        // set screen
        primaryStage.setScene(sceneMap.get("newLookScreen"));
        primaryStage.show();

    }

    Scene createWelcomeScreen(){
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
        };

        // arrange components
        HBox justOneMenuBtn = new HBox(optionsButton1);
        justOneMenuBtn.setMargin(optionsButton1,  new Insets(20, 20, 20, 20));
        VBox login = new VBox(gameTitle, ipAddressText, portNumText, playButton);
        login.setMargin(gameTitle,  new Insets(30, 30, 5, 30));
        login.setMargin(ipAddressText,  new Insets(5, 30, 0, 30));
        login.setMargin(portNumText,  new Insets(5, 30, 0, 30));
        login.setMargin(playButton,  new Insets(10, 30, 0, 30));

        welcomePane.setRight(login);
        welcomePane.setTop(justOneMenuBtn);
        login.setAlignment(Pos.CENTER);
        justOneMenuBtn.setAlignment(Pos.TOP_RIGHT);

        return new Scene(welcomePane, 1400, 790);
    }

    Scene createGameScene(){
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: #4EBFC3");


        Rectangle card = createCard("src/main/resources/clubs-6.png");
        Rectangle card1 = createCard("src/main/resources/clubs-9.png");

        HBox paneCenter = new HBox(card, card1);
        HBox paneTop = new HBox(optionButton2);
        pane.setCenter(paneCenter);
        pane.setTop(paneTop);

        return new Scene(pane, 1400, 750);
    }

    Rectangle createCard(String cardImagePath) {
        Rectangle card = new Rectangle(0, 0, 100, 140);
        card.setArcWidth(20);
        card.setArcHeight(20);

        try {
            ImagePattern pattern = new ImagePattern(new Image(new FileInputStream(cardImagePath)), 0, 0, 100, 140, false);  // position x, position y, width, height
            card.setFill(pattern);
            card.setStyle("-fx-background-radius: 5;");
        } catch (Exception e) {
            System.out.println("Image unable to get");
        };

        return card;
    }

    Scene createEndScreen(){
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
        };

        // assemble components
        HBox contORexit = new HBox(endContinueButton, endExitButton);
        VBox finalResults = new VBox(endingText, contORexit, endTotalWinningsText);

        contORexit.setMargin(endContinueButton,  new Insets(10, 10, 20, 10));
        contORexit.setMargin(endExitButton,  new Insets(10, 10, 20, 10));
        finalResults.setMargin(endingText,  new Insets(20, 50, 20, 20));
        finalResults.setMargin(endTotalWinningsText,  new Insets(20, 50, 20, 20));

        contORexit.setAlignment(Pos.CENTER);
        finalResults.setAlignment(Pos.CENTER);
        endPane.setRight(finalResults);

        return new Scene(endPane, 1400, 790);
    }
    Scene createNewLookScreen(){
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

        newLookBox.setMargin(newLookTitle,  new Insets(30,0,0,0));
        newLookBox.setMargin(newLookDescription,  new Insets(30,0,0,0));
        lookButtonsBox.setMargin(blueModeButton,  new Insets(30,30,0,30));
        lookButtonsBox.setMargin(pinkModeButton,  new Insets(30,30,0,30));

        HBox justOneExitBtn = new HBox(closeNewLookButton);
        pane.setTop(justOneExitBtn);
        justOneExitBtn.setAlignment(Pos.TOP_RIGHT);

        lookButtonsBox.setAlignment(Pos.CENTER);
        newLookBox.setAlignment(Pos.CENTER);
        pane.setCenter(newLookBox);

        return new Scene(pane, 1400, 790);
    }
    Scene createMenuScreen(){
        BorderPane pane = new BorderPane();

        // set up background
        pane.setStyle("-fx-background-color: #2D9BF0");

        // arrange components
        VBox allOptions = new VBox(optionsTitle, freshStartButton, newLookButton, exitGameButton);
        allOptions.setMargin(optionsTitle,  new Insets(30, 30, 5, 30));
        allOptions.setMargin(freshStartButton,  new Insets(5, 30, 0, 30));
        allOptions.setMargin(newLookButton,  new Insets(5, 30, 0, 30));
        allOptions.setMargin(exitGameButton,  new Insets(5, 30, 0, 30));
        allOptions.setAlignment(Pos.CENTER);
        pane.setCenter(allOptions);

        HBox justOneExitBtn = new HBox(closeOptionsButton1);
        pane.setTop(justOneExitBtn);
        justOneExitBtn.setAlignment(Pos.TOP_RIGHT);

        return new Scene(pane, 1400, 790);
    }

    //TODO: ADD GAME SCREEN FUNCTION

    public void applyNewLook(String[] themeStyle){
        blueModeTurnedOn= ((blueModeTurnedOn == false) ? true : false);

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
        ipAddressText.setStyle("-fx-background-color:" + themeStyle[0] + "; -fx-fill:" + themeStyle[2] + "; -fx-prompt-text-fill:" + themeStyle[2] + "; -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        portNumText.setStyle("-fx-background-color:" + themeStyle[0] + "; -fx-fill:" + themeStyle[2] + "; -fx-prompt-text-fill:" + themeStyle[2] + "; -fx-alignment: center; -fx-pref-height: 20px; -fx-font: 24 Calibri;");
        playButton.setStyle("-fx-background-color:" + themeStyle[0] + "; -fx-text-fill:" + themeStyle[2] + "; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionsButton1.setStyle("-fx-background-color: " + themeStyle[0] + "; -fx-text-fill:" + themeStyle[2] + "; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");

        // update end components
        endingText.setStyle("-fx-fill:" + themeStyle[2] + ";-fx-font: bold 25 Garamond; -fx-text-alignment: center;");
        endTotalWinningsText.setStyle("-fx-fill:" + themeStyle[2] + ";-fx-font: bold 25 Garamond; -fx-text-alignment: center;");
    }



}