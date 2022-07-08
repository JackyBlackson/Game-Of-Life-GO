package com.jackyblackson.gameoflifego.server.main;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.server.info.WorldGenInfo;
import com.jackyblackson.gameoflifego.server.net.Accepter;
import com.jackyblackson.gameoflifego.server.net.TCPServer;
import com.jackyblackson.gameoflifego.shared.map.manager.MapManager;
import com.jackyblackson.gameoflifego.shared.player.PlayerSet;

import java.io.IOException;
import java.util.Arrays;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class ServerMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        entrance(args);
    }


    public static void entrance(String[] args) throws IOException, ClassNotFoundException {
        ////////////INITIALIZATION////////////
        ////////////INITIALIZATION////////////
        ////////////INITIALIZATION////////////
        Log(Importance.INFO, "[Main Thread] Starting GAME OF LIFE: GO server...");
        Log(Importance.INFO, "[Main Thread] Starting GAME OF LIFE: GO server...");
        Log(Importance.INFO, "[Main Thread] Starting GAME OF LIFE: GO server...");
        //加载几个单实例类的实例变量
        MapManager.getInstance();
        WatchDog.getInstance();
        TCPServer.getInstance();
        Accepter.getInstance();
        PlayerSet.getInstance();
        loadGameInfo();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Downloading Chunks
        Log(Importance.INFO, "[Main Thread] Generating chunks for the map...");
        Log(Importance.INFO, "[Main Thread] Generating chunks for the map...");
        Log(Importance.INFO, "[Main Thread] Generating chunks for the map...");
        firstGen();

        //Launching
        Log(Importance.INFO, "[Main Thread] Now launching server...");
        Log(Importance.INFO, "[Main Thread] Now launching server...");
        Log(Importance.INFO, "[Main Thread] Now launching server...");

        Log(Importance.INFO, "[Main Thread] Starting watch dog");
        Thread watchdog = new Thread(WatchDog.getInstance());
        watchdog.setDaemon(false);
        watchdog.setName("WATCH_DOG");
        watchdog.setPriority(10);
        watchdog.start();
        Log(Importance.INFO, "[Main Thread] Watch dog has started");
    }



    public static void loadGameInfo() {
        try {
            WorldGenInfo.loadProperties("Config/worldgen.properties");
            GameInfo.loadGameInfo("Config/game.properties");

            Log(Importance.INFO, "Successfully read in world generation properties");
        } catch (Exception ex){
            Log(Importance.SEVERE, ex.getLocalizedMessage());
            Log(Importance.SEVERE, Arrays.toString(ex.getStackTrace()));
        }
    }

    private static void firstGen() throws IOException {
        //遍历用户指定的初始区域，并生成 / 加载区块
        for (long x = -GameInfo.SpawnRange; x < GameInfo.SpawnRange; x++) {
            for (long y = -GameInfo.SpawnRange; y < GameInfo.SpawnRange; y++) {
                MapManager.getInstance().getChunkAt(new Pos((double) x, (double) y));
            }
        }
        MapManager.getInstance().saveMap();
    }
}
