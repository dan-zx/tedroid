package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.activity.GameActivity;
import mx.udlap.is522.tedroid.activity.MainMenuActivity;
import mx.udlap.is522.tedroid.activity.ScoresActivity;
import mx.udlap.is522.tedroid.activity.SettingsActivity;

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