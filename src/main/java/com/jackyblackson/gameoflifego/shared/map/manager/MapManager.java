package com.jackyblackson.gameoflifego.shared.map.manager;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.server.task.*;
import com.jackyblackson.gameoflifego.shared.map.area.Area;
import com.jackyblackson.gameoflifego.shared.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.shared.map.generator.ChunkGenerator;
import com.jackyblackson.gameoflifego.shared.map.saves.SaveInfo;
import com.jackyblackson.gameoflifego.shared.player.Player;
import com.jackyblackson.gameoflifego.shared.player.PlayerSet;
import com.jackyblackson.gameoflifego.shared.tiles.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

//Singleton
public class MapManager {
    private static MapManager instance = new MapManager();
    //managed chunks will be in this list
    private ConcurrentHashMap<String, Area> managedAreaHashMap;
    private HashSet<Cell> cellList;

    public HashSet<Cell> nCellList;
    private Queue<Task> tasks;

    private MapManager() {
        this.managedAreaHashMap = new ConcurrentHashMap<>();
        this.tasks = new LinkedBlockingQueue<>();
        this.cellList = new HashSet<>();
        this.nCellList = new HashSet<>();
    }

    public static MapManager getInstance() {
        return instance;
    }

    public Iterator<Area> getAreas() {
        return managedAreaHashMap.values().iterator();
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
        try {
            if (!f.exists()) {
                f.getParentFile().mkdir();
                f.createNewFile();
            }
            //写入序列化的Area类并保存
            FileOutputStream fileOut = new FileOutputStream(s);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(a);
            //关闭流
            out.close();
            fileOut.close();
        } catch (Exception ex) {
            Log(Importance.SEVERE, "[SAVE AREA] Cannot save area in file: " + f.getAbsolutePath() + ", because: " + ex.getMessage());
        }
    }

    public void saveArea(Pos areaPos) throws IOException {
        Area a = getAreaAt(areaPos);
        if (a != null) {
            saveArea(a);
        } else {
            Log(Importance.WARNING, "Cannot save area at (" + areaPos + "), because: this area is null...");
        }
    }

