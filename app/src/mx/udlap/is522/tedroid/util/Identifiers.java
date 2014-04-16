package mx.udlap.is522.tedroid.util;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Metodos y constantes genéricas para recuperar identificadores.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public final class Identifiers {

    /** Recurso no encontrado */
    public static final int NOT_FOUND = 0;

    /**
     * Tipos de recursos disponibles.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static enum ResourceType {
        ANIM, DRAWABLE, COLOR, LAYOUT, MENU, VALUES, BOOL, DIMEN, ID, INTEGER, ARRAY, RAW;

        @SuppressLint("DefaultLocale")
        public String getDefType() {
            return name().toLowerCase();
        }
    }

    /** NO INVOCAR. */
    private Identifiers() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * @param name nombre del recurso.
     * @param type el tipo del recurso.
     * @param context el contexto de la aplicación.
     * @return el id del recurso o {@link #NOT_FOUND} si ese recurso no existe.
     */
    public static int getFrom(String name, ResourceType type, Context context) {
        return context.getResources().getIdentifier(name, type.getDefType(), context.getPackageName());
    }
}
