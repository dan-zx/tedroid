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
package mx.udlap.is522.tedroid.data.dao.impl.sqlite;

import android.annotation.SuppressLint;
import android.database.Cursor;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object de tipo Score que usa una base de datos SQLite como fuente de datos.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class ScoreSQLiteDAO extends SQLiteTemplate.DAOSupport implements ScoreDAO {

    /**
     * Las tablas que soporta este DAO
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static enum Table {

        CLASSIC, SPECIAL;

        private static final String TABLE_FORMAT = "score_%s";

        /** @return el nombre de la tabla. */
        @SuppressLint("DefaultLocale")
        protected String getTableName() {
            return String.format(TABLE_FORMAT, name().toLowerCase());
        }
    }

    private static final String STR_SUBSTITUTION = "%s";
    
    private Table table;

    /**
     * Crea un nuevo ScoreSQLiteDAO para la tabla dada.
     * 
     * @param which la tabla.
     */
    public ScoreSQLiteDAO(Table which) {
        table = which;
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Score> readAllOrderedByPointsDesc() {
        return getSQLiteTemplate().queryForList(
                String.format(getSqlString(R.string.score_readAllOrderedByPointsDesc_sql), STR_SUBSTITUTION, table.getTableName()), 
                new SQLiteTemplate.RowMapper<Score>() {

                    @Override
                    public Score mapRow(Cursor cursor, int rowNum) {
                        Score score = new Score();
                        score.setObtainedAt(SQLiteUtils.getDateFromUnixTime(cursor, "obtained_at_unix"));
                        score.setLevel(SQLiteUtils.getInteger(cursor, "level"));
                        score.setLines(SQLiteUtils.getInteger(cursor, "lines"));
                        score.setPoints(SQLiteUtils.getInteger(cursor, "points"));
                        return score;
                    }
                });
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Integer> readSumOfLinesAndPoints() {
        return getSQLiteTemplate().queryForSingleResult(
                String.format(getSqlString(R.string.score_readSumOfLinesAndPoints_sql), table.getTableName()), 
                new SQLiteTemplate.RowMapper<Map<String, Integer>>() {

                    @Override
                    public Map<String, Integer> mapRow(Cursor cursor, int rowNum) {
                        HashMap<String, Integer> row = new HashMap<>(2);
                        row.put("lines_sum", SQLiteUtils.getInteger(cursor, "lines_sum"));
                        row.put("points_sum", SQLiteUtils.getInteger(cursor, "points_sum"));
                        return row;
                    }
                });
    }

    /** {@inheritDoc} */
    @Override
    public void save(Score score) {
        getSQLiteTemplate().execute(String.format(getSqlString(R.string.score_insert_sql), table.getTableName()),
                new String[] { String.valueOf(score.getLevel()), String.valueOf(score.getLines()), String.valueOf(score.getPoints()) });
    }

    /** {@inheritDoc} */
    @Override
    public void deleteAll() {
        getSQLiteTemplate().execute(String.format(getSqlString(R.string.score_deleteAll_sql), table.getTableName()));
    }
}