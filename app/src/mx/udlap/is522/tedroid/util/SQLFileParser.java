package mx.udlap.is522.tedroid.util;

import android.util.Log;

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
public final class SQLFileParser {
    
    private static final int END_OF_STREAM = -1;
    private static final String TAG = SQLFileParser.class.getSimpleName();
    private static final String STATEMENT_DELIMITER = ";";
    private static final Pattern COMMENT_PATTERN = Pattern.compile("(?:/\\*[^;]*?\\*/)|(?:--[^;]*?$)", Pattern.DOTALL | Pattern.MULTILINE);

    /**
     * NO INVOCAR.
     */
    private SQLFileParser() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * @param stream un archivo.
     * @return un arreglo de objetos String solo con las sentencias válidas o
     *         {@code null} si hubo algún error.
     */
    public static String[] getSqlStatements(InputStream stream) {
        
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