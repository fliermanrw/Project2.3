package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.games.tictactoe.Move;
import model.server_connection.ServerHandlerWriter;
import model.games.tictactoe.Tictactoe;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by jouke on 3-4-2017.
 */

public class TictactoeController extends GameView implements Initializable{
    Tictactoe tictactoe;
    boolean ourturn = false;
    @FXML GridPane gameBoard;
    String playerName;


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
            if(tictactoe.hasWon(tictactoe.getGrid(), tictactoe.getCurrentPlayer())){
                System.out.println(tictactoe.getCurrentPlayer() + "Has won the game");
            }else{
                if(validMoves.size() == 0){
                    System.out.println("Its a tie");
                }else{
                    if(validMoves.contains(index)){
                        tictactoe.move(index);
                        tictactoe.printGrid();
                        move(index);
                        btn.setText(tictactoe.getCurrentPlayer());
                        ourturn = false;
                    }
                }
            }
        }
    }

    //When server notifies us of a new move
    @Override
    public void serverMove(int index, String currentName) {
        if(!this.playerName.equals(currentName)) {
            tictactoe.move(index, true); //set position in model
            tictactoe.printGrid();
            Platform.runLater(() -> {
                for (Node node : gameBoard.getChildren()) {
                    if (node instanceof Button) {
                        Button button = (Button) node;
                        int buttonid = Integer.valueOf(button.getId());
                        if (buttonid == index) {
                            button.setText(tictactoe.getCurrentPlayer());
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
                    }
                }
            }
        });
        tictactoe.move(bestMove.getIndex());
        move(bestMove.getIndex());
        ourturn=false;
        System.out.println("bestmove="+bestMove.getIndex());
    }

    @Override
    public void ourturn() {
        ourturn = true;
        System.out.println("TictactoeController: Got notified it's now our turn and we can make a move");
//        botMove(); @todo toggle this when its a bot, autogenerates a move
    }

    @Override
    void move(int place) {
        ServerHandlerWriter.writeSend("move " + place);
    }

    @Override
    void forfeit() {
        ServerHandlerWriter.writeSend("forfeit");
    }


    public void setPlayerName(String name){
        this.playerName = name;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameBoard.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth)->{
            for(Node node : gameBoard.getChildren()){
                if(node instanceof Button){
                    Button button = (Button) node;
                    button.setPrefHeight(gameBoard.getHeight()/3);
                    button.setPrefWidth(gameBoard.getWidth()/3);
                }
            }
        });
        gameBoard.heightProperty().addListener((observableValue, oldSceneWidth, newSceneWidth)->{
            for(Node node : gameBoard.getChildren()){
                if(node instanceof Button){
                    Button button = (Button) node;
                    button.setPrefHeight(gameBoard.getHeight()/3);
                    button.setPrefWidth(gameBoard.getWidth()/3);
                }
            }
        });
    }
}

