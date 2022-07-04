package com.jackyblackson.gameoflifego.server.map.manager;

import com.jackyblackson.gameoflifego.server.map.chunk.Chunk;

import java.util.ArrayList;
import java.util.Map;

//Singleton
public class MapManager {
    private static MapManager instance = new MapManager();
    private ArrayList<Chunk> managed;

    private MapManager(){
        this.managed = new ArrayList<>();
    }

    public static MapManager getInstance(){return instance;}
}
