package mx.udlap.is522.tedroid.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Actividad con metodos para usar un MediaPlayer más facilmente.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public abstract class ActivityWithMusic extends FragmentActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = setUpMediaPlayer();
    }

    /**
     * Inicializa el media player que toca la música.
     * 
     * @param mediaPlayer el MediaPlayer a inicializar en {@link #onCreate(Bundle)}.
     */
    protected abstract MediaPlayer setUpMediaPlayer();

    /** Pausa la pista que estaba tocando el MediaPlayer. */
    protected void pauseTrack() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    /** Toca la pista que tiene el MediaPlayer. */
    protected void playOrResumeTrack() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
    }

    /**
     * Pausa la pista que estaba tocando el MediaPlayer y rebobina hasta el inicio para volver a
     * tocar la pista que tiene el MediaPlayer.
     */
    protected void replayTrack() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    }

    /** Detiene la reproducción de la pista tiene el MediaPlayer y libera su memoria. */
    protected void stopPlayback() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}