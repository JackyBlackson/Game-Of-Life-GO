package com.jackyblackson.gameoflifego.shared.player;

import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Utility;

import java.io.Serializable;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class Player implements Serializable {
    private String name;
    private String color;
    private long cells;

    public void setAmountOfWater(long amountOfWater) {
        this.amountOfWater = amountOfWater;
    }

    public void setAmountOfCarbon(long amountOfCarbon) {
        this.amountOfCarbon = amountOfCarbon;
    }

    public void setAmountOfOxygen(long amountOfOxygen) {
        this.amountOfOxygen = amountOfOxygen;
    }

    public long getAmountOfWater() {
        return amountOfWater;
    }

    public long getAmountOfCarbon() {
        return amountOfCarbon;
    }

    public long getAmountOfOxygen() {
        return amountOfOxygen;
    }

    private long amountOfWater;
    private long amountOfCarbon;
    private long amountOfOxygen;

    public long getCells() {
        return cells;
    }

    public void setCells(long cells) {
        this.cells = cells;
    }

    public Player(String name, String color){
        this.name = name;
        this.color = color;
        cells = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void addCell(){
        this.cells++;
    }

    public void addCell(long amount){
        this.cells += amount;
    }

    public double getColorR(){
        try{
            String r = color.substring(0,2);
            double out = 0.0d;
            out += 16.0d * (double) Utility.getHexValue(r.charAt(0));
            out += (double) Utility.getHexValue(r.charAt(1));
            return (out / 255.0d);
        } catch (Exception e){
            Log(Importance.WARNING, "[Player] Illegal color string of player " + name + ", because: " + e.getMessage());
            return 0.0d;
        }
    }

    public double getColorG(){
        try{
            String r = color.substring(2,4);
            double out = 0.0d;
            out += 16.0d * (double) Utility.getHexValue(r.charAt(0));
            out += (double) Utility.getHexValue(r.charAt(1));
            return (out / 255.0d);
        } catch (Exception e){
            Log(Importance.WARNING, "[Player] Illegal color string of player " + name + ", because: " + e.getMessage());
            return 0.0d;
        }
    }

    public double getColorB(){
        try{
            String r = color.substring(4,6);
            double out = 0.0d;
            out += 16.0d * (double) Utility.getHexValue(r.charAt(0));
            out += (double) Utility.getHexValue(r.charAt(1));
            return (out / 255.0d);
        } catch (Exception e){
            Log(Importance.WARNING, "[Player] Illegal color string of player " + name + ", because: " + e.getMessage());
            return 0.0d;
        }
    }

    public String getStatistics(){
        return new StringBuilder()
                .append(cells)
                .append(";")
                .append(amountOfCarbon)
                .append(";")
                .append(amountOfWater)
                .append(";")
                .append(amountOfOxygen)
                .toString()
                ;
    }

}
