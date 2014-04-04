package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.DAOFactory;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;
import mx.udlap.is522.tedroid.view.GameBoardView;
import mx.udlap.is522.tedroid.view.NextTetrominoView;
import mx.udlap.is522.tedroid.view.model.Tetromino;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Actividad principal del juego donde se puede jugar realmente.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class GameActivity extends BaseGameActivity {

    private int totalLines;
    private int score;
    private int level;
    private NextTetrominoView nextTetrominoView;
    private GameBoardView gameBoardView;
    private TextView pauseTextView;
    private TextView gameOverTextView;
    private TextView scoreTextView;
    private TextView levelTextView;
    private TextView linesTextView;
    private MediaPlayer mediaPlayer;
    private Menu menu;
    private AlertDialog restartDialog;
    private AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mediaPlayer = MediaPlayer.create(this, R.raw.tetris_theme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        pauseTextView = (TextView) findViewById(R.id.pause_text);
        gameOverTextView = (TextView) findViewById(R.id.game_over_text);
        scoreTextView = (TextView) findViewById(R.id.score);
        levelTextView = (TextView) findViewById(R.id.levels);
        linesTextView = (TextView) findViewById(R.id.lines);
        nextTetrominoView = (NextTetrominoView) findViewById(R.id.next_tetromino);
        gameBoardView = (GameBoardView) findViewById(R.id.game_board);
        gameBoardView.setOnCommingNextTetrominoListener(new GameBoardView.OnCommingNextTetrominoListener() {

            @Override
            public void onCommingNextTetromino(Tetromino nextTetromino) {
                nextTetrominoView.setTetromino(nextTetromino);
            }
        });
        gameBoardView.setOnPointsAwardedListener(new GameBoardView.OnPointsAwardedListener() {

            @Override
            public void onHardDropped(int gridSpaces) {
                score += gridSpaces * 2;
                scoreTextView.setText(String.valueOf(score));
            }
            
            @Override
            public void onSoftDropped(int gridSpaces) {
                score += gridSpaces;
                scoreTextView.setText(String.valueOf(score));
            }
            
            public void onClearedLines(int linesCleared) {
                if (updateLevelIfNeeded(linesCleared)) gameBoardView.levelUp();
                updateScoreWhenClearLines(linesCleared);
                linesTextView.setText(String.valueOf(totalLines));
                scoreTextView.setText(String.valueOf(score));
                levelTextView.setText(String.valueOf(level));
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
                newScore.setPoints(score);
                new ScoresAsyncTask().execute(newScore);
            }
        });
        restartDialog = new AlertDialog.Builder(this)
            .setMessage(R.string.restart_message)
            .setCancelable(false)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    resetCounters();
                    scoreTextView.setText(String.valueOf(score));
                    levelTextView.setText(String.valueOf(level));
                    linesTextView.setText(String.valueOf(totalLines));
                    pauseTextView.setVisibility(View.GONE);
                    gameOverTextView.setVisibility(View.GONE);
                    gameBoardView.setVisibility(View.VISIBLE);
                    gameBoardView.setLevel(GameBoardView.DEFAULT_LEVEL); // TODO: resetear el nivel seleccionado
                    gameBoardView.restartGame();
                    MenuItem pauseResumeItem = menu.findItem(R.id.action_pause_resume);
                    pauseResumeItem.setIcon(R.drawable.ic_action_pause);
                    if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (pauseTextView.getVisibility() == View.GONE && 
                            !gameBoardView.isGameOver()) {
                        gameBoardView.resumeGame();
                        if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                    }
                }
            })
            .create();
        exitDialog = new AlertDialog.Builder(this)
            .setMessage(R.string.exit_message)
            .setCancelable(false)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (pauseTextView.getVisibility() == View.GONE &&
                            !gameBoardView.isGameOver()) {
                        gameBoardView.resumeGame();
                        if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                    }
                }
            })
            .create();
        scoreTextView.setText(String.valueOf(score));
        levelTextView.setText(String.valueOf(level));
        linesTextView.setText(String.valueOf(totalLines));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pause_resume:
                if (!gameBoardView.isGameOver()) {
                    if (gameBoardView.isPaused()) {
                        pauseTextView.setVisibility(View.GONE);
                        gameBoardView.setVisibility(View.VISIBLE);
                        gameBoardView.resumeGame();
                        if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                        item.setIcon(R.drawable.ic_action_pause);
                    } else {
                        gameBoardView.setVisibility(View.GONE);
                        pauseTextView.setVisibility(View.VISIBLE);
                        gameBoardView.pauseGame();
                        mediaPlayer.pause();
                        item.setIcon(R.drawable.ic_action_play);
                    }
                }
                return true;
            case R.id.action_restart:
                if (!gameBoardView.isGameOver()) {
                    gameBoardView.pauseGame();
                    mediaPlayer.pause();
                }
                restartDialog.show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }

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
        
        score += factor * (level + 1);
        if (score >= 999999) unlockAchievement(R.string.believe_it_or_not_achievement_id);
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
        score = 0;
        level = 0;
        totalLines = 0;
    }

    /**
     * Solo se usa para pruebas.
     * 
     * @return el menu de esta actividad.
     */
    public Menu getMenu() {
        return menu;
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