package mx.udlap.is522.tedroid.view;

import static org.fest.assertions.api.Assertions.assertThat;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;

import mx.udlap.is522.tedroid.view.model.DefaultShape;
import mx.udlap.is522.tedroid.view.model.Tetromino;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.LinkedList;
import java.util.Queue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "../../app/AndroidManifest.xml")
public class GameBoardViewTest {
    
    @Test
    public void shouldNotRepeatMoreThan2EqualTetrominos() {
        GameBoardView gameBoardViewMock = mock(GameBoardView.class, CALLS_REAL_METHODS);
        gameBoardViewMock.setCustomDimensions(4, 4);
        final Activity dummyActivity = Robolectric.buildActivity(Activity.class).create().get();
        when(gameBoardViewMock.getContext()).thenReturn(dummyActivity);
        
        final Queue<Tetromino> expectedTetrominos = buildTestTetrominos(gameBoardViewMock);
        when(gameBoardViewMock.getRandomTetromino()).thenAnswer(new Answer<Tetromino>(){
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