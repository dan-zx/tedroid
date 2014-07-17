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
import android.widget.LinearLayout;

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
        solo.clickOnView(solo.getView(R.id.restart_button));
        solo.waitForDialogToOpen();

        GameBoardView gameBoardView = (GameBoardView) ((LinearLayout) solo.getView(R.id.game_board_layout)).getChildAt(0);
        assertNotNull("GameBoardView should not be null", gameBoardView);
        assertTrue("The game should be paused", gameBoardView.isPaused());

        Log.d(TAG, "Ahh... better not");
        solo.clickOnButton(solo.getString(android.R.string.no));
        solo.waitForDialogToClose();

        assertFalse("The game should be resumed", gameBoardView.isPaused());

        Log.d(TAG, "Restarting game again...");
        solo.clickOnView(solo.getView(R.id.restart_button));
        solo.waitForDialogToOpen();

        assertTrue("The game should be paused", gameBoardView.isPaused());

        Log.d(TAG, "Ok restart");
        solo.clickOnButton(solo.getString(android.R.string.yes));
        solo.waitForDialogToClose();

        assertFalse("The game should be resumed", gameBoardView.isPaused());
    }
}