package com.jackyblackson.gameoflifego.shared.logger;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static String datetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date(System.currentTimeMillis()));

    public static void Log(Importance imp, String text){
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date(System.currentTimeMillis()));
            //System.out.println("Logs\\GOLG_Log_Started_At" + datetime + ".txt: " + "[" + currentTime + "] " + "[" + imp.toString() + "] " + text + "\r\n");
            File file = new File("Logs\\GOLG_Log_Started_At_" + datetime + ".txt");
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            if (GameInfo.ShowDebugInfo || imp != Importance.DEBUG) {
                raf.writeBytes("[" + currentTime + "] " + "[" + imp.toString() + "] " + text + "\r\n");
                System.out.println("[" + currentTime + "] " + "[" + imp.toString() + "] " + text + "\r\n");
            }
        } catch (IOException ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
