package mx.udlap.is522.tedroid.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
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
public class MainMenuActivity extends ActivityWithMusic {

    private TextView appTitle;
    private Button playButton;
    private Button scoresButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        initViews();
        setUpFont();
    }

    /** Inicializa las vistas */
    private void initViews() {
        appTitle = (TextView) findViewById(R.id.app_title);
        playButton = (Button) findViewById(R.id.play_button);
        scoresButton = (Button) findViewById(R.id.scores_button);
        settingsButton = (Button) findViewById(R.id.settings_button);
    }

    /** Inicializa la fuente y la coloca en cada boton */
    private void setUpFont() {
        Typeface typeface = Typefaces.get(this, Typefaces.Font.TWOBIT);
        appTitle.setTypeface(typeface);
        playButton.setTypeface(typeface);
        scoresButton.setTypeface(typeface);
        settingsButton.setTypeface(typeface);
    }

    /** {@inheritDoc} */
    @Override
    protected MediaPlayer setUpMediaPlayer() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.music_intro);
        mediaPlayer.setLooping(true);
        return mediaPlayer;
    }

    @Override
    protected void onResume() {
        super.onResume();
        replayTrack();
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

    public void onPlayButtonClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onScoresButtonClick(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}