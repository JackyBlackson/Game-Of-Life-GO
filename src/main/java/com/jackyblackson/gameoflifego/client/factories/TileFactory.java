package com.jackyblackson.gameoflifego.client.factories;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.jackyblackson.gameoflifego.client.entities.VacuumEntity;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.player.Player;
import com.jackyblackson.gameoflifego.shared.tiles.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class TileFactory {
    public Entity build(Tile tile, Pos worldPos){
        Entity eb = null;
        float scaling = GamePlayInfo.getInstance().scaling;
        Pos scaledPos = GamePlayInfo.getInstance().getScaledPos(worldPos);
        //Log(Importance.NOTICE, "[Tile Factory] Preparing tile at (" + worldPos + "), scaled to +(" + scaledPos + "): " + tile);

        if(tile instanceof Cell t) {
            Player p = t.getOwner();
            //Color inner = new Color(p.getColorR(), p.getColorG(), p.getColorB(), 1.0f);
            //Color outer = new Color(Math.max(p.getColorR() - 0.1, 0.0), Math.max(p.getColorG() - 0.1, 0.0), Math.max(p.getColorB() + 0.1, 1.0), 0.5f);
            Color outer = new Color(0.1, 0.5, 1.0, 0.5);
            Color inner = new Color(0.0, 0.4, 0.9, 0.5);
            //准备Cell填充的节点：
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setStroke(outer);
            gc.setFill(inner);
            gc.fillRoundRect(0, 0, 1.0 * scaling, 1.0 * scaling, 0.25 * scaling, 0.25 * scaling);
            gc.fillRoundRect(0.1 * scaling, 0.1 * scaling, 0.8 * scaling, 0.8 * scaling, 0.2 * scaling, 0.2 * scaling);

            eb = new EntityBuilder()
                    .at(scaledPos.getX(), scaledPos.getY())
                    .viewWithBBox(canvas)
                    .type(TileType.CELL)
                    .anchorFromCenter()
                    .build();
            return eb;
        }

        else if (tile instanceof Water wa){
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.1, 0.2, 0.7, 0.6));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new ImagePattern(new Image("assets/textures/water.png")));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

            eb = new EntityBuilder()
                    .viewWithBBox(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.WATER)
                    .build();
        }

        else if (tile instanceof Oxygen os){
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.6, 0.6, 0.6, 0.6));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new ImagePattern(new Image("assets/textures/oxygen.png")));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

            eb = new EntityBuilder()
                    .viewWithBBox(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.ASTEROID)
                    .build();
        }

        else if (tile instanceof Carbon ca){
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.15, 0.14, 0.17, 0.6));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new ImagePattern(new Image("assets/textures/carbon.png")));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

            eb = new EntityBuilder()
                    .viewWithBBox(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.ASTEROID)
                    .build();
        }

        else if (tile instanceof Asteroid as){
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.1, 0.2, 0.15, 0.6));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new ImagePattern(new Image("assets/textures/asteroid.png")));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle

            eb = new EntityBuilder()
                    .viewWithBBox(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.ASTEROID)
                    .build();
        }

        else if (tile instanceof Vacuum va){
            return new VacuumEntity(va, worldPos);
        }
        if(eb != null){
            FXGL.getGameWorld().addEntity(eb);
        }
        return eb;
    }
}
