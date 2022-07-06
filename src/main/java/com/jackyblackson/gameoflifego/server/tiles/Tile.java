package com.jackyblackson.gameoflifego.server.tiles;

import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.player.Player;

import java.io.Serializable;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public abstract class Tile implements Serializable {
    private Pos worldPos;

    public Tile(Pos worldPos){
        this.worldPos = worldPos;
    }

    public void setWorldPos(Pos worldPos) {
        this.worldPos = worldPos;
    }

    public Pos getWorldPos(){
        return this.worldPos;
    }

    @Override
    public boolean equals(Object obj) {
        Log(Importance.INFO, "Tile.Equals!");

        if(obj.getClass() == this.getClass()){
            if(((Tile) obj).getWorldPos().equals(this.worldPos)){
                if (this instanceof Cell){
                    if(((Cell) this).getOwner().getName().equals(((Cell) obj).getOwner().getName())){
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
