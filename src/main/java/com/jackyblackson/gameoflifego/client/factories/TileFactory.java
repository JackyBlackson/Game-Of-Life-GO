package com.jackyblackson.gameoflifego.client.factories;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.jackyblackson.gameoflifego.client.entities.VacuumEntity;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.player.Player;
import com.jackyblackson.gameoflifego.shared.tiles.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

/**
 * This is a factory class with only one method to build-and-return an entity of a tile according to its type
 */
public class TileFactory {
    /**
     * This method builds-and-returns a built entity according to the type of {@param tile} on {@param worldPos}.
     * @param tile The tile to be built entity on
     * @param worldPos The world position of the tile, NOT THE GAME WORLD POSITION OF THE ENTITY!!!!
     * @return The fine-built entity
     */
    public Entity build(Tile tile, Pos worldPos){
        //the entity to return
        Entity eb = null;
        //get the scaling of the game world
        float scaling = GamePlayInfo.getInstance().scaling;
        //get the scaled world position
        Pos scaledPos = GamePlayInfo.getInstance().getScaledPos(worldPos);

        //if the tile is a cell, then build a cell
        if(tile instanceof Cell t) {
            //the owner of the cell
            Player p = t.getOwner();
            //calculates the cell color (both inner and outer) according to the player's user color
            Color inner = Color.web(t.getOwner().getColor(), 1.0);
            Color outer = Color.web(t.getOwner().getColor(), 0.6).darker().darker();    //the outer color should be darker and have less opacity
            //prepare a canvas node for the cell's view
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(outer);
            gc.fillRoundRect(0, 0, 1.0 * scaling, 1.0 * scaling, 0.25 * scaling, 0.25 * scaling);
            gc.setFill(inner);
            gc.fillRoundRect(0.1 * scaling, 0.1 * scaling, 0.8 * scaling, 0.8 * scaling, 0.2 * scaling, 0.2 * scaling);

            //build the entity of this cell
            eb = new EntityBuilder()
                    .view(canvas)                               //set its view
                    .at(scaledPos.getX(), scaledPos.getY())     //set its position
                    .type(TileType.CELL)                        //set its type
                    .build();                                   //build it
        }

        //if the tile is a water tile
        else if (tile instanceof Water wa){
            //prepare the canvas
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.1, 0.2, 0.7, 0.6));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new ImagePattern(new Image("assets/textures/water.png")));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

            //build it
            eb = new EntityBuilder()
                    .view(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.WATER)
                    .build();
        }

        //if the entity is an oxygen tile
        else if (tile instanceof Oxygen os){
            //preparing its view node
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.6, 0.6, 0.6, 0.6));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new ImagePattern(new Image("assets/textures/oxygen.png")));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

            //build the entity
            eb = new EntityBuilder()
                    .view(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.ASTEROID)
                    .build();
        }

        //if tile is a carbon tile
        else if (tile instanceof Carbon ca){
            //preparing its view node
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.15, 0.14, 0.17, 0.6));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new ImagePattern(new Image("assets/textures/carbon.png")));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

            //build the entity
            eb = new EntityBuilder()
                    .view(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.ASTEROID)
                    .build();
        }

        //if the entity is a kind of asteroid entity
        else if (tile instanceof Asteroid as){
            //preparing its view node
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.1, 0.2, 0.15, 0.6));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new ImagePattern(new Image("assets/textures/asteroid.png")));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

            //build the entity
            eb = new EntityBuilder()
                    .view(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.ASTEROID)
                    .build();
        }

        //if the tile is a vacuum tile
        else if (tile instanceof Vacuum va){
            //use the vacuum entity class to construct it
            return new VacuumEntity(va, worldPos);
        }

        //return the built entity
        return eb;
    }
}
