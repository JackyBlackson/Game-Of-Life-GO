package com.jackyblackson.gameoflifego.client.factories;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.player.Player;
import com.jackyblackson.gameoflifego.shared.tiles.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.texture;
import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

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
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setFill(new Color(0.225, 0.225, 0.225, 0.4));
            gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
            gc.setFill(new Color(0.17, 0.17, 0.17, 0.8));
            gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle


            eb = new EntityBuilder()
                    .viewWithBBox(canvas)
                    .at(scaledPos.getX(), scaledPos.getY())
                    .type(TileType.VACUUM)
                    /*
                    .onClick(new Consumer<Entity>() {
                        @Override
                        public void accept(Entity entity) {
                            run(new SetTileAction(new Pos(entity.getX()/GamePlayInfo.getInstance().scaling, entity.getY()/GamePlayInfo.getInstance().scaling)), Duration.seconds(10));
                        }
                    })

                     */
                    .build();
        }
        if(eb != null){
            FXGL.getGameWorld().addEntity(eb);
        }
        return eb;
    }

    public class SetTileAction implements Runnable{
        Pos worldPos;
        Player player = GamePlayInfo.getInstance().player;
        public SetTileAction(Pos worldPos){
            this.worldPos = worldPos;
        }

        @Override
        public void run() {
            try {
                Socket s = new Socket(GamePlayInfo.getInstance().ip, GamePlayInfo.getInstance().port);
                s.getOutputStream().write(("%SETTILE:" + worldPos.toString()).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
