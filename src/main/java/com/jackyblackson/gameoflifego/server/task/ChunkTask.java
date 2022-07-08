package com.jackyblackson.gameoflifego.server.task;

import com.jackyblackson.gameoflifego.shared.common.Pos;

public abstract class ChunkTask extends Task {
    public Pos chunkPos;

    public ChunkTask(){
        chunkPos = new Pos(0.0d, 0.0d);
    }

    public ChunkTask(Pos chunkPos){
        this.chunkPos = chunkPos;
    }

    public ChunkTask(int x, int y){
        chunkPos = new Pos((double) x, (double) y);
    }

    public ChunkTask(double x, double y){
        chunkPos = new Pos(x, y);
    }
}
