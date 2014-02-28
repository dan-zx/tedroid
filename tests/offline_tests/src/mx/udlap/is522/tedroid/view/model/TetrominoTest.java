package mx.udlap.is522.tedroid.view.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.view.GameBoardView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "../../app/AndroidManifest.xml")
public class TetrominoTest {

    private Activity dummyActivity;

    @Before
    public void setUp() throws Exception {
        dummyActivity = Robolectric.buildActivity(Activity.class).create().get();
        assertNotNull("Activity cannot be null", dummyActivity);
    }

    @Test
    public void shouldBuildWithDefaultShape() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino tetrominoZ = new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.Z)
            .build();

        assertNotNull("Tetromino wasn't built correctly", tetrominoZ);
        assertNotNull("Tetromino matrix cannot be null", tetrominoZ.getShapeMatrix());
        assertArrayEquals("Tetromino matrix isn't the same", DefaultShape.Z.getShapeMatrix(), tetrominoZ.getShapeMatrix());
        assertNotEquals("Tetromino rotation wasn't changed", Tetromino.Builder.DEFAULT_HAS_ROATATION, tetrominoZ.hasRotation());
        assertEquals("Tetromino rotation isn't the same", DefaultShape.Z.hasRotation(), tetrominoZ.hasRotation());
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

        assertNotNull("Tetromino wasn't built correctly", newTetromino);
        assertNotNull("Tetromino matrix cannot be null", newTetromino.getShapeMatrix());
        assertArrayEquals("Tetromino matrix isn't the same", shape, newTetromino.getShapeMatrix());
        assertNotEquals("Tetromino rotation wasn't changed", Tetromino.Builder.DEFAULT_HAS_ROATATION, newTetromino.hasRotation());
        assertEquals("Tetromino rotation isn't the same", true, newTetromino.hasRotation());
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

        assertTrue("Tetromino matrix wasn't rotated correctly", tetrominoT.rotate());
        assertArrayEquals("Tetromino matrix wasn't rotated correctly", rotatedShape1, tetrominoT.getShapeMatrix());
        assertTrue("Tetromino matrix wasn't rotated correctly", tetrominoT.rotate());
        assertArrayEquals("Tetromino matrix wasn't rotated correctly", rotatedShape2, tetrominoT.getShapeMatrix());
        assertTrue("Tetromino matrix wasn't rotated correctly", tetrominoT.rotate());
        assertArrayEquals("Tetromino matrix wasn't rotated correctly", rotatedShape3, tetrominoT.getShapeMatrix());
        assertTrue("Tetromino matrix wasn't rotated correctly", tetrominoT.rotate());
        assertArrayEquals("Tetromino matrix wasn't rotated correctly", DefaultShape.T.getShapeMatrix(), tetrominoT.getShapeMatrix());
    }

    @Test
    public void shouldMove() throws Exception {
        GameBoardView gameBoardView = new GameBoardView(dummyActivity);
        Tetromino tetrominoO = new Tetromino.Builder(gameBoardView)
            .use(DefaultShape.O)
            .build();

        Tetromino.Position expectedPosition = new Tetromino.Position();
        expectedPosition.setX(1);
        expectedPosition.setY(3);

        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.RIGHT));
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.RIGHT));
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.LEFT));
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.DOWN));
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.DOWN));
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.DOWN));
        assertEquals("Tetromino wasn't moved correctly", expectedPosition, tetrominoO.getPositionOnBoard());
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
        
        assertFalse("Tetromino shouldn't have moved", tetrominoO.moveTo(Direction.RIGHT));
        assertFalse("Tetromino shouldn't have moved", tetrominoO.moveTo(Direction.LEFT));
        assertFalse("Tetromino shouldn't have moved", tetrominoO.moveTo(Direction.DOWN));
        
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
        
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.RIGHT));
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.RIGHT));
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.RIGHT));
        assertTrue("Tetromino wasn't moved correctly", tetrominoO.moveTo(Direction.DOWN));
        assertFalse("Tetromino shouldn't have moved", tetrominoO.moveTo(Direction.LEFT));
        assertFalse("Tetromino shouldn't have moved", tetrominoO.moveTo(Direction.DOWN));
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
        
        assertFalse("Tetromino shouldn't have have rotated", tetrominoL.rotate());
        
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
        
        assertTrue("Tetromino wasn't moved correctly", tetrominoL.moveTo(Direction.DOWN));
        assertFalse("Tetromino shouldn't have have rotated", tetrominoL.rotate());
    }
}