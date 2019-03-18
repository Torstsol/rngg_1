package com.rngg.controllers;

import com.rngg.game.Rngg;
import com.rngg.models.Player;
import com.rngg.services.IPlayServices;
import com.rngg.services.Message;
import com.rngg.services.RealtimeListener;
import com.rngg.services.RoomListener;
import com.rngg.views.WaitingRoomView;

import java.util.HashMap;

public class WaitingRoomController extends Controller implements RoomListener, RealtimeListener {

    private IPlayServices sender;
    private WaitingRoomView waitingRoomView;
    private HashMap<String, Player> playerInfo = new HashMap<String, Player>(4);
    int leftToReady = 4;
    private Player localPlayer;

    public WaitingRoomController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void handleDataReceived(Message message) {
        String type = message.getString();
        System.out.println(type);
        if(type.equals("RAN_NUM")) {
            int num = message.getBuffer().getInt();
            System.out.println("Got number");
            System.out.println(num);
            System.out.println(message.getSender() + " " + game.getAPI().getLocalID());

            Player player = playerInfo.get(message.getSender());
            player.randomNumber = num;

            // Do a check to see if ready
            boolean gotall = true;
            for (Player p : playerInfo.values()) {
                if (p.randomNumber <= 0) {
                    gotall = false;
                }
            }
            if (gotall) {
                Message readyMessage = new Message(new byte[512], "", 0);
                readyMessage.putString("READY");
                sender.sendToAllReliably(readyMessage.getData());
            }
        } else if(type.equals("READY")){
            System.out.println("Player is ready " + message.getSender());
            leftToReady--;

            if(leftToReady <= 0){
                waitingRoomView.joinedRoom = true;
                //enterGameScreen();
            }
        }

    }

    @Override
    public void setSender(IPlayServices playServices) {

    }

    @Override
    public void roomConnected() {

    }

    @Override
    public void leftRoom() {
        waitingRoomView.leftRoom = true;
    }
    public Rngg getGame(){
        return game;
    }
}
