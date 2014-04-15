package mx.udlap.is522.tedroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;
import mx.udlap.is522.tedroid.data.dao.impl.DAOFactory;
import mx.udlap.is522.tedroid.util.Identifiers;
import mx.udlap.is522.tedroid.util.Typefaces;
import mx.udlap.is522.tedroid.view.GameBoardView;
import mx.udlap.is522.tedroid.view.NextTetrominoView;
import mx.udlap.is522.tedroid.view.Tetromino;

/**
 * Actividad principal del juego donde se puede jugar realmente.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta, Alejandro Díaz-Torres
 * @since 1.0
 */
public class GameActivity extends Activity {

    private int totalLines;
    private int totalScore;
    private int level;
    private NextTetrominoView nextTetrominoView;
    private GameBoardView gameBoardView;
    private TextView pauseTextView;
    private TextView gameOverTextView;
    private TextView scoreTextView;
    private TextView levelTextView;
    private TextView linesTextView;
    private TextView scoreTextTextView;
    private TextView levelTextTextView;
    private TextView linesTextTextView;
    private TextView nextTetrominoTextTextView;
    private ImageButton pauseButton;
    private MediaPlayer mediaPlayer;
    private AlertDialog restartDialog;
    private AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setUpMediaPlayer();
        initViews();
        setUpFont();
        setUpGameBoardView();
        setUpScoreTextViews();
        setUpRestartDialog();
        setUpExitDialog();
    }

    /** Inicializa el media player que toca la música */
    private void setUpMediaPlayer() {
        int musicId = getSelectedMusicType();
        if (musicId != Identifiers.NOT_FOUND) {
            mediaPlayer = MediaPlayer.create(this, musicId);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    /** Inicializa las vistas */
    private void initViews() {
        scoreTextTextView = (TextView) findViewById(R.id.score_text);
        levelTextTextView = (TextView) findViewById(R.id.level_text);
        linesTextTextView = (TextView) findViewById(R.id.lines_text);
        nextTetrominoTextTextView = (TextView) findViewById(R.id.next_tetromino_text);
        pauseTextView = (TextView) findViewById(R.id.pause_text);
        gameOverTextView = (TextView) findViewById(R.id.game_over_text);
        scoreTextView = (TextView) findViewById(R.id.score);
        levelTextView = (TextView) findViewById(R.id.levels);
        linesTextView = (TextView) findViewById(R.id.lines);
        nextTetrominoView = (NextTetrominoView) findViewById(R.id.next_tetromino);
        pauseButton = (ImageButton) findViewById(R.id.pause_button);
        gameBoardView = (GameBoardView) findViewById(R.id.game_board);
    }

    /** Inicializa el valor de cada textview del puntaje del usuario */
    private void setUpScoreTextViews() {
        linesTextView.setText(String.valueOf(totalLines));
        scoreTextView.setText(String.valueOf(totalScore));
        levelTextView.setText(String.valueOf(level));
    }

    /** Inicializa la fuente y la coloca en cada textview */
    private void setUpFont() {
        Typeface typeface = Typefaces.get(this, Typefaces.Font.TWOBIT);
        scoreTextTextView.setTypeface(typeface);
        levelTextTextView.setTypeface(typeface);
        linesTextTextView.setTypeface(typeface);
        nextTetrominoTextTextView.setTypeface(typeface);
        pauseTextView.setTypeface(typeface);
        gameOverTextView.setTypeface(typeface);
        scoreTextView.setTypeface(typeface);
        levelTextView.setTypeface(typeface);
        linesTextView.setTypeface(typeface);
    }

    /** Inicializa el tablero de juego */
    private void setUpGameBoardView() {
        gameBoardView.setOnCommingNextTetrominoListener(new GameBoardView.OnCommingNextTetrominoListener() {

            @Override
            public void onCommingNextTetromino(Tetromino nextTetromino) {
                nextTetrominoView.setTetromino(nextTetromino);
            }
        });
        gameBoardView.setOnPointsAwardedListener(new GameBoardView.OnPointsAwardedListener() {

            @Override
            public void onHardDropped(int gridSpaces) {
                totalScore += gridSpaces * 2;
                setUpScoreTextViews();
            }

            @Override
            public void onSoftDropped(int gridSpaces) {
                totalScore += gridSpaces;
                setUpScoreTextViews();
            }

            public void onClearedLines(int linesCleared) {
                if (updateLevelIfNeeded(linesCleared)) gameBoardView.levelUp();
                updateScoreWhenClearLines(linesCleared);
                setUpScoreTextViews();
            }
        });
        gameBoardView.setOnGameOverListener(new GameBoardView.OnGameOverListener() {

            @Override
            public void onGameOver() {
                if (mediaPlayer != null) mediaPlayer.pause();
                gameBoardView.setVisibility(View.GONE);
                gameOverTextView.setVisibility(View.VISIBLE);
                Score newScore = new Score();
                newScore.setLevel(level);
                newScore.setLines(totalLines);
                newScore.setPoints(totalScore);
                new ScoreSaver(getApplicationContext()).execute(newScore);
            }
        });
        gameBoardView.startGame();
    }

    /** Inicializa el dialog de reinicio */
    private void setUpRestartDialog() {
        restartDialog = new AlertDialog.Builder(this)
            .setMessage(R.string.restart_message)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    resetCounters();
                    setUpScoreTextViews();
                    pauseTextView.setVisibility(View.GONE);
                    gameOverTextView.setVisibility(View.GONE);
                    gameBoardView.setVisibility(View.VISIBLE);
                    gameBoardView.restartGame();
                    pauseButton.setImageResource(R.drawable.ic_action_pause);
                    pauseButton.setContentDescription(getString(R.string.pause_text));
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    }
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
    
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onCancelDialogs(dialog);
                }
            })
            .setOnCancelListener(new DialogInterface.OnCancelListener() {
    
                @Override
                public void onCancel(DialogInterface dialog) {
                    onCancelDialogs(dialog);
                }
            })
            .create();
    }

    /** Inicializa el dialogo de salida */
    private void setUpExitDialog() {
        exitDialog = new AlertDialog.Builder(this)
            .setMessage(R.string.exit_message)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
    
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onCancelDialogs(dialog);
                }
            })
            .setOnCancelListener(new DialogInterface.OnCancelListener() {
    
                @Override
                public void onCancel(DialogInterface dialog) {
                    onCancelDialogs(dialog);
                }
            })
            .create();
    }

    /**
     * Cierra el dialog provisto y reinicia el juego donde se quedó.
     * 
     * @param dialog que dialogo llamó este método.
     */
    private void onCancelDialogs(DialogInterface dialog) {
        dialog.dismiss();
        if (pauseTextView.getVisibility() == View.GONE && !gameBoardView.isGameOver()) {
            gameBoardView.resumeGame();
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!gameBoardView.isGameOver() && gameBoardView.isPaused()) {
            if (pauseTextView.getVisibility() == View.GONE && !exitDialog.isShowing() && !restartDialog.isShowing()) {
                gameBoardView.resumeGame();
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
            }
        }
    }

    public void onPauseButtonClick(View view) {
        if (!gameBoardView.isGameOver()) {
            if (gameBoardView.isPaused()) {
                pauseTextView.setVisibility(View.GONE);
                gameBoardView.setVisibility(View.VISIBLE);
                gameBoardView.resumeGame();
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
                pauseButton.setImageResource(R.drawable.ic_action_pause);
                pauseButton.setContentDescription(getString(R.string.pause_text));
            } else {
                gameBoardView.setVisibility(View.GONE);
                pauseTextView.setVisibility(View.VISIBLE);
                gameBoardView.pauseGame();
                if (mediaPlayer != null) mediaPlayer.pause();
                pauseButton.setImageResource(R.drawable.ic_action_play);
                pauseButton.setContentDescription(getString(R.string.resume_text));
            }
        }
    }

    public void onRestartButtonClick(View view) {
        gameBoardView.pauseGame();
        if (mediaPlayer != null) mediaPlayer.pause();
        restartDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!gameBoardView.isGameOver()) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
            if (!gameBoardView.isPaused()) gameBoardView.pauseGame();
        }
    }

    @Override
    public void onBackPressed() {
        if (!gameBoardView.isGameOver()) {
            gameBoardView.pauseGame();
            if (mediaPlayer != null) mediaPlayer.pause();
        }
        exitDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        if (!gameBoardView.isGameOver()) gameBoardView.stopGame();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Actualiza el puntaje del juego cuando hay lineas borradas.
     * 
     * @param linesCleared las lineas borradas.
     */
    private void updateScoreWhenClearLines(int linesCleared) {
        int factor;
        switch (linesCleared) {
            case 1: factor = 40; break;
            case 2: factor = 100; break;
            case 3: factor = 300; break;
            case 4: factor = 1200; break;
            default: factor = 1; break;
        }

        totalScore += factor * (level + 1);
    }

    /**
     * Actualiza el nivel del juego al checar las lineas borradas.
     * 
     * @param linesCleared las lineas borradas.
     * @param si se subio de nivel o no.
     */
    private boolean updateLevelIfNeeded(int linesCleared) {
        boolean mayLevelUp = totalLines % 10 >= 6;
        totalLines += linesCleared;
        if (mayLevelUp && totalLines % 10 <= 3) {
            level++;
            return true;
        }

        return false;
    }

    /** Resetea todos los contadores. */
    private void resetCounters() {
        totalScore = 0;
        level = gameBoardView.getInitialLevel();
        totalLines = 0;
    }

    /** @return el id de la música seleccionada o {@link Identifiers#NOT_FOUND}. */
    private int getSelectedMusicType() {
        String resIdName = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.music_type_key), getString(R.string.default_music_type));
        return Identifiers.getFrom(resIdName, Identifiers.ResourceType.RAW, this);
    }

    /**
     * Tarea asíncrona para guardar puntajes.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private static class ScoreSaver extends AsyncTask<Score, Void, Void> {

        private ScoreDAO scoreDAO;

        /**
         * Crea una nueva tarea asíncrona.
         * 
         * @param context el contexto de la aplicación.
         */
        private ScoreSaver(Context context) {
            scoreDAO = new DAOFactory(context).get(ScoreDAO.class);
        }

        @Override
        protected Void doInBackground(Score... params) {
            scoreDAO.save(params[0]);
            return null;
        }
    }
}