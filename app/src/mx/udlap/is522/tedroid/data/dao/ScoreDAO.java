/*
 * Copyright 2014 Tedroid developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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