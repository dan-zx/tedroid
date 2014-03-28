package mx.udlap.is522.tedroid.data.dao.sqlite;

import android.database.Cursor;

import mx.udlap.is522.tedroid.data.dao.BaseAchievementDAO;

/**
 * Data Access Object de tipo Achievement que usa una base de datos SQLite como
 * fuente de datos. Sirve como plantilla para otros AchievementDAO.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public abstract class BaseAchievementSQLiteDAO extends SQLiteTemplate.DaoSupport implements BaseAchievementDAO {

    /**
     * Mapea cada fila del objeto Cursor a un id de logro.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    protected static class AchievementMapper implements SQLiteTemplate.RowMapper<String> {

        /**
         * {@inheritDoc}
         */
        @Override
        public String mapRow(Cursor cursor, int rowNum) {
            return SQLiteUtils.getString(cursor, "achievement_id");
        }
    }
}