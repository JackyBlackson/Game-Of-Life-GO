package com.jackyblackson.gameoflifego.server.tiles;

import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.player.Player;

import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

public abstract class Tile implements Serializable {
    private Pos worldPos;
    private Player owner;

    public Tile(Pos worldPos){
        this.worldPos = worldPos;
    }

    public Pos getPos(){
        return this.worldPos;
    }

    public Player getOwner(){
        return owner;
    }
}
