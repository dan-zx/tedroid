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

import android.graphics.Canvas;

/**
 * Define el comportamiento de cualquier tetromino o pieza de tetris.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta
 * @since 1.0
 */
public class SpecialTetromino extends Tetromino {

    SpecialTetromino(GameBoardView gameBoardView, int[][] shapeMatrix, boolean hasRotation) {
        super(gameBoardView, shapeMatrix, hasRotation);
    }

    /**
     * Dibuja este tetromino en un canvas invertido.
     * 
     * @param canvas el objeto donde dibujar.
     */
    void drawInverted(Canvas canvas) {
        for (int row = 0; row < getShapeMatrix().length; row++) {
            for (int column = 0; column < getShapeMatrix()[0].length; column++) {
                if (getShapeMatrix()[row][column] != android.R.color.transparent) {
                    getForeground().setColor(getGameBoardView().getContext().getResources().getColor(getShapeMatrix()[row][column]));
                    float x0 = (column + getPosition().getBoardMatrixColumn()) * getGameBoardView().getBoardColumnWidth();
                    float y0 = (canvas.getHeight()-getGameBoardView().getBoardRowHeight())-((row + getPosition().getBoardMatrixRow()) * getGameBoardView().getBoardRowHeight());
                    float x1 = (column + 1 + getPosition().getBoardMatrixColumn()) * getGameBoardView().getBoardColumnWidth();
                    float y1 = (canvas.getHeight())-((row + getPosition().getBoardMatrixRow()) * getGameBoardView().getBoardRowHeight());
                    canvas.drawRect(x0, y0, x1, y1, getForeground());
                    canvas.drawRect(x0, y0, x1, y1, getBorder());
                }
            }
        }
    }

    /**
     * Constructor de tetrominos especiales.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    static final class Builder {

        static final Shape DEFAULT_SHAPE = TetrominoShape.O;

        private int[][] shapeMatrix;
        private boolean hasRotation;
        private GameBoardView gameBoardView;

        /**
         * Constructor que inicializa valores por default.
         * 
         * @param gameBoardView el tablero donde crear el nuevo tetromino.
         */
        Builder(GameBoardView gameBoardView) {
            hasRotation = DEFAULT_SHAPE.hasRotation();
            shapeMatrix = new int[DEFAULT_SHAPE.getShapeMatrix().length][];
            System.arraycopy(DEFAULT_SHAPE.getShapeMatrix(), 0, shapeMatrix, 0, DEFAULT_SHAPE.getShapeMatrix().length);
            this.gameBoardView = gameBoardView;
        }

        /**
         * Construira un nuevo tetromino usando una de las figuras predefinadas.
         * 
         * @param shape un {@link SpecialTetrominoShape}.
         * @return este Builder.
         */
        Builder use(SpecialTetrominoShape shape) {
            shapeMatrix = new int[shape.getShapeMatrix().length][];
            System.arraycopy(shape.getShapeMatrix(), 0, shapeMatrix, 0, shape.getShapeMatrix().length);
            hasRotation = shape.hasRotation();
            return this;
        }

        /**
         * Construira un nuevo tetromino con la forma de una matriz de 1s y 0s.
         * 
         * @param shapeMatrix una matriz de 1s y 0s.
         * @return este Builder.
         */
        Builder setShape(int[][] shapeMatrix) {
            this.shapeMatrix = new int[shapeMatrix.length][];
            System.arraycopy(shapeMatrix, 0, this.shapeMatrix, 0, shapeMatrix.length);
            return this;
        }

        /**
         * Construira un nuevo tetromino que podrá rotar.
         * 
         * @return este Builder.
         */
        Builder hasRotation() {
            this.hasRotation = true;
            return this;
        }

        /**
         * @return un nuevo tetromino.
         */
        SpecialTetromino build() {
            return new SpecialTetromino(gameBoardView, shapeMatrix, hasRotation);
        }
    }
}