package com.jackyblackson.gameoflifego.server.main;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.server.net.TCPServer;
import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.map.manager.MapManager;
import com.jackyblackson.gameoflifego.shared.player.PlayerSet;

import java.io.IOException;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class Ticker implements Runnable{
    private long timeStamp = System.currentTimeMillis();
    private long tickTimes = 0L;
    private long saveTimes = 0L;
    private long updateTimes = 0L;

    @Override
    public void run() {
        Log(Importance.INFO, "[Ticker] Now the world is ticking: TICK TOCK TICK TOCK TICK TOCK");

        while(true) {//更新当前的时间戳，（开始计时）
            this.timeStamp = System.currentTimeMillis();

            if(GameInfo.MaxEvolution <= 0){
                TCPServer.getInstance().isGameEnd = true;
            }

            //处理网络需求
            //TODO: 处理积累的细胞放置需求
            MapManager.getInstance().executeNetTask();

            //处理地图更新，如果可行的话
            if (tickTimes - updateTimes >= GameInfo.TicksPerEvolution) {
                MapManager.getInstance().updateMap();
                Log(Importance.DEBUG, "[Ticker] Updated Map!!!");
                updateTimes = tickTimes;
                GameInfo.MaxEvolution--;
            }

            //重新计算分数
            PlayerSet.getInstance().calculateScore();

            //处理地图保存，如果可行的话
            if (tickTimes - saveTimes >= GameInfo.TicksPerSave) {
                try {
                    MapManager.getInstance().saveAllArea();
                    Log(Importance.DEBUG, "[Ticker] Saved Areas!!!");
                } catch (IOException e) {
                    Log(Importance.WARNING, "Cannot save the game map currently, because: " + e.getMessage());
                    Log(Importance.WARNING, "This may cause your game recently lost...");
                }
                saveTimes = tickTimes;
            }
            //更新tick计时器，以便下一轮更新和保存功能使用
            tickTimes++;
            //提醒看门狗，程序没有卡住
            WatchDog.getInstance().inform();
            //等待，直到满足最高tps的要求
            while ((System.currentTimeMillis() - timeStamp) < (1000.0d / (double) GameInfo.MaxTicksPerSecond)) {}
        }
    }
}
