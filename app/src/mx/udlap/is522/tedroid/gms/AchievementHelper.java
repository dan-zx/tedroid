package mx.udlap.is522.tedroid.gms;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import mx.udlap.is522.tedroid.R;

import java.util.ArrayList;

/**
 * Clase ayudente para desbloquear logros y guardar logros desbloquedos
 * pendientes por subir a Google Play.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class AchievementHelper {

    private static final SparseIntArray PENDING_ACHIEVEMENT_TOASTS = new SparseIntArray();
    
    static {
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.epic_win_achievement_id, R.string.pending_epic_win_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.like_a_boss_achievement_id, R.string.pending_like_a_boss_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.believe_it_or_not_achievement_id, R.string.pending_believe_it_or_not_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.in_a_row_achievement_id, R.string.pending_in_a_row_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.boooooring_achievement_id, R.string.pending_boooooring_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.nothing_to_do_achievement_id, R.string.pending_nothing_to_do_win_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.tenacious_achievement_id, R.string.pending_tenacious_achievement_unlocked);
        PENDING_ACHIEVEMENT_TOASTS.put(R.string.get_a_life_achievement_id, R.string.pending_get_a_life_achievement_unlocked);
    }
    
    private final SparseBooleanArray UNLOCKED_ACHIEVEMENTS = new SparseBooleanArray();
    private final ArrayList<Integer> PENDING_ACHIEVEMENTS = new ArrayList<Integer>();
    private final Context context;

    /**
     * Crea un nuevo objeto AchievementHelper y lo llena con los logros 
     * desbloquedos y los logros desbloquedos pendientes por subir a Google Play.
     * 
     * @param context el contexto de la aplicación.
     * @return un nuevo objeto AchievementHelper.
     */
    public static AchievementHelper getInstance(Context context) {
        AchievementHelper pendingAccomplishments = new AchievementHelper(context);
        pendingAccomplishments.loadUnlockedAchievements();
        pendingAccomplishments.loadPendingAchivements();
        return pendingAccomplishments;
    }

    /**
     * Constructor.
     * 
     * @param context el contexto de la aplicación.
     */
    private AchievementHelper(Context context) {
        this.context = context;
    }

    /**
     * Carga los logros desbloquedos desde ???.
     */
    private void loadUnlockedAchievements() {
        // TODO: cargar desde fuente de datos todos los logros desbloqueados.
    }

    /**
     * Carga los logros desbloquedos pendientes por subir a Google Play desde ???.
     */
    private void loadPendingAchivements() {
        // TODO: cargar desde fuente de datos todos los logros que quedaron pendientes por subir.
    }

    /**
     * Salva los logros desbloquedos en ???.
     */
    private void saveUnlockedAchievements() {
        // TODO: guardar en fuente de datos todos los logros desbloqueados.
        // TODO: encriptar datos de logros desbloqueados.
    }

    /**
     * Salva los logros desbloquedos pendientes por subir a Google Play en ???.
     */
    private void savePendingAchivements() {
        // TODO: guardar en fuente de datos todos los logros que quedaron pendientes por subir.
        // TODO: encriptar datos de logros que quedaron pendientes por subir.
    }

    /**
     * Desbloquea el logro con el id proporcionado.
     * 
     * @param id el id del logro.
     * @param apiClient un objeto GoogleApiClient.
     */
    public void unlockAchievement(int id, GoogleApiClient apiClient) {
        if (!UNLOCKED_ACHIEVEMENTS.get(id)) {
            if (apiClient != null && apiClient.isConnected()) {
                Games.Achievements.unlock(apiClient, context.getString(id));
            } else {
                PENDING_ACHIEVEMENTS.add(id);
                savePendingAchivements();
                Toast.makeText(context, PENDING_ACHIEVEMENT_TOASTS.get(id), Toast.LENGTH_LONG).show();
            }

            UNLOCKED_ACHIEVEMENTS.put(id, true);
            saveUnlockedAchievements();
        }
    }

    /**
     * Publica todos los logros desbloquedos pendientes por subir a Google Play.
     * 
     * @param apiClient apiClient un objeto GoogleApiClient.
     */
    public void publishPendingAchievementsIfPossible(GoogleApiClient apiClient) {
        if (apiClient != null && apiClient.isConnected()) {
            for (int achievementResId : PENDING_ACHIEVEMENTS) {
                Games.Achievements.unlock(apiClient, context.getString(achievementResId));
            }
            
            PENDING_ACHIEVEMENTS.clear();
            savePendingAchivements();
        }
    }
}