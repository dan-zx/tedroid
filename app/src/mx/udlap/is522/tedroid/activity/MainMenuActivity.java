package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.util.Typefaces;

/**
 * Actividad que presenta el menu principal del juego.
 * 
 * @author Alejandro Díaz-Torres, Wassim Lima-Saad, Daniel Pedraza-Arcega
 * @since 1.0
 */
public class MainMenuActivity extends BaseGameActivity {

    private static final int UNUSED_REQUEST_CODE = 5471;

    private TextView appTitle;
    private TextView signedUser;
    private TextView signInWhy;
    private Button playButton;
    private Button scoresButton;
    private Button achievementsButton;
    private Button leaderboardsButton;
    private Button settingsButton;
    private SignInButton signInButton;
    private LinearLayout signInLayout;
    private AlertDialog offlineAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        initViews();
        setUpSignInButton();
        setUpFont();
        setUpOfflineAlertDialog();
    }

    /** Inicializa las vistas */
    private void initViews() {
        appTitle = (TextView) findViewById(R.id.app_title);
        signedUser = (TextView) findViewById(R.id.signed_user);
        signInWhy = (TextView) findViewById(R.id.sign_in_why_text);
        playButton = (Button) findViewById(R.id.play_button);
        scoresButton = (Button) findViewById(R.id.scores_button);
        achievementsButton = (Button) findViewById(R.id.achievements_button);
        leaderboardsButton = (Button) findViewById(R.id.leaderboards_button);
        settingsButton = (Button) findViewById(R.id.settings_button);        
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInLayout = (LinearLayout) findViewById(R.id.sign_in_layout);
    }

    /** Inicializa la fuente y la coloca en cada boton */
    private void setUpFont() {
        Typeface typeface = Typefaces.get(this, Typefaces.Font.TWOBIT);
        appTitle.setTypeface(typeface);
        signedUser.setTypeface(typeface);
        signInWhy.setTypeface(typeface);
        playButton.setTypeface(typeface);
        scoresButton.setTypeface(typeface);
        achievementsButton.setTypeface(typeface);
        leaderboardsButton.setTypeface(typeface);
        settingsButton.setTypeface(typeface);
    }

    /** Inicializa el boton de iniciar sesión en Google */
    private void setUpSignInButton() {
        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                beginUserInitiatedSignIn();
            }
        });
    }

    /** Inicializa dialog de alerta de juego fuera de Google */
    private void setUpOfflineAlertDialog() {
        offlineAlertDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.offline_warn_title)
            .setMessage(R.string.offline_warn_message)
            .setPositiveButton(R.string.offline_warn_understand, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gotoGame();
                }
            }).setNegativeButton(R.string.offline_warn_sign_in, new DialogInterface.OnClickListener() {
    
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    beginUserInitiatedSignIn();
                }
            })
            .create();
    }

    public void onPlayButtonClick(View view) {
        if (!isSignedIn()) offlineAlertDialog.show();
        else gotoGame();
    }

    public void onScoresButtonClick(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void onAchievementsButtonClick(View view) {
        Intent intent = Games.Achievements.getAchievementsIntent(getApiClient());
        startActivityForResult(intent, UNUSED_REQUEST_CODE);
    }

    public void onLeaderboardsButtonClick(View view) {
        Intent intent = Games.Leaderboards.getAllLeaderboardsIntent(getApiClient());
        startActivityForResult(intent, UNUSED_REQUEST_CODE);
    }

    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void gotoGame() {
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignInFailed() {
        super.onSignInFailed();
        signInLayout.setVisibility(View.VISIBLE);
        achievementsButton.setVisibility(View.GONE);
        leaderboardsButton.setVisibility(View.GONE);
    }

    @Override
    public void onSignInSucceeded() {
        super.onSignInSucceeded();
        signInLayout.setVisibility(View.GONE);
        achievementsButton.setVisibility(View.VISIBLE);
        leaderboardsButton.setVisibility(View.VISIBLE);
        Player currentPlayer = Games.Players.getCurrentPlayer(getApiClient());
        signedUser.setText(currentPlayer.getDisplayName());
    }
}