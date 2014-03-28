package mx.udlap.is522.tedroid.data.dao;

import java.util.List;

/**
 * Data Access Object de tipo Achievement para operaciones sobre logros. Sirve
 * como plantilla para otros AchievementDAO.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public interface BaseAchievementDAO extends GenericDAO<String, String> {

    /**
     * @return todos los ids de los logros.
     */
    List<String> readAll();
}