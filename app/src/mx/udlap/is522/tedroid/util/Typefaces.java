package mx.udlap.is522.tedroid.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Metodos y constantes genéricas de la clase Typeface.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public final class Typefaces {

    /**
     * Paths de la fuentes usadas en la aplicación.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static enum Font {
        TWOBIT ("fonts/twobit.ttf");
        
        private final String path;
        
        Font(String path) {
            this.path = path;
        }
        
        public String getPath() {
            return path;
        }
    }
    /**
     * NO INVOCAR.
     */
    private Typefaces() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }
    
    /**
     * @param context el contexto de la aplicación.
     * @param which que fuente.
     * @return una fuente que se encuentra en assets
     */
    public static final Typeface get(Context context, Font which) {
        return Typeface.createFromAsset(context.getAssets(), which.path); 
    }
}
