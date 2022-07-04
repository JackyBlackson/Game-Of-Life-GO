package com.jackyblackson.gameoflifego.server.map.generator;

import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.map.chunk.Chunk;

public class ChunkGenerator {
    public static Chunk generate(Pos chunkPos){
        Chunk c = new Chunk(chunkPos);
        for(int x = 0; x < 16; x++){
            for(int y = 0; y < 16; y++){

            }
        }

        return c;
    }
}
