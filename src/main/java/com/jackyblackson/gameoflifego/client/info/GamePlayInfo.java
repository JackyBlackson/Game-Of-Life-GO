package com.jackyblackson.gameoflifego.client.info;

import com.almasb.fxgl.app.GameApplication;
import com.jackyblackson.gameoflifego.shared.common.Pos;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class GamePlayInfo {
    private static GamePlayInfo instance = new GamePlayInfo();

    public String ip = "localhost";
    public int port = 26575;
    public float scaling = 16.0f;

    private GamePlayInfo(){}

    public static GamePlayInfo getInstance(){ return instance; }

    public InetSocketAddress getIP(){
        return new InetSocketAddress(ip, port);
    }

    public Pos getScaledPPos(Pos worldPos){
        return new Pos(worldPos.getX()*scaling, worldPos.getY()*scaling);
    }

    public static double getScaled(double var){
        return GamePlayInfo.getInstance().scaling * var;
    }
}
