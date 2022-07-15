package com.jackyblackson.gameoflifego.client.uicomponents;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.collection.grid.Grid;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.ui.FontType;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.client.net.ConnectionManager;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;


public class MainMenu extends FXGLMenu {

    private VBox scoresRoot = new VBox(10);
    private Node highScores;

    public MainMenu() {
        super(MenuType.MAIN_MENU);
        var bg = texture("bg.png", getAppWidth(), getAppHeight());
        bg.setTranslateY(0);
        bg.setTranslateX(0);
        getContentRoot().getChildren().add(bg);
        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 46.0);
        title.setStroke(Color.WHITESMOKE);
        title.setStrokeWidth(1.5);

        var gameStory = getUIFactoryService().newText("In the vast universe, there are spaces for live.\n" +
                "Lifes born and lifes live; lifes gone and lifes born;\n" +
                "Lifes collect resources and lifes consumes resources...\n" +
                "This is the Game of Life. You are the god,\n" +
                "Make your cells thrive through rounds of evolution!"
                , Color.WHITE, FontType.GAME, 30);
        gameStory.setStroke(Color.WHITESMOKE);
        gameStory.setTextAlignment(TextAlignment.CENTER);
        gameStory.setEffect(new DropShadow(10.0, Color.LIGHTBLUE));
        gameStory.setLineSpacing(5.0);        gameStory.setStrokeWidth(1.0);
        gameStory.setTranslateX(getAppWidth() / 2.9);
        gameStory.setTranslateY(280);


