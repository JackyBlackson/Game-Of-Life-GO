package com.jackyblackson.gameoflifego.server.tiles;

import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.player.Player;

import java.awt.*;
import java.util.Vector;

public abstract class Tile {
    private Pos worldPos;
    private Player owner;

    public Pos getPos(){
        return this.worldPos;
    }

    public Player getOwner(){
        return owner;
    }
}
