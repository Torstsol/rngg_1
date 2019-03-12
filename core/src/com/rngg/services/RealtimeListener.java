package com.rngg.services;

public interface RealtimeListener {
    public void handleDataReceived(Message message);
    public void setSender(IPlayServices playServices);
}
