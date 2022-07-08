package com.jackyblackson.gameoflifego.server.task;

import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.server.net.ClientSocket;

public class TaskSendArea extends AreaTask {
    private ClientSocket clientSocket;

    public TaskSendArea(Pos areaPos, ClientSocket clientSocket){
        super(areaPos);
        this.clientSocket = clientSocket;
    }
}
