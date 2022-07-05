package com.jackyblackson.gameoflifego.server.info;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class Pos implements Serializable {
    private Double posX;
    private Double posY;

    public Pos(Pos p){
        this.posX = p.getX();
        this.posY = p.getY();
    }

    public Pos(Double x, Double y){
        this.posX = x;
        this.posY = y;
    }

    public Pos(){
        this.posX = 0.0d;
        this.posY = 0.0d;
    }

    public Double getX(){
        return this.posX;
    }

    public Double getY(){
        return this.posY;
    }

    public void setX(Double x){
        this.posX = x;
    }

    public void setY(Double y){
        this.posY = y;
    }

    public void setPos(Pos p){
        this.posX = p.getX();
        this.posY = p.getY();
    }

    public void setPos(Double x, Double y){
        this.posX = x;
        this.posY = y;
    }

    public static Pos getChunkPos(Pos worldPos){
        return new Pos((Math.floor(worldPos.getX() / 16.0)), Math.floor(worldPos.getY() / 16.0));
    }

    public static Pos getPosInChunk(Pos worldPos){
        return new Pos(worldPos.getX() - 16.0 * (Math.floor(worldPos.getX() / 16.0)), worldPos.getY() - 16.0 * (Math.floor(worldPos.getY() / 16.0)));
    }

    public static Pos getWorldPos(Pos chunkPos, Pos posInChunk){
        return new Pos(posInChunk.getX() + chunkPos.getX() * 16, posInChunk.getY() + chunkPos.getY() * 16);
    }

    public static Pos getAreaPosFromChunkPos(Pos chunkPos){
        return new Pos(Math.floor(chunkPos.getX() / 4.0d), Math.floor(chunkPos.getY() / 4.0d));
    }

    public static Pos getPosInArea(Pos chunkPos){
        return new Pos(chunkPos.getX() - 4.0d * Math.floor(chunkPos.getX() / 4.0d), chunkPos.getY() - 4.0d * Math.floor(chunkPos.getY() / 4.0d));
    }

    public static Pos parsePos(String formatString){
        String[] s = formatString.split(", ");
        if (s.length == 2){
            return new Pos(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
        } else {
            Log(Importance.WARNING, "Cannot parse format string to Position object: the string \"" + formatString + "\" is broken.");
            return null;
        }
    }

    @Override
    public String toString(){
        return (posX + ", " + posY);
    }
}