    public Area loadAreaAt(Pos areaPos) {
        File areaFile = new File(SaveInfo.getDirForArea(areaPos));
        if (areaFile.exists()) {
            try {
                ObjectInputStream oi = new ObjectInputStream(new FileInputStream(areaFile));
                Area a = (Area) oi.readObject();
                //Find all the cells in the area
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) {
                        Chunk c = a.getChunkAt(x, y);
                        if (c != null) {
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
        //Log(Importance.INFO, "The tile at (" + tile.getWorldPos() + ") is " + getTileAt(tile.getWorldPos()));
    }

    /**
     * Another way to set tile at a specific world coordinate,
     * However, this method uses the world coordinate inside the tile,
     * rather than what user provide in the method.
     *
     * @param tile the tile need to be set
     */
    public void setTileAt(Tile tile) {
        setTileAt(tile.getWorldPos(), tile);
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
            if (a == null) {  //磁盘存储里面不存在
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

    public Chunk requireChunkAt(Pos chunkPos) {
        Area a = managedAreaHashMap.get(Pos.getAreaPosFromChunkPos(chunkPos).toString());

        if(a == null){
            return null;
        } else {
            Pos posInArea = Pos.getPosInArea(chunkPos);
            Chunk c = a.getChunkAt(posInArea.getX().intValue(), posInArea.getY().intValue());
            if(c == null){
                return null;
            } else {
                return c;
            }
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
            if (!(getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(0.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(0.0)));       //右
            if (!(getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(0.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(0.0)));      //左
            if (!(getTileAt(cell.getWorldPos().shiftX(0.0).shiftY(1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(0.0).shiftY(1.0)));       //上
            if (!(getTileAt(cell.getWorldPos().shiftX(0.0).shiftY(-1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(0.0).shiftY(-1.0)));      //下

            if (!(getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(1.0)));       //右上
            if (!(getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(-1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(1.0).shiftY(-1.0)));       //右下
            if (!(getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(1.0)));      //左上
            if (!(getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(-1.0)) instanceof Cell))
                processTiles(MapManager.getInstance().getTileAt(cell.getWorldPos().shiftX(-1.0).shiftY(-1.0)));      //左下
        }
        //开始更改所有添加的tile
        executeAllTask();
    }

    private void processTiles(Tile tile) {
        String[] cal = calculateCellsArount(tile);
        String amtOfCells = cal[0];
        boolean finished = false;
        //是否生成细胞
        for (String amt : GameInfo.AmountNeedForBirth) {
            if (amt.equals(amtOfCells)) {
                Cell nCell = new Cell(tile.getWorldPos(), PlayerSet.instance.getPlayerByName(cal[1]));
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
                    if (tile instanceof Cell c) {
                        nCellList.add(c);
                        if (!(c.getOwner().getName().equals(cal[1]))) {
                            //Log(Importance.NOTICE, "[Updator] Update " + c.getOwner().getName() + "'s CELL to " + cal[1] + "'s CELL!!!!!");
                            MapManager.getInstance().addTask(new TaskChangeTile(tile.getWorldPos(), new Cell(tile.getWorldPos(), PlayerSet.getInstance().getPlayerByName(cal[1]))));
                        }
                    }
                    finished = true;
                    break;
                }
            }
        }

        //细胞死亡
        if (!finished && tile instanceof Cell c) {
            MapManager.getInstance().addTask(new TaskChangeTile(tile.getWorldPos(), new Vacuum(tile.getWorldPos())));
        }
    }

    private Player calculateCellOwner(Tile tile) {
        //TODO:完善细胞所属玩家的统计
        return new Player("Jacky_Blackson", "88FFAA");
    }

    private String[] calculateCellsArount(Tile tile) {
        HashMap<String, Integer> nameCount = new HashMap<>();
        int amt = 0;
        //左
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() - 1.0d,
                tile.getWorldPos().getY()
        )) instanceof Cell c) {
            amt++;
            String name = c.getOwner().getName();
            nameCount.put(name, nameCount.get(name) == null ? 1 : nameCount.get(name) + 1);
        }
        //右
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() + 1.0d,
                tile.getWorldPos().getY()
        )) instanceof Cell c) {
            amt++;
            String name = c.getOwner().getName();
            nameCount.put(name, nameCount.get(name) == null ? 1 : nameCount.get(name) + 1);
        }
        //上
        if (getTileAt(new Pos(
                tile.getWorldPos().getX(),
                tile.getWorldPos().getY() + 1.0d
        )) instanceof Cell c) {
            amt++;
            String name = c.getOwner().getName();
            nameCount.put(name, nameCount.get(name) == null ? 1 : nameCount.get(name) + 1);
        }
        //下
        if (getTileAt(new Pos(
                tile.getWorldPos().getX(),
                tile.getWorldPos().getY() - 1.0d
        )) instanceof Cell c) {
            amt++;
            String name = c.getOwner().getName();
            nameCount.put(name, nameCount.get(name) == null ? 1 : nameCount.get(name) + 1);
        }

        //左上
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() - 1.0d,
                tile.getWorldPos().getY() + 1.0d
        )) instanceof Cell c) {
            amt++;
            String name = c.getOwner().getName();
            nameCount.put(name, nameCount.get(name) == null ? 1 : nameCount.get(name) + 1);
        }
        //右上
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() + 1.0d,
                tile.getWorldPos().getY() + 1.0d
        )) instanceof Cell c) {
            amt++;
            String name = c.getOwner().getName();
            nameCount.put(name, nameCount.get(name) == null ? 1 : nameCount.get(name) + 1);
        }
        //左下
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() - 1.0d,
                tile.getWorldPos().getY() - 1.0d
        )) instanceof Cell c) {
            amt++;
            String name = c.getOwner().getName();
            nameCount.put(name, nameCount.get(name) == null ? 1 : nameCount.get(name) + 1);
        }
        //右下
        if (getTileAt(new Pos(
                tile.getWorldPos().getX() + 1.0d,
                tile.getWorldPos().getY() - 1.0d
        )) instanceof Cell c) {
            amt++;
            String name = c.getOwner().getName();
            nameCount.put(name, nameCount.get(name) == null ? 1 : nameCount.get(name) + 1);
        }
        //返回一个String，方便与配置文件比较
        //Log(Importance.NOTICE, "[CellAmountCalculation] The tile at (" + tile.getWorldPos() + ") has " + amt + " cell around.");
        String owner = "";
        Integer max = -1;
        for (String name : nameCount.keySet()) {
            Integer current = nameCount.get(name);
            if (current > max) {
                max = current;
                owner = name;
            }
        }
        return new String[]{Integer.toString(amt), owner};
    }

    public void saveMap() throws IOException {
        saveAllArea();
        savePlayers();
    }

    private void savePlayers() throws IOException {
        File f = new File(SaveInfo.getPlayerFileName());
        //如果输出的文件夹不存在
        if (!f.exists()) {
            f.getParentFile().mkdir();
            f.createNewFile();
        }
        //写入序列化的Area类并保存
        FileOutputStream fileOut = new FileOutputStream(f);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(PlayerSet.getInstance());
        //关闭流
        out.close();
        fileOut.close();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void executeAllTask() {
        Task task;
        //取出所有的任务并执行
        while ((task = tasks.poll()) != null) {
            //执行的是区块的操作
            executeTask(task);
        }
    }

    public void executeTask(Task task) {
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
        } else if (task instanceof TileTask) {
            if (task instanceof TaskChangeTile t) {    //更改区域的任务
                Tile tile = t.getTileToChange();
                Tile old = MapManager.getInstance().getTileAt(tile.getWorldPos());
                if (tile instanceof Cell cell) {
                    MapManager.getInstance().nCellList.add(cell);
                    if (old instanceof Cell oldCell) {
                        if (!(cell.getOwner().getName().equals(oldCell.getOwner().getName()))){
                            PlayerSet.getInstance().getPlayerByName(cell.getOwner().getName()).addCell(1);
                            PlayerSet.getInstance().getPlayerByName(oldCell.getOwner().getName()).addCell(-1);
                            //Log(Importance.NOTICE, "Cell change owner!");
                        } else {
                            //Log(Importance.NOTICE, "[Execute Task] WHY put " + cell.getOwner().getName() + "'s cell on " + oldCell.getOwner().getName() + "'s cell?????");
                        }
                    } else {
                        Player player = PlayerSet.getInstance().getPlayerByName(cell.getOwner().getName());
                        player.addCell(1);
                        if(old instanceof Asteroid){
                            player.addAmountOfOxygen(1);
                            player.addAmountOfCarbon(1);
                            player.addAmountOfWater(1);
                        }
                        else if(old instanceof Water){
                            player.addAmountOfWater(6);
                        } else if (old instanceof Carbon) {
                            player.addAmountOfCarbon(6);
                        } else if (old instanceof Oxygen) {
                            player.addAmountOfOxygen(6);
                        }
                        //Log(Importance.NOTICE, "New cell to the world!");
                    }
                    //Log(Importance.DEBUG, "[Chunk.setTileAT] Added one cell to the CELLLIST");
                }
                if(old instanceof Cell cell && tile instanceof Vacuum){
                    PlayerSet.getInstance().getPlayerByName(cell.getOwner().getName()).addCell(-1);
                }
                setTileAt(t.worldPos, tile);
            }
        }
    }

    public void executeNetTask(){
        for(Task task : tasks){
            if(task instanceof TaskNetSetCell cellTask){
                tasks.remove(task);
                Player player = PlayerSet.getInstance().getPlayerByName(cellTask.getCell().getOwner().getName());
                //TODO:网络setcell相关的资源消耗等等；
                if(
                        (player.getAmountOfOxygen() >= 1)
                        && (player.getAmountOfCarbon() >= 1)
                        && (player.getAmountOfWater() >= 1)
                ){
                    MapManager.getInstance().setTileAt(cellTask.getCell());
                    MapManager.getInstance().nCellList.add(cellTask.getCell());
                    player.addCell(1);
                    player.addAmountOfWater(-1);
                    player.addAmountOfOxygen(-1);
                    player.addAmountOfCarbon(-1);
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
