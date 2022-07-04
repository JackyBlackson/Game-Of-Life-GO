package com.jackyblackson.gameoflifego.server.info;

public class Pos {
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
        return new Pos(worldPos.getX()%16, worldPos.getY()%16);
    }

    public static Pos getPosInChunk(Pos worldPos){
        return new Pos(worldPos.getX() - worldPos.getX()%16, worldPos.getY() - worldPos.getY()%16);
    }

    public static Pos getWorldPos(Pos chunkPos, Pos posInChunk){
        return new Pos(posInChunk.getX() + chunkPos.getX() * 16, posInChunk.getY() + chunkPos.getY() * 16);
    }
}
