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
        scoreDAO = new DAOFactory(Robolectric.application).get(ScoreDAO.class);
        assertThat(scoreDAO).isNotNull();
    }
    
    @Test
    public void shouldPersist() throws Exception {
        List<Score> all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isEmpty();
        
        Score newScore = new Score();
        newScore.setLevel(5);
        newScore.setLines(54);
        newScore.setPoints(27442346);
        newScore.setIsUploadedToGooglePlay(false);
        
        scoreDAO.save(newScore);
        
        all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isNotEmpty().hasSize(1).doesNotContainNull();
        assertThat(all.get(0)).isNotNull().isLenientEqualsToByIgnoringFields(newScore, "obtainedAt");
        assertThat(all.get(0).getObtainedAt()).isNotNull();
    }
    
    @Test
    public void shouldDeleteAll() throws Exception {
        List<Score> all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isEmpty();

        Score score1 = new Score();
        score1.setLevel(5);
        score1.setLines(54);
        score1.setPoints(27442346);
        score1.setIsUploadedToGooglePlay(false);
        
        Score score2 = new Score();
        score2.setLevel(6);
        score2.setLines(65);
        score2.setPoints(453543678);
        score2.setIsUploadedToGooglePlay(false);
        
        Score score3 = new Score();
        score3.setLevel(2);
        score3.setLines(21);
        score3.setPoints(456456);
        score3.setIsUploadedToGooglePlay(false);
        
        scoreDAO.save(score1);
        Thread.sleep(1000l);
        scoreDAO.save(score2);
        Thread.sleep(1000l);
        scoreDAO.save(score3);
        
        all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isNotEmpty().hasSize(3).doesNotContainNull();

        assertThat(all.get(0)).isNotNull().isLenientEqualsToByIgnoringFields(score2, "obtainedAt");
        assertThat(all.get(0).getObtainedAt()).isNotNull();
        assertThat(all.get(1)).isNotNull().isLenientEqualsToByIgnoringFields(score1, "obtainedAt");
        assertThat(all.get(1).getObtainedAt()).isNotNull();
        assertThat(all.get(2)).isNotNull().isLenientEqualsToByIgnoringFields(score3, "obtainedAt");
        assertThat(all.get(2).getObtainedAt()).isNotNull();
        
        scoreDAO.deleteAll();
        all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isEmpty();
    }
    
    @Test
    public void shouldsetUploadedToGooglePlay() {
        List<Score> all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isEmpty();
        
        Score newScore = new Score();
        newScore.setLevel(5);
        newScore.setLines(54);
        newScore.setPoints(27442346);
        newScore.setIsUploadedToGooglePlay(false);
        
        scoreDAO.save(newScore);
        
        all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isNotEmpty().hasSize(1).doesNotContainNull();
        assertThat(all.get(0)).isNotNull().isLenientEqualsToByIgnoringFields(newScore, "obtainedAt");
        assertThat(all.get(0).getObtainedAt()).isNotNull();
        
        newScore = all.get(0);

        scoreDAO.setUploadedToGooglePlay(newScore.getObtainedAt());
        
        all = scoreDAO.readAllOrderedByPointsDesc();
        assertThat(all).isNotNull().isNotEmpty().hasSize(1).doesNotContainNull();
        assertThat(all.get(0)).isNotNull().isLenientEqualsToByIgnoringFields(newScore, "isUploadedToGooglePlay");
        assertThat(all.get(0).getIsUploadedToGooglePlay()).isTrue();
    }
}