package com.jackyblackson.gameoflifego.server.task;

import com.jackyblackson.gameoflifego.server.info.Pos;

public abstract class AreaTask extends Task {
    public Pos areaPos;

    public AreaTask(){
        areaPos = new Pos(0.0d, 0.0d);
    }

    public AreaTask(Pos areaPos){
        this.areaPos = areaPos;
    }

    public AreaTask(int x, int y){
        areaPos = new Pos((double) x, (double) y);
    }

    public AreaTask(double x, double y){
        areaPos = new Pos(x, y);
    }
}
