package com.rngg.controllers;

import com.badlogic.gdx.utils.Sort;
import com.rngg.configuration.GamePreferences;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.models.Player;
import com.rngg.models.WaitingRoomModel;
import com.rngg.services.IPlayServices;
import com.rngg.services.Message;
import com.rngg.services.RealtimeListener;
import com.rngg.services.RoomListener;
import com.rngg.views.GameView;
import com.rngg.views.WaitingRoomView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class WaitingRoomController extends Controller implements RoomListener, RealtimeListener {

    private WaitingRoomView waitingRoomView;
    private HashMap<String, Player> playerInfo = new HashMap<String, Player>(4);
    int leftToReady = 4;
    private Player localPlayer;
    private WaitingRoomModel model;
    private GamePreferences pref;
    private IPlayServices sender = game.getAPI();

    public WaitingRoomController(Rngg game, WaitingRoomModel model) {

        super(game);
        this.model = model;

        pref = GamePreferences.getInstance();
    }

    @Override
    public void update(float delta) {

    }

    public void enterGameScreen() {

        model.joinedRoom = true;
    }

    public void enterGameScreen2() {

        for(String playerId : sender.getRemotePlayers()){
            playerInfo.put(playerId, new Player(playerId, playerId, false, pref.COLOR2));
        }
        localPlayer = new Player("bob", sender.getLocalID(), true, pref.COLOR1);
        playerInfo.put(localPlayer.playerId, localPlayer);

        Player[] players = playerInfo.values().toArray(new Player[playerInfo.size()]);

        for(int i = 0; i < players.length; i++){
            players[i].playerIndex = i;
        }
        game.screenManager.setGameScreen(new GameModel(4), Arrays.asList(players));;
    }

    @Override
    public void handleDataReceived(Message message) {

        String type = message.getString();
        System.out.println(type);
        if(type.equals("START")) {

            game.getAPI().onStartGameMessageRecieved();
            enterGameScreen();
        }

    }

    @Override
    public void setSender(IPlayServices playServices) {
        this.sender = playServices;
    }

    @Override
    public void roomConnected() {
        System.out.println("roomconnected callback in waiting room controller");
    }


    @Override
    public void leftRoom() {
        model.leftRoom = true;
    }
    public Rngg getGame(){
        return game;
    }
}
