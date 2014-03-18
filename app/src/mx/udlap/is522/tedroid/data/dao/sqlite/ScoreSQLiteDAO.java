package mx.udlap.is522.tedroid.data.dao.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;
import mx.udlap.is522.tedroid.util.Cursors;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object de tipo Score que usa una base de datos SQLite como fuente
 * de datos.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class ScoreSQLiteDAO extends SQLiteTemplate.DaoSupport implements ScoreDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Score> readAllOrderedByPointsDesc() {
        return getSQLiteTemplate().queryForList(
                getSqlString(R.string.score_readAllOrderedByPointsDesc_sql), 
                new ScoreMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Score score) {
        getSQLiteTemplate().execute(
                getSqlString(R.string.score_insert_sql), 
                new ScoreInsertBinder(score));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {
        getSQLiteTemplate().execute(
                getSqlString(R.string.score_deleteAll_sql));
    }

    /**
     * Mapea cada fila del objeto Cursor a un objeto Score.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private static class ScoreMapper implements SQLiteTemplate.RowMapper<Score> {

        /**
         * {@inheritDoc}
         */
        @Override
        public Score mapRow(Cursor cursor, int rowNum) {
            Score score = new Score();
            score.setId(Cursors.getInteger(cursor, "_id"));
            score.setObtainedAt(new Date(Cursors.getLong(cursor, "obtained_at_unix")));
            score.setLevel(Cursors.getInteger(cursor, "level"));
            score.setLines(Cursors.getInteger(cursor, "lines"));
            score.setPoints(Cursors.getInteger(cursor, "points"));
            return score;
        }
    }

    /**
     * Enlaza los valores de un objeto Score a un SQLiteStatement.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private static class ScoreInsertBinder implements SQLiteTemplate.SQLiteStatementBinder {

        private final Score score;

        /**
         * Constructor.
         * 
         * @param score el objeto Score para usar.
         */
        private ScoreInsertBinder(Score score) {
            this.score = score;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void bindValues(SQLiteStatement statement) {
            int index = 0;
            statement.bindString(++index, String.valueOf(score.getLevel()));
            statement.bindString(++index, String.valueOf(score.getLines()));
            statement.bindString(++index, String.valueOf(score.getPoints()));
        }
    }
}