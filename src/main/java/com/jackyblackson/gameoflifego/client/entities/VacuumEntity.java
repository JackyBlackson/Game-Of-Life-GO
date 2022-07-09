package com.jackyblackson.gameoflifego.client.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.tiles.TileType;
import com.jackyblackson.gameoflifego.shared.tiles.Vacuum;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class VacuumEntity extends Entity {
    Vacuum tile;
    boolean isSelected = false;

    public VacuumEntity(Vacuum vacuumTile, Pos worldPos) {

        this.tile = vacuumTile;
        float scaling = GamePlayInfo.getInstance().scaling;
        Pos scaledPos = GamePlayInfo.getInstance().getScaledPos(worldPos);
        Canvas canvas = new Canvas(GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        final Color[] origInner = {new Color(0.17, 0.17, 0.17, 0.8)};
        final Color[] origOuter = {new Color(0.225, 0.225, 0.225, 0.4)};

        gc.setLineWidth(0);
        gc.setFill(origOuter[0]);
        gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
        gc.setFill(origInner[0]);
        gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle
        canvas.onMouseEnteredProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gc.clearRect(0.0, 0.0, GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
                gc.setLineWidth(0);
                gc.setFill(new Color(0.625, 0.225, 0.225, 0.4));
                gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
                gc.setFill(new Color(0.37, 0.17, 0.17, 0.8));
                gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle
            }
        });
        canvas.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gc.clearRect(0.0, 0.0, GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
                gc.setLineWidth(0);
                gc.setFill(origOuter[0]);
                gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
                gc.setFill(origInner[0]);
                gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle
            }
        });
        canvas.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //没有被选择
                if(!isSelected){
                    origInner[0] = Color.web(GamePlayInfo.getInstance().player.getColor(), 0.8).darker();
                    origOuter[0] = Color.web(GamePlayInfo.getInstance().player.getColor(), 0.4).darker().darker();
                    gc.clearRect(0.0, 0.0, GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
                    gc.setLineWidth(0);
                    gc.setFill(origOuter[0]);
                    gc.fillOval(0.0, 0.0, 1.0 * scaling, 1.0 * scaling); //outer circle
                    gc.setFill(origInner[0]);
                    gc.fillOval(0.1 * scaling, 0.1 * scaling, 0.8 * scaling, 0.8 * scaling); //inner circle
                    //TODO: 添加到被选择的 HashSet 里面
                    isSelected = true;
                }
                //被选择
                else {
                    origInner[0] = new Color(0.17, 0.17, 0.17, 0.8);
                    origOuter[0] = new Color(0.225, 0.225, 0.225, 0.4);
                    gc.clearRect(0.0, 0.0, GamePlayInfo.getScaled(1.0d), GamePlayInfo.getScaled(1.0d));
                    gc.setLineWidth(0);
                    gc.setFill(origOuter[0]);
                    gc.fillOval(0.0,            0.0,            1.0 * scaling,  1.0 * scaling); //outer circle
                    gc.setFill(origInner[0]);
                    gc.fillOval(0.1 * scaling,  0.1 * scaling,  0.8 * scaling,  0.8 * scaling); //inner circle
                    //TODO: 从被选择的 HashSet 里面删除掉
                    isSelected = false;
                }
            }
        });

        this.getViewComponent().addChild(canvas);
        this.setPosition(scaledPos.getX(), scaledPos.getY());
        this.setType(TileType.VACUUM);
        FXGL.getGameWorld().addEntity(this);
    }
}
