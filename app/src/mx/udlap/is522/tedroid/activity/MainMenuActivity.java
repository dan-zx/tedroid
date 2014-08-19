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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.util.Typefaces;

/**
 * Actividad que presenta el menu principal del juego.
 * 
 * @author Alejandro Díaz-Torres, Wassim Lima-Saad, Daniel Pedraza-Arcega
 * @since 1.0
 */
public class MainMenuActivity extends Activity {

    private TextView appTitle;
    private Button playButton;
    private Button scoresButton;
    private Button settingsButton;
    private MediaPlayer mediaPlayer;
    private AlertDialog gameChooserAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        initViews();
        setUpFont();
        setUpMediaPlayer();
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
        playButton = (Button) findViewById(R.id.play_button);
        scoresButton = (Button) findViewById(R.id.scores_button);
        settingsButton = (Button) findViewById(R.id.settings_button);
    }

    /** Inicializa la fuente y la coloca en cada botón. */
    private void setUpFont() {
        Typeface typeface = Typefaces.get(this, Typefaces.Font.TWOBIT);
        appTitle.setTypeface(typeface);
        playButton.setTypeface(typeface);
        scoresButton.setTypeface(typeface);
        settingsButton.setTypeface(typeface);
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
        gameChooserAlertDialog.show();
    }

    public void onScoresButtonClick(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /** @return si la musica esta habilitada o no. */
    private boolean isMusicEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean(getString(R.string.music_switch_key), getResources().getBoolean(R.bool.default_music_switch_value));
    }
}