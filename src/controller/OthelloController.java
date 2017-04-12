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
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.games.othello.boardCell;
import model.games.othello.othelloBoard;
import model.games.othello.othelloGameModel;
import model.server_connection.ServerHandler;
import model.server_connection.ServerHandlerReader;
import model.server_connection.ServerHandlerWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.ResourceBundle;



public class OthelloController extends GameView implements Initializable {
    private static othelloGameModel othelloGameModel;
    boolean ourturn = false;

    @FXML
    GridPane othelloGameBoard;

    @FXML
    Label changeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        othelloGameModel = new othelloGameModel('B');
        othelloGameModel.initGrid();
        initBoard();
    }

    public void initBoard() {
        for (int row = 0; row < 8; row++) {
            othelloGameBoard.addRow(row);
            for (int col = 0; col < 8; col++) {
                Button a = new Button();
                a.setPrefWidth(50.0);
                a.setPrefHeight(50.0);
                a.setId(Integer.toString(othelloGameModel.rowColToInt(row, col)));
                a.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        buttonClick(a);
                    }
                });

                if (othelloGameModel.getBoardAsList().get(othelloGameModel.rowColToInt(row, col)).getCharacterInCell() == '#') {
                    if(othelloGameModel.getValidMoves().contains(othelloGameModel.rowColToInt(row,col)) && ourturn){
                        a.setStyle("-fx-background-color: aquamarine; -fx-border-color: lightgray");
                        othelloGameBoard.addColumn(col, a);
                    } else {
                        a.setText(" ");
                        othelloGameBoard.addColumn(col, a);
                    }
                } else if(othelloGameModel.getBoardAsList().get(othelloGameModel.rowColToInt(row, col)).getCharacterInCell() == 'B') {
                    //a.setText(" ");
                    //a.setStyle("-fx-background-image: url("../blackstone.png") ;");
                    a.setText("B");
                    a.setStyle("-fx-background-color: BLACK ; -fx-text-fill: white ;  -fx-font-weight: 500; -fx-border-color: lightgray ");
                    othelloGameBoard.addColumn(col, a);

                } else if(othelloGameModel.getBoardAsList().get(othelloGameModel.rowColToInt(row, col)).getCharacterInCell() == 'W') {
                    a.setText("W");
                    a.setStyle("-fx-background-color: whitesmoke ;  -fx-font-weight: 500 ; -fx-border-color: lightgray ");
                    othelloGameBoard.addColumn(col, a);

                } else {
                    a.setText(Character.toString(othelloGameModel.getBoardAsList().get(othelloGameModel.rowColToInt(row, col)).getCharacterInCell()));
                    othelloGameBoard.addColumn(col, a);
                }

            }

        }
    }

    public void updateBoard() {
            Platform.runLater(()->{
                for (int i = 0; i < othelloGameModel.getBoardAsList().size(); i++) {
                    Button button = (Button) othelloGameBoard.getChildren().get(i);
                    if(String.valueOf(othelloGameModel.getBoardAsList().get(i).getCharacterInCell()).equals("B")){
                        button.setText("B");
                        button.setStyle("-fx-background-color: black; -fx-text-fill: white ; -fx-font-weight: 500; -fx-border-color: lightgray");
                }else if(String.valueOf(othelloGameModel.getBoardAsList().get(i).getCharacterInCell()).equals("W")){
                        button.setText("W");
                        button.setStyle("-fx-background-color: whitesmoke; -fx-font-weight: 500; -fx-border-color: lightgray");
                    }else{
                        if(othelloGameModel.getValidMoves().contains(i) && ourturn){
                            button.setStyle("-fx-background-color: aquamarine; -fx-border-color: lightgray");
                        }else{
                            button.setStyle(null);
                        }
                    }
                    //button.setText(Character.toString(othello.othelloBoard.cellsOnBoard.get(i).getCharacterInCell()));
                    //button.setText(" ");
                }
            });
        }

    public void buttonClick(Button button) {
        ArrayList<Integer> validMoves = othelloGameModel.getValidMoves();

        System.out.println("button text:" + button.getId());
        if (ourturn) {
            changeLabel(ourturn);
            if (!validMoves.contains(Integer.valueOf(button.getId()))) {
                System.out.println("not a valid move");
            } else {
                //@todo don't set button just update the view representation here.
                othelloGameModel.move(Integer.valueOf(button.getId()));
                move(Integer.valueOf(button.getId()));
                updateBoard();
                othelloGameModel.switchPlayer();
                ourturn = false;
            }
        } else {
            System.out.println("Niet onze beurt");
            changeLabel(ourturn);
        }
    }

    // er komt een move binnen van de server.
    @Override
    public void serverMove(int index, String playerName) {
        if(!ServerHandler.playerName.equals(playerName)){
            othelloGameModel.move(index);
            updateBoard();
            othelloGameModel.switchPlayer();
            ourturn();
            System.out.println("HET IS NU IEMAND ANDERS BEURT:" + othelloGameModel.getCurrentPlayer());
        }
    }

    @Override
    public void ourturn() {
        ourturn = true;
        System.out.println("OthelloController: Got notified it's now our turn and we can make a move");
        if(ServerHandlerReader.useBot){
             botMove();
            minimaxMove();
        }
    }

    public void minimaxMove(){
        System.out.println("Bot move turn: " + othelloGameModel.getCurrentPlayer());
        System.out.println("Bot please do a move");

        othelloBoard board = othelloGameModel.othelloBoard;

        char turn = othelloGameModel.turn;
        int move = othelloGameModel.getMiniMaxMove(board, turn);

        othelloGameModel.move(move);
        move(move);

        updateBoard();
        othelloGameModel.switchPlayer();
        ourturn = false;
    }

    public void botMove(){
        System.out.println("Bot move turn: " + othelloGameModel.getCurrentPlayer());
        System.out.println("Bot please do a move");

        ArrayList<Integer> validMoves = othelloGameModel.getValidMoves();
        Random rand = new Random();
        int random = rand.nextInt(validMoves.size());

        othelloGameModel.move(validMoves.get(random));
        move(validMoves.get(random));

        updateBoard();
        othelloGameModel.switchPlayer();
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
            changeLabel.setStyle("-fx-text-fill: BLUE ; ");
            changeLabel.setStyle("-fx-background-color: aqua;");

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
}

