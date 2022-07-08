package com.jackyblackson.gameoflifego.shared.player;

import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.map.saves.SaveInfo;

import java.io.*;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class PlayerSet implements Serializable {
    public static PlayerSet instance = new PlayerSet();

    private Player[] playerSet;

    public static PlayerSet getInstance(){
        return instance;
    }

    public Player[] getPlayerSet() {
        return playerSet;
    }

    private PlayerSet(){
        this.playerSet = new Player[16];
    }

    public void loadPlayerSet() throws IOException, ClassNotFoundException {
        if(new File(SaveInfo.getPlayerFileName()).exists()){
            PlayerSet.getInstance();
            //读入序列化的object
            ObjectInputStream oi = new ObjectInputStream(new FileInputStream(new File(SaveInfo.getPlayerFileName())));
            this.playerSet = ((PlayerSet) oi.readObject()).getPlayerSet();
        }
    }

    public Player addPlayer(Player p){
        for(Player pInSet : playerSet){
            if(p.equals(pInSet)) {
                Log(Importance.INFO, "[Player Set] Player " + p.getName() + " is already in player set, nothing change!");
                return pInSet;
            }
        }
        //玩家并不在现有玩家组里面，是新的玩家，寻找一个空位安放
        for(Player pInSet : playerSet){
            if(pInSet == null){
                pInSet = p;
                Log(Importance.INFO, "[Player Set] Player " + p.getName() + " is new to this server, adding him/her!");
                return pInSet;
            }
        }
        Log(Importance.WARNING, "[Player Set] Player " + p.getName() + " cannot find new place in this server, because it is full!!!!!!!!!!!!!!!!!!!!!");
        //没有空位，玩家满了
        return null;
    }
}
