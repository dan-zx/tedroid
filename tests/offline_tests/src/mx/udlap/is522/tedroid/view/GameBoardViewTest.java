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

import java.util.LinkedList;

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

        final LinkedList<Tetromino> tetrominoQueue = buildTestTetrominos(gameBoardViewMock);
        when(gameBoardViewMock.randomTetromino()).thenAnswer(new Answer<Tetromino>() {
            @Override
            public Tetromino answer(InvocationOnMock invocation) {
                return tetrominoQueue.poll();
            }
        });

        gameBoardViewMock.setUpNewTetrominos();
        assertThat(gameBoardViewMock.getCurrentTetromino().getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.O.getShapeMatrix());
        assertThat(gameBoardViewMock.getNextTetromino().getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.O.getShapeMatrix());
        
        gameBoardViewMock.setUpNewTetrominos();
        assertThat(gameBoardViewMock.getCurrentTetromino().getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.O.getShapeMatrix());
        assertThat(gameBoardViewMock.getNextTetromino().getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.T.getShapeMatrix());
        
        gameBoardViewMock.setUpNewTetrominos();
        assertThat(gameBoardViewMock.getCurrentTetromino().getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.T.getShapeMatrix());
        assertThat(gameBoardViewMock.getNextTetromino().getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.T.getShapeMatrix());
        
        gameBoardViewMock.setUpNewTetrominos();
        assertThat(gameBoardViewMock.getCurrentTetromino().getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.T.getShapeMatrix());
        assertThat(gameBoardViewMock.getNextTetromino().getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.J.getShapeMatrix());
    }

    @Test
    public void shouldLevelUp() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        long previousSpeed = gameBoardView.getCurrentSpeed();

        for (int level = GameBoardView.DEFAULT_LEVEL + 1; level <= GameBoardView.MAX_LEVEL; level++) {
            gameBoardView.setInitialLevel(level);
            long actualSpeed = gameBoardView.getCurrentSpeed();

            assertThat(actualSpeed).isLessThan(previousSpeed).isNotNegative().isNotZero();
            
            previousSpeed = actualSpeed;
        }
    }

    private LinkedList<Tetromino> buildTestTetrominos(GameBoardView gameBoardView) {
        LinkedList<Tetromino> tetrominos = new LinkedList<>();
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.O)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.O)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.O)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.O)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.T)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.T)
            .build());
        tetrominos.add(new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.J)
            .build());
        return tetrominos;
    }
}