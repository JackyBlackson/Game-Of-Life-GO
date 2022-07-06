package com.jackyblackson.gameoflifego.server.player;

import com.jackyblackson.gameoflifego.server.main.ServerMain;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String color;

    public Player(String name, String color){
        this.name = name;
        this.color = color;
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
}
