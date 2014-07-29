/*
 * Copyright 2014 Tedroid developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.robotium.solo.Solo;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.activity.ClassicGameActivity;
import mx.udlap.is522.tedroid.activity.MainMenuActivity;
import mx.udlap.is522.tedroid.activity.ScoresActivity;
import mx.udlap.is522.tedroid.data.source.TedroidSQLiteOpenHelper;
import mx.udlap.is522.tedroid.util.Strings;
import mx.udlap.is522.tedroid.view.GameBoardView;

public class ScorePersistenceTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {

    private static final String TAG = ScorePersistenceTest.class.getSimpleName();

    private Solo solo;

    public ScorePersistenceTest() {
        super(MainMenuActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        TedroidSQLiteOpenHelper.destroyDb(getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        TedroidSQLiteOpenHelper.destroyDb(getActivity());
    }

    public void testRun() throws Exception {
        Log.d(TAG, "Opennig MainMenuActivity...");
        solo.waitForActivity(MainMenuActivity.class);

        Log.d(TAG, "Opennig ScoresActivity...");
        solo.clickOnView(solo.getView(R.id.scores_button));
        solo.waitForActivity(ScoresActivity.class);

        solo.waitForText(solo.getString(R.string.no_scores_message));
        
        Log.d(TAG, "Returning to MainMenuActivity...");
        solo.goBack();
        solo.waitForDialogToClose();
        solo.waitForActivity(MainMenuActivity.class);

        Log.d(TAG, "Opennig GameActivity...");
        solo.clickOnView(solo.getView(R.id.play_button));
        solo.waitForDialogToOpen();
        solo.clickOnButton(solo.getString(R.string.offline_warn_understand));
        solo.waitForDialogToClose();
        solo.waitForActivity(ClassicGameActivity.class);
        
        GameBoardView gameBoardView = (GameBoardView) solo.getView(R.id.game_board);
        TextView scoreTextView = (TextView) solo.getView(R.id.score);
        TextView levelText = (TextView) solo.getView(R.id.levels);
        TextView linesTextView = (TextView) solo.getView(R.id.lines);
        
        for (int i = 0; i < 15; i++) {
            Log.d(TAG, "Long press");
            solo.clickLongOnView(gameBoardView);
            Thread.sleep(100L);
        }
        
        Log.d(TAG, "Game should be over by now");
        solo.waitForText(solo.getString(R.string.game_over_text));
        
        String score = Integer.valueOf(scoreTextView.getText().toString()).toString();
        String levels = Integer.valueOf(levelText.getText().toString().replaceAll("[^0-9]", Strings.EMPTY)).toString();
        String lines = Integer.valueOf(linesTextView.getText().toString()).toString();
        
        Log.d(TAG, "score=" + score + ", levels=" + levels + ", lines=" + lines);
        
        Log.d(TAG, "Exit game and returning to MainMenuActivity...");
        solo.goBack();
        solo.waitForDialogToOpen();
        solo.clickOnButton(solo.getString(android.R.string.yes));
        solo.waitForDialogToClose();
        solo.waitForActivity(MainMenuActivity.class);
        
        Log.d(TAG, "Opennig ScoresActivity...");
        solo.clickOnView(solo.getView(R.id.scores_button));
        solo.waitForActivity(ScoresActivity.class);

        Log.d(TAG, "Searching for last score...");
        assertTrue(solo.searchText(score));
        assertTrue(solo.searchText(levels));
        assertTrue(solo.searchText(lines));
    }
}