package com.jackyblackson.gameoflifego.server.map.manager;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.map.area.Area;
import com.jackyblackson.gameoflifego.server.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.server.map.generator.ChunkGenerator;
import com.jackyblackson.gameoflifego.server.map.saves.SaveInfo;
import com.jackyblackson.gameoflifego.server.player.Player;
import com.jackyblackson.gameoflifego.server.task.*;
import com.jackyblackson.gameoflifego.server.tiles.Cell;
import com.jackyblackson.gameoflifego.server.tiles.Tile;
import com.jackyblackson.gameoflifego.server.tiles.Vacuum;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

//Singleton
public class MapManager {
    private static MapManager instance = new MapManager();
    //managed chunks will be in this list
    private HashMap<String, Area> managedAreaHashMap;
    private HashSet<Cell> cellList;

    public HashSet<Cell> nCellList;
    private Queue<Task> tasks;

    private MapManager() {
        this.managedAreaHashMap = new HashMap<>();
        this.tasks = new LinkedBlockingQueue<>();
        this.cellList = new HashSet<>();
        this.nCellList = new HashSet<>();
    }

    public static MapManager getInstance() {
        return instance;
    }

    public void appendArea(Area a) {
        managedAreaHashMap.put(a.getAreaPos().toString(), a);
    }

    /**
     * This method is used to save all the Areas in the managed list.
     * If the area file (i.e. .gla file) has already existed, it will be overwritten.
     *
     * @throws IOException Throws if the program comes across exception when trying to write the file.
     */
    public void saveAllArea() throws IOException {
        for (Area a : managedAreaHashMap.values()) {
            saveArea(a);
        }
    }

    public void saveArea(Area a) throws IOException {
        String s = SaveInfo.getDirForArea(a);
        File f = new File(s);
        //如果输出的文件夹不存在
        if (!f.exists()) {
            f.createNewFile();
        }
        //写入序列化的Area类并保存
        FileOutputStream fileOut = new FileOutputStream(s);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(a);
        //关闭流
        out.close();
        fileOut.close();
    }

    public void saveArea(Pos areaPos) throws IOException {
        Area a = getAreaAt(areaPos);
        if(a != null){
            saveArea(a);
        } else {
            Log(Importance.WARNING, "Cannot save area at (" + areaPos + "), because: this area is null...");
        }
    }

