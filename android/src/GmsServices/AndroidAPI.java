package GmsServices;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.rngg.services.API;


public class AndroidAPI extends Activity implements API {

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;
    /**
     * Start a sign in activity.  To properly handle the result, call tryHandleSignInResult from
     * your Activity's onActivityResult function
     */

    private GoogleSignInClient signInClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    }

    public void startSignInIntent() {
        startActivityForResult(signInClient.getSignInIntent(), RC_SIGN_IN);
    }
}
