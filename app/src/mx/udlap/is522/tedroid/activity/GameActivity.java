package mx.udlap.is522.tedroid.activity;

import android.app.Activity;
import android.os.Bundle;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.view.GameBoardView;

/**
 * Actividad principal del juego donde se puede jugar realmente.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class GameActivity extends Activity {

    private GameBoardView gameBoardView;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.game);
	gameBoardView = (GameBoardView) findViewById(R.id.game_board);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        gameBoardView.stopGame();
    }
}