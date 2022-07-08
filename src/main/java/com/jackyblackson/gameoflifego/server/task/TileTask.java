package com.jackyblackson.gameoflifego.server.task;

import com.jackyblackson.gameoflifego.shared.common.Pos;

public class TileTask extends Task{
    public Pos worldPos;

    public TileTask(){
        worldPos = new Pos(0.0d, 0.0d);
    }

    public TileTask(Pos worldPos){
        this.worldPos = worldPos;
    }

    public TileTask(int x, int y){
        worldPos = new Pos((double) x, (double) y);
    }

    public TileTask(double x, double y){
        worldPos = new Pos(x, y);
    }
}
