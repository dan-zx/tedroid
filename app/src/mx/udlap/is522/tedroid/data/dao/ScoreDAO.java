package mx.udlap.is522.tedroid.data.dao;

import mx.udlap.is522.tedroid.data.Score;

import java.util.List;
import java.util.Map;

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
     * @return la suma de lineas con la llave "lines_sum" y la suma de puntos con la llave "points_sum".
     */
    Map<String, Integer> readSumOfLinesAndPoints();

    /**
     * Guarda el objeto Score proporcionado en la fuente datos.
     * 
     * @param score el objeto a guardar.
     */
    void save(Score score);

    /** Borra todos los objetos de la fuente datos. */
    void deleteAll();
}