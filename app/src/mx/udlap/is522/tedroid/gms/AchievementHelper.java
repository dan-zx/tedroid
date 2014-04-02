package mx.udlap.is522.tedroid.gms;

import android.content.Context;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import mx.udlap.is522.tedroid.R;
import mx.udlap.is522.tedroid.data.dao.DAOFactory;
import mx.udlap.is522.tedroid.data.dao.PendingAchievementDAO;
import mx.udlap.is522.tedroid.data.dao.UnlockedAchievementDAO;

import java.util.List;

/**
 * Clase ayudente para desbloquear logros y guardar logros desbloquedos
 * pendientes por subir a Google Play.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class AchievementHelper {

    private static final SparseIntArray PENDING_ACHIEVEMENT_TOASTS = new SparseIntArray();
    private static AchievementHelper instance;
    
    static {
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.like_a_boss_achievement_id, R.string.pending_like_a_boss_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.believe_it_or_not_achievement_id, R.string.pending_believe_it_or_not_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.get_a_life_achievement_id, R.string.pending_get_a_life_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.tenacious_achievement_id, R.string.pending_tenacious_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.nothing_to_do_achievement_id, R.string.pending_nothing_to_do_win_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.boooooring_achievement_id, R.string.pending_boooooring_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.whats_next_achievement_id, R.string.pending_whats_next_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.lucky_you_achievement_id, R.string.pending_lucky_you_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.pro_plus_plus_achievement_id, R.string.pending_pro_plus_plus_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.pro_achievement_id, R.string.pending_pro_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.master_achievement_id, R.string.pending_master_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.expert_achievement_id, R.string.pending_expert_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.amateur_achievement_id, R.string.pending_amateur_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.beginner_achievement_id, R.string.pending_beginner_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.as_easy_as_pie_achievement_id, R.string.pending_as_easy_as_pie_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.for_dummies_achievement_id, R.string.pending_for_dummies_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.in_a_row_achievement_id, R.string.pending_in_a_row_achievement_unlocked);
    }

    private final Context context;

    private List<String> pendingAchievements;
    private List<String> unlockedAchievements;
    private PendingAchievementDAO pendingAchievementDAO;
    private UnlockedAchievementDAO unlockedAchievementDAO;

    /**
     * Crea un nuevo objeto AchievementHelper y lo llena con los logros 
     * desbloquedos y los logros desbloquedos pendientes por subir a Google Play.
     * 
     * @param context el contexto de la aplicación.
     * @return el objeto AchievementHelper único.
     */
    public static AchievementHelper getInstance(Context context) {
        if (instance == null) instance = new AchievementHelper(context);
        return instance;
    }

    /**
     * Crea un nuevo objeto AchievementHelper y lo llena con los logros 
     * desbloquedos y los logros desbloquedos pendientes por subir a Google Play.
     * 
     * @param context el contexto de la aplicación.
     */
    private AchievementHelper(Context context) {
        this.context = context;
        setUpDAOs();
        loadAchievements();
    }

    /**
     * Inicializa los DAOs necesarios.
     */
    private void setUpDAOs() {
        DAOFactory daoFactory = new DAOFactory(context);
        pendingAchievementDAO = daoFactory.get(PendingAchievementDAO.class);
        unlockedAchievementDAO = daoFactory.get(UnlockedAchievementDAO.class);
    }

    /**
     * Carga los logros desde una fuente datos.
     */
    private void loadAchievements() {
        pendingAchievements = pendingAchievementDAO.readAll();
        unlockedAchievements = unlockedAchievementDAO.readAll();
    }

    /**
     * Desbloquea el logro con el id proporcionado.
     * 
     * @param id el id del logro.
     * @param apiClient un objeto GoogleApiClient.
     */
    public void unlockAchievement(int id, GoogleApiClient apiClient) {
        String achievementId = context.getString(id);
        if (!unlockedAchievements.contains(achievementId)) {
            if (apiClient != null && apiClient.isConnected()) {
                Games.Achievements.unlock(apiClient, context.getString(id));
            } else {
                pendingAchievements.add(achievementId);
                pendingAchievementDAO.save(achievementId);
                Toast.makeText(context, PENDING_ACHIEVEMENT_TOASTS.get(id), Toast.LENGTH_LONG).show();
            }

            unlockedAchievements.add(achievementId);
            unlockedAchievementDAO.save(achievementId);
        }
    }

    /**
     * Publica todos los logros desbloquedos pendientes por subir a Google Play.
     * 
     * @param apiClient apiClient un objeto GoogleApiClient.
     */
    public void publishPendingAchievements(GoogleApiClient apiClient) {
        if (apiClient != null && apiClient.isConnected()) {
            for (String achievementId : pendingAchievements) {
                Games.Achievements.unlock(apiClient, achievementId);
            }

            pendingAchievements.clear();
            pendingAchievementDAO.deleteAll();
        }
    }
}