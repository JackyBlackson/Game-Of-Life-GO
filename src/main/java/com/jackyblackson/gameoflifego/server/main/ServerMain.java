package com.jackyblackson.gameoflifego.server.main;

import com.jackyblackson.gameoflifego.server.info.Importance;

import java.io.IOException;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class ServerMain {
    //服务端开启入口
    public static void main(String[] args) throws IOException {
        Entrance(args);
    }

    //服务端总入口
    public static void Entrance(String[] args) throws IOException {
        //读取配置信息
        Log(Importance.INFO, "server started");
        Log(Importance.INFO, "server started");
        Log(Importance.INFO, "server started");
        Log(Importance.INFO, "server started");
        Log(Importance.INFO, "server started");
    }
}
