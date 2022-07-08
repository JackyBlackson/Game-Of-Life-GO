package com.jackyblackson.gameoflifego.server.net;

import com.jackyblackson.gameoflifego.shared.common.Importance;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.server.task.TaskChangeTile;
import com.jackyblackson.gameoflifego.shared.map.area.Area;
import com.jackyblackson.gameoflifego.shared.map.manager.MapManager;
import com.jackyblackson.gameoflifego.shared.player.Player;
import com.jackyblackson.gameoflifego.shared.player.PlayerSet;
import com.jackyblackson.gameoflifego.shared.tiles.Cell;
import com.jackyblackson.gameoflifego.shared.tiles.Vacuum;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

public class ClientSocket {
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    private Socket socket;
    private Player player;
    private OutputStream outputStream;
    private InputStream inputStream;

    public boolean isOk;

    public ClientSocket(Socket socket){
        this.socket = socket;
        isOk = false;
        try{
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            //请求客户端获取用户
            requirePlayer();
            isOk = true;
        } catch (Exception e){
            Log(Importance.WARNING, "CANNOT CONNECT TO CLIENT, because: " + e.getMessage());
            close();
        }
    }

    public Player requirePlayer() throws IOException {
        outputStream.write("^PLAYERREQ".getBytes(StandardCharsets.UTF_8));
        Log(Importance.INFO, "[Client] Send PLAYREQ to player");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log(Importance.INFO, "[Client] Wait for player to send profile...");
        byte[] bys = new byte[1024];
        int len = inputStream.read(bys);
        String back[] = new String(bys, 0, len).split(":");
        Log(Importance.INFO, "[Client] The string received: " + back[0] + ":" + back[1]);
        //解析返回的字符串
        try{
            Player p = PlayerSet.getInstance().addPlayer(new Player(back[0], back[1]));
            this.player = p;
            if(this.player == null){
                outputStream.write("BYE:There's no place for you! Please change a server!".getBytes(StandardCharsets.UTF_8));
                Log(Importance.WARNING, "[ClientSocket] Player cannot connect: the server is full");
                close();
            } else {
                Log(Importance.INFO, "[ClientSocket] Successfully accepted player " + player.getName());
            }
            return p;
        } catch (Exception e){
            Log(Importance.SEVERE, "Cannot interpret player Info, because: " + e.getMessage());
            return null;
        }
    }

    public void onInput() throws IOException {
        byte[] bys = new byte[2048];
        int len = inputStream.read(bys);
        String[] in = new String(bys, 0, len).split("%");
        for(String s : in){
            if(s != null){
                //请求获得区域
                if(s.startsWith("AREAREQ")){
                    Pos areaPos = Pos.parsePos(s.split(":")[1]);
                    assert areaPos != null;
                    Area a = MapManager.getInstance().requireAreaAt(areaPos);
                    new ObjectOutputStream(this.outputStream).writeObject(a);
                }
                //请求返回统计数据
                if(s.startsWith("STATISTICS")){
                    this.outputStream.write(("STATISTICS:" + this.player.getStatistics()).getBytes(StandardCharsets.UTF_8));
                }
                //请求放置方块
                if(s.startsWith("SETCELL")){
                    Log(Importance.INFO, "Player " + this.player.getName() + " invoked SERCELL!");
                    Pos tilePos = Pos.parsePos(s.split(":")[1]);
                    assert tilePos != null;
                    if (MapManager.getInstance().getTileAt(tilePos) instanceof Vacuum){
                        MapManager.getInstance().addTask(new TaskChangeTile(tilePos, new Cell(tilePos, this.player)));
                    } else {
                        Log(Importance.WARNING, "Player \""
                                + this.player.getName()
                                + "\" is trying to place Cell on "
                                + MapManager.getInstance().getTileAt(tilePos)
                                + " at ("
                                + tilePos
                                + ") [World Position]"
                                );
                    }
                }
                //请求退出
                if(s.startsWith("QUIT")){
                    outputStream.write("BYE".getBytes(StandardCharsets.UTF_8));
                    this.close();
                }
            }
        }
    }

    public void close() {
        TCPServer.getInstance().getClients().remove(this);
        try {
            this.socket.close();
        } catch (IOException ex) {
            Log(Importance.WARNING, "CANNOT CLOSE CONNECTION TO CLIENT, because: " + ex.getMessage());
        }
    }

    @Override
    public int hashCode() {
        return socket.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ClientSocket){
            return ((ClientSocket) obj).getPlayer().equals(this.player);
        } else {
            return false;
        }
    }
}
