package com.jackyblackson.gameoflifego.server.net;

import com.almasb.fxgl.net.Client;
import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.player.PlayerSet;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class TCPServer implements Runnable{
    private static TCPServer instance = new TCPServer();

    public ClientSocket[] getClients() {
        return clients;
    }

    private ClientSocket[] clients;

    public static TCPServer getInstance(){
        return instance;
    }

    private TCPServer(){
        clients = new ClientSocket[20];
    }

    public boolean isGameEnd = true;

    public void add(ClientSocket cs){
        for(int i = 0; i < 20; i++){
            if(clients[i] == null){
                clients[i] = cs;
                return;
            }
        }
    }

    public void remove(ClientSocket cs){
        for(int i = 0; i < 20; i++){
            if(clients[i] != null){
                if(clients[i].getPlayer().getName().equals(cs.getPlayer().getName())){
                    clients[i] = null;
                    return;
                }
            }
        }
    }

    @Override
    public void run() {
        Log(Importance.INFO, "[TCP Server] Now the server is trying to proccess clients!");
        isGameEnd = false;
        while(true){
            try {
                for (int i = 0; i < 20; i++) {
                    ClientSocket cs = clients[i];
                    //连接没有正确建立，或者已经被客户端关闭
                    if(cs != null){
                        if (!cs.isOk || cs.getSocket().isClosed()) {
                            cs.close();
                        }
                        //连接正常
                        else {
                            Log(Importance.NOTICE, "Reading from " + cs.getSocket().toString());
                            try {
                                cs.onInput();
                            } catch (IOException e) {
                                Log(Importance.WARNING, "[TCP Server] Cannot process information from client of "
                                        + cs.getPlayer().getName()
                                        + " because: "
                                        + e.getMessage()
                                );
                                cs.close();
                            }
                        }
                    }
                }
            } catch (ConcurrentModificationException e){
                Log(Importance.WARNING, "[TCP Server] Is multiple threads trying to modify CLIENTS query?");
            }
        }
    }
}
