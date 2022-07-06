package com.jackyblackson.gameoflifego.server.info;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class WorldInfo {
    public static double AsteroidThreshold = 2.25d;       //how often will astorides (i.e., stone) will be generated
    public static double CarbonGenThreshold = 0.7;               //how many tries will the following stuffs try to generate
    public static double CarbonGenScale = 4.0d;
    public static double OxygenGenThreshold = 0.6;
    public static double OxygenGenScale = 3.0d;
    public static double WaterGenThreshold = 0.5;
    public static double WaterGenScale = 2.0d;
    public static Long SeedOfWorld = null;
    public static int NoiseScale = 16;
    public static double NoiseDynamics = 1.5d;
    public static double DynamicsScale = 13.0d;
    public static double DynamicsShift = -0.1d;
    public static double Octave1Amp = 2.0d;
    public static double Octave2Amp = 1.5d;
    public static double Octave3Amp = 1.0d;

    static{
        try {
            loadProperties("Config/worldgen.properties");
            GameInfo.loadGameInfo("Config/game.properties");

            Log(Importance.INFO, "Successfully read in world generation properties");
        } catch (Exception ex){
            Log(Importance.SEVERE, ex.getLocalizedMessage());
            Log(Importance.SEVERE, Arrays.toString(ex.getStackTrace()));
        }
    }

    public static void loadProperties(String path) throws IOException {
        Properties worldGenProperties = new Properties();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        worldGenProperties.load(bufferedReader);

        AsteroidThreshold = Double.parseDouble(worldGenProperties.getProperty("AsteroidThreshold"));
        NoiseDynamics = Double.parseDouble(worldGenProperties.getProperty("NoiseDynamics"));
        DynamicsScale = Double.parseDouble(worldGenProperties.getProperty("DynamicsScale"));
        DynamicsShift = Double.parseDouble(worldGenProperties.getProperty("DynamicsShift"));
        Octave1Amp = Double.parseDouble(worldGenProperties.getProperty("Octave1Amp"));
        Octave2Amp = Double.parseDouble(worldGenProperties.getProperty("Octave2Amp"));
        Octave3Amp = Double.parseDouble(worldGenProperties.getProperty("Octave3Amp"));

        CarbonGenThreshold = Double.parseDouble(worldGenProperties.getProperty("CarbonGenThreshold"));
        CarbonGenScale = Double.parseDouble(worldGenProperties.getProperty("CarbonGenScale"));
        OxygenGenThreshold = Double.parseDouble(worldGenProperties.getProperty("OxygenGenThreshold"));
        OxygenGenScale = Double.parseDouble(worldGenProperties.getProperty("OxygenGenScale"));
        WaterGenThreshold = Double.parseDouble(worldGenProperties.getProperty("WaterGenThreshold"));
        WaterGenScale = Double.parseDouble(worldGenProperties.getProperty("WaterGenScale"));
        NoiseScale = Integer.parseInt(worldGenProperties.getProperty("NoiseScale"));
        String seed = worldGenProperties.getProperty("SeedOfWorld");
        if (seed.equals("*")){
            SeedOfWorld = new Random().nextLong();
        }else {
            SeedOfWorld = Long.parseLong(seed);
        }

        bufferedReader.close();
    }
}
