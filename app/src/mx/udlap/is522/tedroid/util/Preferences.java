package mx.udlap.is522.tedroid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Preferences {

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
    public static SharedPreferences getDefaultPreferences(Context context) {
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