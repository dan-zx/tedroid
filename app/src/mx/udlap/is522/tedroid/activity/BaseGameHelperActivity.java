package mx.udlap.is522.tedroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import mx.udlap.is522.tedroid.gms.GameHelper;
import mx.udlap.is522.tedroid.util.Preferences;

/**
 * Base para actividades que usan GamesClient. En esta clase se manejan la
 * incialización y los ciclos de vida de GamesClient.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
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

    /**
     * Inicializa GameHelper.
     */
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

    /**
     * @return si ya había completado el proceso de inciar sesión o no.
     */
    protected boolean wasSignIn() {
        return Preferences.getDefaultPreferences(getApplicationContext())
                .getBoolean(Preferences.Keys.WAS_USER_SIGNED_IN_GAMES, false);
    }

    /**
     * @return un objeto GameHelper. Llamar después de 
     *         {@link #onCreate(Bundle)}.
     */
    protected GameHelper getGameHelper() {
        return gameHelper;
    }

    /**
     * @return un objeto GoogleApiClient. Llamar después de
     *         {@link #onCreate(Bundle)}.
     */
    protected GoogleApiClient getApiClient() {
        return gameHelper.getApiClient();
    }

    /**
     * Inicia el proceso de iniciar sesión.
     */
    protected void beginUserInitiatedSignIn() {
        gameHelper.beginUserInitiatedSignIn();
    }
}