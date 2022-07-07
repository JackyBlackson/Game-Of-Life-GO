package com.jackyblackson.gameoflifego.server.main;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.WorldInfo;
import com.jackyblackson.gameoflifego.server.map.manager.MapManager;

import java.io.IOException;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class Ticker implements Runnable{
    private long timeStamp = System.currentTimeMillis();
    private long tickTimes = 0L;
    private long saveTimes = 0L;
    private long updateTimes = 0L;

    @Override
    public void run() {
        //更新当前的时间戳，（开始计时）
        this.timeStamp = System.currentTimeMillis();

        //处理网络需求

        //处理地图更新，如果可行的话
        if(tickTimes - updateTimes >= GameInfo.TicksPerEvolution){
            MapManager.getInstance().updateMap();
            updateTimes = tickTimes;
        }

        //处理地图保存，如果可行的话
        if(tickTimes - saveTimes >= GameInfo.TicksPerEvolution){
            try {
                MapManager.getInstance().saveAllArea();
            } catch (IOException e) {
                Log(Importance.WARNING, "Cannot save the game map currently, because: " + e.getMessage());
                Log(Importance.WARNING, "This may cause your game recently lost...");
            }
            saveTimes = tickTimes;
        }
        //Make the tick counter update
        tickTimes ++;
        //等待，直到满足最高tps的要求
        while(System.currentTimeMillis() - timeStamp < (1000d / (double) GameInfo.MaxTicksPerSecond));
    }
}
