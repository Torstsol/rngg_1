package com.rngg.views;

import com.rngg.controllers.WaitingRoomController;
import com.rngg.models.WaitingRoomModel;
import com.rngg.utils.GameAssetManager;

public class WaitingRoomView extends View{

    private WaitingRoomController controller;
    private WaitingRoomModel model;

    public WaitingRoomView(GameAssetManager assetManager, WaitingRoomController controller, WaitingRoomModel model) {
        super(assetManager);
        this.controller = controller;
        this.model = model;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(!model.created){
            createRoom();
            model.created = true;
        }
        if(model.leftRoom){
            controller.getGame().screenManager.setMenuScreen();
        }
        if(model.joinedRoom){
            controller.getGame().screenManager.setMenuScreen();
        }
    }

    private void createRoom(){

        controller.getGame().getAPI().setRoomListener(controller);
        controller.getGame().getAPI().startMatchMaking();
    }


}
