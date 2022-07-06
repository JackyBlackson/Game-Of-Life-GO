package com.jackyblackson.gameoflifego.server.tiles;

import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.player.Player;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class Cell extends Tile{
    private Player owner;

    public Cell(Pos worldPos, Player owner){
        super(worldPos);
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()){
            if(((Cell) obj).getOwner().getName().equals(this.owner.getName())){
                Log(Importance.DEBUG, "CELL >>> EQUALS");
                return true;
            } else {
                Log(Importance.DEBUG, "CELL >>> NOT_EQUALS");
                return false;
            }
        } else {
            Log(Importance.DEBUG, "CELL >>> NOT_CELL");
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (getWorldPos().getY().toString() + getWorldPos().getX().toString()).hashCode() ^ getOwner().getName().hashCode();
    }
}
