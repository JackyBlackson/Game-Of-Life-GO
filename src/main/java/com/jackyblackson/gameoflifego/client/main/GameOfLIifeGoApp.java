package com.jackyblackson.gameoflifego.client.main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.time.TimerAction;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.client.map.ChunkMatainer;
import com.jackyblackson.gameoflifego.client.net.CacheUpdator;
import com.jackyblackson.gameoflifego.client.net.ConnectionManager;
import com.jackyblackson.gameoflifego.client.uicomponents.MainMenu;
import com.jackyblackson.gameoflifego.client.uicomponents.PlayerScorePane;
import com.jackyblackson.gameoflifego.server.main.ServerMain;
import com.jackyblackson.gameoflifego.shared.tiles.TileType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * The game and the main port of the client app
 */
public class GameOfLIifeGoApp extends GameApplication {
    /**
     * The camera entity
     */
    public static Entity camera;
    /**
     * The timer of the sync data action
     */
    TimerAction downloadAction;
    /**
     * The timer of the refresh display action
     */
    TimerAction updateAction;

    /**
     * This method return init the game after a new game is launched
     * This will be called after the server and player is selected
     */
    @Override
    protected void initGame() {
        //adds the camera
        camera = new EntityBuilder()
                .at(new Point2D(-GamePlayInfo.getInstance().scaling, -GamePlayInfo.getInstance().scaling))
                .type(TileType.CAMERA)
                .viewWithBBox(new Rectangle(GamePlayInfo.getInstance().scaling, GamePlayInfo.getInstance().scaling, new Color(1.0, 0.0, 0.0, 0.6)))
                .build();
        FXGL.getGameWorld().addEntity(camera);
        FXGL.getGameScene().getViewport().bindToEntity(camera, FXGL.getAppWidth()/2.0, FXGL.getAppHeight()/2.0);
        FXGL.getGameScene().setBackgroundColor(new Color(0.05, 0.05, 0.05,1.0 ));

        //starts a music
        FXGL.getAudioPlayer().stopAllMusic();
        loopBGM("Marconi_Union_Weightless.mp3");

        //start the sync action
        downloadAction = getGameTimer().runAtInterval(CacheUpdator::run, Duration.seconds(2.0));
        //start the refresh action
        updateAction = getGameTimer().runAtInterval(ClientTicker::run, Duration.seconds(2.0));
    }


    /**
     * This method will be called every client frame to update the ui variables
     * @param tpf time per frame
     */
    @Override
    protected void onUpdate(double tpf) {
        FXGL.set("cells", (double) GamePlayInfo.getInstance().player.getCells());
        FXGL.set("carbon", (double) GamePlayInfo.getInstance().player.getAmountOfCarbon());
        FXGL.set("water", (double) GamePlayInfo.getInstance().player.getAmountOfWater());
        FXGL.set("oxygen", (double) GamePlayInfo.getInstance().player.getAmountOfOxygen());
        FXGL.set("score", (double) GamePlayInfo.getInstance().player.getScore());
        FXGL.set("evolution", (double) GamePlayInfo.getInstance().evolutionLeft);
    }

