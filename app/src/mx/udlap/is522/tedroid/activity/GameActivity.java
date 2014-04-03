package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

/**
 * Actividad principal del juego donde se puede jugar realmente.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class GameActivity extends ActionBarActivity {

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
                new ScoreSaver(GameActivity.this).execute(newScore);
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
        
        score += factor * (level + 1);
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
     * Tarea asíncrona para guardar puntajes. 
     * (TODO: debe estar en otro lado esta clase)
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
        public ScoreSaver(Context context) {
            scoreDAO = new DAOFactory(context.getApplicationContext()).get(ScoreDAO.class);
        }

        @Override
        protected Void doInBackground(Score... params) {
            for (Score score : params) scoreDAO.save(score);
            return null;
        }
    }
}