        if (!FXGL.isMobile()) {
            title.setEffect(new Bloom(0.6));
        }
        centerTextBind(title, getAppWidth() / 2.0, 200);

        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 22.0);
        centerTextBind(version, getAppWidth() / 2.0, 220);

        getContentRoot().getChildren().addAll(title, version, gameStory);
        MenuButton startGame = new MenuButton("Start Game", this::playerSelector);
        startGame.setEffect(new DropShadow(10, Color.LIGHTGRAY));
        MenuButton controlIntro = new MenuButton("Game Instructions", this::instructions);
        controlIntro.setEffect(new DropShadow(10, Color.LIGHTGRAY));
        MenuButton quitGame = new MenuButton("Quit Game", () -> fireExit());
        quitGame.setEffect(new DropShadow(10, Color.LIGHTGRAY));
        var menuBox = new VBox(
                3,
                startGame,
                controlIntro,
                quitGame
        );
        menuBox.setSpacing(20.0); // 设置按钮之间的距离
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setPadding(new Insets(10, 10, 10, 10));
        menuBox.setTranslateX(getAppWidth() / 2.0 - 160);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 125);
        getContentRoot().getChildren().addAll(menuBox);

        FXGL.getAudioPlayer().stopAllMusic();
        loopBGM("Cubes_Collectives_Wave.mp3");
    }


    private void instructions() {
        GridPane pane = new GridPane();
        if (!FXGL.isMobile()) {
            pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.WHITE));
        }

        float scaling = GamePlayInfo.getInstance().scaling;

        Canvas water = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
        GraphicsContext gc = water.getGraphicsContext2D();
        gc.setLineWidth(0);
        gc.setFill(new Color(0.1, 0.2, 0.7, 0.6));
        gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
        gc.setFill(new ImagePattern(new Image("assets/textures/water.png")));
        gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

        Canvas oxygen = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
        gc = oxygen.getGraphicsContext2D();
        gc.setLineWidth(0);
        gc.setFill(new Color(0.6, 0.6, 0.6, 0.6));
        gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
        gc.setFill(new ImagePattern(new Image("assets/textures/oxygen.png")));
        gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

        Canvas carbon = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
        gc = carbon.getGraphicsContext2D();
        gc.setLineWidth(0);
        gc.setFill(new Color(0.15, 0.14, 0.17, 0.6));
        gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
        gc.setFill(new ImagePattern(new Image("assets/textures/carbon.png")));
        gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

        Canvas asteroid = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
        gc = asteroid.getGraphicsContext2D();
        gc.setLineWidth(0);
        gc.setFill(new Color(0.1, 0.2, 0.15, 0.6));
        gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
        gc.setFill(new ImagePattern(new Image("assets/textures/asteroid.png")));
        gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

        Canvas vacuum = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
        gc = vacuum.getGraphicsContext2D();
        gc.setLineWidth(0);
        gc.setFill(new Color(0.225, 0.225, 0.225, 0.4));
        gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
        gc.setFill(new Color(0.17, 0.17, 0.17, 0.8));
        gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(25);
        pane.setVgap(10);
        pane.addRow(0, getUIFactoryService().newText("--> Here's how to control the game"));
        pane.addRow(1, getUIFactoryService().newText("To transpose viewpoint:"), new HBox(1, new KeyView(A), new KeyView(D), new KeyView(S), new KeyView(W)));
        pane.addRow(2, getUIFactoryService().newText("To add cells"), new HBox(1, new KeyView(F)));
        pane.addRow(3, getUIFactoryService().newText("To refresh"), new HBox(1, new KeyView(R)));
        pane.addRow(4, getUIFactoryService().newText("To check other player's scores"), new HBox(1, new KeyView(E)));
        pane.addRow(5, getUIFactoryService().newText("To exit"), new HBox(1, new KeyView(ESCAPE)));
        pane.addRow(6, getUIFactoryService().newText("--> About tiles:"));
        pane.addRow(7, asteroid, getUIFactoryService().newText("= 1 * Carbon + 1 * Water + 1 * Oxygen"));
        pane.addRow(8, carbon, getUIFactoryService().newText("= 6 * Carbon"));
        pane.addRow(9, water, getUIFactoryService().newText("= 6 * Water"));
        pane.addRow(10, oxygen, getUIFactoryService().newText("= 6 * Oxygen"));
        pane.addRow(11, vacuum, getUIFactoryService().newText("is where you can place cells"));
        pane.addRow(12, getUIFactoryService().newText("Placing a cell costs 1 * Carbon + 1 * Water + 1 * Oxygen,"));
        pane.addRow(13, getUIFactoryService().newText("and your score is calculated by both resources and cells!"));
        pane.addRow(14, getUIFactoryService().newText("GOOD LUCK!"));

        getDialogService().showBox("Game Instruction", pane, getUIFactoryService().newButton("Confirm"));
    }

    private void playerSelector() {
        GridPane pane = new GridPane();
        if (!FXGL.isMobile()) {
            pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.WHITE));
        }
        ObservableList<String> red = new ObservableListBase<String>() {
            String[] value = {"88", "99", "AA", "BB", "CC", "DD", "EE", "FF"};
            @Override
            public String get(int index) {
                return value[index];
            }
            @Override
            public int size() {
                return 8;
            }
        };

        ObservableList<String> green = new ObservableListBase<String>() {
            String[] value = {"88", "99", "AA", "BB", "CC", "DD", "EE", "FF"};
            @Override
            public String get(int index) {
                return value[index];
            }
            @Override
            public int size() {
                return 8;
            }
        };

        ObservableList<String> blue = new ObservableListBase<String>() {
            String[] value = {"88", "99", "AA", "BB", "CC", "DD", "EE", "FF"};
            @Override
            public String get(int index) {
                return value[index];
            }
            @Override
            public int size() {
                return 8;
            }
        };

        ChoiceBox<String> redBox = new ChoiceBox<String>(red);
        redBox.getSelectionModel().select(0);
        ChoiceBox<String> greenBox = new ChoiceBox<String>(green);
        greenBox.getSelectionModel().select(7);
        ChoiceBox<String> blueBox = new ChoiceBox<String>(blue);
        blueBox.getSelectionModel().select(3);
        final Circle[] cell = {new Circle(GamePlayInfo.getInstance().scaling / 2.0d, Color.web(redBox.getSelectionModel().getSelectedItem() + greenBox.getSelectionModel().getSelectedItem() + blueBox.getSelectionModel().getSelectedItem()))};
        TextField playerName = new TextField("Player Name");
        TextField ip = new TextField("Server ip");
        TextField port = new TextField("Server Port");
        Button connect = getUIFactoryService().newButton("CONNECT");
        Button cancel = getUIFactoryService().newButton("CANCEL");

        redBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cell[0] = new Circle(GamePlayInfo.getInstance().scaling, Color.web(redBox.getSelectionModel().getSelectedItem() + greenBox.getSelectionModel().getSelectedItem() + blueBox.getSelectionModel().getSelectedItem()));
        });
        greenBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cell[0] = new Circle(GamePlayInfo.getInstance().scaling, Color.web(redBox.getSelectionModel().getSelectedItem() + greenBox.getSelectionModel().getSelectedItem() + blueBox.getSelectionModel().getSelectedItem()));
        });
        blueBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cell[0] = new Circle(GamePlayInfo.getInstance().scaling, Color.web(redBox.getSelectionModel().getSelectedItem() + greenBox.getSelectionModel().getSelectedItem() + blueBox.getSelectionModel().getSelectedItem()));
        });
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //写入玩家数据
                GamePlayInfo.getInstance().player.setName(playerName.getText());
                GamePlayInfo.getInstance().player.setColor(redBox.getSelectionModel().getSelectedItem() + greenBox.getSelectionModel().getSelectedItem() + blueBox.getSelectionModel().getSelectedItem());
                GamePlayInfo.getInstance().ip = ip.getText();
                GamePlayInfo.getInstance().port = Integer.parseInt(port.getText());
                pane.setVisible(false);
                try {
                    ConnectionManager.getInstance().connect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                fireNewGame();
            }
        });

        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pane.setVisible(false);
            }
        });


        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(25);
        pane.setVgap(10);
        pane.addRow(0, getUIFactoryService().newText("Player name:      "), playerName);
        pane.addRow(0, getUIFactoryService().newText("Server address:   "), new HBox(2, ip, port));
        pane.addRow(1, getUIFactoryService().newText("Player color:     "), new HBox(2, redBox, greenBox, blueBox, cell[0]));
        pane.addRow(1, getUIFactoryService().newText("Connect to server:"), new HBox(2, connect));

        getDialogService().showBox("Connect to Server", pane, cancel);
    }


    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITE, 36.0);
            text.setStrokeWidth(1.5);
            text.strokeProperty().bind(text.fillProperty());

            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.web("88BBFF"))
                            .otherwise(Color.WHITE)
            );

            setOnMouseClicked(e -> action.run());
            setPickOnBounds(true);
            getChildren().add(text);
        }
    }
}
