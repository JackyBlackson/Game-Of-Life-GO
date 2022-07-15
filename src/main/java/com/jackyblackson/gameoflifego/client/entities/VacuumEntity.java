package com.jackyblackson.gameoflifego.client.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.client.net.ConnectionManager;
import com.jackyblackson.gameoflifego.server.net.ClientSocket;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.manager.MapManager;
import com.jackyblackson.gameoflifego.shared.tiles.Cell;
import com.jackyblackson.gameoflifego.shared.tiles.TileType;
import com.jackyblackson.gameoflifego.shared.tiles.Vacuum;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * This class is an entity class that describes the VACUUM tile.
 * As the vacuum tile is the only tile the player can interact,
 * I customized a class to set the controls
 */
public class VacuumEntity extends Entity {
    /**
     * The tile of the VACUUM TILE, in type of VACUUM
     */
    Vacuum tile;
    /**
     * Determines if the vacuum tile is selected
     */
    boolean isSelected = false;

    /**
     * Constructs a VacuumEntity with a tile and a worldPos
     * @param vacuumTile The Vacuum Tile that is presented by this VacuumEntity
     * @param worldPos The world position of the vacuum tile
     */
    public VacuumEntity(Vacuum vacuumTile, Pos worldPos) {
        //set the vacuum tile
        this.tile = vacuumTile;
        //get the world's scaling amount
        float scaling = GamePlayInfo.getInstance().scaling;
        //scale the worldPos to the GAME WORLD's position where the entity will be at this position
        Pos scaledPos = GamePlayInfo.getInstance().getScaledPos(worldPos);

        //The canvas is the look of the tile
        Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //Set the original (default) color of vacuum tile, this can be changed when it is clicked (to the user color)
        final Color[] origInner = {new Color(0.17, 0.17, 0.17, 0.8)};
        final Color[] origOuter = {new Color(0.225, 0.225, 0.225, 0.4)};

        //ignores the outer line with
        gc.setLineWidth(0);
        //set paint, and paint the outer circle
        gc.setFill(origOuter[0]);
        gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
        //set paint, and paint the inner circle
        gc.setFill(origInner[0]);
        gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

        //subscribe event of mouse enter, the vacuum entity will turn to dark red, means that is it pointed
        canvas.onMouseEnteredProperty().set(event -> {
            if(!isSelected){
                play("pointat.wav");
                gc.clearRect(0.0, 0.0, GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
                gc.setLineWidth(0);
                gc.setFill(new Color(0.625, 0.225, 0.225, 0.4));
                gc.fillOval(0.0, 0.0, 1.0 * scaling, 1.0 * scaling); //outer circle
                gc.setFill(new Color(0.37, 0.17, 0.17, 0.8));
                gc.fillOval(0.1 * scaling, 0.1 * scaling, 0.8 * scaling, 0.8 * scaling); //inner circle
            }
        });

        //subscribe event of mouse exit, the vacuum entity will turn to normal means that it is not pointed
        //note that the default color can be changed by the mouse click event to indicate the vacuum entity is selected,
        //but this doesn't trouble this event subscribing code, since it directed changes the default values.
        canvas.onMouseExitedProperty().set(event -> {
            gc.clearRect(0.0, 0.0, GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            gc.setLineWidth(0);
            gc.setFill(origOuter[0]);
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(origInner[0]);
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle
        });

        //subscribe event of mouse click,
        canvas.onMouseClickedProperty().set(event -> {
            //if the tile is NOT selected, then change it color to user color, and add it world position to the setCellList
            if(!isSelected){
                //Change the default value
                origInner[0] = Color.web(GamePlayInfo.getInstance().player.getColor(), 0.8).darker();
                origOuter[0] = Color.web(GamePlayInfo.getInstance().player.getColor(), 0.4).darker().darker();
                //clear the canvas, and re-draw it
                gc.clearRect(0.0, 0.0, GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
                gc.setLineWidth(0);
                gc.setFill(origOuter[0]);
                gc.fillOval(0.0, 0.0, 1.0 * scaling, 1.0 * scaling); //outer circle
                gc.setFill(origInner[0]);
                gc.fillOval(0.1 * scaling, 0.1 * scaling, 0.8 * scaling, 0.8 * scaling); //inner circle
                //set the value indicating that it is selected
                isSelected = true;
                //add the world position to the setCellList HashSet
                ConnectionManager.getInstance().addPosToSetCellList(tile.getWorldPos());
                int rand = FXGL.random(1,4);
                play("setcell" + rand + ".wav");
            }
            //else, if the cell is selected, then reset its default color, and removes its world position from the setCellList HashSet
            else {
                //reset the default color
                origInner[0] = new Color(0.17, 0.17, 0.17, 0.8);
                origOuter[0] = new Color(0.225, 0.225, 0.225, 0.4);
                //clear the canvas, and re-draw it
                gc.clearRect(0.0, 0.0, GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
                gc.setLineWidth(0);
                gc.setFill(origOuter[0]);
                gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
                gc.setFill(origInner[0]);
                gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle
                //removes it from the setCellList
                ConnectionManager.getInstance().deletePosToSetCellList(tile.getWorldPos());
                //set the value indicates that this tile is not selected
                play("deselect.wav");
                isSelected = false;
            }
        });




        this.getViewComponent().addChild(canvas);
        this.setPosition(scaledPos.getX(), scaledPos.getY());
        this.setType(TileType.VACUUM);
        FXGL.getGameWorld().addEntity(this);
    }
}
