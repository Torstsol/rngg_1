package GmsServices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.rngg.models.Player;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.rngg.configuration.GamePreferences;
import com.rngg.game.AndroidLauncher;
import com.rngg.services.IPlayServices;
import com.rngg.services.Message;
import com.rngg.services.RealtimeListener;
import com.rngg.services.RoomListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.InvitationsClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by torstein on 23.03.2018.
 */

public class AndroidAPI implements IPlayServices {

    public GoogleSignInClient googleSignInClient;
    public GoogleSignInAccount googleSignInAccount;
    public RealTimeMultiplayerClient realTimeMultiplayerClient;
    public RoomConfig mJoinedRoomConfig;
    public InvitationsClient invitationsClient;
    public boolean mWaitingRoomFinishedFromCode = false;
    public Room mRoom;
    HashSet<Integer> pendingMessageSet = new HashSet<>();
    public RealtimeListener liveListener;
    public RoomListener roomListener;

    private AndroidLauncher androidLauncher;
    private Activity thisActivity;


    //Status codes
    public static final int RC_SIGN_IN = 9001;
    public static final int RC_WAITING_ROOM = 9002;
    public static final int RC_SELECT_PLAYERS = 9006;
    public static final int RC_INVITATION_INBOX = 9008;

    // minimum and maximum amount of players
    final static int MIN_PLAYERS = 2;
    final static int MAX_PLAYERS = 4;

    // are we already playing?
    public boolean mPlaying = false;


    public AndroidAPI(AndroidLauncher androidLauncher) {

        this.androidLauncher = androidLauncher;
        this.thisActivity = androidLauncher;
        googleSignInClient = GoogleSignIn.getClient(this.androidLauncher, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

    }


    // Sign-in method
    public void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        androidLauncher.startActivityForResult(intent, RC_SIGN_IN);
    }

