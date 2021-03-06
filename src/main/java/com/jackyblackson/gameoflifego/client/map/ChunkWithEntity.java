package com.jackyblackson.gameoflifego.client.map;

import com.almasb.fxgl.entity.Entity;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.chunk.Chunk;

import java.util.HashMap;
import java.util.Set;

/**
 * This class is barely chunk with a set of entity
 * It is convenient to use this type of chunk to remove loaded chunks from screen
 */
public class ChunkWithEntity {
    Chunk chunk;
    Pos chunkPos;
    HashMap<String, Entity> entitySet;

    public ChunkWithEntity(Chunk chunk){
        this.chunk = chunk;
        this.chunkPos = chunk.getChunkPos();
        this.entitySet = new HashMap<>();
    }

    public HashMap<String, Entity> getEntitySet() {
        return entitySet;
    }
}
