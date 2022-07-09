package com.jackyblackson.gameoflifego.server.net;

import com.jackyblackson.gameoflifego.server.info.GameInfo;
import com.jackyblackson.gameoflifego.shared.common.Importance;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashSet;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class TCPServer implements Runnable{
    private static TCPServer instance = new TCPServer();

    public HashSet<ClientSocket> getClients() {
        return clients;
    }

    private HashSet<ClientSocket> clients;

    public static TCPServer getInstance(){
        return instance;
    }

    private TCPServer(){
        clients = new HashSet<>();
    }

    @Override
    public void run() {
        Log(Importance.INFO, "[TCP Server] Now the server is trying to proccess clients!");

        while(true){
            try {
                for (ClientSocket cs : clients) {
                    //连接没有正确建立，或者已经被客户端关闭
                    if (!cs.isOk || cs.getSocket().isClosed()) {
                        cs.close();
                    }
                    //连接正常
                    else {
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
            } catch (ConcurrentModificationException e){
                Log(Importance.WARNING, "[TCP Server] Is multiple threads trying to modify CLIENTS query?");
            }

            try {
                Thread.sleep(1000/ GameInfo.MaxTicksPerSecond);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
