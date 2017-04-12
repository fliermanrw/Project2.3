package controller;

import com.sun.org.apache.xerces.internal.dom.ChildNode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.games.othello.boardCell;
import model.games.othello.othelloGameModel;
import model.server_connection.ServerHandler;
import model.server_connection.ServerHandlerReader;
import model.server_connection.ServerHandlerWriter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.ResourceBundle;



public class OthelloController extends GameView implements Initializable {
    othelloGameModel othello;
    boolean ourturn = false;
    public String startPlayer;
    boolean isLoaded = false;

    @FXML
    GridPane othelloGameBoard;

    @FXML
    Label changeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        othello = new othelloGameModel('B');
        othello.initGrid();
        othello.printBoard();
        initBoard();
        isLoaded = true;
    }
//    public void init() {
//        othello = new othelloGameModel('B');
//        othello.initGrid();
//        othello.printBoard();
//        initBoard();
//    }

    public void initBoard() {
        for (int row = 0; row < 8; row++) {
            othelloGameBoard.addRow(row);
            for (int col = 0; col < 8; col++) {
                Button a = new Button();
                a.setPrefWidth(50.0);
                a.setPrefHeight(50.0);
                a.setId(Integer.toString(othello.rowColToInt(row, col)));
                a.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        buttonClick(a.getId(), a);
                    }
                });
                a.setText(Character.toString(othello.othelloBoard.cellsOnBoard.get(othello.rowColToInt(row, col)).getCharacterInCell()));
                othelloGameBoard.addColumn(col, a);
            }
        }
    }

    public void updateBoard() {
        // for cell

            // for every cell in the list update the text of the button
            // to be equal to the character in the cell.


            Platform.runLater(()->{
                for (int i = 0; i < othello.othelloBoard.cellsOnBoard.size(); i++) {
                    Button button = (Button) othelloGameBoard.getChildren().get(i);
                    if(String.valueOf(othello.othelloBoard.cellsOnBoard.get(i).getCharacterInCell()).equals("B")){
                        button.setStyle("-fx-background-color: black;");
                    }else if(String.valueOf(othello.othelloBoard.cellsOnBoard.get(i).getCharacterInCell()).equals("W")){
                        button.setStyle("-fx-background-color: white;");
                    }
//                    else{
//                        if(othello.getValidMoves().contains(i) && ourturn){
//                            button.setStyle("-fx-background-color: aqua;");
//                        }else{
//                            button.setStyle("-fx-background-color: #E8E8E8;");
//                        }
//                    }
                    button.setText(Character.toString(othello.othelloBoard.cellsOnBoard.get(i).getCharacterInCell()));
                }
            });
        }

    public void buttonClick(String id, Button button) {
        ArrayList<Integer> validMoves = othello.getValidMoves();

        System.out.println("button text:" + button.getId());
        if (ourturn) {
            if (!validMoves.contains(Integer.valueOf(button.getId()))) {
                System.out.println("not a valid move");
            } else {
                //@todo don't set button just update the view representation here.
                othello.move(Integer.valueOf(button.getId()));
                move(Integer.valueOf(button.getId()));
                updateBoard();
//                othello.printBoard();
                othello.switchPlayer();
//                System.out.println("HET IS NU IEMAND ANDERS BEURT:" + othello.getCurrentPlayer());
                ourturn = false;
            }
        } else {
            System.out.println("Niet onze beurt");
        }
    }

    // er komt een move binnen van de server.
    @Override
    public void serverMove(int index, String playerName) {
        if(!ServerHandler.playerName.equals(playerName)){
            System.out.println("new server move");
            othello.move(index);
//            othello.printBoard();
            updateBoard();
            othello.switchPlayer();
//            System.out.println("HET IS NU IEMAND ANDERS BEURT:" + othello.getCurrentPlayer());
            ourturn();
        }
    }

    @Override
    public void ourturn() {
        ourturn = true;
        System.out.println("OthelloController: Got notified it's now our turn and we can make a move");
        if(ServerHandlerReader.useBot){
            System.out.println("OthelloController: Got notified it's now our turn, our bot is going to make a turn");
            botMove();
        }
    }

    /**
     * Only call this when we are the first turn
     * Does NOT switch a player first
     */
    @Override
    public void firstTurn() {
       ourturn = true;
       updateBoard();
        System.out.println("We are the player that is starting the game our move: " + othello.getCurrentPlayer());

        System.out.println("OthelloController: --firstturn Got notified it's now our turn and we can make a move");
        if(ServerHandlerReader.useBot){
            System.out.println("OthelloController: --firstturn Got notified it's now our turn, our bot is going to make a turn");
            botMove();
        }
    }

    public void botMove(){
        othello.printBoard();
        ArrayList<Integer> validMoves = othello.getValidMoves();
        System.out.println("Bot CURRENT PLAYER: " + othello.getCurrentPlayer());
        System.out.println("Bot AVAIABLE MOVES: " + validMoves);

        Random rand = new Random();
        int random = rand.nextInt(validMoves.size());
        othello.move(validMoves.get(random));
        System.out.println("Bot MOVE: " + String.valueOf(validMoves.get(random)));
        move(validMoves.get(random));
        updateBoard();
        othello.printBoard();
        othello.switchPlayer();
        ourturn = false;
    }

    @Override
    void move(int place) {
        ServerHandlerWriter.writeSend("move " + place);
    }

    @Override
    void forfeit() {
        ServerHandlerWriter.writeSend("forfeit");
    }

    public void changeLabel(boolean ourturn) {
        if (!ourturn) {
            changeLabel.setText("NOT YOUR TURN...WAITING FOR OTHER PLAYER");
            changeLabel.setStyle("-fx-background-color: RED ; ");
        } else if(ourturn) {
            changeLabel.setText("YOU NEED TO MAKE A MOVE");
            changeLabel.setStyle("-fx-background-color: BLUE ; ");
            changeLabel.setStyle("-fx-color: WHITE ; ");
        }
    }
    public void forfeitGame(ActionEvent actionEvent) {
        forfeit();

        //Perform this in the javafx thread
        Platform.runLater(() -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/connectedView.fxml"));
            Parent root = null;
            try {
                root = (Parent) fxmlLoader.load();

                //Set writer in controller
                ConnectedController connectedController = fxmlLoader.getController();
                ServerHandlerReader.currentController = connectedController;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException cme) {
                cme.printStackTrace();
            }
            ServerHandlerReader.stage.setTitle("Lobby");
            ServerHandlerReader.stage.setScene(new Scene(root, 300, 400));
        });
    }


    public void closeApplication(ActionEvent actionEvent) {
        ServerHandlerWriter.writeSend("logout");
        System.out.println("gelukt uit te loggen.. nu nog afsluiten");

        Platform.exit();
    }

    public boolean isLoaded(){
        return isLoaded;
    }




}

