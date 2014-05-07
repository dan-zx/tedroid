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
import android.graphics.Paint;

import java.util.Arrays;

/**
 * Define el comportamiento de cualquier tetromino o pieza de tetris.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta
 * @since 1.0
 */
public class Tetromino {

    private final GameBoardView gameBoardView;
    private final Paint foreground;
    private final Paint border;
    private final Position position;
    private final boolean hasRotation;

    private int[][] shapeMatrix;

    /**
     * Constructor. En realidad debe constuirse con un Tetromino.Builder
     * 
     * @param builder objeto para armar un nuevo tetromino.
     */
    private Tetromino(Builder builder) {
        gameBoardView = builder.gameBoardView;
        shapeMatrix = builder.shapeMatrix;
        hasRotation = builder.hasRotation;
        position = new Position();
        foreground = new Paint();
        foreground.setStyle(Paint.Style.FILL); // El color se toma de la matriz
        border = new Paint();
        border.setStyle(Paint.Style.STROKE);
        border.setColor(gameBoardView.getContext().getResources().getColor(android.R.color.white));
    }

    /**
     * Dibuja este tetromino en un canvas con las dimensiones de su tablero de juego asosiado.
     * 
     * @param canvas el objeto donde dibujar.
     */
    public void drawOn(Canvas canvas) {
        for (int row = 0; row < shapeMatrix.length; row++) {
            for (int column = 0; column < shapeMatrix[0].length; column++) {
                if (shapeMatrix[row][column] != android.R.color.transparent) {
                    foreground.setColor(gameBoardView.getContext().getResources().getColor(shapeMatrix[row][column]));
                    float x0 = (column + position.boardMatrixColumn) * gameBoardView.getBoardColumnWidth();
                    float y0 = (row + position.boardMatrixRow) * gameBoardView.getBoardRowHeight();
                    float x1 = (column + 1 + position.boardMatrixColumn) * gameBoardView.getBoardColumnWidth();
                    float y1 = (row + 1 + position.boardMatrixRow) * gameBoardView.getBoardRowHeight();
                    canvas.drawRect(x0, y0, x1, y1, foreground);
                    canvas.drawRect(x0, y0, x1, y1, border);
                }
            }
        }
    }

    /**
     * Mueve este tetromino un lugar tablero usado {@link Direction}.
     * 
     * @param direction {@link Direction#LEFT}, {@link Direction#RIGHT} o {@link Direction#DOWN}.
     * @return si se pudo mover o no.
     */
    public boolean moveTo(Direction direction) {
        switch (direction) {
            case LEFT: return moveLeft();
            case RIGHT: return moveRight();
            case DOWN: return moveDown();
            default: return false;
        }
    }

    /**
     * Centra este tetromino en el tablero padre.
     * 
     * @return si se traslapa con otras piezas o no.
     */
    public boolean centerOnGameBoardView() {
        int[][] boardMatrix = gameBoardView.getBoardMatrix();
        int boardCenterX = boardMatrix[0].length / 2;
        int shapeCenterX = shapeMatrix[0].length / 2;
        int xMoves = boardCenterX - shapeCenterX;
        position.boardMatrixColumn = xMoves;
        return canFit(getShapeMatrix());
    }

    /**
     * Rota este tetromino 90° en sentido de las agujas del reloj en el tablero.
     * 
     * @return si se pudo mover o no.
     */
    public boolean rotate() {
        if (hasRotation) {
            int[][] originalShapeMatrix = shapeMatrix;
            int[][] newShapeMatrix = new int[originalShapeMatrix[0].length][originalShapeMatrix.length];
            for (int row = 0; row < originalShapeMatrix.length; row++) {
                for (int column = 0; column < originalShapeMatrix[0].length; column++) {
                    newShapeMatrix[column][originalShapeMatrix.length - 1 - row] = originalShapeMatrix[row][column];
                }
            }

            if (canFit(newShapeMatrix)) {
                shapeMatrix = newShapeMatrix;
                return true;
            }
        }

        return false;
    }

    /** @return una matriz de 0s y 1s con la forma de este tetromino. */
    public int[][] getShapeMatrix() {
        return shapeMatrix;
    }

    /** @return si la figura tiene o no rotación este tetromino. */
    public boolean hasRotation() {
        return hasRotation;
    }

    /**
     * @return la posición en el tablero donde se encuentra la esquina superior izquierda de la
     *         matriz de este tetromino
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Mueve este tetromino un lugar hacia la derecha en el tablero.
     * 
     * @return si se pudo mover o no.
     */
    private boolean moveRight() {
        if (canFit(Direction.RIGHT)) {
            position.boardMatrixColumn++;
            return true;
        }

        return false;
    }

    /**
     * Mueve este tetromino un lugar hacia la izquierda en el tablero.
     * 
     * @return si se pudo mover o no.
     */
    private boolean moveLeft() {
        if (canFit(Direction.LEFT)) {
            position.boardMatrixColumn--;
            return true;
        }

        return false;
    }

    /**
     * Mueve este tetromino un lugar hacia abajo en el tablero.
     * 
     * @return si se pudo mover o no.
     */
    private boolean moveDown() {
        if (canFit(Direction.DOWN)) {
            position.boardMatrixRow++;
            return true;
        }

        return false;
    }

