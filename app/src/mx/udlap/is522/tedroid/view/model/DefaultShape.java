package mx.udlap.is522.tedroid.view.model;

import mx.udlap.is522.tedroid.R;

/**
 * Constantes de formas básicas de tetrominos.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public enum DefaultShape {

    I(new int[][] { { R.color.red, R.color.red, R.color.red, R.color.red } }, true),
    J(new int[][] { { R.color.gray, R.color.gray, R.color.gray }, { android.R.color.transparent, android.R.color.transparent, R.color.gray } }, true), 
    L(new int[][] { { R.color.magenta, R.color.magenta, R.color.magenta }, { R.color.magenta, android.R.color.transparent, android.R.color.transparent } }, true), 
    O(new int[][] { { R.color.blue, R.color.blue }, { R.color.blue, R.color.blue } }, false), 
    S(new int[][] { { android.R.color.transparent, R.color.green, R.color.green }, { R.color.green, R.color.green, android.R.color.transparent } }, true), 
    T(new int[][] { { R.color.brown, R.color.brown, R.color.brown }, { android.R.color.transparent, R.color.brown, android.R.color.transparent } }, true), 
    Z(new int[][] { { R.color.cyan, R.color.cyan, android.R.color.transparent }, { android.R.color.transparent, R.color.cyan, R.color.cyan } }, true);

    private final int[][] shapeMatrix;
    private final boolean hasRotation;

    /**
     * Construye una de las figura por default.
     * 
     * @param shapeMatrix la forma en una matriz de android.R.color.transparent
     *        y R.color.id.
     * @param hasRotation si tiene o no rotación.
     */
    DefaultShape(int[][] shapeMatrix, boolean hasRotation) {
        this.shapeMatrix = shapeMatrix;
        this.hasRotation = hasRotation;
    }

    /**
     * @return una matriz de android.R.color.transparent y R.color.ids con la
     *         forma de la figura.
     */
    public int[][] getShapeMatrix() {
        return shapeMatrix;
    }

    /**
     * @return si la figura tiene o no rotación
     */
    public boolean hasRotation() {
        return hasRotation;
    }
}