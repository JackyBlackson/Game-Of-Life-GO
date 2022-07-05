package com.jackyblackson.gameoflifego.server.main;

import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.server.map.generator.ChunkGenerator;
import com.jackyblackson.gameoflifego.server.map.manager.MapManager;
import com.jackyblackson.gameoflifego.server.task.TaskGenerateChunk;

import java.io.IOException;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class TestServerMain {
    //服务端开启入口
    public static void main(String[] args) throws IOException {
        //Entrance(args);
        System.out.println(-11.0d % 4.0d );
        System.out.println(Pos.getChunkPos(new Pos(-15.0d, 15d)));
        System.out.println(Pos.getPosInChunk(new Pos(-15.0d, 15d)));
        System.out.println(Pos.getPosInChunk(new Pos(-16d, 0d)));
        Entrance(args);
    }

    //服务端总入口
    public static void Entrance(String[] args) throws IOException {
        //读取配置信息
        Log(Importance.INFO, "server started");
        /*
        Chunk c0_0 = ChunkGenerator.generate(new Pos(0.0d, 0.0d));
        System.out.println("generated:");
        System.out.println(c0_0);
         */
        double x = 0.0d; double y = 3.0d;
        while(true) {
            Chunk cl = ChunkGenerator.generate(new Pos(x,y));
            Chunk cr = ChunkGenerator.generate(new Pos(x,y+1.0d));
            MapManager.getInstance().addTask(new TaskGenerateChunk(new Pos(x,y)));
            MapManager.getInstance().addTask(new TaskGenerateChunk(new Pos(x,y + 1.0d)));
            MapManager.getInstance().executeAllTask();
            //System.out.println("For Chunk on (" + x + ", " + y +"):");
            String[] cls = cl.toString().split("\n");
            String[] crs = cr.toString().split("\n");
            for(int i = 0; i < 16; i++){
                System.out.println(cls[i] + crs[i]);
            }
            x += 1.0d;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
