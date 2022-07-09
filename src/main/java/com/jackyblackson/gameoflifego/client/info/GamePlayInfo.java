package com.jackyblackson.gameoflifego.client.info;

import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.player.Player;

import java.net.InetSocketAddress;

public class GamePlayInfo {
    private static GamePlayInfo instance = new GamePlayInfo();

    public String ip = "localhost";
    public int port = 26575;
    public float scaling = 32.0f;
    public float movingSpeed = 1.14514f;

    public Player player;

    private GamePlayInfo(){}

    public static GamePlayInfo getInstance(){ return instance; }

    public InetSocketAddress getIP(){
        return new InetSocketAddress(ip, port);
    }

    public Pos getScaledPos(Pos worldPos){
        return new Pos(worldPos.getX()*scaling, worldPos.getY()*scaling);
    }

    public static double getScaled(double var){
        return GamePlayInfo.getInstance().scaling * var;
    }
}
