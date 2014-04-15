package mx.udlap.is522.tedroid.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.activity.MockGameActivity;
import mx.udlap.is522.tedroid.view.GameBoardView;
import mx.udlap.is522.tedroid.view.Tetromino;

import java.util.Arrays;

public class SimpleGameTest extends ActivityInstrumentationTestCase2<MockGameActivity> {

    private static final String TAG = SimpleGameTest.class.getSimpleName();

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
        gameBoardView.setOnCommingNextTetrominoListener(new GameBoardView.OnCommingNextTetrominoListener() {
            
            @Override
            public void onCommingNextTetromino(Tetromino nextTetromino) {
                Log.d(TAG, "New tetromino comming");
                assertNotNull(nextTetromino);
            }
        });
        gameBoardView.setOnGameOverListener(new GameBoardView.OnGameOverListener() {

            @Override
            public void onGameOver() {
                Log.d(TAG, "Game over");
            }
        });
        gameBoardView.setOnPointsAwardedListener(new GameBoardView.OnPointsAwardedListener() {

            @Override
            public void onHardDropped(int gridSpaces) {
                Log.d(TAG, "Grid spaces on hard dropped: " + gridSpaces);
            }
            
            @Override
            public void onSoftDropped(int gridSpaces) {
                Log.d(TAG, "Grid spaces on soft dropped: " + gridSpaces);
            }
            
            @Override
            public void onClearedLines(int linesCleared) {
                Log.d(TAG, "Lines cleared: " + linesCleared);
            }
        });

        Thread.sleep(300L);

        Log.d(TAG, "Rotating tetromino...");
        solo.clickOnView(gameBoardView);
        Thread.sleep(200L);

        final int rotated[][] = { { android.R.color.transparent, R.color.cyan },
                                  { R.color.cyan, R.color.cyan },
                                  { R.color.cyan, android.R.color.transparent } };

        assertTrue("Tetromino Z should be rotated", Arrays.deepEquals(rotated, gameBoardView.getCurrentTetromino().getShapeMatrix()));

        int previousRow = gameBoardView.getCurrentTetromino().getPosition().getBoardMatrixRow();
        Log.d(TAG, "Droping tetromino...");
        solo.drag(5, -480, 374, 374, 40);
        solo.drag(5, 5, 200, 700, 1);
        assertTrue("Tetromino should be at the middle of the screen", gameBoardView.getCurrentTetromino().getPosition().getBoardMatrixRow() >= previousRow + 3);

        Thread.sleep(800L);

        int previousColumn = gameBoardView.getCurrentTetromino().getPosition().getBoardMatrixColumn();
        Log.d(TAG, "Moving tetromino L to the right...");
        solo.drag(5, 480, 374, 374, 40);
        assertTrue("Tetromino should be moved to the right at least one space", gameBoardView.getCurrentTetromino().getPosition().getBoardMatrixColumn() >= previousColumn + 1);

        Thread.sleep(2500L);

        Log.d(TAG, "Moving tetromino to the right...");
        solo.drag(5, 480, 374, 374, 40);
        Thread.sleep(500L);
        previousColumn = gameBoardView.getCurrentTetromino().getPosition().getBoardMatrixColumn();
        Log.d(TAG, "Moving tetromino to the left...");
        solo.drag(5, -480, 374, 374, 40);

        assertTrue("Tetromino should be moved to the right at least one space", gameBoardView.getCurrentTetromino().getPosition().getBoardMatrixColumn() <= previousColumn - 1);
        
        Thread.sleep(50L);

        solo.clickOnView(gameBoardView, true);
        solo.clickOnView(gameBoardView, true);
        
        Thread.sleep(2500L);
    }
}