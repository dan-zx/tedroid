package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.activity.GameActivity;
import mx.udlap.is522.tedroid.view.GameBoardView;

public class PauseResumeGameTest extends ActivityInstrumentationTestCase2<GameActivity> {

    private static final String TAG = PauseResumeGameTest.class.getSimpleName();
    private static final int DELAY = 2000;

    private Solo solo;

    public PauseResumeGameTest() {
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

        Log.d(TAG, "Pausing game...");
        solo.clickOnView(solo.getView(R.id.Pause));
        solo.sleep(DELAY);

        GameBoardView gameBoardView = (GameBoardView) solo.getView(R.id.game_board);
        assertNotNull("GameBoardView should not be null", gameBoardView);
        assertTrue("The game should be paused", gameBoardView.isPaused());

        Log.d(TAG, "Resuming game...");
        solo.clickOnView(solo.getView(R.id.Pause));
        solo.sleep(DELAY);

        assertFalse("The game should be resumed", gameBoardView.isPaused());
    }
}