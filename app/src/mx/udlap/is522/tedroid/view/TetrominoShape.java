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
 * Constantes de formas básicas de tetrominos.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public enum TetrominoShape {

    I (new int[][] { { R.color.tetromino_i, R.color.tetromino_i, R.color.tetromino_i, R.color.tetromino_i } }, true),
    J (new int[][] { { R.color.tetromino_j, R.color.tetromino_j, R.color.tetromino_j }, { android.R.color.transparent, android.R.color.transparent, R.color.tetromino_j } }, true), 
    L (new int[][] { { R.color.tetromino_l, R.color.tetromino_l, R.color.tetromino_l }, { R.color.tetromino_l, android.R.color.transparent, android.R.color.transparent } }, true), 
    O (new int[][] { { R.color.tetromino_o, R.color.tetromino_o }, { R.color.tetromino_o, R.color.tetromino_o } }, false), 
    S (new int[][] { { android.R.color.transparent, R.color.tetromino_s, R.color.tetromino_s }, { R.color.tetromino_s, R.color.tetromino_s, android.R.color.transparent } }, true), 
    T (new int[][] { { R.color.tetromino_t, R.color.tetromino_t, R.color.tetromino_t }, { android.R.color.transparent, R.color.tetromino_t, android.R.color.transparent } }, true), 
    Z (new int[][] { { R.color.tetromino_z, R.color.tetromino_z, android.R.color.transparent }, { android.R.color.transparent, R.color.tetromino_z, R.color.tetromino_z } }, true);

    private final int[][] shapeMatrix;
    private final boolean hasRotation;

    /**
     * Construye una de las figura por default.
     * 
     * @param shapeMatrix la forma en una matriz de android.R.color.transparent y R.color.id.
     * @param hasRotation si tiene o no rotación.
     */
    TetrominoShape(int[][] shapeMatrix, boolean hasRotation) {
        this.shapeMatrix = shapeMatrix;
        this.hasRotation = hasRotation;
    }

    /** @return una matriz de android.R.color.transparent y R.color.ids con la forma de la figura. */
    public int[][] getShapeMatrix() {
        return shapeMatrix;
    }

    /** @return si la figura tiene o no rotación */
    public boolean hasRotation() {
        return hasRotation;
    }
}