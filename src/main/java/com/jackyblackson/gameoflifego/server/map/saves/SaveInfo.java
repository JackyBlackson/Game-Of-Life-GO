package com.jackyblackson.gameoflifego.server.map.saves;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.map.area.Area;

public class SaveInfo {
    public static String getDirForArea(Area a){
        return "Saves/" + GameInfo.Save + "/Area/(" + a.getAreaPos() + ").gla";
    }

    public static String getDirForArea(Pos areaPos){
        return "Saves/" + GameInfo.Save + "/Area/(" + areaPos + ").gla";
    }

    public static String getAreaDir(){
        return "Saves/" + GameInfo.Save + "/Area";
    }
}
