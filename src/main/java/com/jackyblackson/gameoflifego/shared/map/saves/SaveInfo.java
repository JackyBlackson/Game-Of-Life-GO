package com.jackyblackson.gameoflifego.shared.map.saves;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.area.Area;

public class SaveInfo {
    public static String getDirForArea(Area a){
        return "Saves/" + GameInfo.SaveDirectory + "/Area/(" + a.getAreaPos() + ").gla";
    }

    public static String getDirForArea(Pos areaPos){
        return "Saves/" + GameInfo.SaveDirectory + "/Area/(" + areaPos + ").gla";
    }

    public static String getAreaDir(){
        return "Saves/" + GameInfo.SaveDirectory + "/Area";
    }

    public static String getPlayerFileName(){ return "Saves/" + GameInfo.SaveDirectory + "/player.dat"; }
}
