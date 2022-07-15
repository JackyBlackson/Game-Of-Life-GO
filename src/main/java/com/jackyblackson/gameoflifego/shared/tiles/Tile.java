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
        //Log(Importance.INFO, "Tile.Equals!");
        //Log(Importance.NOTICE, "[Tile.equals] (" + obj.getClass().toString() + ") v.s. (" + this.getClass().toString() + ")");
        if(obj.getClass().toString().equals(this.getClass().toString())){
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

    public static boolean isTileChange(Tile oldTile, Tile newTile){
        if(newTile instanceof Cell newCell){
            if(!(oldTile instanceof Cell oldCEll)){     //从其他种类变为细胞
                return true;
            } else {                            //从细胞变为细胞
                if(oldCEll.getOwner().getName().equals(newCell.getOwner().getName())){
                    return false;
                } else {
                    return true;
                }
            }
        }
        else if (newTile instanceof Vacuum && oldTile instanceof Cell){ //从细胞变为真空
            return true;
        }
        else return false;
    }
}
