package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
    private othelloGameModel othelloGameModel;
    boolean ourturn = false;

    @FXML
    GridPane othelloGameBoard;

    @FXML
    Label changeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        othelloGameBoard.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth)->{
            for(Node node : othelloGameBoard.getChildren()){
                if(node instanceof Button){
                    Button button = (Button) node;
                    button.setPrefHeight(othelloGameBoard.getHeight()/8);
                    button.setPrefWidth(othelloGameBoard.getWidth()/8);
                }
            }
        });
        othelloGameBoard.heightProperty().addListener((observableValue, oldSceneWidth, newSceneWidth)->{
            for(Node node : othelloGameBoard.getChildren()){
                if(node instanceof Button){
                    Button button = (Button) node;
                    button.setPrefHeight(othelloGameBoard.getHeight()/8);
                    button.setPrefWidth(othelloGameBoard.getWidth()/8);
                }
            }
        });
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

                if (othelloGameModel.getBoard().get(othelloGameModel.rowColToInt(row, col)).getCharacterInCell() == '#') {
                    if (othelloGameModel.getValidMoves().contains(othelloGameModel.rowColToInt(row, col)) && ourturn) {
                        a.setStyle("-fx-background-color: aquamarine; -fx-border-color: lightgray");
                        othelloGameBoard.addColumn(col, a);
                    } else {

                        a.setText(" ");
                        othelloGameBoard.addColumn(col, a);
                    }
                } else if (othelloGameModel.getBoard().get(othelloGameModel.rowColToInt(row, col)).getCharacterInCell() == 'B') {
                    //a.setText(" ");
                    //a.setStyle("-fx-background-image: url("../blackstone.png") ;");
                    a.setText("B");
                    a.setStyle("-fx-background-color: BLACK ; -fx-text-fill: white ;   -fx-border-color: lightgray ");
                    othelloGameBoard.addColumn(col, a);

                } else if (othelloGameModel.getBoard().get(othelloGameModel.rowColToInt(row, col)).getCharacterInCell() == 'W') {
                    a.setText("W");
                    a.setStyle("-fx-background-color: whitesmoke ;   -fx-border-color: lightgray ");
                    othelloGameBoard.addColumn(col, a);

                } else {
                    a.setText(Character.toString(othelloGameModel.getBoard().get(othelloGameModel.rowColToInt(row, col)).getCharacterInCell()));
                    othelloGameBoard.addColumn(col, a);
                }

                if (ServerHandlerReader.useBot){
                    a.setDisable(true);
                }
            }

        }
    }

    public void updateBoard() {
        Platform.runLater(() -> {
            for (int i = 0; i < othelloGameModel.getBoard().size(); i++) {
                Button button = (Button) othelloGameBoard.getChildren().get(i);
                if (String.valueOf(othelloGameModel.getBoard().get(i).getCharacterInCell()).equals("B")) {
                    button.setText("B");
                    button.setStyle("-fx-background-color: black; -fx-text-fill: white ; -fx-font-weight: 500; -fx-border-color: lightgray");
                } else if (String.valueOf(othelloGameModel.getBoard().get(i).getCharacterInCell()).equals("W")) {
                    button.setText("W");
                    button.setStyle("-fx-background-color: whitesmoke; -fx-font-weight: 500; -fx-border-color: lightgray");
                } else {
                    //@todo following commented code results in a bug. Can only call getValidMoves from one position else -> CONCURRENT MODIFICATION EXCEPTION
                    //@todo please fix quick, will bug in alot of situation
                    if (othelloGameModel.getValidMoves().contains(i) && ourturn) {
                        button.setStyle("-fx-background-color: aquamarine; -fx-border-color: lightgray");
                    } else {
                        button.setStyle(null);
                    }
                }
            }
        });
    }

    public void buttonClick(Button button) {
        ArrayList<Integer> validMoves = othelloGameModel.getValidMoves();

        System.out.println("button text:" + button.getId());
        if (ourturn) {
            if (!validMoves.contains(Integer.valueOf(button.getId()))) {
                System.out.println("not a valid move");
            } else {
                //@todo don't set button just update the view representation here.
                othelloGameModel.move(Integer.valueOf(button.getId()));
                move(Integer.valueOf(button.getId()));
                updateBoard();
                othelloGameModel.switchPlayer();
                ourturn = false;
                changeLabel(ourturn);
            }
        } else {
            System.out.println("Niet onze beurt");
            changeLabel(ourturn);
        }
    }

    // er komt een move binnen van de server.
    @Override
    public void serverMove(int index, String playerName) {
        if (!ServerHandler.playerName.equals(playerName)) {
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
        changeLabel(ourturn); //change label every time this method is called
        updateBoard();
        System.out.println("OthelloController: Got notified it's now our turn and we can make a move");
        if (ServerHandlerReader.useBot) {
            //    botMove();
            minimaxMove();
        }
    }

    @Override
    public void ourTurnAgain() {
        ourturn = true;
        changeLabel(ourturn); //don't forget to switch the label
        othelloGameModel.switchPlayer();
        updateBoard();
        System.out.println("We are the player that is starting the game our move: " + othelloGameModel.getCurrentPlayer());

        System.out.println("OthelloController: --firstturn Got notified it's now our turn and we can make a move");
        if (ServerHandlerReader.useBot) {
            System.out.println("OthelloController: --firstturn Got notified it's now our turn, our bot is going to make a turn");
            //botMove();
            minimaxMove();
        }
    }

    public void minimaxMove() {
        System.out.println("Bot move turn: " + othelloGameModel.getCurrentPlayer());
        System.out.println("Bot please do a move");

        char turn = othelloGameModel.getTurn();
        int move = othelloGameModel.getMiniMaxMove(turn);

        othelloGameModel.move(move);
        move(move);

        updateBoard();
        othelloGameModel.switchPlayer();
        ourturn = false;
        changeLabel(ourturn);
    }

    public void botMove() {
        othelloGameModel.getOthelloBoard();
        ArrayList<Integer> validMoves = othelloGameModel.getValidMoves();
        System.out.println("Bot CURRENT PLAYER: " + othelloGameModel.getCurrentPlayer());
        System.out.println("Bot AVAIABLE MOVES: " + validMoves);

        Random rand = new Random();
        int random = rand.nextInt(validMoves.size());
        othelloGameModel.move(validMoves.get(random));
        System.out.println("Bot MOVE: " + String.valueOf(validMoves.get(random)));
        move(validMoves.get(random));
        updateBoard();
        othelloGameModel.getOthelloBoard();
        othelloGameModel.switchPlayer();
        ourturn = false;
        changeLabel(ourturn);
    }

    @Override
    void move(int place) {
        ServerHandlerWriter.writeSend("move " + place);
    }

    @Override
    void forfeit() {
        ServerHandlerWriter.writeSend("forfeit");
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public void weWon() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("We won!");
            alert.setContentText("We won this othello game!");
            alert.setOnHidden(e -> {
               backToLobby();
            });
            alert.show();
        });
    }

    @Override
    public void weLost() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("We lost!");
            alert.setContentText("We lost this othello game!");
            alert.setOnHidden(e -> {
                backToLobby();
            });
            alert.show();
        });
    }

    public void changeLabel(boolean ourturn) {
        Platform.runLater(()->{
            if (!ourturn) {
                changeLabel.setText("NOT YOUR TURN...WAITING FOR OTHER PLAYER");
                changeLabel.setStyle("-fx-background-color: RED ; ");
            } else if (ourturn) {
                changeLabel.setText("YOU NEED TO MAKE A MOVE");
                changeLabel.setStyle("-fx-text-fill: BLUE ; ");
                changeLabel.setStyle("-fx-background-color: aqua;");

            }
        });
    }

    public void forfeitGame(ActionEvent actionEvent) {
        forfeit();
       backToLobby();

    }

    public void backToLobby(){
        Platform.runLater(() -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/connectedView.fxml"));
            Parent root = null;
            try {
                root = (Parent) fxmlLoader.load();

                //Set writer in controller
                ServerHandlerReader.currentController = fxmlLoader.<ConnectedController>getController();

            } catch (IOException | ConcurrentModificationException e) {
                e.printStackTrace();
            }
            ServerHandlerReader.stage.setTitle("Lobby");
            ServerHandlerReader.stage.setScene(new Scene(root, 300, 400));

        });
    }

    public void closeApplication(ActionEvent actionEvent) {
        ServerHandlerWriter.writeSend("logout");
        System.out.println("gelukt uit te loggen.. nu nog afsluiten");

        ServerHandlerReader.stage.close();
    }
}

