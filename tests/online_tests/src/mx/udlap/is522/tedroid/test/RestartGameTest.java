package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.activity.GameActivity;
import mx.udlap.is522.tedroid.view.GameBoardView;

public class RestartGameTest extends ActivityInstrumentationTestCase2<GameActivity> {

    private static final String TAG = RestartGameTest.class.getSimpleName();

    private Solo solo;

    public RestartGameTest() {
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
        solo.waitForActivity(GameActivity.class);
        
        Log.d(TAG, "Restarting game...");
        solo.clickOnActionBarItem(R.id.action_restart);
        solo.waitForDialogToOpen();
        
        GameBoardView gameBoardView = (GameBoardView) solo.getView(R.id.game_board);
        assertNotNull("GameBoardView should not be null", gameBoardView);
        assertTrue("The game should be paused", gameBoardView.isPaused());
        
        Log.d(TAG, "Ahh... better not");
        solo.clickOnButton(solo.getString(android.R.string.no));
        solo.waitForDialogToClose();
        
        assertFalse("The game should be resumed", gameBoardView.isPaused());
        
        Log.d(TAG, "Restarting game again...");
        solo.clickOnActionBarItem(R.id.action_restart);
        solo.waitForDialogToOpen();
        
        assertTrue("The game should be paused", gameBoardView.isPaused());
        
        Log.d(TAG, "Ok restart");
        solo.clickOnButton(solo.getString(android.R.string.yes));
        solo.waitForDialogToClose();

        assertFalse("The game should be resumed", gameBoardView.isPaused());
    }
}