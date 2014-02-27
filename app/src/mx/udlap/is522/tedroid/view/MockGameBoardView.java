package mx.udlap.is522.tedroid.view;

import android.content.Context;
import android.util.AttributeSet;

import mx.udlap.is522.tedroid.view.model.DefaultShape;
import mx.udlap.is522.tedroid.view.model.Tetromino;

import java.util.LinkedList;
import java.util.Queue;

public class MockGameBoardView extends GameBoardView {

    private Queue<Tetromino> expectedTetrominos;

    public MockGameBoardView(Context context) {
        super(context);
    }

    public MockGameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MockGameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void setUp() {
        super.setUp();
        expectedTetrominos = new LinkedList<Tetromino>();
        setCustomDimensions(8, 10);
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.I)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.J)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.O)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.S)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.Z)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.T)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.L)
            .build());
    }
    
    @Override
    protected Tetromino getNextTetromino() {
        return expectedTetrominos.poll();
    }
}