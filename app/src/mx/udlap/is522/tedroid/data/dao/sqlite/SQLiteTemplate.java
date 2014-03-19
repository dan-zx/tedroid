package mx.udlap.is522.tedroid.data.dao.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de conveniencia para no repitir código relacionado con transacciones de
 * SQLite.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class SQLiteTemplate {

    private static final String TAG = SQLiteTemplate.class.getSimpleName();

    private final SQLiteOpenHelper databaseHelper;

    /**
     * Construye un nuevo SQLiteTemplate.
     * 
     * @param databaseHelper el objeto SQLiteOpenHelper a usar.
     */
    public SQLiteTemplate(SQLiteOpenHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Ejecuta una consulta en la base de datos para recuperar un solo
     * resultado.
     * 
     * @param <T> el tipo del objeto a regresar.
     * @param sql la sentencia SQL a ejecutar.
     * @param rowMapper el objeto que mapeará el resultado de la consulta.
     * @return un objeto tipo T o {@code null} si no hubo resultado o hay más de
     *         un resultado.
     */
    public <T> T queryForSingleResult(String sql, RowMapper<T> rowMapper) {
        return queryForSingleResult(sql, null, rowMapper);
    }

    /**
     * Ejecuta una consulta en la base de datos para recuperar un solo
     * resultado.
     * 
     * @param <T> el tipo del objeto a regresar.
     * @param sql la sentencia SQL a ejecutar.
     * @param args los argumentos que reemplazarán los '?' de la consulta.
     * @param rowMapper el objeto que mapeará el resultado de la consulta.
     * @return un objeto tipo T o {@code null} si no hubo resultadoa o hay más
     *         de un resultado.
     */
    public <T> T queryForSingleResult(String sql, String[] args, RowMapper<T> rowMapper) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Log.d(TAG, "<-" + sql);
        Cursor cursor = database.rawQuery(sql, args);
        T object = null;
        if (cursor.getCount() == 1 && cursor.moveToNext()) object = rowMapper.mapRow(cursor, 1);
        cursor.close();
        database.close();
        return object;
    }

    /**
     * Ejecuta una consulta en la base de datos para recuperar una lista de
     * resultados.
     * 
     * @param <T> el tipo de la lista a regresar.
     * @param sql la sentencia SQL a ejecutar.
     * @param rowMapper el objeto que mapeará cada fila del resultado de la
     *        consulta.
     * @return una lista con objetos tipo T o una lista vacia si no hubo
     *         resultados.
     */
    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper) {
        return queryForList(sql, null, rowMapper);
    }

    /**
     * Ejecuta una consulta en la base de datos para recuperar una lista de
     * resultados.
     * 
     * @param <T> el tipo de la lista a regresar.
     * @param sql la sentencia SQL a ejecutar.
     * @param args los argumentos que reemplazarán los '?' de la sentencia.
     * @param rowMapper el objeto que mapeará cada fila del resultado de la
     *        consulta.
     * @return una lista con objetos tipo T o una lista vacia si no hubo
     *         resultados.
     */
    public <T> List<T> queryForList(String sql, String[] args, RowMapper<T> rowMapper) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Log.d(TAG, "<-" + sql);
        Cursor cursor = database.rawQuery(sql, args);
        ArrayList<T> list = new ArrayList<T>(cursor.getCount());
        int rowNum = 0;
        while (cursor.moveToNext())
            list.add(rowMapper.mapRow(cursor, ++rowNum));
        cursor.close();
        database.close();
        return list;
    }

    /**
     * Ejecuta una sentencia SQL (INSERT, UPDATE, DELETE, etc.) en la base de
     * datos.
     * 
     * @param sql la sentencia SQL a ejecutar.
     */
    public void execute(String sql) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.beginTransaction();
        Log.d(TAG, "->" + sql);
        SQLiteStatement statement = database.compileStatement(sql);
        statement.execute();
        statement.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    /**
     * Ejecuta una sentencia SQL (INSERT, UPDATE, DELETE, etc.) en la base de
     * datos.
     * 
     * @param sql la sentencia SQL a ejecutar.
     * @param statementBinder el objeto que reemplazarán los '?' de la
     *        sentencia.
     */
    public void execute(String sql, SQLiteStatementBinder statementBinder) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.beginTransaction();
        Log.d(TAG, "->" + sql);
        SQLiteStatement statement = database.compileStatement(sql);
        statementBinder.bindValues(statement);
        statement.execute();
        statement.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    /**
     * Ejecuta varias sentencias SQL (INSERT, UPDATE, DELETE, etc.) en la base
     * de datos usando una misma transacción.
     * 
     * @param sqls las sentencias SQL a ejecutar.
     */
    public void batchExecute(String[] sqls) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.beginTransaction();
        for (String sql : sqls) {
            Log.d(TAG, "->" + sql);
            SQLiteStatement statement = database.compileStatement(sql);
            statement.execute();
            statement.close();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    /**
     * Ejecuta varias sentencias SQL (INSERT, UPDATE, DELETE, etc.) en la base
     * de datos usando una misma transacción.
     * 
     * @param sql las sentencia SQL a ejecutar.
     * @param statementBinder el objeto que reemplazarán los '?' de la sentencia
     *        varias veces.
     */
    public void batchExecute(String sql, BatchSQLiteStatementBinder statementBinder) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.beginTransaction();
        Log.d(TAG, "->" + sql);
        SQLiteStatement statement = database.compileStatement(sql);
        for (int i = 0; i < statementBinder.getBatchSize(); i++) {
            statement.clearBindings();
            statementBinder.bindValues(statement, i);
            statement.execute();
        }
        statement.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    /**
     * Mapea cada una de las filas del objeto Cursor a un objeto tipo T.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     * @param <T> el tipo del objeto a mapear.
     */
    public static interface RowMapper<T> {

        /**
         * Se debe implementar este método para que se mape cada una de las
         * filas de datos. Este método no debe llamar
         * {@link android.database.Cursor#moveToNext()}.
         * 
         * @param cursor un objeto Cursor que tiene los datos de la fila en
         *        curso.
         * @param rowNum la fila en curso (base 1).
         * @return un objeto tipo T.
         */
        T mapRow(Cursor cursor, int rowNum);
    }

    /**
     * Enlaza valores al objeto SQLiteStatement proporcionado para realizar una
     * actulización en la base datos.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static interface SQLiteStatementBinder {

        /**
         * Se debe implementar este método para que se enlazen los valores para
         * reemplazar los '?' en la sentencia SQL.
         * 
         * @param statement el objeto para enlazar valores.
         */
        void bindValues(SQLiteStatement statement);
    }

    /**
     * Enlaza valores al objeto SQLiteStatement proporcionado para realizar una
     * actulización por lotes en la base datos.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static interface BatchSQLiteStatementBinder {

        /**
         * Se debe implementar este método para que se enlazen los valores para
         * reemplazar los '?' en la sentencia SQL.
         * 
         * @param statement el objeto para enlazar valores.
         * @param i el índice del lote actual.
         */
        void bindValues(SQLiteStatement statement, int i);

        /**
         * @return el tamaño del lote.
         */
        int getBatchSize();
    }

    /**
     * Provee métodos útiles para Data Access Objects basados en SQLite.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static abstract class DaoSupport {

        private SQLiteTemplate sqliteTemplate;
        private Context context;

        protected SQLiteTemplate getSQLiteTemplate() {
            return sqliteTemplate;
        }

        protected Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setSQLiteOpenHelper(SQLiteOpenHelper databaseHelper) {
            if (sqliteTemplate == null || databaseHelper != sqliteTemplate.databaseHelper) {
                sqliteTemplate = new SQLiteTemplate(databaseHelper);
            }
        }

        protected String getSqlString(int resId) {
            return context.getString(resId).replaceAll("\\\\'", "'");
        }
    }
}
