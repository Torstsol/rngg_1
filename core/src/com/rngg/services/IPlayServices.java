package com.rngg.services;


import java.util.ArrayList;

public interface IPlayServices {
    public void signIn();
    public void signOut();
    public boolean isSignedIn();
    public void sendToAllReliably(byte[] message);
    public void sendToAllUnreliably(byte[] message);
    public void sendToOneReliably(byte[] message, String userID);
    public void setRealTimeListener(RealtimeListener listener);
    public String getLocalID();
    public ArrayList<String> getRemotePlayers();
    public void setRoomListener(RoomListener listener);
    public void startQuickGame();
    public void startInvitePlayersRoom();
}