package mx.udlap.is522.tedroid.view.model;

import static org.fest.assertions.api.Assertions.assertThat;

import android.app.Activity;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.view.GameBoardView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TetrominoTest {

    private Activity dummyActivity;

    @Before
    public void setUp() throws Exception {
        dummyActivity = Robolectric.buildActivity(Activity.class).create().get();
        assertThat(dummyActivity).isNotNull();
    }

    @Test
    public void shouldBuildWithDefaultShape() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino tetrominoZ = new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.Z)
            .build();

        assertThat(tetrominoZ).isNotNull();
        assertThat(tetrominoZ.getShapeMatrix()).isNotNull().isEqualTo(DefaultShape.Z.getShapeMatrix());
        assertThat(tetrominoZ.hasRotation()).isNotEqualTo(Tetromino.Builder.DEFAULT_HAS_ROATATION).isEqualTo(DefaultShape.Z.hasRotation());
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
        assertThat(newTetromino.hasRotation()).isNotEqualTo(Tetromino.Builder.DEFAULT_HAS_ROATATION).isTrue();
    }

    @Test
    public void shouldRotate() throws Exception {
        final int[][] rotatedShape1 = { { android.R.color.transparent, R.color.brown, }, 
                                        { R.color.brown, R.color.brown, }, 
                                        { android.R.color.transparent, R.color.brown, } };

        final int[][] rotatedShape2 = { { android.R.color.transparent, R.color.brown, android.R.color.transparent }, 
                                        { R.color.brown, R.color.brown, R.color.brown } };

        final int[][] rotatedShape3 = { { R.color.brown, android.R.color.transparent, }, 
                                        { R.color.brown, R.color.brown, }, 
                                        { R.color.brown, android.R.color.transparent, } };

        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino tetrominoT = new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.T)
            .build();

        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(rotatedShape1);
        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(rotatedShape2);
        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(rotatedShape3);
        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(DefaultShape.T.getShapeMatrix());
        assertThat(tetrominoT.rotate()).isTrue();
        assertThat(tetrominoT.getShapeMatrix()).isEqualTo(rotatedShape1);
    }

    @Test
    public void shouldMove() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino tetrominoO = new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.O)
            .build();

        assertThat(tetrominoO.moveTo(Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.LEFT)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.DOWN)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.DOWN)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.DOWN)).isTrue();
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
            .use(DefaultShape.O)
            .build();

        assertThat(tetrominoO.moveTo(Direction.RIGHT)).isFalse();
        assertThat(tetrominoO.moveTo(Direction.LEFT)).isFalse();
        assertThat(tetrominoO.moveTo(Direction.DOWN)).isFalse();

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
            .use(DefaultShape.O)
            .build();

        assertThat(tetrominoO.moveTo(Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.RIGHT)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.DOWN)).isTrue();
        assertThat(tetrominoO.moveTo(Direction.LEFT)).isFalse();
        assertThat(tetrominoO.moveTo(Direction.DOWN)).isFalse();
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
            .use(DefaultShape.L)
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
            .use(DefaultShape.L)
            .build();

        assertThat(tetrominoL.moveTo(Direction.DOWN)).isTrue();
        assertThat(tetrominoL.rotate()).isFalse();
    }
}