package com.jackyblackson.gameoflifego.server.main;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.server.net.Accepter;
import com.jackyblackson.gameoflifego.server.net.TCPServer;
import com.jackyblackson.gameoflifego.shared.map.manager.MapManager;

import java.io.IOException;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class WatchDog implements Runnable{
    private long lastInformed;
    private static WatchDog instance = new WatchDog();

    public static WatchDog getInstance(){
        return instance;
    }

    private WatchDog(){
        lastInformed = System.currentTimeMillis();
    }

    @Override
    public void run() {
        Log(Importance.INFO, "[Watch Dog] Watch dog launch: WOOF WOOF WOOF WOOF WOOF!");

        TCPServer.getInstance().isGameEnd = false;

        Thread mapTicking = new Thread(new Ticker());
        mapTicking.setDaemon(true);
        mapTicking.setName("TICKER");
        mapTicking.start();

        Thread accept = new Thread(Accepter.getInstance());
        accept.setDaemon(true);
        accept.setName("ACCEPTER");
        accept.start();

        //等一下其他线程，5s
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.lastInformed = System.currentTimeMillis();

        //Watchdog start to ticking
        while(System.currentTimeMillis() - lastInformed <= GameInfo.MaxTickLength){
            if (System.currentTimeMillis() - lastInformed > GameInfo.MaxTickLength / 5){
                Log(Importance.WARNING, "[Watch Dog] The server is running " + (System.currentTimeMillis() - lastInformed) + " ms behind, what's the matter?");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log(Importance.SEVERE, "[Watch Dog] Something happen to the watch dog: I CAN'T SLEEP, BECAUSE: " + e.getMessage());
            }
        }
        //超时自动关闭
        Log(Importance.SEVERE, "[Watch Dog] THE SERVER IS NOW OUT OF PERFORMANCE, NOW CLOSING IT!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log(Importance.SEVERE, "[Watch Dog] THE SERVER IS NOW OUT OF PERFORMANCE, NOW CLOSING IT!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log(Importance.SEVERE, "[Watch Dog] THE SERVER IS NOW OUT OF PERFORMANCE, NOW CLOSING IT!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log(Importance.SEVERE, "[Watch Dog] THE SERVER IS NOW OUT OF PERFORMANCE, NOW CLOSING IT!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log(Importance.SEVERE, "[Watch Dog] THE SERVER IS NOW OUT OF PERFORMANCE, NOW CLOSING IT!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log(Importance.SEVERE, "[Watch Dog] THE SERVER IS NOW OUT OF PERFORMANCE, NOW CLOSING IT!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log(Importance.SEVERE, "[Watch Dog] THE SERVER IS NOW OUT OF PERFORMANCE, NOW CLOSING IT!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log(Importance.SEVERE, "[Watch Dog] THE SERVER IS NOW OUT OF PERFORMANCE, NOW CLOSING IT!!!!!!!!!!!!!!!!!!!!!!!!!");
        try {
            MapManager.getInstance().saveMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void inform(){
        this.lastInformed = System.currentTimeMillis();
        //Log(Importance.DEBUG, "[Watch Dog] Hey, I'm kicked!");
    }
}
