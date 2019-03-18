package com.rngg.views;

import com.rngg.controllers.WaitingRoomController;
import com.rngg.utils.GameAssetManager;

public class WaitingRoomView extends View{

    public boolean created = false;
    public boolean leftRoom = false;
    public boolean joinedRoom = false;
    private WaitingRoomController controller;

    public WaitingRoomView(GameAssetManager assetManager, WaitingRoomController controller) {
        super(assetManager);
        this.controller = controller;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(!created){
            createRoom();
            created = true;
        }
        if(leftRoom){
            controller.getGame().screenManager.setMenuScreen();
        }
        if(joinedRoom){
            controller.getGame().screenManager.setMenuScreen();
        }
    }

    private void createRoom(){

        controller.getGame().getAPI().setRoomListener(controller);
        controller.getGame().getAPI().startMatchMaking();
    }


}
