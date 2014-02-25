package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.activity.GameActivity;
import mx.udlap.is522.tedroid.view.GameBoardView;

public class StopGameTest extends ActivityInstrumentationTestCase2<GameActivity> {

    private static final String TAG = StopGameTest.class.getSimpleName();
    private static final int DELAY = 2000;

    private Solo solo;

    public StopGameTest() {
        super(GameActivity.class);
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
        Log.d(TAG, "Waiting for activity...");
        solo.waitForActivity(GameActivity.class, DELAY);
        
        Log.d(TAG, "Stopping game...");
        solo.goBack();
        solo.waitForDialogToOpen();
        
        GameBoardView gameBoardView = (GameBoardView) solo.getView(R.id.game_board);
        assertNotNull("GameBoardView should not be null", gameBoardView);
        assertTrue("The game should be paused", gameBoardView.isPaused());
        
        Log.d(TAG, "Ahh... better not");
        solo.clickOnButton(solo.getString(android.R.string.no));
        
        assertFalse("The game should be resumed", gameBoardView.isPaused());
        
        Log.d(TAG, "Stopping game again...");
        solo.goBack();
        solo.waitForDialogToOpen();
        
        assertTrue("The game should be paused", gameBoardView.isPaused());
        
        Log.d(TAG, "Ok stop");
        solo.clickOnButton(solo.getString(android.R.string.yes));
        
        assertTrue("Activity should be finished", solo.getCurrentActivity().isFinishing());
    }
}