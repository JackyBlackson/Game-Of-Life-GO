package com.jackyblackson.gameoflifego.server.map.chunk;

import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.tiles.Tile;

public class Chunk {
    //position of the chunk, suppose to be on the left-down corner of the chunk
    private Pos chunkPos;
    private Tile[][] tiles;

    public Chunk(Pos chunkPos){
        this.chunkPos = chunkPos;
        tiles = new Tile[16][16];
    }

    public Pos getChunkPos() {
        return chunkPos;
    }

    public Pos getTilePos(Pos posInChunk){
        return new Pos(posInChunk.getX() + chunkPos.getX() * 16, posInChunk.getY() + chunkPos.getY() * 16);
    }
}
