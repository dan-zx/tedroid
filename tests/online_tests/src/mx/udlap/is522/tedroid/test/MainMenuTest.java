package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import mx.udlap.is522.tedroid.activity.SettingsActivity;

import mx.udlap.is522.tedroid.activity.GameActivity;
import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.activity.ScoresActivity;
import com.robotium.solo.Solo;
import mx.udlap.is522.tedroid.activity.MainMenuActivity;

public class MainMenuTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {

    private static final String TAG = MainMenuTest.class.getSimpleName();

    private Solo solo;

    public MainMenuTest() {
        super(MainMenuActivity.class);
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
        Log.d(TAG, "Opennig MainMenuActivity...");
        solo.waitForActivity(MainMenuActivity.class);
        
        Log.d(TAG, "Opennig GameActivity...");
        solo.clickOnView(solo.getView(R.id.play_button));
        solo.waitForDialogToOpen();
        solo.clickOnButton(solo.getString(R.string.offline_warn_understand));
        solo.waitForDialogToClose();
        solo.waitForActivity(GameActivity.class);
        
        Log.d(TAG, "Exit game and returning to MainMenuActivity...");
        solo.goBack();
        solo.waitForDialogToOpen();
        solo.clickOnButton(solo.getString(android.R.string.yes));
        solo.waitForDialogToClose();
        solo.waitForActivity(MainMenuActivity.class);

        Log.d(TAG, "Opennig ScoresActivity...");
        solo.clickOnView(solo.getView(R.id.scores_button));
        solo.waitForActivity(ScoresActivity.class);
        
        Log.d(TAG, "Returning to MainMenuActivity...");
        solo.goBack();
        solo.waitForDialogToClose();
        solo.waitForActivity(MainMenuActivity.class);
        
        Log.d(TAG, "Opennig SettingsActivity...");
        solo.clickOnView(solo.getView(R.id.settings_button));
        solo.waitForActivity(SettingsActivity.class);
        
        Log.d(TAG, "Returning to MainMenuActivity...");
        solo.goBack();
        solo.waitForDialogToClose();
        solo.waitForActivity(MainMenuActivity.class);
    }
}