package mx.udlap.is522.tedroid.data.dao.impl.sqlite;

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

    /** {@inheritDoc} */
    @Override
    public List<Score> readAllOrderedByPointsDesc() {
        return getSQLiteTemplate().queryForList(
                getSqlString(R.string.score_readAllOrderedByPointsDesc_sql), 
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
                getSqlString(R.string.score_readSumOfLinesAndPoints_sql), 
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
        getSQLiteTemplate().execute(getSqlString(R.string.score_insert_sql), 
                new String[] { String.valueOf(score.getLevel()), String.valueOf(score.getLines()), String.valueOf(score.getPoints()) });
    }

    /** {@inheritDoc} */
    @Override
    public void deleteAll() {
        getSQLiteTemplate().execute(getSqlString(R.string.score_deleteAll_sql));
    }
}