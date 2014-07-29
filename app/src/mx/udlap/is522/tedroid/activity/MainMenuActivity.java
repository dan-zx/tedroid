/*
 * Copyright 2014 Tedroid developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.util.Strings;
import mx.udlap.is522.tedroid.util.Typefaces;

/**
 * Actividad que presenta el menu principal del juego.
 * 
 * @author Alejandro Díaz-Torres, Wassim Lima-Saad, Daniel Pedraza-Arcega
 * @since 1.0
 */
public class MainMenuActivity extends BaseGoogleGamesActivity {

    private static final int UNUSED_REQUEST_CODE = 5471;

    private TextView appTitle;
    private RelativeLayout signedUserLayout;
    private ImageView signedUserImageView;
    private TextView signedUserTextView;
    private TextView signInWhy;
    private Button playButton;
    private Button scoresButton;
    private Button achievementsButton;
    private Button leaderboardsButton;
    private Button settingsButton;
    private MediaPlayer mediaPlayer;
    private SignInButton signInButton;
    private LinearLayout signInLayout;
    private AlertDialog offlineAlertDialog;
    private AlertDialog signOutAlertDialog;
    private AlertDialog gameChooserAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        initViews();
        setUpSignInButton();
        setUpFont();
        setUpOfflineAlertDialog();
        setUpMediaPlayer();
        setUpSignOutAlertDialog();
        setUpGameChooserAlertDialog();
    }

    /** Inicializa el media player que toca la música */
    private void setUpMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.tetris_a_music);
        mediaPlayer.setLooping(true);
    }
    
    /** Pausa la pista que estaba tocando el MediaPlayer. */
    private void pauseTrack() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    /**
     * Pausa la pista que estaba tocando el MediaPlayer y rebobina hasta el inicio para volver a
     * tocar la pista que tiene el MediaPlayer.
     */
    private void replayTrack() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    }

    /** Detiene la reproducción de la pista tiene el MediaPlayer y libera su memoria. */
    private void stopPlayback() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMusicEnabled()) replayTrack();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTrack();
    }

    @Override
    public void finish() {
        super.finish();
        stopPlayback();
    }

    /** Inicializa las vistas */
    private void initViews() {
        appTitle = (TextView) findViewById(R.id.app_title);
        signedUserLayout = (RelativeLayout) findViewById(R.id.signed_user_layout);
        signedUserImageView = (ImageView) findViewById(R.id.signed_user_photo);
        signedUserTextView = (TextView) findViewById(R.id.signed_user_name);
        signInWhy = (TextView) findViewById(R.id.sign_in_why_text);
        playButton = (Button) findViewById(R.id.play_button);
        scoresButton = (Button) findViewById(R.id.scores_button);
        achievementsButton = (Button) findViewById(R.id.achievements_button);
        leaderboardsButton = (Button) findViewById(R.id.leaderboards_button);
        settingsButton = (Button) findViewById(R.id.settings_button);        
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInLayout = (LinearLayout) findViewById(R.id.sign_in_layout);
    }

    /** Inicializa la fuente y la coloca en cada botón. */
    private void setUpFont() {
        Typeface typeface = Typefaces.get(this, Typefaces.Font.TWOBIT);
        appTitle.setTypeface(typeface);
        signedUserTextView.setTypeface(typeface);
        signInWhy.setTypeface(typeface);
        playButton.setTypeface(typeface);
        scoresButton.setTypeface(typeface);
        achievementsButton.setTypeface(typeface);
        leaderboardsButton.setTypeface(typeface);
        settingsButton.setTypeface(typeface);
    }

    /** Inicializa el botón de iniciar sesión en Google. */
    private void setUpSignInButton() {
        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                beginUserInitiatedSignIn();
            }
        });
    }

    /** Inicializa el diálogo de alerta de juego fuera de Google. */
    private void setUpOfflineAlertDialog() {
        offlineAlertDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.offline_warn_title)
            .setMessage(R.string.offline_warn_message)
            .setPositiveButton(R.string.offline_warn_understand, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gameChooserAlertDialog.show();
                }
            })
            .setNegativeButton(R.string.offline_warn_sign_in, new DialogInterface.OnClickListener() {
    
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    beginUserInitiatedSignIn();
                }
            })
            .create();
    }

    /** Inicializa el diálogo de alerta de cerrar sesión en Google. */
    private void setUpSignOutAlertDialog() {
        signOutAlertDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.sign_out_title)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    signOut();
                    showSignInLayout();
                }
            })
            .create();
    }

    /** Inicializa el diálogo de alerta de los tipos de juego. */
    private void setUpGameChooserAlertDialog() {
        gameChooserAlertDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.game_options_title)
            .setItems(R.array.game_options, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = null;
                    switch (which) {
                        case 0: 
                            intent = new Intent(MainMenuActivity.this, ClassicGameActivity.class);
                            break;
                        case 1:
                            intent = new Intent(MainMenuActivity.this, SpecialGameActivity.class);
                            break;
                    }
                    startActivity(intent);
                }
            })
            .create();
    }

    public void onPlayButtonClick(View view) {
        if (!isSignedIn()) offlineAlertDialog.show();
        else gameChooserAlertDialog.show();
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

    public void onSignedUserClick(View view) {
        signOutAlertDialog.show();
    }

    /**
     * Esconde los menus para ver los logros y marcadores, el layout del usuario firmado se resetea
     * y se escode y el layout de inicio de sesión se muestra.
     */
    private void showSignInLayout() {
        signInLayout.setVisibility(View.VISIBLE);
        achievementsButton.setVisibility(View.GONE);
        leaderboardsButton.setVisibility(View.GONE);
        signedUserLayout.setVisibility(View.GONE);
        signedUserTextView.setText(Strings.EMPTY);
        signedUserImageView.setImageResource(R.drawable.no_profile_image);
    }

    @Override
    public void onSignInFailed() {
        super.onSignInFailed();
        showSignInLayout();
    }

    @Override
    public void onSignInSucceeded() {
        super.onSignInSucceeded();
        signInLayout.setVisibility(View.GONE);
        achievementsButton.setVisibility(View.VISIBLE);
        leaderboardsButton.setVisibility(View.VISIBLE);
        signedUserLayout.setVisibility(View.VISIBLE);
        Player currentPlayer = Games.Players.getCurrentPlayer(getApiClient());
        signOutAlertDialog.setMessage(getString(R.string.sign_out_message, currentPlayer.getDisplayName()));
        signedUserTextView.setText(currentPlayer.getDisplayName());
        if (!isUserPhoto(signedUserImageView.getDrawable())) ImageManager.create(this).loadImage(signedUserImageView, currentPlayer.getIconImageUri());
    }

    /**
     * @param drawable el Drawable a revisar
     * @return {@code true} si el Drawable dado no es {@code null} y tampoco
     *         R.drawable.no_profile_image; {@code false} en otro caso.
     */
    private boolean isUserPhoto(Drawable drawable) {
        return drawable != null && 
                !getResources().getDrawable(R.drawable.no_profile_image).getConstantState().equals(drawable.getConstantState());
    }

    /** @return si la musica esta habilitada o no. */
    private boolean isMusicEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean(getString(R.string.music_switch_key), getResources().getBoolean(R.bool.default_music_switch_value));
    }
}