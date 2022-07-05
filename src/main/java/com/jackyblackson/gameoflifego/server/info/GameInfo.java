package com.jackyblackson.gameoflifego.server.info;

import java.io.BufferedReader;
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

    static{
        try {
            Properties gameProperties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Config/game.properties"));
            gameProperties.load(bufferedReader);

            //AstorideThreashhold = Double.parseDouble(worldGenProperties.getProperty("AstorideThreashhold"));

            Save  = gameProperties.getProperty("Save");
            if ("*".equals(Save)){
                Save = "Save_At_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date(System.currentTimeMillis()));
            }
            WorldSizeX = Long.parseLong(gameProperties.getProperty("WorldSizeX"));
            WorldSizeY = Long.parseLong(gameProperties.getProperty("WorldSizeY"));
            SpawnRange = Long.parseLong(gameProperties.getProperty("SpawnRange"));
            ShowDebugInfo = Boolean.parseBoolean(gameProperties.getProperty("ShowDebugInfo"));

            bufferedReader.close();

            Log(Importance.INFO, "Successfully read in game properties");
        } catch (Exception ex){
            Log(Importance.SEVERE, ex.getLocalizedMessage());
            Log(Importance.SEVERE, ex.getStackTrace().toString());
        }
    }
}
