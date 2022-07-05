package com.jackyblackson.gameoflifego.server.map.chunk;

import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.tiles.*;

import java.io.Serializable;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class Chunk implements Serializable {
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

    //access tile at a pos
    public Tile getTileAt(int xInChunk, int yInChunk){
        try {
            return (this.tiles[xInChunk][yInChunk]);
        } catch (Exception ex) {
            Log(Importance.SEVERE, ex.getMessage() + ex.getStackTrace());
            return null;
        }
    }

    public void setTileAt(int xInChunk, int yInChunk, Tile tile){
        try {
            this.tiles[xInChunk][yInChunk] = tile;
        } catch (Exception ex) {
            Log(Importance.SEVERE, ex.getMessage() + ex.getStackTrace());
            return;
        }
    }

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder();
        for(int x = 0; x < 16; x++){
            for(int y = 0; y < 16; y++){
                if(tiles[x][y] instanceof Vacuum){
                    out.append("  ");
                }
                else if (tiles[x][y] instanceof Asteroid){
                    out.append("■ ");
                }
                else if (tiles[x][y] instanceof Oxygen){
                    out.append("⌀ ");
                }
                else if (tiles[x][y] instanceof Carbon){
                    out.append("© ");
                }
                else if (tiles[x][y] instanceof Cell){
                    out.append("▢ ");
                }
                else if (tiles[x][y] instanceof Water){
                    out.append("▤ ");
                }

                out.append("  ");
            }
            out.append("\n");
        }
        return out.toString();
    }
}
