package com.rngg.services;


import com.rngg.models.Player;

import java.util.ArrayList;

public interface IPlayServices {
    public void signIn();
    public void signOut();
    public boolean isSignedIn();
    public void sendToAllReliably(byte[] message);
    public void setRealTimeListener(RealtimeListener listener);
    public String getLocalID();
    public ArrayList<String> getRemotePlayers();
    public void setRoomListener(RoomListener listener);
    public void startQuickGame();
    public void startInvitePlayersRoom();
    public void showInvitationInbox();
    public void setMPlaying(boolean bool);
    public void onStartGameMessageRecieved();
    public boolean isHost();
    public ArrayList<Player> getPlayers();
    public void leaveGame();
}