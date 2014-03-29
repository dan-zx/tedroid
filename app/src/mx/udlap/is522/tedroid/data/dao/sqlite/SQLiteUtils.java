package mx.udlap.is522.tedroid.data.dao.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import mx.udlap.is522.tedroid.util.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Metodos y constantes genéricas para bases de datos SQLite.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public final class SQLiteUtils {

    /**
     * Formatos de tiempo de SQLite.
     * 
     * @see <a href="http://sqlite.org/lang_datefunc.html">SQLite - Date And Time Functions</a>
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static enum TimeString {
        DATE("yyyy-MM-dd"), 
        DATETIME("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"), 
        TIMESTAMP("yyyy-MM-dd HH:mm:ss.SSS"), 
        TIME("HH:mm:ss.SSS", "HH:mm:ss", "HH:mm");
        
        private final String[] formats;
        
        TimeString(String... formats) {
            this.formats = formats;
        }
        
        public String[] getFormats() {
            return formats;
        }
    }

    /**
     * El valor que {@link android.database.Cursor#getColumnIndex(String)}
     * devuelve cuando la columna no se encuentra.
     */
    public static final int COLUMN_NOT_FOUND = -1;
    
    private static final String TAG = SQLiteUtils.class.getSimpleName();

    /**
     * NO INVOCAR.
     */
    private SQLiteUtils() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * Termina la transacción en curso si es posible.
     * 
     * @param database un objeto SQLiteDatabase.
     */
    public static void endTransaction(SQLiteDatabase database) {
        if (database != null) {
            try {
                database.endTransaction();
            } catch (Exception ex) {
                Log.e(TAG, "Couldn't close database correctly");
            }
        }
    }

    /**
     * Cierra el objeto SQLiteDatabase provisto.
     * 
     * @param database un objeto SQLiteDatabase.
     */
    public static void close(SQLiteDatabase database) {
        if (database != null) {
            try {
                database.close();
            } catch (Exception ex) {
                Log.e(TAG, "Couldn't close database correctly");
            }
        }
    }

    /**
     * Cierra el objeto Cursor provisto.
     * 
     * @param cursor un objeto Cursor.
     */
    public static void close(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception ex) {
                Log.e(TAG, "Couldn't close cursor correctly");
            }
        }
    }

    /**
     * Cierra el objeto SQLiteStatement provisto.
     * 
     * @param statement un objeto SQLiteStatement.
     */
    public static void close(SQLiteStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Log.e(TAG, "Couldn't close statement correctly");
            }
        }
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
    public static Byte getByte(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? (byte) cursor.getShort(cursor.getColumnIndex(columnName))
                : null;
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

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
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
    public static Boolean getBoolean(Cursor cursor, String columnName) {
        Boolean value = null;
        if (containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))) {
            short intValue = cursor.getShort(cursor.getColumnIndex(columnName));
            value = intValue == 1 ? true : intValue == 0 ? false : null;
        }

        return value;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    public static Character getCharacter(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getString(cursor.getColumnIndex(columnName)).charAt(0)
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
    public static Date getDateFromUnixTime(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? new Date(cursor.getLong(cursor.getColumnIndex(columnName)) * 1000L)
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @param timeString el formato de la fecha.
     * @return el valor de la columna. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    public static Date getDateFromString(Cursor cursor, String columnName, TimeString timeString) {
        String dateString = containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getString(cursor.getColumnIndex(columnName))
                : null;

        if (!Strings.isNullOrBlank(dateString)) {
            for (String format : timeString.formats) {
                try {
                    return new SimpleDateFormat(format, Locale.ENGLISH).parse(dateString);
                } catch (ParseException ex) { }
            }
        }

        return null;
    }
}