package com.rngg.controllers;

import com.rngg.configuration.GamePreferences;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.models.Player;
import com.rngg.models.WaitingRoomModel;
import com.rngg.services.IPlayServices;
import com.rngg.services.Message;
import com.rngg.services.RealtimeListener;
import com.rngg.services.RoomListener;
import com.rngg.views.WaitingRoomView;

import java.util.HashMap;

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

    public void enterGameScreen() {

        model.joinedRoom = true;
    }

    public void enterGameScreen2() {

        GameModel instance = new GameModel(sender.getPlayers(), sender);
        game.screenManager.setGameScreen(instance);
    }

    @Override
    public void handleDataReceived(Message message) {

        String type = message.getString();
        System.out.println(type);
        if (type.equals(Message.START)) {

            game.getAPI().onStartGameMessageRecieved();
            enterGameScreen();
        }
        if (type.equals(Message.ORDER)) {
            System.out.println("ORDER recieved in waitingRoom" + message.getInt());

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

    public Rngg getGame() {
        return game;
    }
}
