package com.jackyblackson.gameoflifego.client.net;

import com.jackyblackson.gameoflifego.client.main.GameOfLIifeGoApp;
import com.jackyblackson.gameoflifego.client.map.ChunkMatainer;

public class CacheUpdator{
    public static void run() {
        //test if the game end
        ConnectionManager.getInstance().requireGameEnd();
        //require this player's statistics
        ConnectionManager.getInstance().requireStatistics();
        //require other player's scores
        ConnectionManager.getInstance().requirePlayerScore();
        //sync chunks from the server
        ChunkMatainer.getInstance().downloadChunksFromServer(GameOfLIifeGoApp.camera);
    }
}
