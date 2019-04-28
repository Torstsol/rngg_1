package com.rngg.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.rngg.services.Message;


import java.util.ArrayList;

import GmsServices.AndroidAPI;

public class AndroidLauncher extends AndroidApplication {

	private AndroidAPI androidAPI;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		Rngg.RUN_DESKTOP = false;
		super.onCreate(savedInstanceState);
		androidAPI = new AndroidAPI(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Rngg(androidAPI), config);
	}

	//results from intents
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// result from a sign in intent
		if (requestCode == androidAPI.RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// The signed in account is stored in the result.
				androidAPI.googleSignInAccount = result.getSignInAccount();
				new AlertDialog.Builder(this).setMessage("successfull login")
						.setNeutralButton(android.R.string.ok, null).show();
				// signed up, set the realtimemultiplayerclient
				androidAPI.realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, androidAPI.googleSignInAccount);

			} else {
				String message = result.getStatus().getStatusMessage();
				if (message == null || message.isEmpty()) {
					int errorInt = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getContext());
					message = GooglePlayServicesUtil.getErrorString(errorInt);
				}
				new AlertDialog.Builder(this).setMessage(message)
						.setNeutralButton(android.R.string.ok, null).show();
			}

			// result from waiting room intent
		} else if (requestCode == androidAPI.RC_WAITING_ROOM) {
			// Look for finishing the waiting room from code, for example if a
			// "start game" message is received.  In this case, ignore the result.
			if (androidAPI.mWaitingRoomFinishedFromCode) {
				return;
			}

			if (resultCode == Activity.RESULT_OK) {
				// When the waiting room is dismissed, either because the room is full or one of the players
				//initiated the fight, the waiting room sends RESULT_OK.
				//If it is one of the clients that starts the game, a start-message must be sent to the other
				//players in order for their game to start as well.
				if(androidAPI.mPlaying != true){
					androidAPI.mPlaying = true;

					Message message = new Message(new byte[512],"",0);
					message.putString("START");

					androidAPI.sendToAllReliably(message.getData());
					androidAPI.roomListener.enterGameScreen();

				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Waiting room was dismissed with the back button. The meaning of this
				// action is up to the game. You may choose to leave the room and cancel the
				// match, or do something else like minimize the waiting room and
				// continue to connect in the background.

				// in this example, we take the simple approach and just leave the room:
				Games.getRealTimeMultiplayerClient(this,
						GoogleSignIn.getLastSignedInAccount(this))
						.leave(androidAPI.mJoinedRoomConfig, androidAPI.mRoom.getRoomId());
				this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			} else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
				// player wants to leave the room.
				Games.getRealTimeMultiplayerClient(this,
						GoogleSignIn.getLastSignedInAccount(this))
						.leave(androidAPI.mJoinedRoomConfig, androidAPI.mRoom.getRoomId());
				this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		}
		else if (requestCode == androidAPI.RC_SELECT_PLAYERS) {
			if (resultCode != Activity.RESULT_OK) {
				// Canceled or some other error.
				return;
			}

			// Get the invitee list.
			final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

			// Get Automatch criteria.
			int minAutoPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
			int maxAutoPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

			// Create the room configuration.
			RoomConfig.Builder roomBuilder = RoomConfig.builder(androidAPI.roomUpdateCallback)
					.setOnMessageReceivedListener(androidAPI.mMessageReceivedHandler)
					.setRoomStatusUpdateCallback(androidAPI.mRoomStatusCallbackHandler)
					.addPlayersToInvite(invitees);
			if (minAutoPlayers > 0) {
				roomBuilder.setAutoMatchCriteria(
						RoomConfig.createAutoMatchCriteria(minAutoPlayers, maxAutoPlayers, 0));
			}

			// Save the roomConfig so we can use it if we call leave().
			androidAPI.mJoinedRoomConfig = roomBuilder.build();
			Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
					.create(androidAPI.mJoinedRoomConfig);
		}
		else if (requestCode == androidAPI.RC_INVITATION_INBOX) {
			if (resultCode != Activity.RESULT_OK) {
				// Canceled or some error.
				return;
			}
			Invitation invitation = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);
			if (invitation != null) {
				// build the room config:
				androidAPI.mJoinedRoomConfig =
						RoomConfig.builder(androidAPI.roomUpdateCallback)
								.setRoomStatusUpdateCallback(androidAPI.mRoomStatusCallbackHandler)
								.setOnMessageReceivedListener(mMessageReceivedHandler)
								.setInvitationIdToAccept(invitation.getInvitationId())
								.build();
				Games.getRealTimeMultiplayerClient(this,
						GoogleSignIn.getLastSignedInAccount(this))
						.join(androidAPI.mJoinedRoomConfig);
				// prevent screen from sleeping during handshake
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		}
	}
	public OnRealTimeMessageReceivedListener mMessageReceivedHandler =
			new OnRealTimeMessageReceivedListener() {
				@Override
				public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
					Message message = new Message(realTimeMessage.getMessageData(), realTimeMessage.getSenderParticipantId(), realTimeMessage.describeContents());
					androidAPI.liveListener.handleDataReceived(message);
				}
			};

}
