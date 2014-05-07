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
package mx.udlap.is522.tedroid.data.dao.impl.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase de conveniencia para no repitir código relacionado con transacciones de SQLite.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
class SQLiteTemplate {

    private static final String TAG = SQLiteTemplate.class.getSimpleName();

    private final SQLiteOpenHelper databaseHelper;

    /**
     * Construye un nuevo SQLiteTemplate.
     * 
     * @param databaseHelper el objeto SQLiteOpenHelper a usar.
     */
    SQLiteTemplate(SQLiteOpenHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Ejecuta una consulta en la base de datos para recuperar un solo resultado.
     * 
     * @param <T> el tipo del objeto a regresar.
     * @param sql la sentencia SQL a ejecutar.
     * @param rowMapper el objeto que mapeará el resultado de la consulta.
     * @return un objeto tipo T o {@code null} si no hubo resultado o hay más de un resultado o hubo
     *         errores.
     */
    <T> T queryForSingleResult(String sql, RowMapper<T> rowMapper) {
        return queryForSingleResult(sql, null, rowMapper);
    }

    /**
     * Ejecuta una consulta en la base de datos para recuperar un solo resultado.
     * 
     * @param <T> el tipo del objeto a regresar.
     * @param sql la sentencia SQL a ejecutar.
     * @param args los argumentos que reemplazarán los '?' de la consulta.
     * @param rowMapper el objeto que mapeará el resultado de la consulta.
     * @return un objeto tipo T o {@code null} si no hubo resultado, hay más de un resultado o hubo
     *         errores.
     */
    <T> T queryForSingleResult(String sql, String[] args, RowMapper<T> rowMapper) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        T object = null;
        try {
            database = databaseHelper.getReadableDatabase();
            cursor = database.rawQuery(sql, args);
            if (cursor.getCount() == 1 && cursor.moveToNext()) object = rowMapper.mapRow(cursor, 1);
        } catch (Exception ex) {
            Log.e(TAG, "Couldn't complete query [" + sql + "] with args [" + Arrays.deepToString(args) + "]");
        } finally {
            SQLiteUtils.close(cursor);
            SQLiteUtils.close(database);
        }
        return object;
    }

    /**
     * Ejecuta una consulta en la base de datos para recuperar una lista de resultados.
     * 
     * @param <T> el tipo de la lista a regresar.
     * @param sql la sentencia SQL a ejecutar.
     * @param rowMapper el objeto que mapeará cada fila del resultado de la consulta.
     * @return una lista con objetos tipo T o una lista vacia si no hubo resultados o {@code null}
     *         si hubo errores.
     */
    <T> List<T> queryForList(String sql, RowMapper<T> rowMapper) {
        return queryForList(sql, null, rowMapper);
    }

