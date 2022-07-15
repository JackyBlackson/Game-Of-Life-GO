package com.jackyblackson.gameoflifego.client.main;

import com.almasb.fxgl.dsl.FXGL;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.client.map.ChunkMatainer;
import com.jackyblackson.gameoflifego.client.net.ConnectionManager;
import com.jackyblackson.gameoflifego.shared.common.Importance;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class ClientTicker {

    /**
     * After the method, the tile on screen display will refresh to the newest version synced from the server
     */
    public static void run() {
        ChunkMatainer.getInstance().AdjustChunkOnDisplay(GameOfLIifeGoApp.camera);
    }
}
