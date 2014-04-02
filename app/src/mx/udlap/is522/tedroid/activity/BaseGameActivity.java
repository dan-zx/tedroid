package mx.udlap.is522.tedroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import mx.udlap.is522.tedroid.gms.GameHelper;

/**
 * Base para actividades que usan GoogleApiClient. En esta clase se manejan la
 * incialización y los ciclos de vida de GoogleApiClient. Esta basado en la
 * clase del mismo nombre creada por Bruno Oliveira (Google).
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public abstract class BaseGameActivity extends ActionBarActivity implements GameHelper.GameHelperListener {

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
            gameHelper.setConnectOnStart(isSignedIn());
        }

        gameHelper.setup(this);
    }

    @Override
    public void onSignInFailed() { }

    @Override
    public void onSignInSucceeded() { }

    /**
     * @return si ya había completado el proceso de inciar sesión o no.
     */
    protected boolean isSignedIn() {
        return gameHelper.isSignedIn();
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

    /**
     * Inicia el proceso de cerrar sesión.
     */
    protected void signOut() {
        gameHelper.signOut();
    }

    /**
     * Desbloquea el logro con el id dado.
     * 
     * @param id el id del logro a desbloquear.
     */
    protected void unlockAchievement(int id) {
        if (isSignedIn()) {
            Games.Achievements.unlock(getApiClient(), getString(id));
        }
    }
}