    /**
     * Ejecuta una consulta en la base de datos para recuperar una lista de resultados.
     * 
     * @param <T> el tipo de la lista a regresar.
     * @param sql la sentencia SQL a ejecutar.
     * @param args los argumentos que reemplazarán los '?' de la sentencia.
     * @param rowMapper el objeto que mapeará cada fila del resultado de la consulta.
     * @return una lista con objetos tipo T, una lista vacia si no hubo resultados o {@code null} si
     *         hubo errores.
     */
    <T> List<T> queryForList(String sql, String[] args, RowMapper<T> rowMapper) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        ArrayList<T> list = null;
        try {
            database = databaseHelper.getReadableDatabase();
            cursor = database.rawQuery(sql, args);
            list = new ArrayList<>(cursor.getCount());
            int rowNum = 0;
            while (cursor.moveToNext()) list.add(rowMapper.mapRow(cursor, ++rowNum));
        } catch (Exception ex) {
            Log.e(TAG, "Couldn't complete query [" + sql + "] with args [" + Arrays.deepToString(args) + "]");
        } finally {
            SQLiteUtils.close(cursor);
            SQLiteUtils.close(database);
        }
        return list;
    }

    /**
     * Ejecuta una sentencia SQL (INSERT, UPDATE, DELETE, etc.) en la base de datos.
     * 
     * @param sql la sentencia SQL a ejecutar.
     */
    void execute(String sql) {
        SQLiteDatabase database = null;
        SQLiteStatement statement = null;
        try {
            database = databaseHelper.getWritableDatabase();
            database.beginTransaction();
            statement = database.compileStatement(sql);
            statement.execute();
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(TAG, "Couldn't execute [" + sql + "]");
        } finally {
            SQLiteUtils.close(statement);
            SQLiteUtils.endTransaction(database);
            SQLiteUtils.close(database);
        }
    }

    /**
     * Ejecuta una sentencia SQL (INSERT, UPDATE, DELETE, etc.) en la base de datos.
     * 
     * @param sql la sentencia SQL a ejecutar.
     * @param statementBinder el objeto que reemplazarán los '?' de la sentencia.
     */
    void execute(String sql, SQLiteStatementBinder statementBinder) {
        SQLiteDatabase database = null;
        SQLiteStatement statement = null;
        try {
            database = databaseHelper.getWritableDatabase();
            database.beginTransaction();
            statement = database.compileStatement(sql);
            statementBinder.bindValues(statement);
            statement.execute();
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(TAG, "Couldn't execute [" + sql + "] with args");
        } finally {
            SQLiteUtils.close(statement);
            SQLiteUtils.endTransaction(database);
            SQLiteUtils.close(database);
        }
    }

    /**
     * Ejecuta una sentencia SQL (INSERT, UPDATE, DELETE, etc.) en la base de datos.
     * 
     * @param sql la sentencia SQL a ejecutar.
     * @param args el arreglo de String para enlazar valores.
     */
    void execute(String sql, String[] args) {
        SQLiteDatabase database = null;
        SQLiteStatement statement = null;
        try {
            database = databaseHelper.getWritableDatabase();
            database.beginTransaction();
            statement = database.compileStatement(sql);
            for (int index = args.length; index != 0; index--) {
                statement.bindString(index, args[index - 1]);
            }
            statement.execute();
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(TAG, "Couldn't execute [" + sql + "] with args");
        } finally {
            SQLiteUtils.close(statement);
            SQLiteUtils.endTransaction(database);
            SQLiteUtils.close(database);
        }
    }

    /**
     * Ejecuta varias sentencias SQL (INSERT, UPDATE, DELETE, etc.) en la base de datos usando una
     * misma transacción.
     * 
     * @param sqls las sentencias SQL a ejecutar.
     */
    void batchExecute(String[] sqls) {
        SQLiteDatabase database = null;
        try {
            database = databaseHelper.getWritableDatabase();
            database.beginTransaction();
            for (String sql : sqls) {
                SQLiteStatement statement = database.compileStatement(sql);
                statement.execute();
                statement.close();
            }
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(TAG, "Couldn't execute batch " + Arrays.deepToString(sqls));
        } finally {
            SQLiteUtils.endTransaction(database);
            SQLiteUtils.close(database);
        }
    }

    /**
     * Ejecuta varias sentencias SQL (INSERT, UPDATE, DELETE, etc.) en la base de datos usando una
     * misma transacción.
     * 
     * @param sql las sentencia SQL a ejecutar.
     * @param statementBinder el objeto que reemplazarán los '?' de la sentencia varias veces.
     */
    void batchExecute(String sql, BatchSQLiteStatementBinder statementBinder) {
        SQLiteDatabase database = null;
        SQLiteStatement statement = null;
        try {
            database = databaseHelper.getWritableDatabase();
            database.beginTransaction();
            statement = database.compileStatement(sql);
            for (int i = 0; i < statementBinder.getBatchSize(); i++) {
                statement.clearBindings();
                statementBinder.bindValues(statement, i);
                statement.execute();
            }
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(TAG, "Couldn't execute batch [" + sql + "]");
        } finally {
            SQLiteUtils.close(statement);
            SQLiteUtils.endTransaction(database);
            SQLiteUtils.close(database);
        }
    }

    /**
     * Mapea cada una de las filas del objeto Cursor a un objeto tipo T.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     * @param <T> el tipo del objeto a mapear.
     */
    static interface RowMapper<T> {

        /**
         * Se debe implementar este método para que se mape cada una de las filas de datos. Este
         * método no debe llamar {@link android.database.Cursor#moveToNext()}.
         * 
         * @param cursor un objeto Cursor que tiene los datos de la fila en curso.
         * @param rowNum la fila en curso (base 1).
         * @return un objeto tipo T.
         */
        T mapRow(Cursor cursor, int rowNum);
    }

    /**
     * Enlaza valores al objeto SQLiteStatement proporcionado para realizar una actulización en la
     * base datos.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    static interface SQLiteStatementBinder {

        /**
         * Se debe implementar este método para que se enlazen los valores para reemplazar los '?'
         * en la sentencia SQL.
         * 
         * @param statement el objeto para enlazar valores.
         */
        void bindValues(SQLiteStatement statement);
    }

    /**
     * Implementación de RowMapper que convierte una sola columna en un solo String por fila.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    static class SingleColumnRowMapper implements RowMapper<String> {

        private static final int COLUMN_INDEX = 0;

        /** {@inheritDoc} */
        @Override
        public String mapRow(Cursor cursor, int rowNum) {
            return cursor.getString(COLUMN_INDEX);
        }
    }

    /**
     * Implementación de RowMapper que crea un Map por cada fila, representando todas las columnas
     * como pares de llaves y valores: cada entrada por cada columna con el nombre de la columna
     * como llave.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    static class ColumnMapRowMapper implements RowMapper<Map<String, String>> {

        /** {@inheritDoc} */
        @Override
        public Map<String, String> mapRow(Cursor cursor, int rowNum) {
            int columnCount = cursor.getColumnCount();
            Map<String, String> row = new HashMap<>(columnCount);
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.put(cursor.getColumnName(columnIndex), cursor.getString(columnIndex));
            }
            return row;
        }

    }

    /**
     * Enlaza valores al objeto SQLiteStatement proporcionado para realizar una actulización por
     * lotes en la base datos.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    static interface BatchSQLiteStatementBinder {

        /**
         * Se debe implementar este método para que se enlazen los valores para reemplazar los '?'
         * en la sentencia SQL.
         * 
         * @param statement el objeto para enlazar valores.
         * @param i el índice del lote actual.
         */
        void bindValues(SQLiteStatement statement, int i);

        /** @return el tamaño del lote. */
        int getBatchSize();
    }

    /**
     * Provee métodos útiles para Data Access Objects basados en SQLite.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    static abstract class DAOSupport {

        private SQLiteTemplate sqliteTemplate;
        private Context context;

        /** @return el objeto SQLiteTemplate a utilizar en las operaciones. */
        protected SQLiteTemplate getSQLiteTemplate() {
            return sqliteTemplate;
        }

        /** @return el contexto de la aplicación. */
        protected Context getContext() {
            return context;
        }

        /** @param context el contexto de la aplicación. */
        public void setContext(Context context) {
            this.context = context;
        }

        /** @param databaseHelper el objeto ayudante para la conexión a SQLite. */
        public void setSQLiteOpenHelper(SQLiteOpenHelper databaseHelper) {
            sqliteTemplate = new SQLiteTemplate(databaseHelper);
        }

        /**
         * Quita los caracteres especiales del query con id dado.
         * 
         * @param resId el id del query.
         * @return un query SQL compilante.
         */
        protected String getSqlString(int resId) {
            return context.getString(resId).replaceAll("\\\\'", "'");
        }
    }
}