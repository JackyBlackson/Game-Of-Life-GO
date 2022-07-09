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
import com.jackyblackson.gameoflifego.client.factories.TileFactory;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.client.map.ChunkLoader;
import com.jackyblackson.gameoflifego.client.uicomponents.MainMenu;
import com.jackyblackson.gameoflifego.server.main.ServerMain;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.area.Area;
import com.jackyblackson.gameoflifego.shared.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.shared.map.manager.MapManager;
import com.jackyblackson.gameoflifego.shared.player.Player;
import com.jackyblackson.gameoflifego.shared.tiles.Cell;
import com.jackyblackson.gameoflifego.shared.tiles.Tile;
import com.jackyblackson.gameoflifego.shared.tiles.TileType;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Iterator;

import static com.almasb.fxgl.dsl.FXGLForKtKt.loopBGM;

public class GameOfLIifeGoApp extends GameApplication {
    long ticks = 0;

    Entity player;
    @Override
    protected void initGame() {
        player = new EntityBuilder()
                .at(new Point2D(-GamePlayInfo.getInstance().scaling, -GamePlayInfo.getInstance().scaling))
                .type(TileType.CAMERA)
                .viewWithBBox(new Rectangle(GamePlayInfo.getInstance().scaling, GamePlayInfo.getInstance().scaling, new Color(1.0, 0.0, 0.0, 1.0)))
                .build();
        FXGL.getGameWorld().addEntity(player);
        FXGL.getGameScene().getViewport().bindToEntity(player, FXGL.getAppWidth()/2.0, FXGL.getAppHeight()/2.0);
        FXGL.getGameScene().setBackgroundColor(new Color(0.05, 0.05, 0.05,1.0 ));

        try {
            ServerMain.main(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Player player = new Player("Jacky", "11aa77");



        for(Iterator<Area> it = MapManager.getInstance().getAreas();it.hasNext() ; ){
            ChunkLoader.loadArea(it.next());
        }

        Pos worldPos;
        Player player1 = new Player("Jacky", "44FF22");
        worldPos = new Pos(17.0, 17.0);
        new TileFactory().build(new Cell(worldPos, player), worldPos);
        worldPos = new Pos(18.0, 17.0);
        new TileFactory().build(new Cell(worldPos, player), worldPos);
        worldPos = new Pos(19.0, 17.0);
        new TileFactory().build(new Cell(worldPos, player), worldPos);
        worldPos = new Pos(20.0, 17.0);
        new TileFactory().build(new Cell(worldPos, player), worldPos);

        loopBGM("Marconi_Union_Weightless.mp3");
    }

    @Override
    protected void onUpdate(double tpf) {
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        float speed = GamePlayInfo.getInstance().movingSpeed;
        input.addAction(new UserAction("Transpose camera up") {
            @Override
            protected void onAction() {
                player.translateY(-speed * GamePlayInfo.getInstance().scaling);
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Transpose camera down") {
            @Override
            protected void onAction() {
                player.translateY(speed * GamePlayInfo.getInstance().scaling);
            }
        }, KeyCode.S);

        input.addAction(new UserAction("Transpose camera right") {
            @Override
            protected void onAction() {
                player.translateX(speed * GamePlayInfo.getInstance().scaling);
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Transpose camera left") {
            @Override
            protected void onAction() {
                player.translateX(-speed * GamePlayInfo.getInstance().scaling);
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Reset camera position") {
            @Override
            protected void onAction() {
                player.setX(0);
                player.setY(0);
            }
        }, KeyCode.O);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Game of Life: GO");
        settings.setVersion("V0.1.0 - INNER DEVELOPER");
        settings.setManualResizeEnabled(true);


        // 启用主界面菜单
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

    /**
     * main 程序的开始
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        launch(args);
    }
}
