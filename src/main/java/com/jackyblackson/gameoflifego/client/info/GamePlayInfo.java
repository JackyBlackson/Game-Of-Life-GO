package com.jackyblackson.gameoflifego.client.info;

import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.player.Player;

import java.net.InetSocketAddress;

/**
 * This class stores all the information that the client need
 */
public class GamePlayInfo {
    private static GamePlayInfo instance = new GamePlayInfo();

    /**
     * ip of the server
     */
    public String ip = "localhost";
    /**
     * port of server
     */
    public int port = 26575;
    /**
     * Scaling amount of the client's interface
     */
    public float scaling =32.0f;

    /**
     * Moving speed (world position per key hit) of the camera
     */
    public float movingSpeed = 2.9172f;

    /**
     * The left evolution time synced from the server
     */
    public long evolutionLeft = 300;

    /**
     * The synced player score from the server
     */
    public String[] playerInfoStringSet;

    /**
     * The stored player information of the client's player
     */
    public Player player = new Player("Jacky_Blackson", "11ffff");

    private GamePlayInfo(){}

    /**
     * returns the singleton's instance
     * @return
     */
    public static GamePlayInfo getInstance(){ return instance; }

    /**
     * returns a InetSocketAddress of the server
     */
    public InetSocketAddress getIP(){
        return new InetSocketAddress(ip, port);
    }

    /**
     * returned a scaled world position
     * @param worldPos the world position to be translated
     * @return the scaled world position
     */
    public Pos getScaledPos(Pos worldPos){
        return new Pos(worldPos.getX()*scaling, worldPos.getY()*scaling);
    }

    /**
     * scaled a double value according to the singleton's scaling field
     * @param var the double value to be scaled
     * @return the scaled double value
     */
    public static double getScaled(double var){
        return GamePlayInfo.getInstance().scaling * var;
    }
}
