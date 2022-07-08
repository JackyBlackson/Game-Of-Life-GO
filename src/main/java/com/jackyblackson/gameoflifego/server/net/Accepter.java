package com.jackyblackson.gameoflifego.server.net;

import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class Accepter implements Runnable{

    public static Accepter getInstance() {
        return instance;
    }

    private static Accepter instance = new Accepter();
    private ServerSocket serverSocket;

    private Accepter(){

    }


    @Override
    public void run() {
        Log(Importance.INFO, "[Accepter] Now the server can accept threads at " + GameInfo.ServerPort);
        try {
            //serverSocket = new ServerSocket(GameInfo.ServerPort);
            serverSocket = new ServerSocket(2000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(true){
            try{
                Log(Importance.INFO, "[Accepter] Waiting for new connection to connect!");
                //不断监听，如果有连接那么直接添加
                Socket acceptedSocket = serverSocket.accept();
                Log(Importance.INFO, "[Accepter] Accepting new connection from [" + acceptedSocket.toString());
                TCPServer.getInstance().getClients().add(new ClientSocket(acceptedSocket));
            } catch (Exception e){
                Log(Importance.SEVERE, "[Accepter] CANNOT CONNECT TO CLIENT, BECAUSE: " + e.getMessage());
            }
        }
    }
}
