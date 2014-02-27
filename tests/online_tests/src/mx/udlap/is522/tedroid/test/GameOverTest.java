package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.view.GameBoardView;

import com.robotium.solo.Solo;
import mx.udlap.is522.tedroid.activity.MockGameActivity;

public class GameOverTest extends ActivityInstrumentationTestCase2<MockGameActivity>{

    private static final String TAG = GameOverTest.class.getSimpleName();
    private static final long DELAY = 10800l;
    
    private Solo solo;
    
    public GameOverTest() {
        super(MockGameActivity.class);
    }
    
    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
    
    public void testGameOver() throws Exception {
        Log.d(TAG, "Waiting for activity...");
        solo.waitForActivity(MockGameActivity.class);
        
        Log.d(TAG, "Wainting for Game Over...");
        Thread.sleep(DELAY);
        
        GameBoardView gameBoardView = (GameBoardView) solo.getView(R.id.mock_game_board);
        
        assertTrue("Game should be Over", gameBoardView.isGameOver());
    }
}