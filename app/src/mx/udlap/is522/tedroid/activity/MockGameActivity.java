package mx.udlap.is522.tedroid.activity;

import android.app.Activity;
import android.os.Bundle;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.view.MockGameBoardView;

/**
 * Actividad donde solo hay un tablero de muestra para pruebas. NO LLAMAR A ESTA CLASE DIRECTAMENTE
 * pues solo es para pruebas unitarias.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class MockGameActivity extends Activity {

    private MockGameBoardView gameBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mockgame);
        gameBoardView = (MockGameBoardView) findViewById(R.id.mock_game_board);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameBoardView.pauseGame();
    }

    @Override
    public void finish() {
        super.finish();
        gameBoardView.stopGame();
    }
}