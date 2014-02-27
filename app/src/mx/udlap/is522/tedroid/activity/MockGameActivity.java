package mx.udlap.is522.tedroid.activity;

import android.app.Activity;
import android.os.Bundle;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.view.MockGameBoardView;

public class MockGameActivity extends Activity {

    private MockGameBoardView gameBoardView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mock_game);
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