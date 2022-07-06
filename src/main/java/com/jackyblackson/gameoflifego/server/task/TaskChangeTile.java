package com.jackyblackson.gameoflifego.server.task;

import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.tiles.Tile;
import com.jackyblackson.gameoflifego.server.tiles.Vacuum;

public class TaskChangeTile extends TileTask{
    public Tile getTileToChange() {
        return tileToChange;
    }

    public void setTileToChange(Tile tileToChange) {
        this.tileToChange = tileToChange;
    }

    private Tile tileToChange;

    public TaskChangeTile() {
        super();
        this.tileToChange = new Vacuum(this.worldPos);
    }

    public TaskChangeTile(Pos worldPos, Tile tileToChange) {
        super(worldPos);
        this.tileToChange = tileToChange;
    }

    public TaskChangeTile(int x, int y, Tile tileToChange) {
        super(x, y);
        this.tileToChange = tileToChange;
    }

    public TaskChangeTile(double x, double y, Tile tileToChange) {
        super(x, y);
        this.tileToChange = tileToChange;
    }
}