    /**
     * Binds the actions to the keys
     */
    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        float speed = GamePlayInfo.getInstance().movingSpeed;
        input.addAction(new UserAction("Transpose camera up") {
            @Override
            protected void onActionBegin() {
                camera.translateY(-speed * GamePlayInfo.getInstance().scaling);
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Transpose camera down") {
            @Override
            protected void onActionBegin() {
                camera.translateY(speed * GamePlayInfo.getInstance().scaling);
            }
        }, KeyCode.S);

        input.addAction(new UserAction("Transpose camera right") {
            @Override
            protected void onActionBegin() {
                camera.translateX(speed * GamePlayInfo.getInstance().scaling);
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Transpose camera left") {
            @Override
            protected void onActionBegin() {
                camera.translateX(-speed * GamePlayInfo.getInstance().scaling);
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Reset camera position") {
            @Override
            protected void onActionBegin() {
                camera.setX(0);
                camera.setY(0);
            }
        }, KeyCode.O);

        input.addAction(new UserAction("Update display area") {
            @Override
            protected void onActionBegin() {
                ChunkMatainer.getInstance().AdjustChunkOnDisplay(camera);
            }
        }, KeyCode.U);

        input.addAction(new UserAction("Remove all tiles") {
            @Override
            protected void onActionBegin() {
                FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByType(TileType.OXYGEN));
                FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByType(TileType.WATER));
                FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByType(TileType.ASTEROID));
                FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByType(TileType.CARBON));
                FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByType(TileType.CELL));
                FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByType(TileType.VACUUM));
                ChunkMatainer.getInstance().loadedChunkList.clear();
            }
        }, KeyCode.R);

        input.addAction(new UserAction("Set cells selected") {
            @Override
            protected void onActionBegin() {
                ConnectionManager.getInstance().setCellToServer();
                int rand = FXGL.random(1,3);
                play("shoot" + rand + ".wav");
            }
        }, KeyCode.F);

        input.addAction(new UserAction("Show players scores") {
            @Override
            protected void onActionBegin() {
                getDialogService().showBox(
                        "Player's Scores",
                        PlayerScorePane.getPlayerInfoPane(),
                        new Button("back")
                );
            }
        }, KeyCode.E);
    }

    /**
     * This method is called when the game app is launched to init some settings
     * @param settings
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setAppIcon("icon.png");
        settings.setTitle("Game of Life: GO");
        settings.setVersion("V0.1.0 By Jacky_Blackson");
        settings.setManualResizeEnabled(false);
        settings.setTicksPerSecond(60);


        // Launch the main menu of the game
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setMenuKey(KeyCode.ESCAPE);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new MainMenu();
            }
            @Override
            public FXGLMenu newGameMenu() {
                return new SimpleGameMenu();
            }

        });


    }

    @Override
    protected void initUI() {
        //新建一个矩形
        StackPane cellsPane = scorePaneFactory(Color.web(GamePlayInfo.getInstance().player.getColor()), "cells", "Cells: ");
        StackPane carbonPane = scorePaneFactory(new ImagePattern(new Image("assets/textures/carbon.png")), "carbon", "Carbon: ");
        StackPane waterPane = scorePaneFactory(new ImagePattern(new Image("assets/textures/water.png")), "water", "Water: ");
        StackPane oxygenPane = scorePaneFactory(new ImagePattern(new Image("assets/textures/oxygen.png")), "oxygen", "Oxygen: ");
        StackPane scorePane = scorePaneFactory(new Color(0.5, 0.2, 0.2, 1.0), "score", "Score: ");
        StackPane evolutionPane = scorePaneFactory(new Color(0.2, 0.7, 0.5, 1.0), "evolution", "Time: ");


        FXGL.addUINode(cellsPane,620,20);
        FXGL.addUINode(carbonPane,170,20);
        FXGL.addUINode(waterPane,320,20);
        FXGL.addUINode(oxygenPane,470,20);
        FXGL.addUINode(scorePane,20,20);
        FXGL.addUINode(evolutionPane,1770,20);
    }

    private StackPane scorePaneFactory(Paint background, String varName, String title){
        Rectangle windowsUi = new Rectangle(130, 40);

        windowsUi.setStroke(background);
        windowsUi.setStrokeWidth(3);

        Text titleText = FXGL.getUIFactoryService().newText(title);
        Text score = FXGL.getUIFactoryService().newText(FXGL.getdp(varName).asString());
        titleText.setEffect(new DropShadow(5, Color.WHITE));
        score.setEffect(new DropShadow(5, Color.WHITE));

        HBox box = new HBox(titleText, score);
        StackPane pane = new StackPane(windowsUi, box);
        StackPane.setAlignment(pane, Pos.CENTER);
        pane.setEffect(new DropShadow(10, Color.WHITE));

        return pane;
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("cells", 0.0d);
        vars.put("carbon", 0.0d);
        vars.put("water", 0.0d);
        vars.put("oxygen", 0.0d);
        vars.put("score", 0.0d);
        vars.put("evolution", 0.0d);
    }

    /**
     * main 程序的开始
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        launch(args);
    }
}