    /**
     * Genera una predicción poniendo el tetromino en el lugar en el que quedaria después de
     * rotarse.
     * 
     * @param rotatedShape la matriz rotada.
     * @return si cupo o no después de moverse.
     */
    private boolean canFit(int[][] rotatedShape) {
        int[][] boardMatrix = gameBoardView.getBoardMatrix();
        for (int row = 0; row < rotatedShape.length; row++) {
            for (int column = 0; column < rotatedShape[0].length; column++) {
                int boardMatrixRow = position.boardMatrixRow + row;
                int boardMatrixColumn = position.boardMatrixColumn + column;
                if (isRowOutOfBoundsOfBoard(boardMatrixRow) || 
                        isColumnOutOfBoundsOfBoard(boardMatrixColumn) || 
                        (boardMatrix[boardMatrixRow][boardMatrixColumn] != android.R.color.transparent && 
                        rotatedShape[row][column] != android.R.color.transparent)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Genera una predicción poniendo el tetromino en el lugar en el que quedaria después de
     * moverse.
     * 
     * @param direction {@link Direction#LEFT}, {@link Direction#RIGHT} o {@link Direction#DOWN}.
     * @return si cupo o no después de moverse.
     */
    private boolean canFit(Direction direction) {
        int[][] shape = getShapeMatrix();
        int[][] boardMatrix = gameBoardView.getBoardMatrix();
        for (int row = 0; row < shape.length; row++) {
            for (int column = 0; column < shape[0].length; column++) {
                int boardMatrixRow = position.boardMatrixRow + row;
                int boardMatrixColumn = position.boardMatrixColumn + column;
                switch (direction) {
                    case DOWN: boardMatrixRow++; break;
                    case LEFT: boardMatrixColumn--; break;
                    case RIGHT: boardMatrixColumn++; break;
                    default: break;
                }
                if (isRowOutOfBoundsOfBoard(boardMatrixRow) || 
                        isColumnOutOfBoundsOfBoard(boardMatrixColumn) || 
                        (boardMatrix[boardMatrixRow][boardMatrixColumn] != android.R.color.transparent && 
                        shape[row][column] != android.R.color.transparent)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @param row la fila.
     * @return si la fila esta fuera o no de los indices del tablero.
     */
    private boolean isRowOutOfBoundsOfBoard(int row) {
        int[][] boardMatrix = gameBoardView.getBoardMatrix();
        return row < 0 || row >= boardMatrix.length;
    }

    /**
     * @param column la columna.
     * @return si la columna esta fuera o no de los indices del tablero.
     */
    private boolean isColumnOutOfBoundsOfBoard(int column) {
        int[][] boardMatrix = gameBoardView.getBoardMatrix();
        return column < 0 || column >= boardMatrix[0].length;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (hasRotation ? 1231 : 1237);
        result = prime * result + Arrays.deepHashCode(shapeMatrix);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Tetromino other = (Tetromino) obj;
        if (hasRotation != other.hasRotation) return false;
        if (!Arrays.deepEquals(shapeMatrix, other.shapeMatrix)) return false;
        return true;
    }

    /**
     * Contiene las coordenadas de un tetromino en su tablero.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static class Position {

        private int boardMatrixColumn;
        private int boardMatrixRow;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + boardMatrixColumn;
            result = prime * result + boardMatrixRow;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Position other = (Position) obj;
            if (boardMatrixColumn != other.boardMatrixColumn) return false;
            if (boardMatrixRow != other.boardMatrixRow) return false;
            return true;
        }

        /**
         * @return la columna donde se encuentra la esquina superior izquierda de la matriz de este
         *         tetromino.
         */
        public int getBoardMatrixColumn() {
            return boardMatrixColumn;
        }

        /**
         * @return la fila donde se encuentra la esquina superior izquierda de la matriz de este
         *         tetromino.
         */
        public int getBoardMatrixRow() {
            return boardMatrixRow;
        }
    }

    /**
     * Constructor de tetrominos.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static class Builder {

        public static final TetrominoShape DEFAULT_SHAPE = TetrominoShape.O;

        private int[][] shapeMatrix;
        private boolean hasRotation;
        private GameBoardView gameBoardView;

        /**
         * Constructor que inicializa valores por default.
         * 
         * @param gameBoardView el tablero donde crear el nuevo tetromino.
         */
        public Builder(GameBoardView gameBoardView) {
            hasRotation = DEFAULT_SHAPE.hasRotation();
            shapeMatrix = new int[DEFAULT_SHAPE.getShapeMatrix().length][];
            System.arraycopy(DEFAULT_SHAPE.getShapeMatrix(), 0, shapeMatrix, 0, DEFAULT_SHAPE.getShapeMatrix().length);
            this.gameBoardView = gameBoardView;
        }

        /**
         * Construira un nuevo tetromino usando una de las figuras predefinadas.
         * 
         * @param shape un {@link TetrominoShape}.
         * @return este Builder.
         */
        public Builder use(TetrominoShape shape) {
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
        public Builder setShape(int[][] shapeMatrix) {
            this.shapeMatrix = new int[shapeMatrix.length][];
            System.arraycopy(shapeMatrix, 0, this.shapeMatrix, 0, shapeMatrix.length);
            return this;
        }

        /**
         * Construira un nuevo tetromino que podrá rotar.
         * 
         * @return este Builder.
         */
        public Builder hasRotation() {
            this.hasRotation = true;
            return this;
        }

        /**
         * @return un nuevo tetromino.
         */
        public Tetromino build() {
            return new Tetromino(this);
        }
    }

    /**
     * Constantes de dirección
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static enum Direction {

        RIGHT, LEFT, DOWN
    }
}