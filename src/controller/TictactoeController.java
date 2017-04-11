package controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.games.tictactoe.Move;
import model.server_connection.ServerHandler;
import model.server_connection.ServerHandlerReader;
import model.server_connection.ServerHandlerWriter;
import model.games.tictactoe.Tictactoe;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by jouke on 3-4-2017.
 */

public class TictactoeController extends GameView implements Initializable {
    Tictactoe tictactoe;
    boolean ourturn = false;
    @FXML
    GridPane gameBoard;
    @FXML
    Label changeLabel;


    public TictactoeController() {
        tictactoe = new Tictactoe();
        tictactoe.setHumanPlayer("X");
        tictactoe.setAiPlayer("O");
        tictactoe.setCurrentPlayer(tictactoe.getHumanPlayer()); //case is human
    }

    public void buttonClick(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Integer index = Integer.valueOf(btn.getId().toString());
        System.out.println("INDEX: " + index);
        ArrayList<Integer> validMoves = tictactoe.getValidMoves();
        System.out.println("Validmoves" + validMoves);

        if(ourturn){
            System.out.println("OURMOVE = TRUE");
            if (tictactoe.hasWon(tictactoe.getGrid(), tictactoe.getCurrentPlayer())) {
                System.out.println(tictactoe.getCurrentPlayer() + "Has won the game");
            }else{
                if(validMoves.size() == 0){
                    System.out.println("Its a tie");
                } else {
                    if (validMoves.contains(index)) {
                        tictactoe.move(index, true);
                        tictactoe.printGrid();
                        move(index);
                        btn.setText(tictactoe.getCurrentPlayer());
                        btn.setStyle("-fx-font-size: 30px");
                        ourturn = false;
                        changeLabel(ourturn);
                    }
                }
            }
        } else{changeLabel(ourturn);}
    }

    //When server notifies us of a new move
    @Override
    public void serverMove(int index, String currentName) {
        if(!ServerHandler.playerName.equals(currentName)) {
            tictactoe.move(index, true); //set position in model
            tictactoe.printGrid();
            Platform.runLater(() -> {
                for (Node node : gameBoard.getChildren()) {
                    if (node instanceof Button) {
                        Button button = (Button) node;
                        int buttonid = Integer.valueOf(button.getId());
                        if (buttonid == index) {
                            button.setText(tictactoe.getCurrentPlayer());
                            button.setStyle("-fx-font-size: 30px");
                            changeLabel(ourturn);

                        }
                    }
                }
            });
            ourturn();
        }
    }

    public void botMove(){
        Move bestMove = tictactoe.minimax(tictactoe.getGrid(), tictactoe.getAIPlayer(), new Move(0));
        Platform.runLater(()->{
            for(Node node : gameBoard.getChildren()){
                if(node instanceof Button){
                    Button button = (Button) node;
                    int buttonid = Integer.valueOf(button.getId());
                    if(buttonid == bestMove.getIndex()){
                        button.setText(tictactoe.getCurrentPlayer());
                        button.setStyle("-fx-font-size: 30px");
                        changeLabel(ourturn);

                    }
                }
            }
        });
        tictactoe.move(bestMove.getIndex(),true);
        move(bestMove.getIndex());
        ourturn=false;
        System.out.println("bestmove="+bestMove.getIndex());
    }

    @Override
    public void ourturn() {
        ourturn = true;
        System.out.println("TictactoeController: Got notified it's now our turn and we can make a move");
        if(ServerHandlerReader.useBot){
            botMove();
            System.out.println("TictactoeController: Got notified it's now our turn, our bot is going to make a turn");
        }
    }

    @Override
    void move(int place) {
        ServerHandlerWriter.writeSend("move " + place);
        changeLabel(ourturn);
    }

    @Override
    void forfeit() {
        ServerHandlerWriter.writeSend("forfeit");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameBoard.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth)->{
            for(Node node : gameBoard.getChildren()){
                if(node instanceof Button){
                    Button button = (Button) node;
                    button.setPrefHeight(gameBoard.getHeight()/3);
                    button.setPrefWidth(gameBoard.getWidth()/3);
                    String style = "-fx-font-size:"+gameBoard.getHeight()/7 + ";";
                    button.setStyle(style);
                }
            }
        });
        gameBoard.heightProperty().addListener((observableValue, oldSceneWidth, newSceneWidth)->{
            for(Node node : gameBoard.getChildren()){
                if(node instanceof Button){
                    Button button = (Button) node;
                    button.setPrefHeight(gameBoard.getHeight()/3);
                    button.setPrefWidth(gameBoard.getWidth()/3);
                    String style = "-fx-font-size:"+gameBoard.getHeight()/7 + ";";
                    button.setStyle(style);
                }
            }
        });
    }

    public void changeLabel(boolean ourturn) {
        if (!ourturn) {
            changeLabel.setText("NOT YOUR TURN...WAITING FOR OTHER PLAYER");
            changeLabel.setStyle("-fx-background-color: RED ; ");
        } else if(ourturn) {
            changeLabel.setText("YOU NEED TO MAKE A MOVE");
            changeLabel.setStyle("-fx-background-color: GREEN ; ");
        }
    }

    // Forfeits the game and returns to lobby / connectedView
    public void forfeitGame(ActionEvent actionEvent) {
        forfeit();

        //TODO change view to connectedView here

    }

    public void closeApplication(ActionEvent actionEvent) {
        ServerHandlerWriter.writeSend("logout");
        System.out.println("gelukt uit te loggen.. nu nog afsluiten");

        //TODO close the whole application



    }


}