    public Area loadAreaAt(Pos areaPos){
        File areaFile = new File(SaveInfo.getDirForArea(areaPos));
        if(areaFile.exists()){
            try {
                ObjectInputStream oi = new ObjectInputStream(new FileInputStream(areaFile));
                Area a = (Area) oi.readObject();
                //Find all the cells in the area
                for(int x = 0; x < 4; x++){
                    for(int y = 0; y < 4; y++){
                        Chunk c = a.getChunkAt(x, y);
                        if(c != null){
                            for (int cx = 0; cx < 16; cx++) {
                                for (int cy = 0; cy < 16; cy++) {
                                    Tile t = c.getTileAt(cx, cy);
                                    if (t instanceof Cell) {
                                        nCellList.add((Cell) t);
                                    }
                                }
                            }
                        }
                    }
                }
                managedAreaHashMap.put(a.getAreaPos().toString(), a);
                return a;
            } catch (IOException | ClassNotFoundException e) {
                Log(Importance.SEVERE, "Cannot load Area at (" + areaPos + "), because: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Tile getTileAt(Pos worldPos) {
        Chunk c = getChunkAt(Pos.getChunkPos(worldPos));
        Pos posInChunk = Pos.getPosInChunk(worldPos);
        return c.getTileAt(posInChunk.getX().intValue(), posInChunk.getY().intValue());
    }

    public void setTileAt(Pos worldPos, Tile tile) {
        Chunk c = getChunkAt(Pos.getChunkPos(worldPos));
        Pos posInChunk = Pos.getPosInChunk(worldPos);
        tile.setWorldPos(worldPos);
        c.setTileAt(posInChunk.getX().intValue(), posInChunk.getY().intValue(), tile);
    }

    /**
     * Another way to set tile at a specific world coordinate,
     * However, this method uses the world coordinate inside the tile,
     * rather than what user provide in the method.
     * @param tile the tile need to be set
     */
    public void setTileAt(Tile tile) {
        Chunk c = getChunkAt(Pos.getChunkPos(tile.getWorldPos()));
        Pos posInChunk = Pos.getPosInChunk(tile.getWorldPos());
        c.setTileAt(posInChunk.getX().intValue(), posInChunk.getY().intValue(), tile);
        //Log(Importance.INFO, "The tile at (" + tile.getWorldPos() + ") is " + getTileAt(tile.getWorldPos()));
    }

    public Chunk getChunkAt(Pos chunkPos) {
        Pos areaPos = Pos.getAreaPosFromChunkPos(chunkPos);
        Area a = getAreaAt(areaPos);
        Chunk c = a.getChunkAt(Pos.getPosInArea(chunkPos));
        if (c == null) {      //此区块没有生成
            a.setChunkAt(Pos.getPosInArea(chunkPos), ChunkGenerator.generate(chunkPos));
            return a.getChunkAt(Pos.getPosInArea(chunkPos));
        } else {            //区块已经生成完成，直接返回
            return c;
        }

    }

    public Area getAreaAt(Pos areaPos) {
        Area a = managedAreaHashMap.get(areaPos.toString());
        if (a == null) {    //内存里面不存在，去磁盘存储里面找
            a = loadAreaAt(areaPos);
            if(a == null){  //磁盘存储里面不存在
                managedAreaHashMap.put(areaPos.toString(), new Area(areaPos));
                Log(Importance.DEBUG, "Created a new AREA at (" + areaPos + ") (Area Position)");
                return managedAreaHashMap.get(areaPos.toString());
            } else {        //磁盘里面找到了，直接加载进内存
                managedAreaHashMap.put(a.getAreaPos().toString(), a);
                return a;
            }
        } else {            //磁盘里面存在
            return a;
        }
    }

    public void updateMap() {
        cellList = nCellList;
        nCellList = new HashSet<>();
        for (Iterator<Cell> it = this.cellList.iterator(); it.hasNext(); ) {

            Cell cell = it.next();
            //System.out.println("Now processing Cell at (" + cell.getWorldPos() + ")...");
            //检测自己：
            processTiles(cell);
            //开始检测这个细胞周围的八个细胞，如果是细胞那么自动跳过
            if (! (getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(0.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(0.0)));       //右
            if (! (getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(0.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(0.0)));      //左
            if (! (getTileAt(cell.getWorldPos().shiftX(0.0).shiftY(1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(0.0).shiftY(1.0)));       //上
            if (! (getTileAt(cell.getWorldPos().shiftX(0.0).shiftY(-1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(0.0).shiftY(-1.0)));      //下

            if (! (getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(1.0)));       //右上
            if (! (getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(-1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(-1.0)));       //右下
            if (! (getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(1.0)));      //左上
            if (! (getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(-1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(-1.0)));      //左下
        }
        //开始更改所有添加的tile
        executeAllTask();
    }

    private void processTiles(Tile tile) {
        String amtOfCells = calculateCellsArount(tile);
        boolean finished = false;
        //是否生成细胞
        for (String amt : GameInfo.AmountNeedForBirth) {
            if (amt.equals(amtOfCells)) {
                Cell nCell = new Cell(tile.getWorldPos(), calculateCellOwner(tile));
                nCellList.add(nCell);
                MapManager.getInstance().addTask(new TaskChangeTile(nCell.getWorldPos(), nCell));
                finished = true;
                break;
            }
        }

        //是否保持细胞存活
        if (!finished) {
            for (String amt : GameInfo.AmountNeedForSurvive) {
                if (amt.equals(amtOfCells)) {
                    if (tile instanceof Cell) {
                        nCellList.add((Cell) tile);
                        MapManager.getInstance().addTask(new TaskChangeTile(tile.getWorldPos(), new Cell(tile.getWorldPos(), calculateCellOwner(tile))));
                    }
                    finished = true;
                    break;
                }
            }
        }

        //细胞死亡
        if (!finished && tile instanceof Cell) {
            MapManager.getInstance().addTask(new TaskChangeTile(tile.getWorldPos(), new Vacuum(tile.getWorldPos())));
        }
    }

    private Player calculateCellOwner(Tile tile) {
        //TODO:完善细胞所属玩家的统计
        return new Player("Jacky_Blackson", "FFFFFF");
    }

    private String calculateCellsArount(Tile tile) {
        int amt = 0;
        //左
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() - 1.0d,
                tile.getWorldPos().getY()
        )) instanceof Cell) {
            amt++;
        }
        //右
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() + 1.0d,
                tile.getWorldPos().getY()
        )) instanceof Cell) {
            amt++;
        }
        //上
        if (getTileAt(new Pos(
                tile.getWorldPos().getX(),
                tile.getWorldPos().getY() + 1.0d
        )) instanceof Cell) {
            amt++;
        }
        //下
        if (getTileAt(new Pos(
                tile.getWorldPos().getX(),
                tile.getWorldPos().getY() - 1.0d
        )) instanceof Cell) {
            amt++;
        }

        //左上
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() - 1.0d,
                tile.getWorldPos().getY() + 1.0d
        )) instanceof Cell) {
            amt++;
        }
        //右上
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() + 1.0d,
                tile.getWorldPos().getY() + 1.0d
        )) instanceof Cell) {
            amt++;
        }
        //左下
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() - 1.0d,
                tile.getWorldPos().getY() - 1.0d
        )) instanceof Cell) {
            amt++;
        }
        //右下
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() + 1.0d,
                tile.getWorldPos().getY() - 1.0d
        )) instanceof Cell) {
            amt++;
        }
        //返回一个String，方便与配置文件比较
        //Log(Importance.NOTICE, "[CellAmountCalculation] The tile at (" + tile.getWorldPos() + ") has " + amt + " cell around.");
        return Integer.toString(amt);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void executeAllTask() {
        Task task;
        //取出所有的任务并执行
        while ((task = tasks.poll()) != null) {
            //执行的是区块的操作
            if (task instanceof ChunkTask) {
                //是生成区块的操作
                if (task instanceof TaskGenerateChunk) {
                    generateChunk((TaskGenerateChunk) task);
                }
            }
            //执行的是区域的操作
            else if (task instanceof AreaTask) {
                if (task instanceof TaskSaveArea) {
                    try {
                        saveAllArea();
                        Log(Importance.DEBUG, "Successfully saved all chunks in areas.");
                    } catch (IOException e) {
                        Log(Importance.SEVERE, "Saving: Cannot save the world, because " + e.getMessage());
                        Log(Importance.SEVERE, Arrays.toString(e.getStackTrace()));
                        throw new RuntimeException(e);
                    }
                }
            }

            else if (task instanceof TileTask){
                if (task instanceof TaskChangeTile t){    //更改区域的任务
                    setTileAt(t.worldPos, t.getTileToChange());
                }
            }
        }
    }

    private void generateChunk(TaskGenerateChunk task) {
        //如果任务所需要的区块在已有的区域里面
        Pos areaPos = Pos.getAreaPosFromChunkPos(task.chunkPos);
        getChunkAt(task.chunkPos);
    }
}
