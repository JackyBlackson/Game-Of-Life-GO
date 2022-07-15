package com.jackyblackson.gameoflifego.server.info;

import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Utility;
import com.jackyblackson.gameoflifego.shared.map.saves.SaveInfo;
import com.jackyblackson.gameoflifego.shared.player.PlayerSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class GameInfo {
    public static String SaveDirectory = "Save_At_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date(System.currentTimeMillis()));
    public static long WorldSizeX = 64L;
    public static long WorldSizeY = 64L;
    public static long SpawnRange = 16L;
    public static boolean ShowDebugInfo = true;

    public static String[] AmountNeedForBirth = {"3", "6"};
    public static String[] AmountNeedForSurvive = {"2", "3"};

    public static int MaxTicksPerSecond = 20;
    public static int TicksPerEvolution = 20;
    public static int TicksPerSave = 100;
    public static int ServerPort = 26575;

    public static long MaxTickLength = 60000;

    public static long MaxEvolution = 300;

    public static void loadGameInfo(String path) throws Exception {
        if(!new File("Config").exists())
            new File("Config").mkdir();

        if((!new File("Config/game.properties").exists()) || !new File("Config/worldgen.properties").exists())
            Files.copy(new File("resources/config").toPath(), new File("Config").toPath());

        loadGameInfoFromFile(path);
        WorldGenInfo.loadProperties("Config/worldgen.properties");
        //Preparing folders for the game when starting
        if(!new File("Saves").exists())
            new File("Saves").mkdir();
        if(!new File("Saves/" + SaveDirectory).exists()){
            new File("Saves/" + SaveDirectory).mkdir();
            new File(SaveInfo.getAreaDir()).mkdir();
        }
        //If the map does not have local properties, then copy the current properties File to the map dir.
        if(!new File("Saves/" + SaveDirectory + "/LocalConfig/").exists()){
            File f = new File("Saves/" + SaveDirectory + "/LocalConfig/");
            f.mkdir();
            //Make sure that the File object is a dir, not a file
            if(f.isDirectory()){
                //复制主文件夹里面Config目录下的所有文件到当前的世界文件夹下面
                Files.copy(new File("Config/worldgen.properties").toPath(), new File("Saves/" + SaveDirectory + "/LocalConfig/worldgen.properties").toPath());
                Files.copy(new File("Config/game.properties").toPath(), new File("Saves/" + SaveDirectory + "/LocalConfig/game.properties").toPath());
                //更新配置文件里的通配符，包括seed和文件目录
                Utility.updateProperties("Saves/" + SaveDirectory + "/LocalConfig/game.properties", "SaveDirectory", SaveDirectory);
                Utility.updateProperties("Saves/" + SaveDirectory + "/LocalConfig/worldgen.properties", "SeedOfWorld", WorldGenInfo.SeedOfWorld.toString());
            }
        } else {    //If the map already have local config file, then load it:
            loadGameInfoFromFile("Saves/" + SaveDirectory + "/LocalConfig/game.properties");
            WorldGenInfo.loadProperties("Saves/" + SaveDirectory + "/LocalConfig/world.properties");
        }
        //读入玩家信息，如果存在的话：
        PlayerSet.getInstance().loadPlayerSet();

        Log(Importance.INFO, "Successfully read in game properties");
    }

    private static void loadGameInfoFromFile(String path) throws IOException {
        Properties gameProperties = new Properties();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        gameProperties.load(bufferedReader);

        SaveDirectory = gameProperties.getProperty("SaveDirectory");
        if ("*".equals(SaveDirectory)){
            SaveDirectory = "Map_Created_AT_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date(System.currentTimeMillis()));
        }
        WorldSizeX = Long.parseLong(gameProperties.getProperty("WorldSizeX"));
        WorldSizeY = Long.parseLong(gameProperties.getProperty("WorldSizeY"));
        SpawnRange = Long.parseLong(gameProperties.getProperty("SpawnRange"));
        ShowDebugInfo = Boolean.parseBoolean(gameProperties.getProperty("ShowDebugInfo"));
        AmountNeedForBirth = gameProperties.getProperty("AmountNeedForBirth").split(",");
        AmountNeedForSurvive = gameProperties.getProperty("AmountNeedForSurvive").split(",");
        MaxTicksPerSecond = Integer.parseInt(gameProperties.getProperty("MaxTicksPerSecond"));
        TicksPerEvolution = Integer.parseInt(gameProperties.getProperty("TicksPerEvolution"));
        TicksPerSave = Integer.parseInt(gameProperties.getProperty("TicksPerSave"));
        ServerPort = Integer.parseInt((gameProperties.getProperty("ServerPort")));
        MaxTickLength = Long.parseLong(gameProperties.getProperty("MaxTickLength"));
        MaxEvolution = Long.parseLong(gameProperties.getProperty("MaxEvolution"));

        bufferedReader.close();
    }
}
