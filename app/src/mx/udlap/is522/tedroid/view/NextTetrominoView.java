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

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Vista que muestra el siguiente tetromino en caer.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class NextTetrominoView extends View {

    private Tetromino tetromino;

    /**
     * Construye la vista mediante un context.
     * 
     * @see android.view.View#View(Context)
     */
    public NextTetrominoView(Context context) {
        super(context);
    }

    /**
     * Construye la vista mediante XML
     * 
     * @see android.view.View#View(Context, AttributeSet)
     */
    public NextTetrominoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Construye la vista mediante XML y aplicando un estilo.
     * 
     * @see android.view.View#View(Context, AttributeSet, int)
     */
    public NextTetrominoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode()) {
            if (tetromino != null) {
                canvas.translate(10, 10);
                tetromino.drawOn(canvas);
            }
        }
    }

    /** @param tetromino el tetromino a dibujar. */
    public void setTetromino(Tetromino tetromino) {
        this.tetromino = tetromino;
        invalidate();
    }
}