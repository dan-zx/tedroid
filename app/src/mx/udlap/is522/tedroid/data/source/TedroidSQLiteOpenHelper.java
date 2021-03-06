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
package mx.udlap.is522.tedroid.data.source;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Crea/maneja la base datos usada por Tedroid y provee acceso a ella.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class TedroidSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int CURRENT_VERSION = 2;
    private static final String TAG = TedroidSQLiteOpenHelper.class.getSimpleName();
    private static final String SCHEMA_FILE_FORMAT = "db/schema-v%s.sql";
    private static final String NAME = "tedroid";

    private final int version;
    private final Context context;

    /**
     * Crea un nuevo objeto usando el context proporcionado.
     * 
     * @param context el contexto de la aplicación.
     */
    public TedroidSQLiteOpenHelper(Context context) {
        super(context, NAME, null, CURRENT_VERSION);
        this.context = context;
        version = CURRENT_VERSION;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            InputStream fileStream = context.getAssets().open(String.format(SCHEMA_FILE_FORMAT, version));
            String[] statements = SQLFileParser.getSqlStatements(fileStream);
            for (String statement : statements) database.execSQL(statement);
        } catch (IOException | SQLException ex) {
            Log.e(TAG, "Unable to execute schema", ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onCreate(database);
    }

    /**
     * Destruye la base datos de Tedroid.
     * 
     * @param context el contexto de la aplicación.
     */
    public static void destroyDb(Context context) {
        context.deleteDatabase(NAME);
    }
}