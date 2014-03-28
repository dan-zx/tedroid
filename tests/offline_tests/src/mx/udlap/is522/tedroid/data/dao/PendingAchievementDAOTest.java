package mx.udlap.is522.tedroid.data.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class PendingAchievementDAOTest {

    private PendingAchievementDAO pendingAchievementDAO;
    
    @Before
    public void setUp() throws Exception {
        pendingAchievementDAO = new DAOFactory(Robolectric.application).get(PendingAchievementDAO.class);
        assertThat(pendingAchievementDAO).isNotNull();
    }
    
    @Test
    public void shouldPersist() throws Exception {
        List<String> all = pendingAchievementDAO.readAll();
        assertThat(all).isNotNull().isEmpty();

        final String achievementId = "CgkI9f_0j-ABEAIQCA";
        
        pendingAchievementDAO.save(achievementId);
        
        all = pendingAchievementDAO.readAll();
        assertThat(all).isNotNull().isNotEmpty().hasSize(1).doesNotContainNull();
        assertThat(all.get(0)).isNotNull().isEqualTo(achievementId);
    }
    
    @Test
    public void shouldDeleteAll() throws Exception {
        List<String> all = pendingAchievementDAO.readAll();
        assertThat(all).isNotNull().isEmpty();

        String achievementId1 = "CgkI9f_0j-ABEAIQCA";
        String achievementId2 = "CgkI9f_0j-ABEAIQBQ";
        String achievementId3 = "CgkI9f_0j-ABEAIQAw";
        
        pendingAchievementDAO.save(achievementId1);
        pendingAchievementDAO.save(achievementId2);
        pendingAchievementDAO.save(achievementId3);
        
        all = pendingAchievementDAO.readAll();
        assertThat(all).isNotNull().isNotEmpty().hasSize(3).doesNotContainNull();
        
        assertThat(all.get(0)).isNotNull().isEqualTo(achievementId1);
        assertThat(all.get(1)).isNotNull().isEqualTo(achievementId2);
        assertThat(all.get(2)).isNotNull().isEqualTo(achievementId3);
        
        pendingAchievementDAO.deleteAll();
        all = pendingAchievementDAO.readAll();
        assertThat(all).isNotNull().isEmpty();
    }
}