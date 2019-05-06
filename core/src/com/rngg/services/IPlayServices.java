package com.rngg.services;


import com.rngg.models.Player;

import java.util.ArrayList;

public interface IPlayServices {
    void signIn();
    void signOut();
    boolean isSignedIn();
    void sendToAllReliably(byte[] message);
    void setRealTimeListener(RealtimeListener listener);
    String getLocalID();
    ArrayList<String> getRemotePlayers();
    void setRoomListener(RoomListener listener);
    void startQuickGame();
    void startInvitePlayersRoom();
    void showInvitationInbox();
    void setMPlaying(boolean bool);
    void onStartGameMessageRecieved();
    boolean isHost();
    Player[] getPlayers();
    void leaveGame();
}