package com.jackyblackson.gameoflifego.server.map.area;

import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.server.player.Player;

import java.io.Serializable;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;


public class Area implements Serializable {
    private Pos areaPos;
    private Chunk[][] chunks = new Chunk[4][4];

    public Area(Pos areaPos){
        this.areaPos = areaPos;
    }

    public Pos getAreaPos() {
        return areaPos;
    }

    public boolean setChunkAt(Pos posInArea, Chunk chunk){
        if(0.0d <= posInArea.getX() && posInArea.getX() <= 3.0d && 0.0d <= posInArea.getY() && posInArea.getY() <= 3.0d){ //not out of bound
            int x = (int)Math.floor(posInArea.getX());
            int y = (int)Math.floor(posInArea.getY());
            try{
                chunks[x][y] = chunk;
            } catch (Exception ex) {
                Log(Importance.SEVERE, "Cannot set chunk at " + posInArea + " in area at " + areaPos + " (Area Position), because: " + ex.getMessage());
                Log(Importance.SEVERE, ex.getStackTrace().toString());
                return false;
            }
            return true;
        } else {
            Log(Importance.WARNING, "Position out of bound when setting chunk at " + posInArea + " in area at " + areaPos + " (Area Position)");
            return false;
        }
    }

    public Chunk getChunkAt(Pos posInArea){
        if(0.0d <= posInArea.getX() && posInArea.getX() <= 3.0d && 0.0d <= posInArea.getY() && posInArea.getY() <= 3.0d){ //not out of bound
            int x = (int)Math.floor(posInArea.getX());
            int y = (int)Math.floor(posInArea.getY());
            try{
                return chunks[x][y];
            } catch (Exception ex) {
                Log(Importance.SEVERE, "Cannot get chunk at " + posInArea + " in area at " + areaPos + " (Area Position), because: " + ex.getMessage());
                Log(Importance.SEVERE, ex.getStackTrace().toString());
                return null;
            }
        } else {
            Log(Importance.WARNING, "Position out of bound when getting chunk at " + posInArea + " in area at " + areaPos + " (Area Position)");
            return null;
        }
    }

    public Chunk getChunkAt(int x, int y){
        if(0 <= x && x <= 3 && 0 <= y && y <= 3){
            try{
                return chunks[x][y];
            } catch (Exception ex) {
                Log(Importance.SEVERE, "Cannot get chunk at (" + x + ", " + y + ") in area at " + areaPos + " (Area Position), because: " + ex.getMessage());
                Log(Importance.SEVERE, ex.getStackTrace().toString());
                return null;
            }
        } else {
            Log(Importance.WARNING, "Position out of bound when getting chunk at (" + x + ", " + y + ") in area at " + areaPos + " (Area Position)");
            return null;
        }
    }

    public boolean setChunkAt(int x, int y, Chunk chunk){
        if(0 <= x && x <= 3 && 0 <= y && y <= 3){
            try{
                chunks[x][y] = chunk;
            } catch (Exception ex) {
                Log(Importance.SEVERE, "Cannot set chunk at (" + x + ", " + y + ") in area at " + areaPos + " (Area Position), because: " + ex.getMessage());
                Log(Importance.SEVERE, ex.getStackTrace().toString());
                return false;
            }
            return true;
        } else {
            Log(Importance.WARNING, "Position out of bound when setting chunk at (" + x + ", " + y + ") in area at " + areaPos + " (Area Position)");
            return false;
        }
    }

}
