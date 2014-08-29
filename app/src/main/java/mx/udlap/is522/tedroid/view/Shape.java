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

/**
 * Formas de la piezas de tetris.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public interface Shape {

    /** Color del borde de las formas. */
    int BORDER_COLOR = android.R.color.white;

    /** @return una matriz de android.R.color.transparent y R.color.ids con la forma de la figura. */
    int[][] getShapeMatrix();

    /** @return si la figura tiene o no rotación */
    boolean hasRotation();
}