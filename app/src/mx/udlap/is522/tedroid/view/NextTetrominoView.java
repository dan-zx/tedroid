package mx.udlap.is522.tedroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import mx.udlap.is522.tedroid.view.model.Tetromino;

public class NextTetrominoView extends View {

    private Tetromino tetromino;

    public NextTetrominoView(Context context) {
        super(context);
    }

    public NextTetrominoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NextTetrominoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (tetromino != null) tetromino.drawOnParentGameBoardView(canvas);
    }

    public void setTetromino(Tetromino tetromino) {
        this.tetromino = tetromino;
        invalidate();
    }
}