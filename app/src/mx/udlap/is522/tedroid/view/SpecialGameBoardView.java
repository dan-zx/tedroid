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
import android.view.GestureDetector;
import android.view.MotionEvent;

import mx.udlap.is522.tedroid.R;

/**
 * Tablero del juego donde los tetrominos y los tetrominos especiales se acumlan.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta
 * @since 1.0
 */
public class SpecialGameBoardView extends GameBoardView {

    private SpecialGestureListener gestureListener;
    private GestureDetector gestureDetector;
    private boolean invertedBoardMatrix;
    private boolean invisibleBoardMatrix;
    private boolean extraFeature;

    public SpecialGameBoardView(Context context) {
        super(context);
    }

    public SpecialGameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpecialGameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setUp() {
        super.setUp();
        gestureListener = new SpecialGestureListener();
        gestureDetector = new GestureDetector(getContext(), gestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void drawViewOnCanvas(Canvas canvas) {
        if (!isInEditMode()) {
            drawBackgroundGrid(canvas);
            if (!invertedBoardMatrix) getCurrentTetromino().drawOn(canvas);
            else getCurrentTetromino().drawInverted(canvas);
            if (!invertedBoardMatrix && !invisibleBoardMatrix) drawBoardMatrix(canvas);
            else if (!invisibleBoardMatrix) drawInvertedBoardMatrix(canvas);
        } else super.drawViewOnCanvas(canvas);
    }

    @Override
    protected SpecialTetromino randomTetromino() {
        int randomIndex = getRandom().nextInt(SpecialTetrominoShape.values().length);
        SpecialTetrominoShape randomShape = SpecialTetrominoShape.values()[randomIndex];
        return new SpecialTetromino.Builder(this).use(randomShape).build();
    }

    /**
     * Dibuja los tetrominos acumlados en el tablero al revés.
     * 
     * @param canvas un Canvas donde dibujar.
     */
    private void drawInvertedBoardMatrix(Canvas canvas) {
        for (int row = 0; row < getBoardMatrix().length; row++) {
            for (int column = 0; column < getBoardMatrix()[0].length; column++) {
                if (getBoardMatrix()[row][column] != android.R.color.transparent) {
                    float x0 = column * getBoardColumnWidth();
                    float y0 = (canvas.getHeight() - getBoardRowHeight()) - ((row) * getBoardRowHeight());
                    float x1 = (column + 1) * getBoardColumnWidth();
                    float y1 = (canvas.getHeight()) - ((row) * getBoardRowHeight());
                    getTetrominoForeground().setColor(getContext().getResources().getColor(getBoardMatrix()[row][column]));
                    canvas.drawRect(x0, y0, x1, y1, getTetrominoForeground());
                    canvas.drawRect(x0, y0, x1, y1, getTetrominoBorder());
                }
            }
        }
    }

    @Override
    protected void onCurrentTetrominoOnFloor() {
        super.onCurrentTetrominoOnFloor();
        resetExtraFeatures();
    }

    /** Regresa los booleanos de las características extra a sus valores originales. */
    private void resetExtraFeatures() {
        invisibleBoardMatrix = false;
        invertedBoardMatrix = false;
        extraFeature = false;
    }

    @Override
    public SpecialTetromino getCurrentTetromino() {
        return (SpecialTetromino) super.getCurrentTetromino();
    }

    /** @return el siguiente tetromino en caer. */
    public SpecialTetromino getNextTetromino() {
        return (SpecialTetromino) super.getNextTetromino();
    }

    /**
     * Escucha los eventos del tablero para mover el tetromino en juego.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private class SpecialGestureListener extends GestureListener {

        private boolean shouldStopScrollEvent;
        private float totalDistanceX;

        @Override // TODO: Reducir la complejidad ciclomática de este método
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isGameStarted() && !isPaused() && !isGameOver() && !shouldStopScrollEvent) {
                // Scroll a los lados
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    totalDistanceX += distanceX;
                    // Derecha
                    if (distanceX < 0) {
                        if (Math.abs(totalDistanceX) >= getBoardColumnWidth()) {
                            totalDistanceX = 0;
                            if (!invertedBoardMatrix) {
                                if (getCurrentTetromino().moveTo(Tetromino.Direction.RIGHT)) onTetrominoMoved();
                            } else {
                                if (getCurrentTetromino().moveTo(Tetromino.Direction.LEFT)) onTetrominoMoved();
                            }
                        }
                        // Izquierda
                    } else {
                        if (Math.abs(totalDistanceX) >= getBoardColumnWidth()) {
                            totalDistanceX = 0;
                            if (!invertedBoardMatrix) {
                                if (getCurrentTetromino().moveTo(Tetromino.Direction.LEFT)) onTetrominoMoved();
                            } else {
                                if (getCurrentTetromino().moveTo(Tetromino.Direction.RIGHT)) onTetrominoMoved();
                            }

                        }
                    }
                    // Scroll hacia abajo
                } else super.onScroll(e1, e2, distanceX, distanceY);
            }

            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isGameStarted() && !isPaused() && !isGameOver()) {
                if (getCurrentTetromino().rotate()) {
                    onTetrominoRotated();

                    //Tetrominos con poder
                    int tetrominoColor = getCurrentTetromino().getForeground().getColor();     
                    if (tetrominoColor == getResources().getColor(R.color.tetromino_special_i) && !extraFeature) {
                        extraFeature = true;
                        invisibleBoardMatrix = !invisibleBoardMatrix;
                    }
                    if (tetrominoColor == getResources().getColor(R.color.tetromino_special_l) && !extraFeature) {
                        extraFeature = true;
                        gravity();
                    }
                    if (tetrominoColor == getResources().getColor(R.color.tetromino_special_s) && !extraFeature) {
                        extraFeature = true;
                        invertedBoardMatrix = !invertedBoardMatrix;
                    }
                }
            }
            return true;
        }

        private void gravity() {
            for (int col = 0; col < getBoardMatrix()[0].length; col++) {
                int firstEmpty = -1;
                for (int ren = getBoardMatrix().length - 1; ren > 0; ren--) {
                    if (getBoardMatrix()[ren][col] == android.R.color.transparent && firstEmpty == -1) firstEmpty = ren;
                    if (getBoardMatrix()[ren][col] != android.R.color.transparent && firstEmpty != -1) {
                        getBoardMatrix()[firstEmpty][col] = getBoardMatrix()[ren][col];
                        // Con el anterior solito hay un efecto interesante
                        getBoardMatrix()[ren][col] = android.R.color.transparent;
                        firstEmpty = firstEmpty - 1;
                    }
                }
            }
        }
    }
}