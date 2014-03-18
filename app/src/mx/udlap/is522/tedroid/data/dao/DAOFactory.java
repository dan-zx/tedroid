package mx.udlap.is522.tedroid.data.dao;

import android.content.Context;

import mx.udlap.is522.tedroid.data.dao.sqlite.ScoreSQLiteDAO;
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
     * @return un DAOFactory.
     */
    public static DAOFactory build(Context context) {
        return new DAOFactory(context);
    }

    /**
     * Crea una nueva fabrica.
     * 
     * @param context el contexto de la aplicación.
     */
    private DAOFactory(Context context) {
        this.context = context;
    }

    /**
     * @return un ScoreDAO.
     */
    public ScoreDAO getScoreDAO() {
        ScoreSQLiteDAO scoreSQLiteDAO = new ScoreSQLiteDAO();
        scoreSQLiteDAO.setContext(context);
        scoreSQLiteDAO.setSQLiteOpenHelper(new TedroidSQLiteOpenHelper(context));
        return scoreSQLiteDAO;
    }
}