package com.jackyblackson.gameoflifego.client.map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.jackyblackson.gameoflifego.client.factories.TileFactory;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.client.net.ConnectionManager;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.area.Area;
import com.jackyblackson.gameoflifego.shared.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.shared.tiles.Tile;

import java.util.concurrent.ConcurrentHashMap;

public class ChunkMatainer {
    private static ChunkMatainer instance = new ChunkMatainer();

    private ChunkMatainer(){
        this.loadedChunkList = new ConcurrentHashMap<>();
        this.downloadedChunkHashMap = new ConcurrentHashMap<>();
    }

    public static ChunkMatainer getInstance(){ return instance; }

    public ConcurrentHashMap<String, ChunkWithEntity> loadedChunkList;  //The list of loaded chunks, can be added/removed by key, which is the string of POS chunk position
    public ConcurrentHashMap<String, Chunk> downloadedChunkHashMap;

    public void loadChunk(){

    }

    /**
     * Will load areas that need to be display on the screen currently,
     * and unload chunks that are invisible currently...
     * also will automatically manage the loadedChunkList
     * @param player the player that the camera attaches to
     */
    public void AdjustChunkOnDisplay(Entity player){
        //BASIC CALCULATIONS
        double width = FXGL.getAppWidth();
        double height = FXGL.getAppHeight();
        double scale = GamePlayInfo.getInstance().scaling;

        //calculate the max and min position of chunk that can appear on the screen
        Pos maxChunkPos = Pos.getChunkPos(
                new Pos(
                        Math.floor((player.getX() + width) / scale) ,
                        Math.floor((player.getY() + height) / scale)
                ));                 //transpose it to real worldPosition, not position of display
        Pos minChunkPos = Pos.getChunkPos(
                new Pos(
                        Math.floor((player.getX() - width) / scale) ,
                        Math.floor((player.getY() - height) / scale)
                ));                //transpose it to real worldPosition, not position of display



        //////REMOVING ALL INVISIBLE CHUNKS//////
        //////REMOVING ALL INVISIBLE CHUNKS//////
        //////REMOVING ALL INVISIBLE CHUNKS//////
        for (ChunkWithEntity chunkLoaded : loadedChunkList.values()) {
            if (                                                                                                            // CONDITION:
                    (chunkLoaded.chunkPos.getX() < minChunkPos.getX() || chunkLoaded.chunkPos.getX() > maxChunkPos.getX())  // X position is out of visible range
                            ||                                                                                              // OR
                    (chunkLoaded.chunkPos.getY() < minChunkPos.getY() || chunkLoaded.chunkPos.getY() > maxChunkPos.getY())  // Y position is out of visible range
            ) {
                // Then it is invisible, remove it
                //REMOVES ALL THE TILE ENTITIES OF THIS CHUNK
                FXGL.getGameWorld().removeEntities(chunkLoaded.getEntitySet().values());
                //REMOVES THIS CHUNK FROM THE LOADED CHUNK LIST
                loadedChunkList.remove(chunkLoaded.chunk.getChunkPos().toString());
            }
        }

        //////UPDATE DISPLAY//////
        //////UPDATE DISPLAY//////
        //////UPDATE DISPLAY//////
        for(double x = minChunkPos.getX(); x < maxChunkPos.getX(); x+=1.0d){
            for(double y = minChunkPos.getY(); y < maxChunkPos.getY(); y+=1.0d){
                //newer version of this chunk, synced from server
                Chunk downloadedChunk = downloadedChunkHashMap.get(new Pos(x,y).toString());
                //older chunk, from the cache
                ChunkWithEntity loadedChunk = loadedChunkList.get(new Pos(x,y).toString());
                if(downloadedChunk != null && loadedChunk == null){    //This chunk is not loaded
                    this.loadedChunkList.put(downloadedChunk.getChunkPos().toString(), ChunkLoader.loadChunk(downloadedChunk));
                } else if (downloadedChunk != null && loadedChunk != null){     //this chunk is now loading
                    for(int chunkX = 0; chunkX < 16; chunkX ++){
                        for(int chunkY = 0; chunkY < 16; chunkY++){
                            Tile oldTile = loadedChunk.chunk.getTileAt(chunkX, chunkY);
                            Tile newTile = downloadedChunk.getTileAt(chunkX, chunkY);
                            if(Tile.isTileChange(oldTile, newTile)){       //The state of this tile changed
                                Pos worldPos = oldTile.getWorldPos();    //re calculate the world position
                                //remove entity from the loadedChunk and FXGL's game world
                                FXGL.getGameWorld().removeEntity(loadedChunk.getEntitySet().get(worldPos.toString()));
                                loadedChunk.getEntitySet().remove(worldPos.toString());
                                //build and load new entity to the loadedChunk and FXGL's game world
                                Entity entity = new TileFactory().build(newTile, worldPos);
                                FXGL.getGameWorld().addEntity(entity);
                                loadedChunk.getEntitySet().put(worldPos.toString(), entity);
                                loadedChunk.chunk.setTileAt(chunkX, chunkY, downloadedChunk.getTileAt(chunkX, chunkY));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Will load areas that need to be display on the screen currently,
     * and unload chunks that are invisible currently...
     * also will automatically manage the loadedChunkList
     * @param player the player that the camera attaches to
     */
    public void downloadChunksFromServer(Entity player) {
        double width = FXGL.getAppWidth();
        double height = FXGL.getAppHeight();
        double scale = GamePlayInfo.getInstance().scaling;
        //calculate the max and min position of chunk that can appear on the screen
        Pos maxChunkPos = Pos.getChunkPos(
                new Pos(
                        Math.floor(player.getX() / scale + width / (2.0d * scale)) + 2.0d,
                        Math.floor(player.getY() / scale + height / (2.0d * scale)) + 2.0d
                ));                 //transpose it to real worldPosition, not position of display
        Pos minChunkPos = Pos.getChunkPos(
                new Pos(
                        Math.floor(player.getX() / scale - width / (2.0d * scale)) - 2.0d,
                        Math.floor(player.getY() / scale - height / (2.0d * scale)) - 2.0d
                ));                //transpose it to real worldPosition, not position of display

        for(double x = minChunkPos.getX(); x <= maxChunkPos.getX(); x += 1.0d){
            for(double y = minChunkPos.getY(); y <= maxChunkPos.getY(); y += 1.0d){
                Chunk c = ConnectionManager.getInstance().requireChunkAt(new Pos(x, y));
                if(c != null){
                    ChunkMatainer.getInstance().downloadedChunkHashMap.put(c.getChunkPos().toString(), c);
                }
            }
        }
    }
}
