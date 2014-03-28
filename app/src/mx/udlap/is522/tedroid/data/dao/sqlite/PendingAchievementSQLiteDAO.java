package mx.udlap.is522.tedroid.data.dao.sqlite;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.dao.PendingAchievementDAO;

import java.util.List;

/**
 * Data Access Object de tipo Achievement que usa una base de datos SQLite como
 * fuente de datos para operaciones sobre logros pendientes por subir a Google
 * Play.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class PendingAchievementSQLiteDAO extends BaseAchievementSQLiteDAO implements PendingAchievementDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> readAll() {
        return getSQLiteTemplate().queryForList(
                getSqlString(R.string.pending_achievement_readAll_sql), 
                new AchievementMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(String achievementId) {
        getSQLiteTemplate().execute(
                getSqlString(R.string.pending_achievement_insert_sql), 
                new String[] { achievementId });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {
        getSQLiteTemplate().execute(
                getSqlString(R.string.pending_achievement_deleteAll_sql));
    }
}