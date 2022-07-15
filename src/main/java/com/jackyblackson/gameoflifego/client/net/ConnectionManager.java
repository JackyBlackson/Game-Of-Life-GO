package com.jackyblackson.gameoflifego.client.net;

import com.almasb.fxgl.dsl.FXGL;
import com.jackyblackson.gameoflifego.client.info.GamePlayInfo;
import com.jackyblackson.gameoflifego.shared.common.Pos;
import com.jackyblackson.gameoflifego.shared.map.area.Area;
import com.jackyblackson.gameoflifego.shared.map.chunk.Chunk;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * Final port class to communicate with server
 */
public class ConnectionManager {
    private static ConnectionManager instance = new ConnectionManager();

    /**
     * The list of cells that will be added to the server's world next time user presses Key F
     */
    private ConcurrentLinkedDeque<Pos> setCellPosList;

    private Socket clientSocket;
    private OutputStream clientOutputStream;
    private InputStream clientInputStream;

    private ConnectionManager(){
        setCellPosList = new ConcurrentLinkedDeque<>();
        clientSocket = null;
    }

    public static ConnectionManager getInstance(){ return instance;}

    /**
     * Add a position to the cellList to set the cell
     * @param worldPos The world position of the cell to be added
     */
    public void addPosToSetCellList(Pos worldPos){
        this.setCellPosList.add(worldPos);
    }

    /**
     * Delete a position to the cellList, if exists
     * @param worldPos The world position of the cell to be deleted
     */
    public void deletePosToSetCellList(Pos worldPos){
        this.setCellPosList.remove(worldPos);
    }


    /**
     * Connect to the server and send the user profile (i.e., user's name and user's color)
     * @throws IOException Throws this IOException if connection failed or the server is full
     */
    public void connect() throws IOException {
        //get ip from the INFO class
        clientSocket = new Socket(GamePlayInfo.getInstance().ip, GamePlayInfo.getInstance().port);
        //if connected, get input and output streams
        clientInputStream = clientSocket.getInputStream();
        clientOutputStream = clientSocket.getOutputStream();
        //send the player name and color to the server
        clientOutputStream.write((
                GamePlayInfo.getInstance().player.getName() +
                ":" +
                GamePlayInfo.getInstance().player.getColor()
        ).getBytes(StandardCharsets.UTF_8));
        //wait for the server to return a message
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //receive message from server, and, if not accept, show a message of why
        byte[] bys = new byte[1024];
        int len = clientInputStream.read(bys);
        String back = new String(bys, 0, len);
        if(!back.equals("ACCEPTED")){
            FXGL.showMessage("Cannot connect to the server, because: " + back); ;
        }
    }

    /**
     * Send all the position in the setCellList to the server to set them to the world
     */
    public void setCellToServer(){
        for(Pos worldPos : this.setCellPosList){
            try{
                clientOutputStream.write(("%SETCELL:" + worldPos.toString()).getBytes(StandardCharsets.UTF_8));
            } catch(Exception ex){
                FXGL.showMessage("Cannot connect to server, because: " + ex.getMessage());
            }
        }
        //Clear the setCellList after it all send to the server
        this.setCellPosList.clear();
    }

    /**
     * Require this client's player's statistics from the server and store it to the client data set
     */
    public void requireStatistics(){
        try{
            //Send a request
            clientOutputStream.write(("%STATISTICS:").getBytes(StandardCharsets.UTF_8));
            boolean isReceived = false;
            //wait, until the server properly replied
            while(!isReceived){
                byte[] bys = new byte[1024];
                int len = clientInputStream.read(bys);
                String back = new String(bys, 0, len);
                //If the reply is fine, then process it
                if(back.startsWith("STATISTICS:")){
                    isReceived = true;
                    String[] stat = back.split(":")[1].split(";");

                    GamePlayInfo.getInstance().player.setCells(Long.parseLong(stat[0]));            //Cells
                    GamePlayInfo.getInstance().player.setAmountOfCarbon(Long.parseLong(stat[1]));   //carbon
                    GamePlayInfo.getInstance().player.setAmountOfWater(Long.parseLong(stat[2]));    //water
                    GamePlayInfo.getInstance().player.setAmountOfOxygen(Long.parseLong(stat[3]));   //oxygen
                    GamePlayInfo.getInstance().player.setScore(Long.parseLong(stat[4]));            //score
                    GamePlayInfo.getInstance().evolutionLeft = Long.parseLong(stat[5]);             //evolution time left
                }
            }
        } catch(Exception ex){
            FXGL.showMessage("Cannot connect to server, because: " + ex.getMessage());
        }
    }

    /**
     * Query the server to check if the game is end, and,
     * If it is ended, then show the information of the winner
     * else, just go on
     */
    public void requireGameEnd(){
        String backOut = "nothing";
        try{
            //send the request
            clientOutputStream.write(("%GAMEEND").getBytes(StandardCharsets.UTF_8));
            boolean isReceived = false;
            //wait, until message is received properly
            while(!isReceived){
                byte[] bys = new byte[1024];
                int len = clientInputStream.read(bys);
                String back = new String(bys, 0, len);
                backOut = back;
                //The game end, so the server reply message will start with "winner"
                if(back.startsWith("WINNER:")){
                    isReceived = true;
                    String[] stat = back.split(":")[1].split(";");
                    //TODO: 显示游戏结束窗口
                    FXGL.showMessage(stat[0] +
                            " wins the game with score " +
                                    stat[6]
                            );
                }
                //else, just continue
                else if (back.startsWith("NOTEND")){
                    isReceived = true;
                }
            }
        } catch(Exception ex){
            FXGL.showMessage("Cannot interpret message from server: \"" + backOut + "\", because: " + ex.getMessage());
        }
    }

    /**
     * This method will require all the score of available players in the server
     */
    public void requirePlayerScore(){
        try{
            clientOutputStream.write(("%PLAYERSCORE").getBytes(StandardCharsets.UTF_8));
            boolean isReceived = false;
            while(!isReceived){
                byte[] bys = new byte[1024];
                int len = clientInputStream.read(bys);
                String back = new String(bys, 0, len);
                if(back.startsWith("PLAYERSCORE:")){
                    isReceived = true;
                    GamePlayInfo.getInstance().playerInfoStringSet = back.split(":")[1].split(";");
                }
            }
        } catch(Exception ex){
            FXGL.showMessage("Cannot connect to server, because: " + ex.getMessage());
        }
    }

    /**
     * This method will require the area of the areaPos and return it
     * @param chunkPos The area position of the area
     * @return the downloaded area
     */
    public Chunk requireChunkAt(Pos chunkPos){
        try{
            clientOutputStream.write(("%CHUNKREQ:" + chunkPos.toString()).getBytes(StandardCharsets.UTF_8));
            return (Chunk) new ObjectInputStream(clientInputStream).readObject();
        } catch(Exception ex){
            FXGL.showMessage("Cannot connect to server, because: " + ex.getMessage());
            return null;
        }
    }
}
