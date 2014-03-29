package mx.udlap.is522.tedroid.data.dao.sqlite;

import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.dao.UnlockedAchievementDAO;

import java.util.List;

/**
 * Data Access Object de tipo Achievement que usa una base de datos SQLite como
 * fuente de datos para operaciones sobre logros desbloqueados.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class UnlockedAchievementSQLiteDAO extends SQLiteTemplate.DaoSupport implements UnlockedAchievementDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> readAll() {
        return getSQLiteTemplate().queryForList(
                getSqlString(R.string.unlocked_achievement_readAll_sql), 
                new SQLiteTemplate.SingleColumnRowMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(String achievementId) {
        getSQLiteTemplate().execute(
                getSqlString(R.string.unlocked_achievement_insert_sql), 
                new String[] { achievementId });
    }
}