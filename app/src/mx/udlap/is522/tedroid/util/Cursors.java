package mx.udlap.is522.tedroid.util;

import android.database.Cursor;

/**
 * Metodos y constantes genéricas de la clase Cursor.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public final class Cursors {

    /**
     * El valor que {@link android.database.Cursor#getColumnIndex(String)}
     * devuelve cuando la columna no se encuentra.
     */
    public static final int COLUMN_NOT_FOUND = -1;

    /**
     * NO INVOCAR.
     */
    private Cursors() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return si contiene o no la columna.
     */
    public static boolean containsColumn(Cursor cursor, String columnName) {
        return cursor.getColumnIndex(columnName) != COLUMN_NOT_FOUND;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    public static Short getShort(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getShort(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    public static Integer getInteger(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getInt(cursor.getColumnIndex(columnName))
                : null;
    }

    public static Long getLong(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getLong(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    public static Float getFloat(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getFloat(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    public static Double getDouble(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getDouble(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    public static byte[] getBlob(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getBlob(cursor.getColumnIndex(columnName)) 
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    public static String getString(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getString(cursor.getColumnIndex(columnName)) 
                : null;
    }
}