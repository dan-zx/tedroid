package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.activity.MockGameActivity;
import mx.udlap.is522.tedroid.view.GameBoardView;

import java.util.Arrays;

public class SimpleGameTest extends ActivityInstrumentationTestCase2<MockGameActivity>{

    private static final String TAG = GameOverTest.class.getSimpleName();
    
    private Solo solo;
    
    public SimpleGameTest() {
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
    
    public void testRun() throws Exception {
        solo.waitForActivity(MockGameActivity.class);
        GameBoardView gameBoardView = (GameBoardView) solo.getView(R.id.mock_game_board);
        gameBoardView.setOnPointsGainedListener(new GameBoardView.OnPointsGainedListener() {

            @Override
            public void onClearedLines(int linesCleared) {
                Log.d(TAG, "Lines cleared: " + linesCleared);
                assertEquals("Only one line should be cleared", 1, linesCleared);
            }
        });
        
        Thread.sleep(800l);
        
        Log.d(TAG, "Rotating tetromino...");
        solo.clickOnView(gameBoardView, true);
        
        final int rotated[][] = { { android.R.color.transparent, R.color.cyan },
                                  { R.color.cyan, R.color.cyan },
                                  { R.color.cyan, android.R.color.transparent } };

        assertTrue("Tetromino Z should be rotated", Arrays.deepEquals(rotated, gameBoardView.getCurrentTetromino().getShapeMatrix()));
        
        int previousY = gameBoardView.getCurrentTetromino().getPositionOnBoard().getY();
        Log.d(TAG, "Droping tetromino...");
        solo.drag(5, 5, 200, 700, 4);
        assertTrue("Tetromino should be in the middle of the screen", gameBoardView.getCurrentTetromino().getPositionOnBoard().getY() >= previousY+3);
        
        Thread.sleep(800l);
        
        int previousX = gameBoardView.getCurrentTetromino().getPositionOnBoard().getX();
        Log.d(TAG, "Moving tetromino L to the right...");
        solo.drag(5, 480, 374, 374, 40);
        assertTrue("Tetromino should be moved to the right at least one space", gameBoardView.getCurrentTetromino().getPositionOnBoard().getX() >= previousX+2);
        
        Thread.sleep(2500l);

        Log.d(TAG, "Moving tetromino to the right...");
        solo.drag(5, 480, 374, 374, 40);
        Thread.sleep(500l);
        previousY = gameBoardView.getCurrentTetromino().getPositionOnBoard().getX();
        Log.d(TAG, "Moving tetromino to the left...");
        solo.drag(5, -480, 374, 374, 40);

        assertTrue("Tetromino should be moved to the right at least one space", gameBoardView.getCurrentTetromino().getPositionOnBoard().getX() <= 0);
        Thread.sleep(800l);
    }
}