package com.nsu.burym.snakegame.controller;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.nsu.burym.snakegame.model.SnakesProto;
import com.nsu.burym.snakegame.model.SnakesProto.Direction;
import com.nsu.burym.snakegame.model.SnakesProto.GameState.Coord;
import com.nsu.burym.snakegame.model.SnakesProto.GamePlayer;
import com.nsu.burym.snakegame.model.SnakesProto.GameState.Snake;
import com.nsu.burym.snakegame.model.events.EventListener;
import com.nsu.burym.snakegame.model.GameModel;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable, EventListener {
    private Alert alert;

    private GameModel gameModel = GameModel.getInstance();

    private final int CELLS_PANEL_AREA = 250000;
    private final double WINDOW_WIDTH = 720;
    private final double WINDOW_HEIGHT = 500;

    private final Image defaultCell = new Image("com.nsu.burym.snakegame/game/lavaCell.png");
    private final Image foodCell = new Image("com.nsu.burym.snakegame/game/foodCell.png");
    private final Image snakeDefaultCell = new Image("com.nsu.burym.snakegame/game/snakeCell.png");
    private final Image snakeHeadCell = new Image("com.nsu.burym.snakegame/game/snakeHead.png");

    @FXML
    AnchorPane root;

    @FXML
    AnchorPane rightAnchorPane;

    @FXML
    private GridPane cellsPanel;

    @FXML
    private ListView<Text> playersListView;

    private ListProperty<Text> playersListProperty = new SimpleListProperty<>();

    public GameController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Warning");

        playersListView.itemsProperty().bind(playersListProperty);

        gameModel.getEventManager().subscribe(this,  "gameState", "serverDown");

        AnchorPane.setRightAnchor(rightAnchorPane, 11.0);

        try {
            gameModel.startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initCellsPanel();
    }

    private void initCellsPanel() {
        double cellImageSize;

        if (gameModel.getFieldHeight() > gameModel.getFieldWidth()) {
            cellImageSize = Math.floor(Math.sqrt(CELLS_PANEL_AREA * 1.0 / (gameModel.getFieldHeight() * gameModel.getFieldHeight())));
        } else {
            cellImageSize = Math.floor(Math.sqrt(CELLS_PANEL_AREA * 1.0 / (gameModel.getFieldWidth() * gameModel.getFieldWidth())));
        }

        for (int i = 0; i < gameModel.getFieldHeight(); i++) {
            for (int j = 0; j < gameModel.getFieldWidth(); j++) {
                ImageView cellImage = new ImageView(defaultCell);

                cellImage.setFitWidth(cellImageSize);
                cellImage.setFitHeight(cellImageSize);

                cellsPanel.add(cellImage, j, i);
            }
        }

        double cellsPanelX = WINDOW_WIDTH/2 - (gameModel.getFieldHeight() * cellImageSize)/2;
        double cellsPanelY = WINDOW_HEIGHT/2 - (gameModel.getFieldWidth() * cellImageSize)/2;

        cellsPanel.setLayoutX(11 + cellsPanelX);
        cellsPanel.setLayoutY(89 + cellsPanelY);

        cellsPanel.setGridLinesVisible(false);
    }

    @Override
    public void update(String eventType, List<Snake> snakes, List<Coord> food, List<GamePlayer> players) {
        Platform.runLater(() -> {
            for (int i = 0; i < gameModel.getFieldHeight(); i++) {
                for (int j = 0; j < gameModel.getFieldWidth(); j++) {
                    getNodeByRowColumnIndex(i, j, cellsPanel).setImage(defaultCell);
                }
            }

            refreshSnakes(snakes);
            refreshFood(food);

            refreshPlayersListView(players);
        });
    }

    @Override
    public void update(String eventType) {
        Platform.runLater(() -> {
            alert.setContentText("Server is down");
            alert.showAndWait();
        });
    }

    private void refreshPlayersListView(List<GamePlayer> players) {
        List<Text> playersTextList = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getRole() != SnakesProto.NodeRole.VIEWER) {
                Text playerText = new Text((i + 1) + ") " + players.get(i).getName() + ": " + players.get(i).getScore());


                playerText.setFill(Color.rgb(66, 100, 139));
                playersTextList.add(playerText);
            }
        }
       playersListProperty.setValue(FXCollections.observableArrayList(playersTextList));
    }

    private void refreshSnakes(List<Snake> snakes) {
        for(Snake snake : snakes) {
            for (int i = 0; i < snake.getPointsCount(); i++) {
                Coord snakeCoord = snake.getPoints(i);
                if (i == 0) {
                    getNodeByRowColumnIndex(snakeCoord.getY(), snakeCoord.getX(), cellsPanel).setImage(snakeHeadCell);
                } else {
                    getNodeByRowColumnIndex(snakeCoord.getY(), snakeCoord.getX(), cellsPanel).setImage(snakeDefaultCell);
                }
            }
        }
    }

    private void refreshFood(List<Coord> food) {
        for (Coord f : food) {
            getNodeByRowColumnIndex(f.getY(), f.getX(), cellsPanel).setImage(foodCell);
        }
    }

    private ImageView getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return (ImageView) result;
    }

    @FXML
    public void handleOnKeyPressed(KeyEvent event) throws IOException, InterruptedException {
        SnakesProto.Direction direction = gameModel.findMyOwnSnakeDirection();

        if (event.getCode().equals(KeyCode.W)) {
            if (direction != Direction.DOWN && direction != Direction.UP)  {
                gameModel.sendMessage(gameModel.getClientServer().getServerIp(), gameModel.getClientServer().getServerPort(),
                        gameModel.createSteerMsg(SnakesProto.Direction.UP));
            }
        } else if (event.getCode().equals(KeyCode.S)) {
            if (direction != Direction.UP && direction != Direction.DOWN) {
                gameModel.sendMessage(gameModel.getClientServer().getServerIp(), gameModel.getClientServer().getServerPort(),
                        gameModel.createSteerMsg(SnakesProto.Direction.DOWN));
            }
        } else if (event.getCode().equals(KeyCode.A)) {
            if (direction != Direction.RIGHT && direction != Direction.LEFT) {
                gameModel.sendMessage(gameModel.getClientServer().getServerIp(), gameModel.getClientServer().getServerPort(),
                        gameModel.createSteerMsg(SnakesProto.Direction.LEFT));
            }
        } else if (event.getCode().equals(KeyCode.D)) {
            if (direction != Direction.LEFT && direction != Direction.RIGHT) {
                gameModel.sendMessage(gameModel.getClientServer().getServerIp(), gameModel.getClientServer().getServerPort(),
                        gameModel.createSteerMsg(SnakesProto.Direction.RIGHT));
            }
        }

        System.out.println("НАЖАЛ КНОПКУ+ " + gameModel.getClientServer().getServerIp() + " " + gameModel.getClientServer().getServerPort());
    }

    public void backButtonPressed(MouseEvent event) throws IOException, InterruptedException {
        gameModel.endGame();

        Parent gameViewParent = FXMLLoader.load(getClass().getResource("/com.nsu.burym.snakegame/fxml/menu.fxml"));

        Scene gameViewScene = new Scene(gameViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(gameViewScene);
        window.show();
    }
}
