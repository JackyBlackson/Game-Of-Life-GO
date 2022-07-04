package com.jackyblackson.gameoflifego.server.logger;

import com.jackyblackson.gameoflifego.server.info.Importance;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static String datetime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date(System.currentTimeMillis()));

    public static void Log(Importance imp, String text) throws IOException {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date(System.currentTimeMillis()));
        System.out.println("Logs\\GOLG_Log_Started_At" + datetime + ".txt: " + "[" + currentTime + "] " + "[" + imp.toString() + "] " + text + "\r\n");
        File file = new File("Logs\\GOLG_Log_Started_At" + datetime + ".txt");
        if(!file.exists()){
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(raf.length());
        raf.writeBytes("[" + currentTime + "] " + "[" + imp.toString() + "] " + text + "\r\n");
    }
}
