package mx.udlap.is522.tedroid.data.dao;

import android.content.Context;

import mx.udlap.is522.tedroid.data.dao.sqlite.PendingAchievementSQLiteDAO;
import mx.udlap.is522.tedroid.data.dao.sqlite.ScoreSQLiteDAO;
import mx.udlap.is522.tedroid.data.dao.sqlite.UnlockedAchievementSQLiteDAO;
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
     * @return un GenericDAO o {@code null} si no hay implementación de esa
     *         interfaz.
     */
    @SuppressWarnings("unchecked")
    public <T extends GenericDAO<?, ?>> T get(Class<T> which) {
        if (which == ScoreDAO.class) return (T) buildScoreDAO();
        if (which == PendingAchievementDAO.class) return (T) buildPendingAchievementDAO();
        if (which == UnlockedAchievementDAO.class) return (T) buildUnlockedAchievementDAO();
        return null;
    }

    /**
     * @return un ScoreSQLiteDAO.
     */
    private ScoreSQLiteDAO buildScoreDAO() {
        ScoreSQLiteDAO scoreSQLiteDAO = new ScoreSQLiteDAO();
        scoreSQLiteDAO.setContext(context);
        scoreSQLiteDAO.setSQLiteOpenHelper(new TedroidSQLiteOpenHelper(context));
        return scoreSQLiteDAO;
    }
    
    /**
     * @return un PendingAchievementSQLiteDAO.
     */
    private PendingAchievementSQLiteDAO buildPendingAchievementDAO() {
        PendingAchievementSQLiteDAO pendingAchievementDAO = new PendingAchievementSQLiteDAO();
        pendingAchievementDAO.setContext(context);
        pendingAchievementDAO.setSQLiteOpenHelper(new TedroidSQLiteOpenHelper(context));
        return pendingAchievementDAO;
    }
    
    /**
     * @return un UnlockedAchievementSQLiteDAO.
     */
    private UnlockedAchievementSQLiteDAO buildUnlockedAchievementDAO() {
        UnlockedAchievementSQLiteDAO unlockedAchievementDAO = new UnlockedAchievementSQLiteDAO();
        unlockedAchievementDAO.setContext(context);
        unlockedAchievementDAO.setSQLiteOpenHelper(new TedroidSQLiteOpenHelper(context));
        return unlockedAchievementDAO;
    }
}