package com.jackyblackson.gameoflifego.server.main;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.server.net.Accepter;
import com.jackyblackson.gameoflifego.server.net.TCPServer;
import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.server.task.TaskGenerateChunk;
import com.jackyblackson.gameoflifego.shared.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.shared.map.generator.ChunkGenerator;
import com.jackyblackson.gameoflifego.shared.map.manager.MapManager;
import com.jackyblackson.gameoflifego.shared.player.Player;
import com.jackyblackson.gameoflifego.shared.player.PlayerSet;
import com.jackyblackson.gameoflifego.shared.tiles.Cell;

import java.io.IOException;

import static com.jackyblackson.gameoflifego.server.main.ServerMain.loadGameInfo;
import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class TestServerMain {
    //服务端开启入口
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Entrance(args);
        MapManager.getInstance();
        WatchDog.getInstance();
        TCPServer.getInstance();
        Accepter.getInstance();
        PlayerSet.getInstance();
        loadGameInfo();
        //ServerMain.entrance(args);
        firstGen();



        Player player = new Player("Jacky_Blackson", "11ffff");
        MapManager.getInstance().setTileAt(new Cell(new Pos(0.0, 0.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(-2.0, 0.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(2.0, 0.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(-2.0, 1.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(2.0, 1.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(-2.0, 2.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(2.0, 2.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(-2.0, 3.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(2.0, 3.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(-2.0, 4.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(2.0, 4.0), player));
        MapManager.getInstance().setTileAt(new Cell(new Pos(0.0, 4.0), player));
        for(double x = -2; x <= 2; x+=1){
            for(double y = 0; y <= 4; y += 1){
                MapManager.getInstance().setTileAt(new Cell(new Pos(x, y), player));
            }
        }

        /*
        for(int x = 1; x <= 16; x++){
            for(int y = 1; y <= 16; y++){
                MapManager.getInstance().setTileAt(new Cell(new Pos((double) -x, (double) y), player));
            }
        }

         */

        printArea();

        while (true) {

            MapManager.getInstance().updateMap();
            System.out.println("=================================================================================");

            printArea();

            MapManager.getInstance().saveAllArea();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static void printArea() {
        Chunk coo = MapManager.getInstance().getChunkAt(new Pos(0.0, 0.0));
        Chunk cox = MapManager.getInstance().getChunkAt(new Pos(0.0, -1.0));
        Chunk cxo = MapManager.getInstance().getChunkAt(new Pos(-1.0, 0.0));
        Chunk cxx = MapManager.getInstance().getChunkAt(new Pos(-1.0, -1.0));

        for (int y = 15; y >= 0; y--) {
            String[] sxo = cxo.toString().split("\n");
            String[] soo = coo.toString().split("\n");
            System.out.println(sxo[y] + soo[y]);
        }

        for (int y = 15; y >= 0; y--) {
            String[] sxx = cxx.toString().split("\n");
            String[] sox = cox.toString().split("\n");
            System.out.println(sxx[y] + sox[y]);
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


    //服务端总入口
    public static void Entrance(String[] args) throws IOException {
        //读取配置信息
        Log(Importance.INFO, "server started");
        /*
        Chunk c0_0 = ChunkGenerator.generate(new Pos(0.0d, 0.0d));
        System.out.println("generated:");
        System.out.println(c0_0);
         */
        double x = 0.0d;
        double y = 3.0d;
        while (true) {
            Chunk cl = ChunkGenerator.generate(new Pos(x, y));
            Chunk cr = ChunkGenerator.generate(new Pos(x, y + 1.0d));
            MapManager.getInstance().addTask(new TaskGenerateChunk(new Pos(x, y)));
            MapManager.getInstance().addTask(new TaskGenerateChunk(new Pos(x, y + 1.0d)));
            MapManager.getInstance().executeAllTask();
            //System.out.println("For Chunk on (" + x + ", " + y +"):");
            String[] cls = cl.toString().split("\n");
            String[] crs = cr.toString().split("\n");
            for (int i = 0; i < 16; i++) {
                System.out.println(cls[i] + crs[i]);
            }
            x += 1.0d;

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
