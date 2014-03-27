package mx.udlap.is522.tedroid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Metodos y constantes para salvar y accesar a configuraciones de la aplicación.
 *  
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public final class Preferences {

    /**
     * Constantes que representan las llaves de cada configuración. 
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static final class Keys {

        public static final String WAS_USER_SIGNED_IN_GAMES = "wasSignedIn";

        /**
         * NO INVOCAR.
         */
        private Keys() {
            throw new IllegalAccessError("This class cannot be instantiated nor extended");
        }
    }

    /**
     * NO INVOCAR.
     */
    private Preferences() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * @param context el contexto de la aplicación.
     * @return el objeto SharedPreferences que contiene todos las
     *         configuraciones de esta aplicación.
     */
    public static SharedPreferences defaultPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Restaura las configuraciones a las originales.
     * 
     * @param context el contexto de la aplicación.
     */
    public static void restore(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .clear()
            .commit();
    }
}