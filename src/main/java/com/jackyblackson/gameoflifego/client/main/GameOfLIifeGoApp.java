package com.jackyblackson.gameoflifego.client.main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.jackyblackson.gameoflifego.client.uicomponents.MainMenu;

public class GameOfLIifeGoApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Game of Life: GO");
        settings.setVersion("V0.1.0 - INNER DEVELOPER");

        // 启用主界面菜单
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
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
