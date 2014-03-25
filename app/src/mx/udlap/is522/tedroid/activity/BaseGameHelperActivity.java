package mx.udlap.is522.tedroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import mx.udlap.is522.tedroid.gms.GameHelper;
import mx.udlap.is522.tedroid.util.Preferences;

public abstract class BaseGameHelperActivity extends ActionBarActivity implements GameHelper.GameHelperListener {

    private static final String TAG = BaseGameHelperActivity.class.getSimpleName();

    private GameHelper gameHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGameHelper();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        gameHelper.onActivityResult(request, response, data);
    }
    
    private void initGameHelper() {
        if (gameHelper == null) {
            gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
            gameHelper.enableDebugLog(true);
            gameHelper.setConnectOnStart(wasSignIn());
        }

        gameHelper.setup(this);
    }
    
    @Override
    public void onSignInFailed() {
        Log.d(TAG, "Sign In Failed");
        if (wasSignIn()) {
            Preferences.getDefaultPreferences(getApplicationContext()).edit()
                .putBoolean(Preferences.Keys.WAS_USER_SIGNED_IN_GAMES, false)
                .commit();
        }
    }

    @Override
    public void onSignInSucceeded() {
        Log.d(TAG, "Sign In Succeeded");
        if (!wasSignIn()) {
            Preferences.getDefaultPreferences(getApplicationContext()).edit()
                .putBoolean(Preferences.Keys.WAS_USER_SIGNED_IN_GAMES, true)
                .commit();
        }
    }

    protected boolean wasSignIn() {
        return Preferences.getDefaultPreferences(getApplicationContext())
                .getBoolean(Preferences.Keys.WAS_USER_SIGNED_IN_GAMES, false);
    }

    protected GameHelper getGameHelper() {
        return gameHelper;
    }
}