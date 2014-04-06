package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.DAOFactory;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;
import mx.udlap.is522.tedroid.util.Typefaces;
import mx.udlap.is522.tedroid.view.GameBoardView;
import mx.udlap.is522.tedroid.view.NextTetrominoView;
import mx.udlap.is522.tedroid.view.model.Tetromino;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Actividad principal del juego donde se puede jugar realmente.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta, Alejandro Díaz-Torres
 * @since 1.0
 */
public class GameActivity extends BaseGameActivity {

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
        setUpScoreTextViews();
        setUpGameBoardView();
        setUpRestartDialog();
        setUpExitDialog();
    }

    /** Inicializa el media player que toca la música */
    private void setUpMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.tetris_theme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
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
                mediaPlayer.pause();
                gameBoardView.setVisibility(View.GONE);
                gameOverTextView.setVisibility(View.VISIBLE);
                Score newScore = new Score();
                newScore.setLevel(level);
                newScore.setLines(totalLines);
                newScore.setPoints(totalScore);
                new ScoresAsyncTask().execute(newScore);
            }
        });
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
                    gameBoardView.setLevel(GameBoardView.DEFAULT_LEVEL); // TODO: resetear al nivel seleccionado
                    gameBoardView.restartGame();
                    pauseButton.setImageResource(R.drawable.ic_action_pause);
                    pauseButton.setContentDescription(getString(R.string.pause_text));
                    if (!mediaPlayer.isPlaying()) mediaPlayer.start();
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
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
    
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
        if (pauseTextView.getVisibility() == View.GONE && 
                !gameBoardView.isGameOver()) {
            gameBoardView.resumeGame();
            if (!mediaPlayer.isPlaying()) mediaPlayer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!gameBoardView.isGameOver() && gameBoardView.isPaused()) {
            if (pauseTextView.getVisibility() == View.GONE && 
                    !exitDialog.isShowing() &&
                    !restartDialog.isShowing()) {
                gameBoardView.resumeGame();
                if (!mediaPlayer.isPlaying()) mediaPlayer.start();
            }
        }
    }

    public void onPauseButtonClick(View view) {
        if (!gameBoardView.isGameOver()) {
            if (gameBoardView.isPaused()) {
                pauseTextView.setVisibility(View.GONE);
                gameBoardView.setVisibility(View.VISIBLE);
                gameBoardView.resumeGame();
                if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                pauseButton.setImageResource(R.drawable.ic_action_pause);
                pauseButton.setContentDescription(getString(R.string.pause_text));
            } else {
                gameBoardView.setVisibility(View.GONE);
                pauseTextView.setVisibility(View.VISIBLE);
                gameBoardView.pauseGame();
                mediaPlayer.pause();
                pauseButton.setImageResource(R.drawable.ic_action_play);
                pauseButton.setContentDescription(getString(R.string.resume_text));
            }
        }
    }

    public void onRestartButtonClick(View view) {
        if (!gameBoardView.isGameOver()) {
            gameBoardView.pauseGame();
            mediaPlayer.pause();
        }
        restartDialog.show();
    }

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
            mediaPlayer.pause();
        }
        exitDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        if (!gameBoardView.isGameOver()) gameBoardView.stopGame();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    /**
     * Actualiza el puntaje del juego cuando hay lineas borradas y desbloquea
     * logros.
     * 
     * @param linesCleared las lineas borradas.
     */
    private void updateScoreWhenClearLines(int linesCleared) {
        int factor;
        switch (linesCleared) {
            case 1: factor = 40; break;
            case 2: factor = 100; break;
            case 3: factor = 300; break;
            case 4: factor = 1200; unlockAchievement(R.string.in_a_row_achievement_id); break;
            default: factor = 1; break;
        }
        
        totalScore += factor * (level + 1);
        if (totalScore >= 999999) unlockAchievement(R.string.believe_it_or_not_achievement_id);
    }

    /**
     * Actualiza el nivel del juego al checar las lineas borradas y desbloquea
     * logros.
     * 
     * @param linesCleared las lineas borradas.
     * @param si se subio de nivel o no.
     */
    private boolean updateLevelIfNeeded(int linesCleared) {
        boolean shouldLevelUp = totalLines % 10 >= 6;
        totalLines += linesCleared;
        if (totalLines >= 9999) unlockAchievement(R.string.like_a_boss_achievement_id);
        if (shouldLevelUp && totalLines % 10 <= 3) {
            level++;
            if (level >= 10) unlockAchievement(R.string.whats_next_achievement_id);
            else {
                switch(level) {
                    case 1: unlockAchievement(R.string.for_dummies_achievement_id); break;
                    case 2: unlockAchievement(R.string.as_easy_as_pie_achievement_id); break;
                    case 3: unlockAchievement(R.string.beginner_achievement_id); break;
                    case 4: unlockAchievement(R.string.amateur_achievement_id); break;
                    case 5: unlockAchievement(R.string.expert_achievement_id); break;
                    case 6: unlockAchievement(R.string.master_achievement_id); break;
                    case 7: unlockAchievement(R.string.pro_achievement_id); break;
                    case 8: unlockAchievement(R.string.pro_plus_plus_achievement_id); break;
                    case 9: unlockAchievement(R.string.lucky_you_achievement_id); break;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Resetea todos los contadores.
     */
    private void resetCounters() {
        totalScore = 0;
        level = 0;
        totalLines = 0;
    }

    /**
     * Tarea asíncrona para guardar puntajes y revisar si hay logros a desbloquear.
     *  
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private class ScoresAsyncTask extends AsyncTask<Score, Void, List<Integer>> {

        private ScoreDAO scoreDAO;
        private Score awardedScore;

        @Override
        protected void onPreExecute() {
            scoreDAO = new DAOFactory(getApplicationContext()).get(ScoreDAO.class);
        }

        @Override
        protected List<Integer> doInBackground(Score... score) {
            awardedScore = score[0];
            scoreDAO.save(awardedScore);
            ArrayList<Integer> unlockedAchievements = new ArrayList<Integer>();
            Map<String, Integer> sums = scoreDAO.readSumOfLinesAndPoints();
            int linesSum = sums.get("lines_sum");
            int pointsSum = sums.get("points_sum");
            if (linesSum >= 9999) unlockedAchievements.add(R.string.nothing_to_do_achievement_id);
            if (linesSum >= 999999) unlockedAchievements.add(R.string.get_a_life_achievement_id);
            if (pointsSum >= 9999) unlockedAchievements.add(R.string.boooooring_achievement_id);
            if (pointsSum >= 999999) unlockedAchievements.add(R.string.tenacious_achievement_id);
            return unlockedAchievements;
        }

        @Override
        protected void onPostExecute(List<Integer> unlockedAchievements) {
            for (int id : unlockedAchievements) {
                unlockAchievement(id);
            }
            
            submitScore(R.string.scores_leaderboard_id, awardedScore.getPoints());
            submitScore(R.string.levels_leaderboard_id, awardedScore.getLevel());
            submitScore(R.string.cleared_lines_leaderboard_id, awardedScore.getLines());
        }
    }
}