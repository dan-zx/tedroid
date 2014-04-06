package mx.udlap.is522.tedroid.data.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import mx.udlap.is522.tedroid.data.Score;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class ScoreDAOTest {

    private ScoreDAO scoreDAO;

    @Before
    public void setUp() throws Exception {
        scoreDAO = new DAOFactory(Robolectric.application).get(ScoreDAO.class);
        assertThat(scoreDAO).isNotNull();
    }

    @Test
    public void shouldPersist() throws Exception {
        Score newScore = new Score();
        newScore.setLevel(5);
        newScore.setLines(54);
        newScore.setPoints(27442346);

        shouldPersist(newScore);
    }

    @Test
    public void shouldDeleteAll() throws Exception {
        Score score1 = new Score();
        score1.setLevel(5);
        score1.setLines(54);
        score1.setPoints(27442346);

        Score score2 = new Score();
        score2.setLevel(6);
        score2.setLines(65);
        score2.setPoints(453543678);

        Score score3 = new Score();
        score3.setLevel(2);
        score3.setLines(21);
        score3.setPoints(456456);

        shouldPersist(score1, score2, score3);

        scoreDAO.deleteAll();
        List<Score> all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isEmpty();
    }

    private void shouldPersist(Score... scores) throws Exception {
        List<Score> all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isEmpty();

        for (Score score : scores) {
            scoreDAO.save(score);
            Thread.sleep(1000L);
        }

        Arrays.sort(scores, new Comparator<Score>() {

            @Override
            public int compare(Score o1, Score o2) {
                return o1.getPoints() < o2.getPoints() ? 1 
                       : o1.getPoints() > o2.getPoints() ? -1 
                       : 0;
            }
        });

        all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isNotEmpty().hasSize(scores.length).doesNotContainNull();

        for (int i = 0; i < scores.length; i++) {
            assertThat(all.get(i)).isNotNull().isLenientEqualsToByIgnoringFields(scores[i], "obtainedAt");
            assertThat(all.get(i).getObtainedAt()).isNotNull();
        }
    }
}