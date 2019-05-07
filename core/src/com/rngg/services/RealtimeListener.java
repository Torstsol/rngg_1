package com.rngg.services;

public interface RealtimeListener {
    void handleDataReceived(Message message);
    void setSender(IPlayServices playServices);
}
