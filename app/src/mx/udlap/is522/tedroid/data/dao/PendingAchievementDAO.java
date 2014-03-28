package mx.udlap.is522.tedroid.data.dao;

/**
 * Data Access Object de tipo Achievement para operaciones sobre logros
 * pendientes por subir a Google Play.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public interface PendingAchievementDAO extends BaseAchievementDAO {

    /**
     * Borra todos los objetos de la fuente datos.
     */
    void deleteAll();
}