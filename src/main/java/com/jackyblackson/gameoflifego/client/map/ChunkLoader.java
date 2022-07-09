package com.jackyblackson.gameoflifego.client.map;

import com.jackyblackson.gameoflifego.client.factories.TileFactory;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.area.Area;
import com.jackyblackson.gameoflifego.shared.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.shared.tiles.Tile;

public class ChunkLoader {
    public static void loadChunk(Chunk chunk){
        Pos chunkPos = chunk.getChunkPos();
        for(int x = 0; x < 16; x+=1){
            for(int y = 0; y < 16; y+=1){
                Tile t = chunk.getTileAt(x, y);
                if(t != null){
                    new TileFactory().build(t, Pos.getWorldPos(chunkPos, new Pos((double) x, (double) y)));
                }
            }
        }
    }

    public static void loadArea(Area a){
        for(int ax = 0; ax < 4; ax++){
            for(int ay = 0; ay < 4; ay++){
                Chunk c = a.getChunkAt(ax,ay);
                if(c != null){
                    loadChunk(c);
                }
            }
        }
    }
}
