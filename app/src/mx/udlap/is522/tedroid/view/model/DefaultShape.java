package mx.udlap.is522.tedroid.view.model;

import mx.udlap.is522.tedroid.R;

/**
 * Constantes de formas básicas de tetrominos.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public enum DefaultShape {

    I (new int[][] { { 1, 1, 1, 1 } }, R.color.tretromino_i, true), 
    J (new int[][] { { 1, 1, 1 }, { 0, 0, 1 } }, R.color.tretromino_j, true), 
    L (new int[][] { { 1, 1, 1 }, { 1, 0, 0 } }, R.color.tretromino_l, true), 
    O (new int[][] { { 1, 1 }, { 1, 1 } }, R.color.tretromino_o, false), 
    S (new int[][] { { 0, 1, 1 }, { 1, 1, 0 } }, R.color.tretromino_s, true), 
    T (new int[][] { { 1, 1, 1 }, { 0, 1, 0 } }, R.color.tretromino_t, true), 
    Z (new int[][] { { 1, 1, 0 }, { 0, 1, 1 } }, R.color.tretromino_z, true);

    private final int[][] shapeMatrix;
    private final int colorId;
    private final boolean hasRotation;

    /**
     * Construye una de las figura por default.
     * 
     * @param shapeMatrix la forma en una matriz de 0s y 1s.
     * @param colorId el id del color de la figura.
     * @param hasRotation si tiene o no rotación.
     */
    DefaultShape(int[][] shapeMatrix, int colorId, boolean hasRotation) {
	this.shapeMatrix = shapeMatrix;
	this.colorId = colorId;
	this.hasRotation = hasRotation;
    }

    /**
     * @return una matriz de 0s y 1s con la forma de la figura.
     */
    public int[][] getShapeMatrix() {
	return shapeMatrix;
    }

    /**
     * @return el id del color de la figura.
     */
    public int getColorId() {
	return colorId;
    }

    /**
     * @return si la figura tiene o no rotación
     */
    public boolean hasRotation() {
        return hasRotation;
    }
}