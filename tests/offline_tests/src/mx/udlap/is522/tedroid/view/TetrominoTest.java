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

import android.app.Activity;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPreferenceManager;

import mx.udlap.is522.tedroid.R;

@RunWith(RobolectricTestRunner.class)
public class TetrominoTest {

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
    public void shouldBuildWithDefaultShape() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino tetrominoZ = new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.Z)
            .build();

        assertThat(tetrominoZ).isNotNull();
        assertThat(tetrominoZ.getShapeMatrix()).isNotNull().isEqualTo(TetrominoShape.Z.getShapeMatrix());
        assertThat(tetrominoZ.hasRotation()).isNotEqualTo(Tetromino.Builder.DEFAULT_SHAPE.hasRotation()).isEqualTo(TetrominoShape.Z.hasRotation());
    }

    @Test
    public void shouldBuildWithCustomShape() throws Exception {
        final int[][] shape = { { android.R.color.black,       android.R.color.black, android.R.color.transparent }, 
                                { android.R.color.transparent, android.R.color.black, android.R.color.black }, 
                                { android.R.color.transparent, android.R.color.black, android.R.color.transparent } };

        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino newTetromino = new Tetromino.Builder(gameBoardView)
            .setShape(shape)
            .hasRotation()
            .build();

        assertThat(newTetromino).isNotNull();
        assertThat(newTetromino.getShapeMatrix()).isNotNull().isEqualTo(shape);
        assertThat(newTetromino.hasRotation()).isNotEqualTo(Tetromino.Builder.DEFAULT_SHAPE.hasRotation()).isTrue();
    }

    @Test
    public void shouldRotate() throws Exception {
        final int[][] rotatedShape1 = { { android.R.color.transparent, R.color.tetromino_t, }, 
                                        { R.color.tetromino_t, R.color.tetromino_t, }, 
                                        { android.R.color.transparent, R.color.tetromino_t, } };

        final int[][] rotatedShape2 = { { android.R.color.transparent, R.color.tetromino_t, android.R.color.transparent }, 
                                        { R.color.tetromino_t, R.color.tetromino_t, R.color.tetromino_t } };

        final int[][] rotatedShape3 = { { R.color.tetromino_t, android.R.color.transparent, }, 
                                        { R.color.tetromino_t, R.color.tetromino_t, }, 
                                        { R.color.tetromino_t, android.R.color.transparent, } };

        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino tetrominoT = new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.T)
            .build();

        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(rotatedShape1);
        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(rotatedShape2);
        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(rotatedShape3);
        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(TetrominoShape.T.getShapeMatrix());
        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(rotatedShape1);
    }

    @Test
    public void shouldMove() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino tetrominoO = new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.O)
            .build();

        assertThat(tetrominoO.moveTo(Tetromino.Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.LEFT)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.DOWN)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.DOWN)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.DOWN)).isTrue();
        assertThat(tetrominoO.getPosition()).isNotNull();
        assertThat(tetrominoO.getPosition().getBoardMatrixColumn()).isEqualTo(1);
        assertThat(tetrominoO.getPosition().getBoardMatrixRow()).isEqualTo(3);
    }

    @Test
    public void shouldNotMove() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity) {
            @Override
            public int[][] getBoardMatrix() {
                return new int[][] { {android.R.color.transparent, android.R.color.transparent},
                                     {android.R.color.transparent, android.R.color.transparent} };
            }
        };

        Tetromino tetrominoO = new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.O)
            .build();

        assertThat(tetrominoO.moveTo(Tetromino.Direction.RIGHT)).isFalse();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.LEFT)).isFalse();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.DOWN)).isFalse();

        gameBoardView = new GameBoardView(dummyActivity) {
            @Override
            public int[][] getBoardMatrix() {
                return new int[][] { {android.R.color.transparent, android.R.color.transparent, android.R.color.transparent, android.R.color.transparent, android.R.color.transparent},
                                     {android.R.color.transparent, android.R.color.transparent, android.R.color.transparent, android.R.color.transparent, android.R.color.transparent},
                                     {android.R.color.transparent, android.R.color.black,       android.R.color.black,       android.R.color.transparent, android.R.color.transparent},
                                     {android.R.color.black,       android.R.color.black,       android.R.color.transparent, android.R.color.transparent, android.R.color.black} };
            }
        };

        tetrominoO = new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.O)
            .build();

        assertThat(tetrominoO.moveTo(Tetromino.Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.DOWN)).isTrue();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.LEFT)).isFalse();
        assertThat(tetrominoO.moveTo(Tetromino.Direction.DOWN)).isFalse();
    }

    @Test
    public void shouldNotRotate() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity) {
            @Override
            public int[][] getBoardMatrix() {
                return new int[][] { {android.R.color.transparent, android.R.color.transparent, android.R.color.transparent},
                                     {android.R.color.transparent, android.R.color.transparent, android.R.color.transparent},
                                     {android.R.color.black,       android.R.color.black,       android.R.color.black},
                                     {android.R.color.black,       android.R.color.transparent, android.R.color.black} };
            }
        };

        Tetromino tetrominoL = new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.L)
            .build();

        assertThat(tetrominoL.rotate()).isFalse();

        gameBoardView = new GameBoardView(dummyActivity) {
            @Override
            public int[][] getBoardMatrix() {
                return new int[][] { {android.R.color.transparent, android.R.color.transparent, android.R.color.transparent},
                                     {android.R.color.transparent, android.R.color.transparent, android.R.color.transparent},
                                     {android.R.color.transparent, android.R.color.transparent, android.R.color.transparent}, };
            }
        };

        tetrominoL = new Tetromino.Builder(gameBoardView)
            .use(TetrominoShape.L)
            .build();

        assertThat(tetrominoL.moveTo(Tetromino.Direction.DOWN)).isTrue();
        assertThat(tetrominoL.rotate()).isFalse();
    }
}