    //sign-out method
    public void signOut() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(androidLauncher,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.signOut().addOnCompleteListener(androidLauncher,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // at this point, the user is signed out.
                    }
                });
    }

    // display the waiting room ui
    public void showWaitingRoom(Room room, int maxPlayersToStartGame) {
        Games.getRealTimeMultiplayerClient(androidLauncher, googleSignInAccount)
                .getWaitingRoomIntent(room, maxPlayersToStartGame)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        androidLauncher.startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                });
    }

    //Start quick game
    public void startQuickGame() {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0x0);

        // build the room config:
        RoomConfig roomConfig =
                RoomConfig.builder(roomUpdateCallback)
                        .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                        .setAutoMatchCriteria(autoMatchCriteria)
                        .setOnMessageReceivedListener(mMessageReceivedHandler)
                        .build();
        // prevent screen from sleeping during handshake
        //androidLauncher.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Save the roomConfig so we can use it if we call leave().
        mJoinedRoomConfig = roomConfig;

        // create room:
        Games.getRealTimeMultiplayerClient(androidLauncher, googleSignInAccount)
                .create(roomConfig);
        System.out.println("Room created");
    }

    //Invite players option, gets room to search and add players
    public void startInvitePlayersRoom() {
        // launch the player selection screen
        // minimum: 1 other player; maximum: 3 other players
        Games.getRealTimeMultiplayerClient(androidLauncher, GoogleSignIn.getLastSignedInAccount(androidLauncher))
                .getSelectOpponentsIntent(MIN_PLAYERS-1, MAX_PLAYERS-1, true)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        androidLauncher.startActivityForResult(intent, RC_SELECT_PLAYERS);
                    }
                });
    }

    //Shows the pending invititations
    public void showInvitationInbox() {
        Games.getInvitationsClient(androidLauncher, GoogleSignIn.getLastSignedInAccount(androidLauncher))
                .getInvitationInboxIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        androidLauncher.startActivityForResult(intent, RC_INVITATION_INBOX);
                    }
                });
    }

    @Override
    public void setMPlaying(boolean bool) {
        this.mPlaying = bool;
    }

    @Override
    public void onStartGameMessageRecieved() {
        mPlaying = true;
        mWaitingRoomFinishedFromCode = true;

        androidLauncher.finishActivity(RC_WAITING_ROOM);
    }


    // sending data to all
    @Override
    public void sendToAllReliably(byte[] message) {
        if(mRoom == null){return;}
        for (String participantId : mRoom.getParticipantIds()) {
            if (!participantId.equals(mMyParticipantId)) {
                Task<Integer> task = Games.
                        getRealTimeMultiplayerClient(androidLauncher, GoogleSignIn.getLastSignedInAccount(androidLauncher))
                        .sendReliableMessage(message, mRoom.getRoomId(), participantId,
                                handleMessageSentCallback).addOnCompleteListener(new OnCompleteListener<Integer>() {
                            @Override
                            public void onComplete(@NonNull Task<Integer> task) {
                                // Keep track of which messages are sent, if desired.
                                recordMessageToken(task.getResult());
                            }
                        });
            }
        }
    }

    synchronized void recordMessageToken(int tokenId) {
        pendingMessageSet.add(tokenId);
    }

    @Override
    public void setRealTimeListener(RealtimeListener listener){
        this.liveListener = listener;
        listener.setSender(this);
    }


    @Override
    public void setRoomListener(RoomListener listener) {

        this.roomListener = listener;
        this.liveListener = (RealtimeListener) listener;
    }

    @Override
    public boolean isSignedIn() {
        return googleSignInAccount != null;
    }

    @Override
    public String getLocalID() {
        return mMyParticipantId;
    }

    @Override
    public ArrayList<String> getRemotePlayers() {
        ArrayList<String> participants = new ArrayList<>();
        String localID = getLocalID();
        for(String s : mRoom.getParticipantIds()) {
            if(s != localID) {
                participants.add(s);
            }
        }
        return participants;
    }

    //------Listeners and callback-handlers
    private RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback =
            new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                @Override
                public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
                    // handle the message being sent.
                    synchronized (this) {
                        pendingMessageSet.remove(tokenId);
                    }
                }
            };



    public OnRealTimeMessageReceivedListener mMessageReceivedHandler =
            new OnRealTimeMessageReceivedListener() {
                @Override
                public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
                    Message message = new Message(realTimeMessage.getMessageData(), realTimeMessage.getSenderParticipantId(), realTimeMessage.describeContents());
                    liveListener.handleDataReceived(message);
                }
            };

    public RoomUpdateCallback roomUpdateCallback = new RoomUpdateCallback() {
        @Override
        public void onRoomCreated(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                System.out.println("Room " + room.getRoomId() + " created.");
                mRoom = room;
                showWaitingRoom(room, MIN_PLAYERS);
            } else {
                System.out.println("Error creating room: " + code);
                String message = "Error creating room: " + code ;
                new AlertDialog.Builder(androidLauncher).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();

                // let screen go to sleep
                androidLauncher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }

        @Override
        public void onJoinedRoom(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                System.out.println("Room " + room.getRoomId() + " joined.");
                mRoom = room;
                showWaitingRoom(room, MIN_PLAYERS);
            } else {
                System.out.println("Error joining room: " + code);
                // let screen go to sleep
                androidLauncher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }

        @Override
        public void onLeftRoom(int code, @NonNull String roomId) {
            System.out.println("Left room" + roomId);
            roomListener.leftRoom();
        }

        @Override
        public void onRoomConnected(int code, @Nullable Room room) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                System.out.println("Room " + room.getRoomId() + " connected.");
            } else {
                System.out.println("Error connecting to room: " + code);
                // let screen go to sleep
                androidLauncher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }
    };

    public RoomStatusUpdateCallback mRoomStatusCallbackHandler = new RoomStatusUpdateCallback() {
        @Override
        public void onRoomConnecting(@Nullable Room room) {
            // Update the UI status since we are in the process of connecting to a specific room.
        }

        @Override
        public void onRoomAutoMatching(@Nullable Room room) {
            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
            // Peer declined invitation, see if game should be canceled
            if (!mPlaying && shouldCancelGame(room)) {
                realTimeMultiplayerClient.leave(mJoinedRoomConfig, room.getRoomId());
                androidLauncher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
            // Update UI status indicating new players have joined!
        }

        @Override
        public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
            // Peer left, see if game should be canceled.
            if (!mPlaying && shouldCancelGame(room)) {
                realTimeMultiplayerClient.leave(mJoinedRoomConfig, room.getRoomId());
                androidLauncher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onConnectedToRoom(@Nullable Room room) {
            // Connected to room, record the room Id.
            mRoom = room;
            Games.getPlayersClient(thisActivity, GoogleSignIn.getLastSignedInAccount(thisActivity))
                    .getCurrentPlayerId().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String playerId) {
                    mMyParticipantId = mRoom.getParticipantId(playerId);
                    roomListener.roomConnected();
                }
            });
        }

        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            // This usually happens due to a network error, leave the game.
            Games.getRealTimeMultiplayerClient(thisActivity, GoogleSignIn.getLastSignedInAccount(thisActivity))
                    .leave(mJoinedRoomConfig, room.getRoomId());
            androidLauncher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // show error message and return to main screen
            mRoom = null;
            mJoinedRoomConfig = null;
        }

        @Override
        public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
            /*if (mPlaying) {
                // add new player to an ongoing game
            } else if (shouldStartGame(room)) {
                // start game!
            }*/
        }

        @Override
        public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
            if (mPlaying) {
                // do game-specific handling of this -- remove player's avatar
                // from the screen, etc. If not enough players are left for
                // the game to go on, end the game and leave the room.
            } else if (shouldCancelGame(room)) {
                // cancel the game
                Games.getRealTimeMultiplayerClient(thisActivity,
                        GoogleSignIn.getLastSignedInAccount(thisActivity))
                        .leave(mJoinedRoomConfig, room.getRoomId());
                androidLauncher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onP2PConnected(@NonNull String participantId) {
            // Update status due to new peer to peer connection.
        }

        @Override
        public void onP2PDisconnected(@NonNull String participantId) {
            // Update status due to  peer to peer connection being disconnected.
        }
    };

    //-------Unused methods as of now--------------------------


    private String mMyParticipantId;

    //determine if this client is the host
    public boolean isHost(){
        Collections.sort(mRoom.getParticipantIds());
        return mRoom.getParticipantIds().get(0).equals(mMyParticipantId);
    }

    public String hostID(){
        Collections.sort(mRoom.getParticipantIds());
        return mRoom.getParticipantIds().get(0);
    }



    // returns whether there are enough players to start the game
    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) {
                ++connectedPlayers;
            }
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    public Player[] getPlayers(){

        GamePreferences pref = GamePreferences.getInstance();

        Player[] players = new Player[mRoom.getParticipantIds().size()];

        for (int i = 0; i < mRoom.getParticipants().size(); i++) {
            /*if(!mRoom.getParticipants().get(0).getParticipantId().equals(getLocalID())){
                players[i] = new Player(mRoom.getParticipants().get(i).getDisplayName(), mRoom.getParticipants().get(i).getParticipantId(), false, pref.getColorArray().get(i));
            }
            else {
                players[i] = new Player(mRoom.getParticipants().get(i).getDisplayName(), mRoom.getParticipants().get(i).getParticipantId(), true, pref.getColorArray().get(i));
            }*/
            Participant player = mRoom.getParticipants().get(i);
            if(!player.getParticipantId().equals(getLocalID())){
                players[i] = new Player(player.getDisplayName(), player.getParticipantId(), false, this.hostID() == player.getParticipantId(), pref.getEnemyColorArray().get(i));
            }
            else {
                players[i] = new Player(player.getDisplayName(), player.getParticipantId(), true, this.hostID() == player.getParticipantId(), pref.getMainColor());
            }
        }
        return players;

    }

    // Returns whether the room is in a state where the game should be canceled.
    boolean shouldCancelGame(Room room) {
        return false;
    }

    public void leaveGame(){
        mPlaying = false;
    }


}

