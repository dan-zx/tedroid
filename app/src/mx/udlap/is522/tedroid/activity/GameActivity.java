package mx.udlap.is522.tedroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.view.GameBoardView;
import mx.udlap.is522.tedroid.view.NextTetrominoView;

/**
 * Actividad principal del juego donde se puede jugar realmente.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class GameActivity extends ActionBarActivity {

    private NextTetrominoView nextTetrominoView;
    private GameBoardView gameBoardView;
    private Menu menu;
    private AlertDialog restartDialog;
    private AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        nextTetrominoView = (NextTetrominoView) findViewById(R.id.next_tetromino);
        gameBoardView = (GameBoardView) findViewById(R.id.game_board);
        gameBoardView.setNextTetrominoView(nextTetrominoView);
        restartDialog = new AlertDialog.Builder(this)
            .setMessage(R.string.restart_message)
            .setCancelable(false)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gameBoardView.restartGame();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    gameBoardView.resumeGame();
                }
            })
            .create();
        
        exitDialog = new AlertDialog.Builder(this)
            .setMessage(R.string.exit_message)
            .setCancelable(false)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.super.onBackPressed();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    gameBoardView.resumeGame();
                }
            })
            .create();
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
                if (gameBoardView.isPaused()) {
                    gameBoardView.resumeGame();
                    item.setIcon(R.drawable.ic_action_pause);
                } else {
                    gameBoardView.pauseGame();
                    item.setIcon(R.drawable.ic_action_play);
                }
                return true;
            case R.id.action_restart:
                gameBoardView.pauseGame();
                restartDialog.show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameBoardView.pauseGame();
    }

    @Override
    public void onBackPressed() {
        gameBoardView.pauseGame();
        exitDialog.show();
    }

    @Override
    public void finish() {
        gameBoardView.stopGame();
        super.finish();
    }

    /**
     * Solo se usa para pruebas.
     * 
     * @return el menu de esta actividad. 
     */
    public Menu getMenu() {
        return menu;
    }
}