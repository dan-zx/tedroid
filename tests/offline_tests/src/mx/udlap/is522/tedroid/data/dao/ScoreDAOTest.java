package mx.udlap.is522.tedroid.data.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import mx.udlap.is522.tedroid.data.Score;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class ScoreDAOTest {

    private ScoreDAO scoreDAO;
    
    @Before
    public void setUp() throws Exception {
        scoreDAO = DAOFactory.build(Robolectric.application).getScoreDAO();
        assertThat(scoreDAO).isNotNull();
    }
    
    @Test
    public void shouldPersist() throws Exception {
        List<Score> all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isEmpty();
        
        Score newScore = new Score();
        newScore.setId(1);
        newScore.setLevel(5);
        newScore.setLines(54);
        newScore.setPoints(27442346);
        
        scoreDAO.save(newScore);
        
        all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isNotEmpty().hasSize(1).doesNotContainNull();
        assertThat(all.get(0)).isNotNull().isLenientEqualsToByIgnoringFields(newScore, "obtainedAt");
        assertThat(all.get(0).getObtainedAt()).isNotNull();
    }
    
    @Test
    public void shouldDeleteAll() throws Exception {
        Score score1 = new Score();
        score1.setId(1);
        score1.setLevel(5);
        score1.setLines(54);
        score1.setPoints(27442346);
        
        Score score2 = new Score();
        score2.setId(2);
        score2.setLevel(6);
        score2.setLines(65);
        score2.setPoints(453543678);
        
        Score score3 = new Score();
        score3.setId(3);
        score3.setLevel(2);
        score3.setLines(21);
        score3.setPoints(456456);
        
        scoreDAO.save(score1);
        Thread.sleep(1000l);
        scoreDAO.save(score2);
        Thread.sleep(1000l);
        scoreDAO.save(score3);
        
        List<Score> all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isNotEmpty().hasSize(3).doesNotContainNull();

        assertThat(all.get(0)).isNotNull().isLenientEqualsToByIgnoringFields(score2, "obtainedAt");
        assertThat(all.get(0).getObtainedAt()).isNotNull();
        assertThat(all.get(1)).isNotNull().isLenientEqualsToByIgnoringFields(score1, "obtainedAt");
        assertThat(all.get(1).getObtainedAt()).isNotNull();
        assertThat(all.get(2)).isNotNull().isLenientEqualsToByIgnoringFields(score3, "obtainedAt");
        assertThat(all.get(2).getObtainedAt()).isNotNull();
        
        scoreDAO.deleteAll();
        all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isEmpty();
    }
}