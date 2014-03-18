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
    public void shouldPersist() {
        List<Score> all = scoreDAO.readAll();
        assertThat(all).isNullOrEmpty();
        
        Score newScore = new Score();
        newScore.setId(1l);
        newScore.setLevel(5);
        newScore.setLines(54);
        newScore.setPoints(27442346);
        
        scoreDAO.save(newScore);
        
        all = scoreDAO.readAll();
        assertThat(all).isNotNull().isNotEmpty().contains(newScore);
    }
    
    @Test
    public void shouldDeleteAll() {
        Score score1 = new Score();
        score1.setId(1l);
        score1.setLevel(5);
        score1.setLines(54);
        score1.setPoints(27442346);
        
        Score score2 = new Score();
        score2.setId(2l);
        score2.setLevel(6);
        score2.setLines(65);
        score2.setPoints(453543678);
        
        Score score3 = new Score();
        score3.setId(3l);
        score3.setLevel(2);
        score3.setLines(21);
        score3.setPoints(456456);
        
        scoreDAO.save(score1);
        scoreDAO.save(score2);
        scoreDAO.save(score3);
        
        List<Score> all = scoreDAO.readAll();
        assertThat(all).isNotNull().isNotEmpty().contains(score1, score2, score3);
        
        scoreDAO.deleteAll();
        all = scoreDAO.readAll();
        assertThat(all).isNullOrEmpty();
    }
}