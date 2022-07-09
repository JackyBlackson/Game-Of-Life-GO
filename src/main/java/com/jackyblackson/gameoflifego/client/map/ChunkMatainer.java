package com.jackyblackson.gameoflifego.client.map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.area.Area;
import com.jackyblackson.gameoflifego.shared.map.chunk.Chunk;

import java.util.HashMap;
import java.util.Iterator;

public class ChunkMatainer {
    /**
     * Try to load chunks that need to be on the screen, and try to unload chunks that is not on the screen
     * @param player the entity which the camera attaches to, at this case, is the player
     */
    private static ChunkMatainer instance;

    private ChunkMatainer(){
        this.loadedChunkList = new HashMap<>();
    }

    public static ChunkMatainer getInstance(){ return instance; }

    public HashMap<String, ChunkWithEntity> loadedChunkList;  //The list of loaded chunks, can be added/removed by key, which is the string of POS chunk position
    public HashMap<String, Area> managedAreaHashMap;

    public void loadChunk(){

    }

    /**
     * Will load areas that need to be display on the screen currently,
     * and unload chunks that are invisible currently
     * also will automatically manage the loadedChunkList
     * @param player
     */
    public void AdjustChunkOnDisplay(Entity player){
        //BASIC CALCULATIONS
        double width = FXGL.getAppWidth();
        double height = FXGL.getAppHeight();
        //calculate the max and min position of chunk that can appear on the screen
        Pos maxChunkPos = Pos.getChunkPos(
                new Pos(player.getX() + width / 2.0d, player.getY() + height / 2.0d)    //pos of pixel in the bottom-right corner of the screen
                        .scaledBy(1.0 / GamePlayInfo.getInstance().scaling));                 //transpose it to real worldPosition, not position of display
        Pos minChunkPos = Pos.getChunkPos(
                new Pos(player.getX() - width / 2.0d, player.getY() - height / 2.0d)    //pos of pixel in the top-left corner of the screen
                        .scaledBy(1.0 / GamePlayInfo.getInstance().scaling));                 //transpose it to real worldPosition, not position of display
        //////REMOVING ALL INVISIBLE CHUNKS//////
        //////REMOVING ALL INVISIBLE CHUNKS//////
        //////REMOVING ALL INVISIBLE CHUNKS//////
        for (ChunkWithEntity chunk : loadedChunkList.values()) {
            if (                                                                                                // CONDITION:
                    (chunk.chunkPos.getX() < minChunkPos.getX() || chunk.chunkPos.getX() > maxChunkPos.getX())  // X position is out of visible range
                            ||                                                                                  // OR
                    (chunk.chunkPos.getY() < minChunkPos.getY() || chunk.chunkPos.getY() > maxChunkPos.getY())  // Y position is out of visible range
            ) {                                                                                                 // Then it is invisible, remove it
                //REMOVES ALL THE TILE ENTITIES OF THIS CHUNK
                FXGL.getGameWorld().removeEntities(chunk.getEntitySet());
                //REMOVES THIS CHUNK FROM THE LOADED CHUNK LIST
                loadedChunkList.remove(chunk.chunk.getChunkPos().toString());
            }
        }
        //////ADDING ALL VISIBLE CHUNKS//////
        //////ADDING ALL VISIBLE CHUNKS//////
        //////ADDING ALL VISIBLE CHUNKS//////
        for(double x = minChunkPos.getX(); x <= maxChunkPos.getX(); x+=1.0d){
            for(double y = minChunkPos.getY(); y <= maxChunkPos.getY(); y+=1.0d){
                //TODO: get chunks from client-side area manager:
                //  if there's one in managed hashset, then get & load it
                //  if it's not found in the managed hashset, then require its area from the server
                //      and it will be added in the next tick of the client if it has successfully required from the server
            }
        }
    }
}
