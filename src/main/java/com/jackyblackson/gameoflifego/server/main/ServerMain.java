package com.jackyblackson.gameoflifego.server.main;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.map.manager.MapManager;
import com.jackyblackson.gameoflifego.server.task.TaskGenerateArea;
import com.jackyblackson.gameoflifego.server.task.TaskGenerateChunk;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class ServerMain {
    public static void main(String[] args) {
        //Starting
        Log(Importance.INFO, "Starting GAME OF LIFE: GO server...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Generating
        Log(Importance.INFO, "Generating chunks for the map...");
        firstGen();
        //Launching
        Log(Importance.INFO, "Now launching server...");
    }

    private static void firstGen() {
        //TODO: 根据初始生成范围在地图管理器中添加足够多的area

        for(long x = -GameInfo.SpawnRange; x < GameInfo.SpawnRange ; x++){
            for (long y = -GameInfo.SpawnRange; y < GameInfo.SpawnRange; y++){
                //TODO：生成相应的区块并保存在正确的area里面
                MapManager.getInstance().addTask(new TaskGenerateChunk(new Pos((double) x, (double) y)));
            }
        }

        MapManager.getInstance().executeAllTask();
        //TODO：最终命令地图管理器持久化初始生成的地图数据
    }
}
