package com.nsu.burym.snakegame.controller;

import com.nsu.burym.snakegame.model.SnakesProto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.nsu.burym.snakegame.model.GameModel;
import com.nsu.burym.snakegame.model.exceptions.WrongArgException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewGameController implements Initializable{
    private GameModel gameModel = GameModel.getInstance();

    private Alert alert;

    @FXML
    private ImageView startButton;

    @FXML
    private ImageView backButton;

    @FXML
    private TextField sizeField1;

    @FXML
    private TextField sizeField2;

    @FXML
    private TextField foodStatic;

    @FXML
    private TextField foodPerPlayer;

    @FXML
    private TextField stateDelayMs;

    @FXML
    private TextField deadFoodProb;

    @FXML
    private TextField pingDelayMs;

    @FXML
    private TextField nodeTimeoutMs;

    @FXML
    private TextField maxPlayersField;

    public NewGameController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Warning");

        sizeField1.setText(Integer.toString(gameModel.getFieldHeight()));
        stateDelayMs.setText(Integer.toString(gameModel.getStateDelayMs()));
    }

    public void startButtonPressed(MouseEvent event) throws IOException {
        try {
            gameModel.setFieldHeight(Integer.parseInt(sizeField1.getText()));
            gameModel.setFieldWidth(Integer.parseInt(sizeField1.getText()));
            gameModel.setStateDelayMs(Integer.parseInt(stateDelayMs.getText()));
            gameModel.setNodeRole(SnakesProto.NodeRole.MASTER);

            gameModel.startServerLogic();

            gameModel.sendMessage(InetAddress.getByName("127.0.0.1"), gameModel.getSelfPort(),
                    gameModel.createJoinMsg());

            Parent gameViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com.nsu.burym.snakegame/fxml/game.fxml")));

            Scene gameViewScene = new Scene(gameViewParent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(gameViewScene);
            window.show();
        } catch (WrongArgException | InterruptedException ex) {
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    public void backButtonPressed(MouseEvent event) throws IOException {

        Parent gameViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com.nsu.burym.snakegame/fxml/menu.fxml")));

        Scene gameViewScene = new Scene(gameViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(gameViewScene);
        window.show();
    }
}
