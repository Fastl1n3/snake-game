package com.nsu.burym.snakegame.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.nsu.burym.snakegame.model.GameModel;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private final GameModel gameModel = GameModel.getInstance();

    @FXML
    private ImageView newGameButton;

    @FXML
    private ImageView connectButton;

    @FXML
    private ImageView optionsButton;

    @FXML
    private ImageView exitButton;

    @FXML
    private TextField playerNameField;

    @FXML
    private ImageView applyButton;

    public MenuController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerNameField.setText(gameModel.getMasterPlayerName());
    }


    public void newGameButtonPressed(MouseEvent event) throws IOException {
        Parent gameViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com.nsu.burym.snakegame/fxml/new_game.fxml")));

        Scene gameViewScene = new Scene(gameViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(gameViewScene);
        window.show();
    }

    public void connectButtonPressed(MouseEvent event) throws IOException {
        gameModel.receiveAnnouncementMessages();

        Parent gameViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com.nsu.burym.snakegame/fxml/connect.fxml")));

        Scene gameViewScene = new Scene(gameViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(gameViewScene);
        window.show();
    }

    public void optionsButtonPressed(MouseEvent event) throws IOException {
        Parent gameViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com.nsu.burym.snakegame/fxml/options.fxml")));

        Scene gameViewScene = new Scene(gameViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(gameViewScene);
        window.show();
    }

    public void exitButtonPressed(MouseEvent event) {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.close();
    }

    public void applyButtonPressed(MouseEvent event) throws IOException {
        gameModel.setMasterPlayerName(playerNameField.getText());
    }
}
