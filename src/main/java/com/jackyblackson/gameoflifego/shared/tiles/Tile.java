package com.jackyblackson.gameoflifego.shared.tiles;

import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Pos;

import java.io.Serializable;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

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
