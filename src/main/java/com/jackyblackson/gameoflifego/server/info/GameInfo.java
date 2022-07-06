package com.jackyblackson.gameoflifego.server.info;

import com.jackyblackson.gameoflifego.server.map.saves.SaveInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class GameInfo {
    public static String Save = "Save_At_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date(System.currentTimeMillis()));
    public static long WorldSizeX = 64L;
    public static long WorldSizeY = 64L;
    public static long SpawnRange = 16L;
    public static boolean ShowDebugInfo = true;

    public static String[] AmountNeedForBirth = {"3", "6"};
    public static String[] AmountNeedForSurvive = {"2", "3"};

    static{
        try {
            Properties gameProperties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Config/game.properties"));
            gameProperties.load(bufferedReader);

            //AstorideThreashhold = Double.parseDouble(worldGenProperties.getProperty("AstorideThreashhold"));

            Save  = gameProperties.getProperty("Save");
            if ("*".equals(Save)){
                Save = "Map_Created_AT_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date(System.currentTimeMillis()));
            }
            WorldSizeX = Long.parseLong(gameProperties.getProperty("WorldSizeX"));
            WorldSizeY = Long.parseLong(gameProperties.getProperty("WorldSizeY"));
            SpawnRange = Long.parseLong(gameProperties.getProperty("SpawnRange"));
            ShowDebugInfo = Boolean.parseBoolean(gameProperties.getProperty("ShowDebugInfo"));
            AmountNeedForBirth = gameProperties.getProperty("AmountNeedForBirth").split(",");
            AmountNeedForSurvive = gameProperties.getProperty("AmountNeedForSurvive").split(",");

            bufferedReader.close();

            Log(Importance.INFO, "Successfully read in game properties");

            //Preparing folders for the game when starting
            if(!new File("Saves").exists())
                new File("Saves").mkdir();
            if(!new File("Saves/" + Save).exists()){
                new File("Saves/" + Save).mkdir();
                new File(SaveInfo.getAreaDir()).mkdir();
            }
        } catch (Exception ex){
            Log(Importance.SEVERE, ex.getLocalizedMessage());
            Log(Importance.SEVERE, ex.getStackTrace().toString());
        }
    }
}
