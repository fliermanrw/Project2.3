package controller;

import com.sun.org.apache.xerces.internal.dom.ChildNode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.games.othello.boardCell;
import model.games.othello.othelloGameModel;
import model.server_connection.ServerHandler;
import model.server_connection.ServerHandlerWriter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class OthelloController extends GameView implements Initializable {
    othelloGameModel othello;
    boolean ourturn = false;
    public String startPlayer;

    @FXML
    GridPane othelloGameBoard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        othello = new othelloGameModel('B');
        othello.initGrid();
        othello.printBoard();
        initBoard();
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
                    }
                    if(String.valueOf(othello.othelloBoard.cellsOnBoard.get(i).getCharacterInCell()).equals("W")){
                        button.setStyle("-fx-background-color: white;");
                    }
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
                othello.switchPlayer();
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
            othello.move(index);
            othello.printBoard();
            updateBoard();
            othello.switchPlayer();
            ourturn();
            System.out.println("HET IS NU IEMAND ANDERS BEURT:" + othello.getCurrentPlayer());
        }
    }

    @Override
    public void ourturn() {
        ourturn = true;
    }

    @Override
    void move(int place) {
        ServerHandlerWriter.writeSend("move " + place);
    }

    @Override
    void forfeit() {
        ServerHandlerWriter.writeSend("forfeit");
    }

}

