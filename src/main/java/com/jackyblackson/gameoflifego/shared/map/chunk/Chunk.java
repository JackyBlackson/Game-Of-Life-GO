package com.jackyblackson.gameoflifego.shared.map.chunk;

import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.manager.MapManager;
import com.jackyblackson.gameoflifego.shared.tiles.*;

import java.io.Serializable;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class Chunk implements Serializable {
    //position of the chunk, suppose to be on the left-down corner of the chunk
    private Pos chunkPos;
    private Tile[][] tiles;

    public Chunk(Pos chunkPos) {
        this.chunkPos = chunkPos;
        tiles = new Tile[16][16];
    }

    public Pos getChunkPos() {
        return chunkPos;
    }

    public Pos getTilePos(Pos posInChunk) {
        return new Pos(posInChunk.getX() + chunkPos.getX() * 16, posInChunk.getY() + chunkPos.getY() * 16);
    }

    //access tile at a pos
    public Tile getTileAt(int xInChunk, int yInChunk) {
        try {
            Log(Importance.DEBUG, "[Chunk.getTileAT] The tile now at chunk (" + chunkPos + ").(" + xInChunk + "," + yInChunk + ") is " + this.tiles[xInChunk][yInChunk]);
            return (this.tiles[xInChunk][yInChunk]);
        } catch (Exception ex) {
            Log(Importance.DEBUG, "[Chunk.getTileAT] The tile now at chunk (" + chunkPos + ").(" + xInChunk + "," + yInChunk + ") is NULL");
            Log(Importance.SEVERE, ex.getMessage() + ex.getStackTrace());
            return null;
        }
    }

    public void setTileAt(int xInChunk, int yInChunk, Tile tile) {
        //showStackTrace(xInChunk, yInChunk);

        try {
            Pos worldPos = Pos.getWorldPos(chunkPos, new Pos((double) xInChunk, (double) yInChunk));
            tile.setWorldPos(worldPos);
            tiles[xInChunk][yInChunk] = tile;
            //Log(Importance.DEBUG, "[Chunk.setTileAT] The tile now at chunk (" + chunkPos + ").(" + xInChunk + "," + yInChunk + ") is " + this.tiles[xInChunk][yInChunk]);
            if (tile instanceof Cell) {
                MapManager.getInstance().nCellList.add((Cell) tile);
                Log(Importance.DEBUG, "[Chunk.setTileAT] Added one cell to the CELLLIST");
            }
        } catch (Exception ex) {
            Log(Importance.SEVERE, ex.getMessage() + ex.getStackTrace());
            return;
        }
    }

    private void showStackTrace(int xInChunk, int yInChunk) {
        StackTraceElement[] st = new Throwable().getStackTrace();
        System.out.println("============ CHUNK : SET TILE AT (" + chunkPos + ").(" + xInChunk + "," + yInChunk + ")============");
        for(StackTraceElement ste : st){
            System.out.println(ste.getClassName()+"."+ste.getMethodName()+"(...);");
            System.out.println("-- "+ste.getMethodName());
            System.out.println("-- "+ste.getFileName());
            System.out.println("-- "+ste.getLineNumber());
        }
        System.out.println("==========================================================");
    }

    public void hello(){
        int a = 0;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int  y =  0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                if(tiles[x][y].getWorldPos().equals(new Pos(0.0, 0.0))){
                    out.append("OO ");
                }
                else if (tiles[x][y] instanceof Vacuum) {
                    out.append("   ");
                } else if (tiles[x][y] instanceof Cell) {
                    out.append("XX ");
                } else if (tiles[x][y] instanceof Asteroid) {
                    out.append("As ");
                } else if (tiles[x][y] instanceof Oxygen) {
                    out.append("Ox ");
                } else if (tiles[x][y] instanceof Carbon) {
                    out.append("Ca ");
                } else if (tiles[x][y] instanceof Water) {
                    out.append("Wa ");
                } else if (tiles[x][y] instanceof Tile) {
                    out.append("TT ");
                }

                out.append("  ");
            }
            out.append("\n");
        }
        return out.toString();
    }
}
