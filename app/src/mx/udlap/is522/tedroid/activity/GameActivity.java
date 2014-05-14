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
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;
import mx.udlap.is522.tedroid.data.dao.impl.DAOFactory;
import mx.udlap.is522.tedroid.fragment.InstructionsFragment;
import mx.udlap.is522.tedroid.util.Typefaces;
import mx.udlap.is522.tedroid.view.GameBoardView;
import mx.udlap.is522.tedroid.view.NextTetrominoView;
import mx.udlap.is522.tedroid.view.Tetromino;

import java.util.ArrayList;
import java.util.Map;

/**
 * Actividad principal del juego donde se puede jugar realmente.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta, Alejandro Díaz-Torres
 * @since 1.0
 */
public class GameActivity extends BaseGoogleGamesActivity {

    private int totalLines;
    private int totalScore;
    private int level;
    private NextTetrominoView nextTetrominoView;
    private GameBoardView gameBoardView;
    private TextView gameOverTextView;
    private TextView scoreTextView;
    private TextView levelTextView;
    private TextView linesTextView;
    private TextView scoreTextTextView;
    private TextView linesTextTextView;
    private TextView nextTetrominoTextTextView;
    private ImageButton pauseButton;
    private MediaPlayer mediaPlayer;
    private AlertDialog restartDialog;
    private AlertDialog exitDialog;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setUpMediaPlayer();
        initViews();
        setUpFont();
        setUpInstructionsFragment();
        setUpGameBoardView();
        setUpScoreTextViews();
        setUpRestartDialog();
        setUpExitDialog();
        connectOnStartIfSignedIn();
    }

    /** Inicializa el media player que toca la música */
    private void setUpMediaPlayer() {
        if (isMusicEnabled()) {
            mediaPlayer = MediaPlayer.create(this, R.raw.the_distance);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }
    
    /** Pausa la pista que estaba tocando el MediaPlayer. */
    private void pauseTrack() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    /** Toca la pista que tiene el MediaPlayer. */
    private void playOrResumeTrack() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
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

    /** Inicializa las vistas */
    private void initViews() {
        scoreTextTextView = (TextView) findViewById(R.id.score_text);
        linesTextTextView = (TextView) findViewById(R.id.lines_text);
        nextTetrominoTextTextView = (TextView) findViewById(R.id.next_tetromino_text);
        gameOverTextView = (TextView) findViewById(R.id.game_over_text);
        scoreTextView = (TextView) findViewById(R.id.score);
        levelTextView = (TextView) findViewById(R.id.levels);
        linesTextView = (TextView) findViewById(R.id.lines);
        nextTetrominoView = (NextTetrominoView) findViewById(R.id.next_tetromino);
        pauseButton = (ImageButton) findViewById(R.id.pause_button);
        gameBoardView = (GameBoardView) findViewById(R.id.game_board);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }

    /** Inicializa el valor de cada textview del puntaje del usuario */
    private void setUpScoreTextViews() {
        linesTextView.setText(getString(R.string.scores_format, totalLines));
        scoreTextView.setText(getString(R.string.scores_format, totalScore));
        levelTextView.setText(getString(R.string.level_text, level));
    }

    /** Inicializa la fuente y la coloca en cada textview */
    private void setUpFont() {
        Typeface typeface = Typefaces.get(this, Typefaces.Font.TWOBIT);
        scoreTextTextView.setTypeface(typeface);
        linesTextTextView.setTypeface(typeface);
        nextTetrominoTextTextView.setTypeface(typeface);
        gameOverTextView.setTypeface(typeface);
        scoreTextView.setTypeface(typeface);
        levelTextView.setTypeface(typeface);
        linesTextView.setTypeface(typeface);
        PagerTabStrip strip = (PagerTabStrip) viewPager.findViewById(R.id.pager_tab_strip);
        for (int i = 0; i < strip.getChildCount(); i++) {
            View nextChild = strip.getChildAt(i);
            if (nextChild instanceof TextView) {
                TextView textViewToConvert = (TextView) nextChild;
                textViewToConvert.setTypeface(typeface);
            }
        }
    }

    /** Inicializa el layout de instrucciones en la pausa */
    private void setUpInstructionsFragment() {
        viewPager.setAdapter(new InstructionsSlidePagerAdapter());
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
                new ScoresAsyncTask().execute(newScore);
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
                    viewPager.setVisibility(View.GONE);
                    gameOverTextView.setVisibility(View.GONE);
                    gameBoardView.setVisibility(View.VISIBLE);
                    gameBoardView.restartGame();
                    pauseButton.setImageResource(R.drawable.ic_action_pause);
                    pauseButton.setContentDescription(getString(R.string.pause_text));
                    replayTrack();
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

    /** Inicializa el diálogo de salida */
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
     * @param dialog que diálogo llamó este método.
     */
    private void onCancelDialogs(DialogInterface dialog) {
        dialog.dismiss();
        if (viewPager.getVisibility() == View.GONE && !gameBoardView.isGameOver()) {
            gameBoardView.resumeGame();
            playOrResumeTrack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!gameBoardView.isGameOver() && gameBoardView.isPaused()) {
            if (viewPager.getVisibility() == View.GONE && !exitDialog.isShowing() && !restartDialog.isShowing()) {
                gameBoardView.resumeGame();
                playOrResumeTrack();
            }
        }
    }

    public void onPauseButtonClick(View view) {
        if (!gameBoardView.isGameOver()) {
            if (gameBoardView.isPaused()) {
                viewPager.setVisibility(View.GONE);
                gameBoardView.setVisibility(View.VISIBLE);
                gameBoardView.resumeGame();
                playOrResumeTrack();
                pauseButton.setImageResource(R.drawable.ic_action_pause);
                pauseButton.setContentDescription(getString(R.string.pause_text));
            } else {
                gameBoardView.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                gameBoardView.pauseGame();
                pauseTrack();
                pauseButton.setImageResource(R.drawable.ic_action_play);
                pauseButton.setContentDescription(getString(R.string.resume_text));
            }
        }
    }

    public void onRestartButtonClick(View view) {
        if (!gameBoardView.isGameOver()) {
            gameBoardView.pauseGame();
            pauseTrack();
        }
        restartDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!gameBoardView.isGameOver()) {
            pauseTrack();
            if (!gameBoardView.isPaused()) gameBoardView.pauseGame();
        }
    }

    @Override
    public void onBackPressed() {
        if (!gameBoardView.isGameOver()) {
            gameBoardView.pauseGame();
            pauseTrack();
            exitDialog.show();
        } else super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        if (!gameBoardView.isGameOver()) gameBoardView.stopGame();
        stopPlayback();
    }

    /**
     * Actualiza el puntaje del juego cuando hay lineas borradas y desbloquea logros.
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
     * Actualiza el nivel del juego al checar las lineas borradas y desbloquea logros.
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
            switch (level) {
                case 1: unlockAchievement(R.string.for_dummies_achievement_id); break;
                case 2: unlockAchievement(R.string.as_easy_as_pie_achievement_id); break;
                case 3: unlockAchievement(R.string.beginner_achievement_id); break;
                case 4: unlockAchievement(R.string.amateur_achievement_id); break;
                case 5: unlockAchievement(R.string.expert_achievement_id); break;
                case 6: unlockAchievement(R.string.master_achievement_id); break;
                case 7: unlockAchievement(R.string.pro_achievement_id); break;
                case 8: unlockAchievement(R.string.pro_plus_plus_achievement_id); break;
                case 9: unlockAchievement(R.string.lucky_you_achievement_id); break;
                case 10: unlockAchievement(R.string.whats_next_achievement_id); break;
            }
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

    /** @return si la musica esta habilitada o no. */
    private boolean isMusicEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean(getString(R.string.music_switch_key), getResources().getBoolean(R.bool.default_music_switch_value));
    }

    /**
     * Clase para hacer el slide en el layout de la pusa para ver instrucciones.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private class InstructionsSlidePagerAdapter extends FragmentStatePagerAdapter {

        private InstructionsSlidePagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return InstructionsFragment.create(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.drag_instruction_title);
                case 1: return getString(R.string.long_press_instruction_title);
                case 2: return getString(R.string.single_tap_instruction_title);
                default: return null;
            }
        }
        
    }

    /**
     * Tarea asíncrona para guardar puntajes y revisar si hay logros a desbloquear.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private class ScoresAsyncTask extends AsyncTask<Score, Void, ArrayList<Integer>> {

        private ScoreDAO scoreDAO;
        private Score awardedScore;

        @Override
        protected void onPreExecute() {
            scoreDAO = new DAOFactory(getApplicationContext()).get(ScoreDAO.class);
        }

        @Override
        protected ArrayList<Integer> doInBackground(Score... score) {
            awardedScore = score[0];
            scoreDAO.save(awardedScore);
            ArrayList<Integer> unlockedAchievements = new ArrayList<>();
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
        protected void onPostExecute(ArrayList<Integer> unlockedAchievements) {
            for (int id : unlockedAchievements) unlockAchievement(id);
            submitScore(R.string.scores_leaderboard_id, awardedScore.getPoints());
            submitScore(R.string.levels_leaderboard_id, awardedScore.getLevel());
            submitScore(R.string.cleared_lines_leaderboard_id, awardedScore.getLines());
        }
    }
}