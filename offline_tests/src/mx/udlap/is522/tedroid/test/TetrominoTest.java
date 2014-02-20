package mx.udlap.is522.tedroid.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import mx.udlap.is522.tedroid.view.GameBoardView;
import mx.udlap.is522.tedroid.view.model.DefaultShape;
import mx.udlap.is522.tedroid.view.model.Tetromino;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;

@RunWith(RobolectricTestRunner.class)
public class TetrominoTest {
    
    private Activity fakeActivity;
    private GameBoardView fakeGameBoardView;
    
    @Before
    public void setUp() throws Exception {
	fakeActivity = Robolectric.buildActivity(Activity.class).create().get();
	assertNotNull("fakeActivity cannot be null", fakeActivity);
	
	fakeGameBoardView = new GameBoardView(fakeActivity);
	assertNotNull("fakeGameBoardView cannot be null", fakeGameBoardView);
    }

    @Test
    public void shouldBuildWithDefaultShape() throws Exception {
	Tetromino tetrominoZ = new Tetromino.Builder(fakeGameBoardView)
		.use(DefaultShape.Z)
		.build();
	
	assertNotNull("Tetromino wasn't built correctly", tetrominoZ);
	assertNotNull("Tetromino matrix cannot be null", tetrominoZ.getShapeMatrix());
	assertArrayEquals("Tetromino matrix is not the same", DefaultShape.Z.getShapeMatrix(), tetrominoZ.getShapeMatrix());
	assertNotEquals("Tetromino rotation wasn't changed", Tetromino.Builder.DEFAULT_HAS_ROATATION, tetrominoZ.hasRotation());
	assertEquals("Tetromino rotation is not the same", DefaultShape.Z.hasRotation(), tetrominoZ.hasRotation());
	assertNotEquals("Tetromino color wasn't changed", Tetromino.Builder.DEFAULT_COLOR, tetrominoZ.getForegroundColor());
	assertEquals("Tetromino color is not the same", fakeActivity.getResources().getColor(DefaultShape.Z.getColorId()), tetrominoZ.getForegroundColor());
    }

    @Test
    public void shouldBuildRandomDefaultShape() throws Exception {
	Tetromino randomTetromino = new Tetromino.Builder(fakeGameBoardView)
		.useRandomDefaultShape()
		.build();
	
	assertNotNull("Tetromino wasn't built correctly", randomTetromino);
	assertNotNull("Tetromino matrix cannot be null", randomTetromino.getShapeMatrix());
	assertNotEquals("Tetromino color wasn't changed", Tetromino.Builder.DEFAULT_COLOR, randomTetromino.getForegroundColor());
    }

    @Test
    public void shouldBuildNewShape() throws Exception {
	final int color = 0xffffff00; // amarillo
	final int[][] shape = { 
			{1, 1, 0},
			{0, 1, 1},
			{0, 1, 0},
		};
	
	Tetromino newTetromino = new Tetromino.Builder(fakeGameBoardView)
		.setHexColor(color) 
		.setShape(shape)
		.hasRotation()
		.build();
	
	assertNotNull("Tetromino wasn't built correctly", newTetromino);
	assertNotNull("Tetromino matrix cannot be null", newTetromino.getShapeMatrix());
	assertArrayEquals("Tetromino matrix is not the same", shape, newTetromino.getShapeMatrix());
	assertNotEquals("Tetromino rotation wasn't changed", Tetromino.Builder.DEFAULT_HAS_ROATATION, newTetromino.hasRotation());
	assertEquals("Tetromino rotation is not the same", true, newTetromino.hasRotation());
	assertNotEquals("Tetromino color wasn't changed", Tetromino.Builder.DEFAULT_COLOR, newTetromino.getForegroundColor());
	assertEquals("Tetromino color is not the same", color, newTetromino.getForegroundColor());
    }
    
    @Test
    public void shouldRotate() throws Exception {
	final int[][] rotatedShape1 = { 
		{ 0, 1,}, 
		{ 1, 1,},
		{ 0, 1,}
	};
	
	final int[][] rotatedShape2 = { 
		{ 0, 1, 0 },
		{ 1, 1, 1 } 
	};
	
	final int[][] rotatedShape3 = { 
		{ 1, 0,}, 
		{ 1, 1,},
		{ 1, 0,}
	};
	
	Tetromino tetrominoT = new Tetromino.Builder(fakeGameBoardView)
		.use(DefaultShape.T)
		.build();
	
	tetrominoT.rotate();
	assertArrayEquals("Tetromino matrix was not rotated correctly", rotatedShape1, tetrominoT.getShapeMatrix());
	
	tetrominoT.rotate();
	assertArrayEquals("Tetromino matrix was not rotated correctly", rotatedShape2, tetrominoT.getShapeMatrix());
	
	tetrominoT.rotate();
	assertArrayEquals("Tetromino matrix was not rotated correctly", rotatedShape3, tetrominoT.getShapeMatrix());
	
	tetrominoT.rotate();
	assertArrayEquals("Tetromino matrix was not rotated correctly", DefaultShape.T.getShapeMatrix(), tetrominoT.getShapeMatrix());
    }
}