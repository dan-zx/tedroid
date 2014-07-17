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

import mx.udlap.is522.tedroid.R;

/**
 * Constantes de formas básicas y especiales de tetrominos.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta
 * @since 1.0
 */
public enum SpecialTetrominoShape implements Shape {

    // Tetrominos clásicos
    I (TetrominoShape.I.getShapeMatrix(), TetrominoShape.I.hasRotation()),
    J (TetrominoShape.J.getShapeMatrix(), TetrominoShape.J.hasRotation()),
    L (TetrominoShape.L.getShapeMatrix(), TetrominoShape.L.hasRotation()),
    O (TetrominoShape.O.getShapeMatrix(), TetrominoShape.O.hasRotation()),
    S (TetrominoShape.S.getShapeMatrix(), TetrominoShape.S.hasRotation()),
    T (TetrominoShape.T.getShapeMatrix(), TetrominoShape.T.hasRotation()),
    Z (TetrominoShape.Z.getShapeMatrix(), TetrominoShape.Z.hasRotation()),

    // Tetrominos especiales
    SPECIAL_I (new int[][] { { R.color.tetromino_special_i, R.color.tetromino_special_i, R.color.tetromino_special_i, R.color.tetromino_special_i } }, true),
    SPECIAL_L (new int[][] { { R.color.tetromino_special_l, R.color.tetromino_special_l, R.color.tetromino_special_l }, { R.color.tetromino_special_l, android.R.color.transparent, android.R.color.transparent } }, true), 
    SPECIAL_S (new int[][] { { android.R.color.transparent, R.color.tetromino_special_s, R.color.tetromino_special_s }, { R.color.tetromino_special_s, R.color.tetromino_special_s, android.R.color.transparent } }, true);

    private final int[][] shapeMatrix;
    private final boolean hasRotation;

    /**
     * Construye una de las figura por default.
     * 
     * @param shapeMatrix la forma en una matriz de android.R.color.transparent y R.color.id.
     * @param hasRotation si tiene o no rotación.
     */
    SpecialTetrominoShape(int[][] shapeMatrix, boolean hasRotation) {
        this.shapeMatrix = shapeMatrix;
        this.hasRotation = hasRotation;
    }

    @Override
    public int[][] getShapeMatrix() {
        return shapeMatrix;
    }

    @Override
    public boolean hasRotation() {
        return hasRotation;
    }
}