package mx.udlap.is522.tedroid.view;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPreferenceManager;

import mx.udlap.is522.tedroid.R;

import mx.udlap.is522.tedroid.view.model.DefaultShape;
import mx.udlap.is522.tedroid.view.model.Tetromino;

import java.util.LinkedList;
import java.util.Queue;

@RunWith(RobolectricTestRunner.class)
public class GameBoardViewTest {

    private Activity dummyActivity;

    @Before
    public void setUp() throws Exception {
        dummyActivity = Robolectric.buildActivity(Activity.class).create().get();
        assertThat(dummyActivity).isNotNull();
        
        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext());
        sharedPreferences
            .edit()
            .putBoolean(Robolectric.application.getString(R.string.sounds_switch_key), false)
            .commit();
    }

    @Test
    public void shouldNotRepeatMoreThan2EqualTetrominos() throws Exception {
        GameBoardView gameBoardViewMock = mock(GameBoardView.class, CALLS_REAL_METHODS);
        when(gameBoardViewMock.getContext()).thenReturn(dummyActivity);
        gameBoardViewMock.setUp();
        gameBoardViewMock.setCustomDimensions(4, 4);

        final Queue<Tetromino> expectedTetrominos = buildTestTetrominos(gameBoardViewMock);
        when(gameBoardViewMock.getRandomTetromino()).thenAnswer(new Answer<Tetromino>() {
            @Override
            public Tetromino answer(InvocationOnMock invocation) {
                return expectedTetrominos.poll();
            }
        });

        gameBoardViewMock.setUpCurrentAndNextTetrominos();
        assertThat(gameBoardViewMock.shouldGetAnotherRandomTetromino()).isFalse();

        gameBoardViewMock.setUpCurrentAndNextTetrominos();
        assertThat(gameBoardViewMock.shouldGetAnotherRandomTetromino()).isTrue();

        gameBoardViewMock.setUpCurrentAndNextTetrominos();
        assertThat(gameBoardViewMock.shouldGetAnotherRandomTetromino()).isTrue();

        gameBoardViewMock.setUpCurrentAndNextTetrominos();
        assertThat(gameBoardViewMock.shouldGetAnotherRandomTetromino()).isFalse();

        gameBoardViewMock.setUpCurrentAndNextTetrominos();
        assertThat(gameBoardViewMock.shouldGetAnotherRandomTetromino()).isFalse();

        gameBoardViewMock.setUpCurrentAndNextTetrominos();
        assertThat(gameBoardViewMock.shouldGetAnotherRandomTetromino()).isTrue();
    }

    @Test
    public void shouldLevelUp() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        long previousSpeed = gameBoardView.getCurrentSpeed();

        for (int level = GameBoardView.DEFAULT_LEVEL + 1; level <= GameBoardView.MAX_LEVEL; level++) {
            gameBoardView.setLevel(level);
            long actualSpeed = gameBoardView.getCurrentSpeed();

            assertThat(actualSpeed).isLessThan(previousSpeed).isNotNegative().isNotZero();
            
            previousSpeed = actualSpeed;
        }
    }

    private Queue<Tetromino> buildTestTetrominos(GameBoardView gameBoardView) {
        Queue<Tetromino> tetrominos = new LinkedList<Tetromino>();
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.O)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.O)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.O)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.O)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.T)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.T)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.T)
            .build());
        return tetrominos;
    }
}