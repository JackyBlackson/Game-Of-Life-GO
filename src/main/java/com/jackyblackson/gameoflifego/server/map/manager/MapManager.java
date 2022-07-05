package com.jackyblackson.gameoflifego.server.map.manager;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.map.area.Area;
import com.jackyblackson.gameoflifego.server.map.generator.ChunkGenerator;
import com.jackyblackson.gameoflifego.server.task.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

//Singleton
public class MapManager {
    private static MapManager instance = new MapManager();
    //managed chunks will be in this list
    private HashMap<String, Area> managed;
    private Queue<Task> tasks;

    private MapManager(){
        this.managed = new HashMap();
        this.tasks = new LinkedBlockingQueue<Task>();
    }

    public static MapManager getInstance(){return instance;}

    public void appendArea(Area a){
        managed.put(a.getAreaPos().toString(), a);
    }

    public void save() throws IOException {
        for(Iterator<Area> it = managed.values().iterator(); it.hasNext(); ){
            Area a = it.next();
            FileOutputStream fileOut = new FileOutputStream("Save/" + GameInfo.Save + "/" + a.getAreaPos() + ".gla");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(a);
            out.close();
            fileOut.close();
        }
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void executeAllTask(){
        Task task = null;
        //取出所有的任务并执行
        while ((task = tasks.poll()) != null){
            //执行的是区块的操作
            if(task instanceof ChunkTask){
                //是生成区块的操作
                if(task instanceof TaskGenerateChunk){
                    generateChunk((TaskGenerateChunk) task);
                }
            }
            //执行的是区域的操作
            else if (task instanceof AreaTask){

            }
        }
    }

    private void generateChunk(TaskGenerateChunk task){
        //如果任务所需要的区块在已有的区域里面
        Pos areaPos = Pos.getAreaPosFromChunkPos(task.chunkPos);
        if(managed.containsKey(areaPos.toString())){
            managed.get(areaPos.toString()).setChunkAt(Pos.getPosInArea(task.chunkPos), ChunkGenerator.generate(task.chunkPos));
            Log(Importance.INFO, "MapManager: Successfully add and generated one chunk at [" + task.chunkPos + "]");
        } else {
            managed.put (areaPos.toString(), new Area(areaPos));
            managed.get(areaPos.toString()).setChunkAt(Pos.getPosInArea(task.chunkPos), ChunkGenerator.generate(task.chunkPos));
            Log(Importance.INFO, "MapManager: Successfully add and generated one chunk at [" + task.chunkPos + "] (Chunk Pos) with new area at [" + areaPos + "] (Area Pos)");
        }
    }
}
