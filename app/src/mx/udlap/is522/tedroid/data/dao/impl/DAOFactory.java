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
package mx.udlap.is522.tedroid.data.dao.impl;

import android.content.Context;

import mx.udlap.is522.tedroid.data.dao.GenericDAO;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;
import mx.udlap.is522.tedroid.data.dao.impl.sqlite.ScoreSQLiteDAO;
import mx.udlap.is522.tedroid.data.source.TedroidSQLiteOpenHelper;

/**
 * Fabrica de Data Access Object.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class DAOFactory {

    private final Context context;

    /**
     * Crea una nueva fabrica.
     * 
     * @param context el contexto de la aplicación.
     */
    public DAOFactory(Context context) {
        this.context = context;
    }

    /**
     * @param which que tipo de DAO (interfaz).
     * @return un GenericDAO o {@code null} si no hay implementación de esa interfaz.
     */
    @SuppressWarnings("unchecked")
    public <T extends GenericDAO<?, ?>> T get(Class<T> which) {
        if (which == ScoreDAO.class) return (T) buildScoreDAO();
        return null;
    }

    /** @return un ScoreSQLiteDAO. */
    private ScoreSQLiteDAO buildScoreDAO() {
        ScoreSQLiteDAO scoreSQLiteDAO = new ScoreSQLiteDAO();
        scoreSQLiteDAO.setContext(context);
        scoreSQLiteDAO.setSQLiteOpenHelper(new TedroidSQLiteOpenHelper(context));
        return scoreSQLiteDAO;
    }
}