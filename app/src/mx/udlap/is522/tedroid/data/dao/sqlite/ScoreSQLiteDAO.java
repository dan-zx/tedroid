package mx.udlap.is522.tedroid.data.dao.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.Score;
import mx.udlap.is522.tedroid.data.dao.ScoreDAO;

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

    @Override
    public void setUploadedToGooglePlay(int id) {
        getSQLiteTemplate().execute(
                getSqlString(R.string.score_setUploadedToGooglePlay_sql),
                new ScoreSetUploadedToGooglePlayBinder(id));
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
            score.setId(SQLiteUtils.getInteger(cursor, "_id"));
            score.setObtainedAt(SQLiteUtils.getDateFromUnixTime(cursor, "obtained_at_unix"));
            score.setLevel(SQLiteUtils.getInteger(cursor, "level"));
            score.setLines(SQLiteUtils.getInteger(cursor, "lines"));
            score.setPoints(SQLiteUtils.getInteger(cursor, "points"));
            score.setUploadedToGooglePlay(SQLiteUtils.getBoolean(cursor, "is_uploaded_to_google_play"));
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
            statement.bindLong(++index, score.getLevel());
            statement.bindLong(++index, score.getLines());
            statement.bindLong(++index, score.getPoints());
        }
    }
    
    /**
     * Enlaza el valore de un id de un Score a un SQLiteStatement.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private static class ScoreSetUploadedToGooglePlayBinder implements SQLiteTemplate.SQLiteStatementBinder {

        private final int id;

        /**
         * Constructor.
         * 
         * @param score el id para usar.
         */
        private ScoreSetUploadedToGooglePlayBinder(int id) {
            this.id = id;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void bindValues(SQLiteStatement statement) {
            statement.bindLong(1, id);
        }
    }
}