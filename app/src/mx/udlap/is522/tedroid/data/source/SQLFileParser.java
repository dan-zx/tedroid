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

import android.util.Log;

import mx.udlap.is522.tedroid.util.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Toma un archivo .sql y lo convierte a un arreglo de objetos String solo con
 * las sentencias válidas.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
final class SQLFileParser {
    
    private static final int END_OF_STREAM = -1;
    private static final String TAG = SQLFileParser.class.getSimpleName();
    private static final String STATEMENT_DELIMITER = ";";
    private static final Pattern COMMENT_PATTERN = Pattern.compile("(?:/\\*[^;]*?\\*/)|(?:--[^;]*?$)", Pattern.DOTALL | Pattern.MULTILINE);

    /** NO INVOCAR. */
    private SQLFileParser() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * @param stream un archivo.
     * @return un arreglo de objetos String solo con las sentencias válidas o
     *         {@code null} si hubo algún error.
     */
    static String[] getSqlStatements(InputStream stream) {
        
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(stream));
            int r;
            StringBuilder sb = new StringBuilder();
            while ((r = reader.read()) != END_OF_STREAM) {
                char character = (char) r;
                sb.append(character);
            }

            return COMMENT_PATTERN.matcher(sb)
                    .replaceAll(Strings.EMPTY)
                    .split(STATEMENT_DELIMITER);

        } catch (IOException ex) {
            Log.e(TAG, "Unable to parse SQL Statements", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Log.e(TAG, "Unable to close stream", ex);
                }
            }
        }

        return null;
    }
}