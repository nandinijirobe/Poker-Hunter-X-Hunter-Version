import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

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

import java.io.FileInputStream;
import java.util.HashMap;


public class GUIClient extends Application {

    TextField ipAddressText;
    TextField portNumText;
    Button playButton;
    Button optionsButton;
    HashMap<String, Scene> sceneMap;
    Text gameTitle;

    // Gameplay
    Button dealButton;
    Button foldButton;
    Button submitButton;
    Button optionButton2;
    Text gameInfo, totalWinningInfo;
    Text pairPlusNum, anteWageNum, playWagerNum;

    Rectangle dealerCard1, dealerCard2, dealerCard3;
    Rectangle playerCard1, playerCard2, playerCard3;



    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("Welcome to JavaFX");

        // initialize all components
        gameTitle = new Text("POKER");
        ipAddressText = new TextField("IP ADDRESS");
        portNumText = new TextField("PORT NUMBER");
        playButton = new Button ("PLAY");

        // style the components
        gameTitle.setStyle("-fx-text-fill: white;-fx-font: bold 175 Garamond;");
        ipAddressText.setStyle("-fx-background-color: #B8DADB; -fx-text-fill: black; -fx-pref-height: 50px; -fx-pref-width: 175px; -fx-font: 24 Garamond;");
        portNumText.setStyle("-fx-background-color: #B8DADB; -fx-text-fill: black; -fx-pref-height: 50px; -fx-pref-width: 175px; -fx-font: 24 Garamond;");
        playButton.setStyle("-fx-background-color: #3D9295; -fx-text-fill: black; -fx-pref-height: 50px; -fx-pref-width: 175px; -fx-pref-width: 175px;  -fx-font: bold 18 Garamond; ");


        // Game
        optionButton2 = new Button("OPTIONS");
        optionButton2.setStyle("-fx-background-color: #E6E6E6; -fx-text-fill: #010101; -fx-pref-height: 20px; -fx-pref-width: 150px;  -fx-font: bold 24 Calibri; ");
        optionButton2.setOnAction(e -> {
            primaryStage.setScene(sceneMap.get("menuScreen"));
        });


        // Put scenes into a hashmap
        sceneMap = new HashMap<>();
        sceneMap.put("welcomeScreen", createWelcomeScreen());
        sceneMap.put("gameplay",createGameScene());
        sceneMap.put("endScreen", createEndScreen());
        sceneMap.put("newLookScreen", createNewLookScreen());
        sceneMap.put("menuScreen", createMenuScreen());

        // set screen
        primaryStage.setScene(sceneMap.get("gameplay"));
        primaryStage.show();

    }

    Scene createWelcomeScreen(){
        // set up background
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: #4EBFC3");

        // assemble components
        VBox login = new VBox(gameTitle, ipAddressText, portNumText, playButton);
        login.setMargin(portNumText, new Insets(30, 10, 20, 10));
        login.setAlignment(Pos.CENTER);
        pane.setRight(login);
        return new Scene(pane, 1400, 750);
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
        BorderPane pane = new BorderPane();
        return new Scene(pane, 1400, 750);
    }
    Scene createNewLookScreen(){
        BorderPane pane = new BorderPane();
        return new Scene(pane, 1400, 750);
    }
    Scene createMenuScreen(){
        BorderPane pane = new BorderPane();
        return new Scene(pane, 1400, 750);
    }

}