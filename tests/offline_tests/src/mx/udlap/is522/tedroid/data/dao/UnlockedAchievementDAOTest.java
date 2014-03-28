package mx.udlap.is522.tedroid.data.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class UnlockedAchievementDAOTest {

    private UnlockedAchievementDAO unlockedAchievementDAO;

    @Before
    public void setUp() throws Exception {
        unlockedAchievementDAO = new DAOFactory(Robolectric.application).get(UnlockedAchievementDAO.class);
        assertThat(unlockedAchievementDAO).isNotNull();
    }

    @Test
    public void shouldPersist() throws Exception {
        List<String> all = unlockedAchievementDAO.readAll();
        assertThat(all).isNotNull().isEmpty();

        final String achievementId = "CgkI9f_0j-ABEAIQCA";

        unlockedAchievementDAO.save(achievementId);

        all = unlockedAchievementDAO.readAll();
        assertThat(all).isNotNull().isNotEmpty().hasSize(1).doesNotContainNull();
        assertThat(all.get(0)).isNotNull().isEqualTo(achievementId);
    }
}