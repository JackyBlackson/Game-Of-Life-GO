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

    public Player getPlayerByName(String name){
        for(int i = 0; i < 16; i++){
            if(playerSet[i] != null){
                if (playerSet[i].getName().equals(name)) {
                    return playerSet[i];
                }
            }
        }
        return null;
    }

    public Player addPlayer(Player p){
        for(int i = 0; i < 16; i++){
            if(playerSet[i] != null){
                //Log(Importance.NOTICE, "[Player Set] Player name: " + playerSet[i].getName());
                if (p.getName().equals(playerSet[i].getName())) {
                    Log(Importance.INFO, "[Player Set] Player " + playerSet[i].getName() + " is already in player set, nothing change!");
                    return playerSet[i];
                }
            }
        }
        //玩家并不在现有玩家组里面，是新的玩家，寻找一个空位安放
        for(int i = 0; i < 16; i++){
            if(playerSet[i] == null){
                playerSet[i] = p;
                playerSet[i].setAmountOfOxygen(24);
                playerSet[i].setAmountOfCarbon(24);
                playerSet[i].addAmountOfWater(24);
                Log(Importance.INFO, "[Player Set] Player " + playerSet[i].getName() + " is new to this server, adding him/her!");
                return playerSet[i];
            }
        }
        Log(Importance.WARNING, "[Player Set] Player " + p.getName() + " cannot find new place in this server, because it is full!!!!!!!!!!!!!!!!!!!!!");
        //没有空位，玩家满了
        return null;
    }

    public String getPlayerScores(){
        StringBuilder re = new StringBuilder();
        for(int i = 0; i < 16; i++){
            Player player;
            if((player = playerSet[i]) != null){
                re.append(player.getName());
                re.append('%');
                re.append(player.getColor());
                re.append('%');
                re.append(player.getCells());
                re.append('%');
                re.append(player.getScore());
                re.append(';');
            }
        }
        return re.toString();
    }

    public void calculateScore(){
        for(int i = 0; i < 16; i++){
            if(playerSet[i] != null){
                playerSet[i].setScore(
                        4 * playerSet[i].getCells()
                        + 1 * playerSet[i].getAmountOfCarbon()
                        + 2 * playerSet[i].getAmountOfOxygen()
                        + 3 * playerSet[i].getAmountOfWater()
                );
            }
        }
    }

    public Player getWinner(){
        long maxScore = -100;
        Player winner = null;
        for(int i = 0; i < 16; i++){
            if(playerSet[i] != null){
                if(playerSet[i].getScore() > maxScore){
                    maxScore = playerSet[i].getScore();
                    winner = playerSet[i];
                }
            }
        }
        return winner;
    }
}
