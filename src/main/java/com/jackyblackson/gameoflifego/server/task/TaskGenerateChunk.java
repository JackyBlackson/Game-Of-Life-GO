package com.jackyblackson.gameoflifego.server.task;

import com.jackyblackson.gameoflifego.server.info.Pos;

public class TaskGenerateChunk extends ChunkTask {
    public TaskGenerateChunk(Pos chunkPos){
        super(chunkPos);
    }

    public TaskGenerateChunk(int x, int y) {
        super(x, y);
    }
}
