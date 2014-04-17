package mx.udlap.is522.tedroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.gms.GameHelper;

/**
 * Base para actividades que usan GoogleApiClient. En esta clase se manejan la incialización y los
 * ciclos de vida de GoogleApiClient. Esta basado en la clase BaseGameActivity creada por Bruno
 * Oliveira (Google).
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public abstract class BaseGoogleGamesActivity extends FragmentActivity implements GameHelper.GameHelperListener {

    private GameHelper gameHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (gameHelper == null) getGameHelper();
        gameHelper.setup(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getGameHelper().onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getGameHelper().onStop();
    }

    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        getGameHelper().onActivityResult(request, response, data);
    }

    @Override
    public void onSignInFailed() { 
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .edit()
            .putBoolean(getString(R.string.is_signed_in_key), false)
            .commit();
    }

    @Override
    public void onSignInSucceeded() { 
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .edit()
            .putBoolean(getString(R.string.is_signed_in_key), true)
            .commit();
    }

    /** Impide la conexión a Google al inicio si el usuario no ha iniciado sesión. */
    protected void connectOnStartIfSignedIn() {
        boolean isSigned = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .getBoolean(getString(R.string.is_signed_in_key), false);
        getGameHelper().setConnectOnStart(isSigned);
    }

    /** @return si ya completado el proceso de inciar sesión o no. */
    protected boolean isSignedIn() {
        return getGameHelper().isSignedIn();
    }

    /** @return un objeto GameHelper. Llamar después de {@link #onCreate(Bundle)}. */
    protected GameHelper getGameHelper() {
        if (gameHelper == null) gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        return gameHelper;
    }

    /** @return un objeto GoogleApiClient. Llamar después de {@link #onCreate(Bundle)}. */
    protected GoogleApiClient getApiClient() {
        return getGameHelper().getApiClient();
    }

    /** Inicia el proceso de iniciar sesión. */
    protected void beginUserInitiatedSignIn() {
        getGameHelper().beginUserInitiatedSignIn();
    }

    /** Inicia el proceso de cerrar sesión. */
    protected void signOut() {
        getGameHelper().signOut();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .edit()
            .putBoolean(getString(R.string.is_signed_in_key), false)
            .commit();
    }

    /**
     * Desbloquea el logro con el id dado.
     * 
     * @param id el id del logro a desbloquear.
     */
    protected void unlockAchievement(int id) {
        if (isSignedIn()) Games.Achievements.unlock(getApiClient(), getString(id));
    }

    /**
     * Envía los puntos provistos al marcador indicado en Google Play.
     * 
     * @param id el id del marcador.
     * @param points los puntos a enviar.
     */
    protected void submitScore(int id, long points) {
        if (isSignedIn()) Games.Leaderboards.submitScore(getApiClient(), getString(id), points);
    }
}