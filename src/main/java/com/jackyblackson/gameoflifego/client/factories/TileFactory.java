package com.jackyblackson.gameoflifego.client.factories;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.player.Player;
import com.jackyblackson.gameoflifego.shared.tiles.Cell;
import com.jackyblackson.gameoflifego.shared.tiles.Tile;
import com.jackyblackson.gameoflifego.shared.tiles.TileType;
import com.jackyblackson.gameoflifego.shared.tiles.Vacuum;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.function.Consumer;

import static com.almasb.fxgl.dsl.FXGL.getUIFactoryService;

public class TileFactory {
    public static Entity build(Tile tile, Pos worldPos){
        Entity eb = null;
        float scaling = GamePlayInfo.getInstance().scaling;
        Pos scaledPos = GamePlayInfo.getInstance().getScaledPPos(worldPos);
        if(tile instanceof Cell t) {
            Player p = t.getOwner();
            Color inner = new Color(p.getColorR(), p.getColorG(), p.getColorB(), 1.0f);
            Color outer = new Color(Math.max(p.getColorR() - 0.1, 0.0), Math.max(p.getColorG() - 0.1, 0.0), Math.max(p.getColorB() + 0.1, 1.0), 0.5f);
            //准备Cell填充的节点：
            Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(0);
            gc.setStroke(outer);
            gc.setFill(inner);
            gc.fillRoundRect(0, 0, 1.0 * scaling, 1.0 * scaling, 0.25 * scaling, 0.25 * scaling);
            gc.fillRoundRect(0.1 * scaling, 0.1 * scaling, 0.8 * scaling, 0.8 * scaling, 0.2 * scaling, 0.2 * scaling);
            gc.applyEffect(new Bloom());

            eb = new EntityBuilder()
                    .at(scaledPos.getX(), scaledPos.getY())
                    .viewWithBBox(canvas)
                    .view(new Text(scaledPos.getX() + GamePlayInfo.getScaled(0.5), scaledPos.getX() + GamePlayInfo.getScaled(0.5), p.getName()))
                    .type(TileType.ASTEROID)
                    .anchorFromCenter()
                    .build();
            return eb;
        }
        else if (tile instanceof Vacuum va){
            eb = new EntityBuilder()
                    .viewWithBBox(new Rectangle(scaledPos.getX(), scaledPos.getY(), new Color(0,0,0,0)))
                    .build();
        }
        return eb;
    }
}
