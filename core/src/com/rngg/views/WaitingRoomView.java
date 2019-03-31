package com.rngg.views;

import com.rngg.controllers.WaitingRoomController;
import com.rngg.models.WaitingRoomModel;
import com.rngg.utils.GameAssetManager;

public class WaitingRoomView extends View{

    private WaitingRoomController controller;
    private WaitingRoomModel model;
    private boolean isQuickGame;
    private boolean isInviteRoom;

    public WaitingRoomView(GameAssetManager assetManager, WaitingRoomController controller, boolean isQuickGame, boolean isInviteRoom, WaitingRoomModel model) {
        super(assetManager);
        this.controller = controller;
        this.model = model;
        this.isQuickGame = isQuickGame;
        this.isInviteRoom = isInviteRoom;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(!model.created){
            if(isQuickGame) {
                createQuickRoom();
                model.created = true;
            }
            else if (isInviteRoom){
                controller.getGame().getAPI().setRoomListener(controller);
                controller.getGame().getAPI().showInvitationInbox();
                model.created = true;
            }
            else{
                createInvitePlayersRoom();
                model.created = true;
            }
        }
        if(model.leftRoom){
            controller.getGame().screenManager.setMenuScreen();
        }
        if(model.joinedRoom){
            controller.enterGameScreen2();
        }
    }

    private void createQuickRoom(){

        controller.getGame().getAPI().setRoomListener(controller);
        controller.getGame().getAPI().startQuickGame();
    }

    private void createInvitePlayersRoom(){

        controller.getGame().getAPI().setRoomListener(controller);
        controller.getGame().getAPI().startInvitePlayersRoom();
    }


}
