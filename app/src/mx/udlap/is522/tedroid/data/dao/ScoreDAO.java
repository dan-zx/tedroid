package mx.udlap.is522.tedroid.data.dao;

import mx.udlap.is522.tedroid.data.Score;

import java.util.List;

/**
 * Data Access Object de tipo Score.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public interface ScoreDAO extends GenericDAO<Integer, Score> {

    /** @return todos los objetos Score de fuente datos ordenados puntos en orden descendente. */
    List<Score> readAllOrderedByPointsDesc();

    /**
     * Guarda el objeto Score proporcionado en la fuente datos.
     * 
     * @param score el objeto a guardar.
     */
    void save(Score score);

    /** Borra todos los objetos de la fuente datos. */
    void deleteAll();
}