package mx.udlap.is522.tedroid.util;

/**
 * Metodos y constantes genéricas de la clase String.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public final class Strings {

    /**
     * Un objeto String vacio.
     */
    public static final String EMPTY = "";

    /**
     * NO INVOCAR.
     */
    private Strings() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * @param str el objeto String a provar.
     * @return si es o no vacio (sin texto con espacios) o {@code null}.
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().length() == 0;
    }
}