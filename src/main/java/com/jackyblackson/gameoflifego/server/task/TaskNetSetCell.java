package com.jackyblackson.gameoflifego.server.task;

import com.jackyblackson.gameoflifego.shared.tiles.Cell;
import com.jackyblackson.gameoflifego.shared.tiles.Tile;

public class TaskNetSetCell extends Task{
    private Cell cell;

    public TaskNetSetCell(Cell cell){
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
