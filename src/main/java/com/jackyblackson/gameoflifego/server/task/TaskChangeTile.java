package com.jackyblackson.gameoflifego.server.task;

import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.tiles.Tile;
import com.jackyblackson.gameoflifego.shared.tiles.Vacuum;

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
